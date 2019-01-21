package org.einnovator.documents.client;

import static org.einnovator.util.UriUtils.appendFormattedQueryParameters;
import static org.einnovator.util.UriUtils.appendQueryParameter;
import static org.einnovator.util.UriUtils.appendQueryParameters;
import static org.einnovator.util.UriUtils.makeURI;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import org.einnovator.util.MappingUtils;
import org.einnovator.util.UriUtils;
import org.einnovator.documents.client.config.DocumentsConfiguration;
import org.einnovator.documents.client.config.DocumentsEndpoints;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.Permission;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.documents.client.modelx.TemplateFilter;

/**
 * Client API for Document Gateway.
 *
 */
public class DocumentsClient {

	@Autowired
	private DocumentsConfiguration config;

	@Autowired
	@Qualifier("documentsRestTemplate")
	private OAuth2RestTemplate restTemplate;

	@Autowired
	public DocumentsClient() {
	}

	public DocumentsClient(DocumentsConfiguration config) {
		this.config = config;
	}

	public DocumentsClient(OAuth2RestTemplate restTemplate, DocumentsConfiguration config) {
		this.restTemplate = restTemplate;
		this.config = config;
	}

	public OAuth2RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(OAuth2RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}


	public URI write(Document document, DocumentOptions options) {
		try {
			String path = document.getPath();
			URI uri = makeURI(DocumentsEndpoints.upload(path, config));
			if (options!=null) {	
				uri = appendQueryParameters(uri, options);
			}

			LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			HttpHeaders headers = new HttpHeaders();

			String filename = StringUtils.hasText(document.getName()) ? document.getName() : new File(path).getName();

			if (document.getInputStream() == null && document.getContent() != null) {
				document.setInputStream(new ByteArrayInputStream(document.getContent()));
			}
			if (document.getInputStream() != null) {
				HttpHeaders fileHeaders = new HttpHeaders();
				String contentType = document.getContentType();
				MediaType mediaType = null;
				if (StringUtils.hasText(contentType)) {
					mediaType = MediaType.parseMediaType(contentType);
				}
				if (mediaType == null) {
					mediaType = MediaType.APPLICATION_OCTET_STREAM;
				}
				fileHeaders.setContentType(mediaType);
				fileHeaders.setContentLength(document.getInputStream().available());
				fileHeaders.setContentDispositionFormData("file", filename);
				HttpEntity<InputStreamResource> entity = new HttpEntity<>(new InputStreamResource(document.getInputStream()), fileHeaders);
				map.add("file", entity);
			}

			List<Document> attachments = document.getAttachments();
			for (int i = 0; i < attachments.size(); i++) {
				if (attachments.get(i).getInputStream() != null) {
					HttpHeaders fileHeaders = new HttpHeaders();
					fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					fileHeaders.setContentLength(attachments.get(i).getInputStream().available());
					fileHeaders.setContentDispositionFormData("attachment[]", attachments.get(i).getName());
					InputStream in = attachments.get(i).getInputStream();
					if (in == null && attachments.get(i).getContent() != null) {
						in = new ByteArrayInputStream(attachments.get(i).getContent());
					}

					HttpEntity<InputStreamResource> entity = new HttpEntity<>(new InputStreamResource(in), fileHeaders);
					map.add("attachment[" + i + "]", entity);
				}
			}

			if (document != null) {
				map.add("document", serialize(document));
			}

			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
			URI response = restTemplate.postForLocation(uri, requestEntity);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Document serialize(Document document) {
		Document copy = new Document(document);
		List<Document> attachmentsCopy = new ArrayList<Document>();
		for (Document attachment : document.getAttachments()) {
			attachmentsCopy.add(serialize(attachment));
		}
		copy.setAttachments(attachmentsCopy);
		copy.setAttributes(document.getAttributes());
		copy.setDescription(document.getDescription());
		copy.setName(document.getName());
		copy.setPermissions(document.getPermissions());
		copy.setCategory(document.getCategory());
		return copy;
	}

	public Document download(URI uri, String username, boolean content) {
		URI metaUri = makeMetaUri(uri);
		if (StringUtils.hasText(username) && !(uri.getQuery() != null && uri.getQuery().contains("username"))) {
			uri = appendQueryParameter(uri, "username", username);
		}
		if (StringUtils.hasText(username) && !(metaUri.getQuery() != null && metaUri.getQuery().contains("username"))) {
			metaUri = appendQueryParameter(metaUri, "username", username);
		}
		RequestEntity<Void> request = RequestEntity.get(metaUri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<Document> response = exchange(request, Document.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			Document metaDoc = response.getBody();
			if (content) {
				String contentType = metaDoc != null && metaDoc.getMeta() != null
						? (String) metaDoc.getMeta().get("Content-Type")
						: null;
				if (contentType == null) {
					contentType = MediaType.ALL.toString();
				}
				Document contentDoc = downloadInternal(uri, username, contentType);
				metaDoc.setInputStream(contentDoc.getInputStream());
				metaDoc.setContent(contentDoc.getContent());
			}

			return metaDoc;
		}
		return null;
	}

	private URI makeMetaUri(URI uri) {
		return URI.create(uri.toString().replace("/_/", "/api/_meta/"));
	}

	public Document download(URI uri) {
		return downloadInternal(uri, null, null);
	}

	public Document download(URI uri, String username, String contentType) {
		return downloadInternal(uri, username, contentType);
	}

	public Document download(URI uri, String contentType) {
		return downloadInternal(uri, null, contentType);
	}

	private Document downloadInternal(URI uri, String username, String contentType) {
		Document document = new Document();
		if (StringUtils.hasText(username) && !(uri.getQuery() != null && uri.getQuery().contains("username"))) {
			uri = appendQueryParameter(uri, "username", username);
		}

		byte[] byteContent = content(uri, username, contentType);
		if (byteContent != null) {
			document.setContent(byteContent);
			document.setInputStream(new ByteArrayInputStream(byteContent));
		}

		return document;
	}

	public byte[] content(URI uri, String username, String contentType) {
		HeadersBuilder<?> builder = RequestEntity.get(uri);
		if (StringUtils.hasText(contentType)) {
			builder.accept(MediaType.valueOf(contentType));
		}
		RequestEntity<Void> request = builder.build();
		ResponseEntity<byte[]> response = exchange(request, byte[].class);
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}
		return null;
	}

	public List<Document> list(String path, DocumentFilter filter, Pageable pageable) {
		URI uri = makeURI(DocumentsEndpoints.list(path, config));
		uri = appendQueryParameters(uri, filter);
		uri = appendQueryParameters(uri, pageable);
		RequestEntity<Void> request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<Document[]> response = exchange(request, Document[].class);
		if (response.getStatusCode() == HttpStatus.OK) {
			Document[] documents = response.getBody();
			return Arrays.asList(documents);
		}
		return null;
	}

	public void delete(String path, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.delete(path, config));
		delete(uri, options);
	}

	public void delete(URI uri, DocumentOptions options) {		
		uri = appendQueryParameters(uri, options);
		restTemplate.delete(uri);
	}

	public void share(String path, String username, List<Permission> permissions) {
		URI uri = makeURI(DocumentsEndpoints.share(path, config));
		if (StringUtils.hasText(username) && !(uri.getQuery() != null && uri.getQuery().contains("username"))) {
			uri = appendQueryParameter(uri, "username", username);
		}

		Document document = new Document();
		document.setPath(path);
		document.setPermissions(permissions);
		RequestEntity<Document> request = RequestEntity.put(uri).contentType(MediaType.APPLICATION_JSON).body(document);
		exchange(request, Void.class);
	}

	public void share(String path, List<Permission> permissions) {
		share(path, null, permissions);
	}

	public void shareDelete(String path, String username, List<Permission> permissions) {
		URI uri = makeURI(DocumentsEndpoints.deleteShare(path, config));
		if (StringUtils.hasText(username) && !(uri.getQuery() != null && uri.getQuery().contains("username"))) {
			uri = appendQueryParameter(uri, "username", username);

		}

		Document document = new Document();
		document.setPath(path);
		document.setPermissions(permissions);
		RequestEntity<Document> request = RequestEntity.put(uri).contentType(MediaType.APPLICATION_JSON).body(document);

		exchange(request, Void.class);
	}

	public void shareDelete(String path, List<Permission> permissions) {
		shareDelete(path, null, permissions);
	}

	public static String getUsername(Principal principal) {
		return principal.getName();
	}

	public Document restore(String path, String username) {
		URI uri = makeURI(DocumentsEndpoints.restore(path, config));
		if (StringUtils.hasText(username) && !(uri.getQuery() != null && uri.getQuery().contains("username"))) {
			uri = appendQueryParameter(uri, "username", username);
		}
		RequestEntity<Void> request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<Document> response = exchange(request, Document.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}
		return null;

	}

	public Document restore(String path) {
		return restore(path, null);
	}

	public URI newFolder(String path, String username) {
		URI uri = makeURI(DocumentsEndpoints.folder(path, config));
		if (StringUtils.hasText(username) && !(uri.getQuery() != null && uri.getQuery().contains("username"))) {
			uri = appendQueryParameter(uri, "username", username);
		}
		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		URI response = restTemplate.postForLocation(uri, request);
		return response;
	}

	public URI newFolder(String path) {
		return newFolder(path, null);
	}

	public URI copy(String path, String username) {
		URI uri = makeURI(DocumentsEndpoints.copy(path, config));
		if (StringUtils.hasText(username) && !(uri.getQuery() != null && uri.getQuery().contains("username"))) {
			uri = appendQueryParameter(uri, "username", username);
		}
		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		URI response = restTemplate.postForLocation(uri, request);
		return response;
	}

	public URI copy(String path) {
		return copy(path, null);
	}

	public URI move(String path, String newLocation, String username) {
		URI uri = makeURI(DocumentsEndpoints.move(path, config));
		if (StringUtils.hasText(username) && !(uri.getQuery() != null && uri.getQuery().contains("username"))) {
			uri = appendQueryParameter(uri, "username", username);
		}
		newLocation = UriUtils.encode(newLocation, DocumentsConfiguration.DEFAULT_ENCODING);
		uri = appendQueryParameter(uri, "newLocation", newLocation);

		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		URI response = restTemplate.postForLocation(uri, request);
		return response;
	}

	public URI move(String path, String newLocation) {
		return move(path, newLocation, null);
	}


	protected <T> ResponseEntity<T> exchange(RequestEntity<?> request, Class<T> responseType)
			throws RestClientException {
		return restTemplate.exchange(request, responseType);
	}

}
