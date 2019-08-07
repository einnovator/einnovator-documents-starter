package org.einnovator.documents.client.web;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.einnovator.documents.client.manager.DocumentManager;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.util.PageOptions;


@RequestMapping({"/api"})
public class DocumentRestController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	protected DocumentManager manager;
	
	
	@GetMapping({ "/_/**" })
	@CrossOrigin(origins = "*")
	public ResponseEntity<Void> download(DocumentOptions options,
			HttpServletResponse response, HttpServletRequest request, Principal principal) {
		
		String path = getPath(request, "/_/");

		try {
			logger.debug("download: " + path + " " + format(principal));

			Document document = manager.read(path, options);

			if (document == null) {
				return badrequest("download", response, path);
			}

			response.setContentLengthLong(document.getContentLength());
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
			return status(String.format("download: %s", path), e, response);
		}
	}
	
	@GetMapping("/__/**")
	public  ResponseEntity<List<Document>>  list(DocumentFilter filter, PageOptions options,
			Principal principal, Authentication authentication, HttpServletRequest request) {
		if (principal==null) {
			logger.error("list: " + format(HttpStatus.UNAUTHORIZED));
			return new ResponseEntity<List<Document>>(HttpStatus.UNAUTHORIZED);	
		}
		String path = getPath(request, "/api/__/");
		setupToken(principal, authentication);
		List<Document> documents = manager.list(path, filter, options.toPageRequest());
		logger.info("list: " + path + " " + documents);

		return new ResponseEntity<List<Document>>(documents, HttpStatus.OK);
	}
		
	
	@GetMapping({ "/_meta/**" })
	public ResponseEntity<Document> meta(DocumentOptions options,
			Principal principal, Authentication authentication, HttpServletRequest request) {
		if (principal==null) {
			logger.error("meta:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return new ResponseEntity<Document>(HttpStatus.UNAUTHORIZED);	
		}
		
		String path = getPath(request, "/api/_meta/");

		setupToken(principal, authentication);
		Document document = manager.read(path, options);
		
		if (document==null) {
			logger.error("meta: " + HttpStatus.NOT_FOUND.getReasonPhrase() + ": " + path);			
			return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);	
		}

		logger.info("meta: " + document);
		return ResponseEntity.ok(document);
	}
	
	@PostMapping("/__/**")
	public ResponseEntity<Void> mkdir(@ModelAttribute("document") Document document, DocumentOptions options,
			HttpServletRequest request, Principal principal, Authentication authentication) {
		if (principal==null) {
			logger.error("mkdir:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);	
		}
		String path = getPath(request, "/api/__/");
		
		try {
			setupToken(principal, authentication);
			URI uri = manager.mkdir(path, options);
			if (uri==null) {
				logger.error("mkdir: " + document);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			return ResponseEntity.created(uri).build();
		} catch (RuntimeException e) {
			logger.error("mkdir: " + document + " " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	
	@DeleteMapping("/__/**")
	public String delete(DocumentOptions options,
			Principal principal, Authentication authentication, HttpServletRequest request) {
		if (principal==null) {
			logger.error("delete:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return redirect("/");
		}
		String path = getPath(request, "/api/__/");

		setupToken(principal, authentication);
		if (!manager.delete(path, options)) {
			logger.error("delete: " + HttpStatus.NOT_FOUND.getReasonPhrase() + ": " + path);			
			return "redirect:/document";
		}
		return"redirect:/document";
	}

	
	private String getPath(HttpServletRequest request, String prefix) {
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
			logger.error("getPath: could not decode URL ", e);
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
