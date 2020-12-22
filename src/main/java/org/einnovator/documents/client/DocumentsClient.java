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
import org.einnovator.documents.client.manager.DocumentManager;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.util.UriUtils;
import org.einnovator.util.security.Authority;
import org.einnovator.util.web.RequestOptions;
import org.einnovator.util.web.Result;
import org.einnovator.util.web.WebUtil;
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
import org.springframework.web.client.RestTemplate;

/**
 * Client API for Documents Service.
 * 
 * <p>Provide methods for all server endpoints, mostly to perform {@link Document} I/O or control operations.
 * <p>Errors are propagated using Java runtime exceptions.
 * <p>For caching enabled "high-level" API, see {@link DocumentManager}.
 * <p>{@code DocumentsClientConfiguration} specifies configuration details, including server URL and client credentials.
 * <p>Property {@link #getConfig()} provides the default {@code DocumentsClientConfiguration} to use.
 * <p>All API methods that invoke a server endpoint accept an <em>optional</em> tail parameter to connect to alternative server
 *  (e.g. for cover the less likely case where an application need to connect to multiple servers in different clusters).
 * <p>Internally, {@code DocumentsClient} uses a {@code OAuth2RestTemplate} to invoke remote server.
 * <p>When setup as a <b>Spring Bean</b> both {@code SsoClientConfiguration} and {@code OAuth2RestTemplate} are auto-configured.
 * 
 * @see org.einnovator.documents.client.manager.DocumentManager
 * 
 * @author support@einnovator.org
 *
 */
public class DocumentsClient {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private DocumentsClientConfiguration config;

	@Autowired
	@Qualifier("documentsRestTemplate")
	private OAuth2RestTemplate restTemplate;

	private RestTemplate restTemplate0;

	private boolean web = true;
	
	/**
	 * Create instance of {@code DocumentsClient}.
	 *
	 */
	@Autowired
	public DocumentsClient() {
	}

	/**
	 * Create instance of {@code DocumentsClient}.
	 *
	 * @param config the {@code DocumentsClientConfiguration}
	 */
	public DocumentsClient(DocumentsClientConfiguration config) {
		this.config = config;
	}

	/**
	 * Create instance of {@code DocumentsClient}.
	 *
	 * @param restTemplate the {@code OAuth2RestTemplate} used for HTTP transport
	 * @param config the {@code DocumentsClientConfiguration}
	 */
	public DocumentsClient(OAuth2RestTemplate restTemplate, DocumentsClientConfiguration config) {
		this.restTemplate = restTemplate;
		this.config = config;
	}
	
	/**
	 * Create instance of {@code DocumentsClient}.
	 *
	 * @param restTemplate the {@code OAuth2RestTemplate} used for HTTP transport
	 * @param config the {@code DocumentsClientConfiguration}
	 */
	public DocumentsClient(RestTemplate restTemplate, DocumentsClientConfiguration config) {
		this.restTemplate0 = restTemplate;
		this.config = config;
	}

	//
	// Getters/Setters
	//
	
	/**
	 * Get the value of property {@code config}.
	 *
	 * @return the config
	 */
	public DocumentsClientConfiguration getConfig() {
		return config;
	}

	/**
	 * Set the value of property {@code config}.
	 *
	 * @param config the value of property config
	 */
	public void setConfig(DocumentsClientConfiguration config) {
		this.config = config;
	}

	/**
	 * Get the value of property {@code restTemplate}.
	 *
	 * @return the restTemplate
	 */
	public OAuth2RestTemplate getRestTemplate() {
		return restTemplate;
	}

	/**
	 * Set the value of property {@code restTemplate}.
	 *
	 * @param restTemplate the value of property restTemplate
	 */
	public void setRestTemplate(OAuth2RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Get the value of property {@code restTemplate0}.
	 *
	 * @return the restTemplate0
	 */
	public RestTemplate getRestTemplate0() {
		return restTemplate0;
	}

	/**
	 * Set the value of property {@code restTemplate0}.
	 *
	 * @param restTemplate0 the value of property restTemplate0
	 */
	public void setRestTemplate0(RestTemplate restTemplate0) {
		this.restTemplate0 = restTemplate0;
	}

	/**
	 * Get the value of property {@code web}.
	 *
	 * @return the web
	 */
	public boolean isWeb() {
		return web;
	}

	/**
	 * Set the value of property {@code web}.
	 *
	 * @param web the value of property web
	 */
	public void setWeb(boolean web) {
		this.web = web;
	}
	
	//
	// Documents and Folders
	//


	/**
	 * Write a {@code Document}.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner of {@code Document} folder.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document} folder.

	 * @param document the {@code Document}
	 * @param options optional {@code DocumentOptions}
	 
	 * @return the location {@code URI} for the written {@code Document}
	 * @throws RestClientException if request fails
	 */
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

			URI response = postForLocation(uri, requestEntity, options);
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

	/**
	 * Read a {@code Document}.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Member of {@code Document} group if sharing type is {@code RESTRICTED}.
	 * 
	 * @param path the {@code Document} path in the tree of the user (the principal, by default)
	 * @param options optional {@code DocumentOptions}
	 
	 * @return the {@code Document}
	 * @throws RestClientException if request fails
	 */
	public Document read(String path, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.download(path, config));
		return read(uri, options);
	}

