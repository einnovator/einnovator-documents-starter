package org.einnovator.documents.client.web;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.einnovator.documents.client.manager.DocumentManager;
import org.einnovator.documents.client.manager.LocalDocumentManager;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.util.PageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping({"/api"})
public class DocumentRestController extends ControllerBase {
	
	@Autowired
	protected DocumentManager manager;

	@Autowired
	protected LocalDocumentManager documentManagerLocal;

	
	@GetMapping({ "/_/**" })
	@CrossOrigin(origins = "*")
	public ResponseEntity<Void> download(DocumentOptions options,
			HttpServletResponse response, HttpServletRequest request, Principal principal) {
		
		String path = getPath(request, "/_/");

		try {
			if (logger.isDebugEnabled()) {
	 			debug("download", path, principal.getName());				
			}

			Document document = manager.read(path, options, null);

			if (document == null) {
				return badrequest("download", response, path);
			}

			if (document.getContentLength()!=null) {
				response.setContentLengthLong(document.getContentLength());				
			}
			if (StringUtils.hasText(document.getContentEncoding())) {
				response.setCharacterEncoding(document.getContentEncoding());
			}
			if (StringUtils.hasText(document.getContentLanguage())) {
				response.setHeader(HttpHeaders.CONTENT_LANGUAGE, document.getContentLanguage());
			}
			if (StringUtils.hasText(document.getContentType())) {
				response.setHeader(HttpHeaders.CONTENT_TYPE, document.getContentType());
			}
			if (StringUtils.hasText(document.getContentDisposition())) {
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, document.getContentDisposition());
			}

			InputStream inputStream = document.getOrCreateInputStream();
			StreamUtils.copy(inputStream, response.getOutputStream());
			inputStream.close();

			return null;
		} catch (Exception e) {
			return status("download", e, response, path);
		}
	}
	
	protected Document read(String path, DocumentOptions options) {
		if (isLocal(path)) {
			return documentManagerLocal.read(path, options, null);
		}
		Document document = manager.read(path, options, null);
		return document;
	}
	
	/**
	 * Check if uri or path is local.
	 * 
	 * @param path the uri or path
	 * @return true, if local path or uri; false, otherwise.
	 */
	protected boolean isLocal(String path) {
		return false;
	}

	@GetMapping("/__/**")
	public  ResponseEntity<List<Document>>  list(DocumentFilter filter, PageOptions options,
			Principal principal, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

		String path = getPath(request, "/api/__/");
		setupToken(principal, authentication);
		
		List<Document> documents = manager.list(path, filter, options.toPageRequest(), null);
		
		if (logger.isDebugEnabled()) {
 			debug("list", path, documents!=null ? documents.size() : null);				
		}

		return ok(documents, "list", response);
	}
		
	
	@GetMapping({ "/_meta/**" })
	public ResponseEntity<Document> meta(DocumentOptions options,
			Principal principal, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

		
		String path = getPath(request, "/api/_meta/");

		setupToken(principal, authentication);
		Document document = manager.read(path, options, null);
		
		if (document==null) {
			return notfound("meta", response, path);
		}
		return ok(document, "meta", response);
	}
	
	@PostMapping("/__/**")
	public ResponseEntity<Void> mkdir(@ModelAttribute("document") Document document, DocumentOptions options,
			HttpServletRequest request, Principal principal, Authentication authentication, HttpServletResponse response) {

		String path = getPath(request, "/api/__/");
		
		try {
			setupToken(principal, authentication);
			URI uri = manager.mkdir(path, options, null);
			if (uri==null) {
				return badrequest("mkdir", response, path);
			}
			return ResponseEntity.created(uri).build();
		} catch (RuntimeException e) {
			return badrequest("mkdir", response, e, path);
		}		
	}
	
	
	@DeleteMapping("/__/**")
	public ResponseEntity<Void> delete(DocumentOptions options,
			Principal principal, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

		String path = getPath(request, "/api/__/");

		try {
			setupToken(principal, authentication);
			if (!manager.delete(path, options, null)) {
				return notfound("delete", response, path);
			}
			return nocontent("delete", response, path);
		} catch (RuntimeException e) {
			return badrequest("delete", response, e, path);
		}	
		
	}

	
	protected String getPath(HttpServletRequest request, String prefix) {
		String path = request.getPathInfo();
				
		if (path == null) {
			path = request.getServletPath();
		}
		if (path == null) {
			return "";
		}
		
		String aux = path;
		
		try {
			while(!path.equals(URLDecoder.decode(path, "UTF-8"))) {
				path = URLDecoder.decode(path, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			error("getPath: could not decode URL", e, path);
			path = aux;
		}
		
		if (path.startsWith(prefix)) {
			path = path.substring(prefix.length());
		}
		if (path == null) {
			return "";
		}
		return path;
	}


}
