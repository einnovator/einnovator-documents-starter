package org.einnovator.documents.client.web;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.einnovator.documents.client.DocumentsClient;
import org.einnovator.documents.client.model.Document;


@RequestMapping({"/api"})
public class DocumentRestController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private DocumentsClient docClient;
	
	
	@GetMapping("/__/**")
	public  ResponseEntity<List<Document>>  list(Principal principal, Authentication authentication, HttpServletRequest request) {
		if (principal==null) {
			logger.error("list: " + format(HttpStatus.UNAUTHORIZED));
			return new ResponseEntity<List<Document>>(HttpStatus.UNAUTHORIZED);	
		}
		String path = getPath(request, "/api/__/");
		setupToken(principal, authentication);
		List<Document> documents = docClient.list(path);
		logger.info("list: " + path + " " + documents);

		return new ResponseEntity<List<Document>>(documents, HttpStatus.OK);
	}
		
	
	@GetMapping({ "/_meta/**" })
	public ResponseEntity<Document> meta(Principal principal, Authentication authentication, HttpServletRequest request) {
		if (principal==null) {
			logger.error("meta:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return new ResponseEntity<Document>(HttpStatus.UNAUTHORIZED);	
		}
		
		String path = getPath(request, "/api/_meta/");

		setupToken(principal, authentication);
		Document document = docClient.meta(path);
		
		if (document==null) {
			logger.error("meta: " + HttpStatus.NOT_FOUND.getReasonPhrase() + ": " + path);			
			return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);	
		}

		logger.info("meta: " + document);
		return ResponseEntity.ok(document);
	}
	
	@PostMapping("/__/**")
	public ResponseEntity<Void> mkdir(@ModelAttribute("document") Document document, BindingResult errors, HttpServletRequest request, Principal principal, Authentication authentication) {
		if (principal==null) {
			logger.error("newFolder:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);	
		}
		if (errors.hasErrors()) {
			logger.error("newFolder:  " + HttpStatus.BAD_REQUEST.getReasonPhrase() + ":" + errors);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		String path = getPath(request, "/api/__/");
		
		try {
			setupToken(principal, authentication);
			URI uri = docClient.newFolder(path);
			if (uri==null) {
				logger.error("newFolder: " + document);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			return ResponseEntity.created(uri).build();
		} catch (RuntimeException e) {
			logger.error("createPOST: " +document + " " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	
	
	
	@DeleteMapping("/__/**")
	public String remove(@PathVariable("id") String id, Principal principal, HttpServletRequest request) {
		if (principal==null) {
			logger.error("createPOST:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return "redirect:/";
		}
		String path = getPath(request, "/api/__/");

		Document document = docClient.meta(path);
		
		if (document==null) {
			logger.error("show: " + HttpStatus.NOT_FOUND.getReasonPhrase() + ": " + id);			
			return "redirect:/document";
		}
		Boolean force = false;
		try {
			docClient.delete(path, force);
		} catch (RuntimeException e) {
			logger.error("remove: " +document + " " + e);
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