	/**
	 * Read a {@code Document}.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Member of {@code Document} group if sharing type is {@code RESTRICTED}.
	 * 
	 * @param uri the {@code URI} of the {@code Document}
	 * @param options optional {@code DocumentOptions}
	 
	 * @return the {@code Document}
	 * @throws RestClientException if request fails
	 */
	public Document read(URI uri, DocumentOptions options) {
		Document document = null;

		if (DocumentOptions.isMeta(options)) {
			URI metaUri = makeMetaUri(uri);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("read meta: %s %s", uri, metaUri));				
			}
			UriUtils.appendQueryParameters(metaUri, options);
			RequestEntity<Void> request = RequestEntity.get(metaUri).accept(MediaType.APPLICATION_JSON).build();
			ResponseEntity<Document> response = exchange(request, Document.class, options);			
			document = response.getBody();
		}

		if (DocumentOptions.isContent(options)) {
				String contentType = document != null ? document.getContentType() : null;
				byte[] bytes = content(uri, options, contentType);
				if (bytes != null) {
					if (document==null) {
						document = new Document(uri, bytes);
					}
					document.setContent(bytes);
				}
		}
		if (DocumentOptions.isVersions(options)) {
		}
		
		if (DocumentOptions.isAttachments(options)) {
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

	/**
	 * Read content of a {@code Document}.
	 * 
	 * @param uri the {@code URI} of the {@code Document}
	 * @param options optional {@code DocumentOptions}
	 * @param contentType the requested content type
	 
	 * @return the content
	 * @throws RestClientException if request fails
	 */
	public byte[] content(URI uri, DocumentOptions options, String contentType) {
		uri = UriUtils.appendQueryParameters(uri, options);
		HeadersBuilder<?> builder = RequestEntity.get(uri);
		if (!StringUtils.hasText(contentType)) {
			contentType = MediaType.ALL.toString();
		}
		builder.accept(MediaType.valueOf(contentType));
		RequestEntity<Void> request = builder.build();
		ResponseEntity<byte[]> response = exchange(request, byte[].class, options);
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("content: %s %s", uri, response.getBody().length));			
		}
		return response.getBody();
	}

	/**
	 * List {@code Document}s in a folder.
	 * 
	 * <p><b>Required Security Credentials</b>: Matching {@link Document#getSharing()} and {@link Document#getAuthorities()}.
	 * 
	 * @param path the folder path in the tree of the user (the principal, by default)
	 * @param filter a {@code DocumentFilter}
	 * @param pageable a {@code Pageable} (optional)
	 
	 * @throws RestClientException if request fails
	 * @return a {@code List} with {@code Document}s
	 * @throws RestClientException if request fails
	 */
	public List<Document> list(String path, DocumentFilter filter, Pageable pageable) {
		URI uri = makeURI(DocumentsEndpoints.list(path, config));
		uri = processURI(uri, filter, pageable);
		RequestEntity<Void> request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<Document[]> response = exchange(request, Document[].class, filter);
		if (response.getStatusCode() == HttpStatus.OK) {
			Document[] documents = response.getBody();
			return Arrays.asList(documents);
		}
		return null;
	}

	/**
	 * Delete or moves recycle-bin/trash to existing {@code Document}
	 * 
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param path the {@code Document} path in the tree of the user (the principal, by default)
	 * @param options optional {@code RequestOptions}
	 
	 * @throws RestClientException if request fails
	 */
	public void delete(String path, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.delete(path, config));
		delete(uri, options);
	}

	/**
	 * Delete or moves recycle-bin/trash to existing {@code Document}
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param uri the {@code URI} of the {@code Document}
	 * @param options optional {@code RequestOptions}
	 
	 * @throws RestClientException if request fails
	 */
	public void delete(URI uri, DocumentOptions options) {		
		uri = appendQueryParameters(uri, options);
		RequestEntity<Void> request = RequestEntity.delete(uri).build();
		exchange(request, Void.class, options);
	}

	/**
	 * Restore previous delete {@code Document} if found in recycle-bin/trash folder.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param path the {@code Document} path in the tree of the user (the principal, by default)
	 * @param options optional {@code RequestOptions}
	 * @return the restored {@code Document} meta-data
	 * @throws RestClientException if request fails
	 */
	public Document restore(String path, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.restore(path, config));
		uri = appendQueryParameters(uri, options);
		RequestEntity<Void> request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<Document> response = exchange(request, Document.class, options);
		return response.getBody();
	}


	/**
	 * Restore previous delete {@code Document} if found in recycle-bin/trash folder.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param uri the {@code URI} of the {@code Document}
	 * @param options optional {@code RequestOptions}
	 * @return the restored {@code Document} meta-data
	 * @throws RestClientException if request fails
	 */
	public Document restore(URI uri, DocumentOptions options) {
		return restore(getPath(uri), options);
	}

	/**
	 * Make a new directory/folder.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner of parent folder.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the parent folder.
	 * @param path the folder path in the tree of the user (the principal, by default)
	 * @param options optional {@code DocumentOptions}
	 * @return the location {@code URI} for the written folder 
	 * @throws RestClientException if request fails
	 */
	public URI mkdir(String path, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.mkdir(path, config));
		uri = appendQueryParameters(uri, options);
		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<?> response = exchange(request, Void.class, options);
		return response.getHeaders().getLocation();
	}
	
	/**
	 * Copy {@code Document}
	 * 
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param path the source {@code Document} path, in the tree of the user (the principal, by default)
	 * @param destPath the path where to write the {@code Document} copy path, in the tree of the user (the principal, by default)
	 * @param options optional {@code RequestOptions}
	 * @return the location {@code URI} for the {@code Document} copy
	 * @throws RestClientException if request fails
	 */
	public URI copy(String path, String destPath, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.copy(path, config));
		uri = appendQueryParameters(uri, options);
		destPath = UriUtils.encode(destPath, DocumentsClientConfiguration.DEFAULT_ENCODING);
		uri = appendQueryParameter(uri, "path", destPath);

		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<?> response = exchange(request, Void.class, options);
		return response.getHeaders().getLocation();
	}

	/**
	 * Move {@code Document}
	 * 
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param path the original {@code Document} path, in the tree of the user (the principal, by default)
	 * @param destPath the path where to move the {@code Document}, in the tree of the user (the principal, by default)
	 * @param options optional {@code RequestOptions} 
	 * @return the new location {@code URI} for the {@code Document}
	 * @throws RestClientException if request fails
	 */
	public URI move(String path, String destPath, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.move(path, config));
		uri = appendQueryParameters(uri, options);
		destPath = UriUtils.encode(destPath, DocumentsClientConfiguration.DEFAULT_ENCODING);
		uri = appendQueryParameter(uri, "path", destPath);
		RequestEntity<Void> request = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<?> response = exchange(request, Void.class, options);
		return response.getHeaders().getLocation();
	}

	//
	// Authorities
	//

	/**
	 * Add {@code Authority} to {@code Document} in the specified path.
	 * 
	 * @param path the {@code Document} path, in the tree of the user (the principal, by default)
	 * @param authority the  {@code Authority}
	 * @param options optional {@code RequestOptions} 
	 * @return the {@code URI} that uniquely identifies the {@code Authority}
	 * @throws RestClientException if request fails
	 */
	public URI addAuthority(String path, Authority authority, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.authorities(path, config));
		uri = appendQueryParameters(uri, options);
		Document document = new Document();
		document.setPath(path);
		RequestEntity<Authority> request = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON).body(authority);
		ResponseEntity<?> response = exchange(request, Void.class, options);
		return response.getHeaders().getLocation();
	}

	/**
	 * Remove {@code Authority} from {@code Document} in the specified path.
	 * 
	 * @param path the {@code Document} path, in the tree of the user (the principal, by default)
	 * @param id the  {@code Authority} identifier (UUID)
	 * @param options optional {@code RequestOptions}
	 * @throws RestClientException if request fails
	 */
	public void removeAuthority(String path, String id, DocumentOptions options) {
		URI uri = makeURI(DocumentsEndpoints.authorities(path, config));
		uri = appendQueryParameter(uri, "id", id);
		uri = processURI(uri, options);
		Document document = new Document();
		document.setPath(path);
		RequestEntity<Void> request = RequestEntity.delete(uri).build();
		exchange(request, Void.class, options);
	}

	//
	// HTTP transport
	//
	
	/**
	 * Submit HTTP request.
	 * 
	 * 
	 * @param <T> response type
	 * @param request the {@code RequestEntity}
	 * @param responseType the response type
	 * @param options optional {@code SsoClientContext}
	 * @return result {@code ResponseEntity}
	 * @throws RestClientException if request fails
	 */
	protected <T> ResponseEntity<T> exchange(RequestEntity<?> request, Class<T> responseType, RequestOptions options) throws RestClientException {
		RestTemplate restTemplate = getRequiredRestTemplate();

		try {
			return exchange(restTemplate, request, responseType);			
		} catch (RuntimeException e) {
			if (options!=null && !options.isSingleton()) {
				options.setResult(new Result<Object>(e));
			}
			throw e;
		}
	}
	
	/**
	 * Submit HTTP POST request.
	 * 
	 * @param uri the request {@code URI}
	 * @param request the {@code HttpEntity}
	 * @param options optional {@code SsoClientContext}
	 * @return result {@code ResponseEntity}
	 * @throws RestClientException if request fails
	 */
	protected URI postForLocation(URI uri, HttpEntity<LinkedMultiValueMap<String, Object>> request, RequestOptions options) throws RestClientException {
		RestTemplate restTemplate = getRequiredRestTemplate();
		try {
			return postForLocation(restTemplate, uri, request);			
		} catch (RuntimeException e) {
			if (options!=null && !options.isSingleton()) {
				options.setResult(new Result<Object>(e));
			}
			throw e;
		}
	}

	/**
	 * Get the {@code OAuth2RestTemplate} to use to perform a request.
	 * 
	 * If the options is not null, returns the {@code OAuth2RestTemplate} specified by the options (if any).
	 * Otherwise, return the configured {@code OAuth2RestTemplate} in property {@link #restTemplate}.
	 * If property {@link web} is true, check if current thread is bound to a web request with a session-scope. If not, fallback
	 * to client credential {@code OAuth2RestTemplate} in property {@link #restTemplate0} or create one if needed.
	 * 
	 * @return the {@code OAuth2RestTemplate}
	 */
	protected RestTemplate getRequiredRestTemplate() {
		RestTemplate restTemplate = this.restTemplate;
		if (this.restTemplate0!=null) {
			if (restTemplate==null || WebUtil.getHttpServletRequest()==null) {
				restTemplate = this.restTemplate0;				
			}
		}			
		return restTemplate;
	}
	

	/**
	 * Submit HTTP request.
	 * 
	 * May be overriden by sub-classes for custom/advanced functionality.
	 * 
	 * @param <T> response type
	 * @param restTemplate the {@code RestTemplate} to use
	 * @param request the {@code RequestEntity}
	 * @param responseType the response type
	 * @return the result {@code ResponseEntity}
	 * @throws RestClientException if request fails
	 */
	protected <T> ResponseEntity<T> exchange(RestTemplate restTemplate, RequestEntity<?> request, Class<T> responseType) throws RestClientException {
		return restTemplate.exchange(request, responseType);
	}

	/**
	 * Submit HTTP request.
	 * 
	 * May be overriden by sub-classes for custom/advanced functionality.
	 * 
	 * @param <T> response type
	 * @param restTemplate the {@code RestTemplate} to use
	 * @param uri the request {@code URI}
	 * @param request the {@code HttpEntity}
	 * @param request the {@code RequestEntity}
	 * @return the result {@code ResponseEntity}
	 * @throws RestClientException if request fails
	 */
	protected <T> URI postForLocation(RestTemplate restTemplate, URI uri, HttpEntity<LinkedMultiValueMap<String, Object>> request) throws RestClientException {
		return restTemplate.postForLocation(uri, request);
	}
	
	//
	// Other
	//

	/**
	 * Process URI by adding parameters from properties of specified objectes.
	 * 
	 * @param uri the {@code URI}
	 * @param objs a variadic array of objects
	 * @return the processed {@code URI}
	 */
	private static URI processURI(URI uri, Object... objs) {
		return UriUtils.appendQueryParameters(uri, objs);
	}
	
	
}
