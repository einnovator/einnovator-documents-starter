package org.einnovator.documents.client;

import static org.einnovator.util.UriUtils.appendQueryParameter;
import static org.einnovator.util.UriUtils.appendQueryParameters;
import static org.einnovator.util.UriUtils.makeURI;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.documents.client.config.DocumentsClientConfiguration;
import org.einnovator.documents.client.config.DocumentsEndpoints;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.util.UriUtils;
import org.einnovator.util.security.Authority;
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

/**
 * Client API for Documents Service.
 *
 */
public class DocumentsClient {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private DocumentsClientConfiguration config;

	@Autowired
	@Qualifier("documentsRestTemplate")
	private OAuth2RestTemplate restTemplate;

	@Autowired
	public DocumentsClient() {
	}

	public DocumentsClient(DocumentsClientConfiguration config) {
		this.config = config;
	}

	public DocumentsClient(OAuth2RestTemplate restTemplate, DocumentsClientConfiguration config) {
		this.restTemplate = restTemplate;
		this.config = config;
	}

	public OAuth2RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(OAuth2RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}


	public URI write(Document document, DocumentOptions options, DocumentsClientConfiguration context) {
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
		copy.setInfo(document.getInfo());
		copy.setName(document.getName());
		copy.setAuthorities(document.getAuthorities());
		copy.setCategory(document.getCategory());
		copy.setTags(document.getTags());
		return copy;
	}

	public Document read(String path, DocumentOptions options, DocumentsClientConfiguration context) {
		URI uri = makeURI(DocumentsEndpoints.download(path, config));
		return read(uri, options, context);
	}

	public Document read(URI uri, DocumentOptions options, DocumentsClientConfiguration context) {
		Document document = null;

		if (DocumentOptions.meta(options)) {
			URI metaUri = makeMetaUri(uri);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("read meta: %s %s", uri, metaUri));				
			}
			UriUtils.appendQueryParameters(metaUri, options);
			RequestEntity<Void> request = RequestEntity.get(metaUri).accept(MediaType.APPLICATION_JSON).build();
			ResponseEntity<Document> response = exchange(request, Document.class);			
			document = response.getBody();
		}

		if (DocumentOptions.content(options)) {
				String contentType = document != null ? document.getContentType() : null;
				byte[] bytes = content(uri, options, contentType, context);
				if (bytes != null) {
					if (document==null) {
						document = new Document(uri, bytes);
					}
					document.setContent(bytes);
				}
		}
		if (DocumentOptions.versions(options)) {
			
		}
		
		if (DocumentOptions.attachments(options)) {
			
		}

		return document;
	}

	private URI makeMetaUri(URI uri) {
		return URI.create(uri.toString().replace("/_/", "/api/_meta/"));
	}
	
	private String getPath(URI uri) {
		String path = uri.getPath();
		if (path.startsWith("/_/") && path.length()>="/_/".length()) {
			path = path.substring("/_/".length()-1);
		}
		return path;
	}

	public byte[] content(URI uri, DocumentOptions options, String contentType, DocumentsClientConfiguration context) {
		uri = UriUtils.appendQueryParameters(uri, options);
		HeadersBuilder<?> builder = RequestEntity.get(uri);
		if (!StringUtils.hasText(contentType)) {
			contentType = MediaType.ALL.toString();
		}
		builder.accept(MediaType.valueOf(contentType));
		RequestEntity<Void> request = builder.build();
		ResponseEntity<byte[]> response = exchange(request, byte[].class);
		if (logger.isDebugEnabled()) {
			System.out.println("content:" + uri + " " + response.getBody().length);			
		}
		return response.getBody();
	}

	public List<Document> list(String path, DocumentFilter filter, Pageable pageable, DocumentsClientConfiguration context) {
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

	public void delete(String path, DocumentOptions options, DocumentsClientConfiguration context) {
		URI uri = makeURI(DocumentsEndpoints.delete(path, config));
		delete(uri, options, context);
	}

	public void delete(URI uri, DocumentOptions options, DocumentsClientConfiguration context) {		
		uri = appendQueryParameters(uri, options);
		restTemplate.delete(uri);
	}

	public Document restore(URI uri, DocumentOptions options, DocumentsClientConfiguration context) {
		return restore(getPath(uri), options, context);
	}

	public Document restore(String path, DocumentOptions options, DocumentsClientConfiguration context) {
		URI uri = makeURI(DocumentsEndpoints.restore(path, config));
		uri = appendQueryParameters(uri, options);
		RequestEntity<Void> request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<Document> response = exchange(request, Document.class);
		return response.getBody();
	}

	public URI mkdir(String path, DocumentOptions options, DocumentsClientConfiguration context) {
		URI uri = makeURI(DocumentsEndpoints.folder(path, config));
		uri = appendQueryParameters(uri, options);
		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		URI response = restTemplate.postForLocation(uri, request);
		return response;
	}

	public URI copy(String path, String destPath, DocumentOptions options, DocumentsClientConfiguration context) {
		URI uri = makeURI(DocumentsEndpoints.copy(path, config));
		uri = appendQueryParameters(uri, options);
		destPath = UriUtils.encode(destPath, DocumentsClientConfiguration.DEFAULT_ENCODING);
		uri = appendQueryParameter(uri, "path", destPath);

		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		URI response = restTemplate.postForLocation(uri, request);
		return response;
	}

	public URI move(String path, String destPath, DocumentOptions options, DocumentsClientConfiguration context) {
		URI uri = makeURI(DocumentsEndpoints.move(path, config));
		uri = appendQueryParameters(uri, options);
		destPath = UriUtils.encode(destPath, DocumentsClientConfiguration.DEFAULT_ENCODING);
		uri = appendQueryParameter(uri, "path", destPath);
		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		return restTemplate.postForLocation(uri, request);
	}


	public URI addAuthority(String path, Authority authority, DocumentOptions options, DocumentsClientConfiguration context) {
		URI uri = makeURI(DocumentsEndpoints.authorities(path, config));
		uri = appendQueryParameters(uri, options);
		Document document = new Document();
		document.setPath(path);
		RequestEntity<Authority> request = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON).body(authority);
		return postForLocation(uri, request);
	}

	public void removeAuthority(String path, String id, DocumentOptions options, DocumentsClientConfiguration context) {
		URI uri = makeURI(DocumentsEndpoints.authorities(path, config));
		uri = appendQueryParameter(uri, "id", id);

		uri = appendQueryParameters(uri, options);
		Document document = new Document();
		document.setPath(path);
		RequestEntity<Void> request = RequestEntity.delete(uri).build();
		exchange(request, Void.class);
	}

	protected <T> ResponseEntity<T> exchange(RequestEntity<?> request, Class<T> responseType) throws RestClientException {
		return restTemplate.exchange(request, responseType);
	}

	protected URI postForLocation(URI url, RequestEntity<?> request) throws RestClientException {
		return restTemplate.postForLocation(url, request);
	}

}
