package org.einnovator.documents.client;

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
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.einnovator.documents.client.model.Document;

public class DocumentController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private DocumentsClient docClient;
	
	@GetMapping("/__/**")
	public String list(Principal principal, Authentication authentication, HttpServletRequest request, Model model) {
		if (principal==null) {
			logger.error("show:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());			
			return "redirect:/";
		}
		String path = getPath(request, "/__/");

		setupToken(principal, authentication);
		List<Document> documents = docClient.list(path);
		
		model.addAttribute("documents", documents);
		return "document/list";
	}
		
	
	@GetMapping({ "/_meta/**" })
	public String meta(Principal principal, Authentication authentication, HttpServletRequest request, Model model) {
		if (principal==null) {
			logger.error("meta:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return "redirect:/";
		}
		
		String username = principal.getName();

		String path = getPath(request, "/_meta/");

		setupToken(principal, authentication);
		Document document = docClient.meta(path, username);
		
		if (document==null) {
			logger.error("show: " + HttpStatus.NOT_FOUND.getReasonPhrase() + ": " + path);			
			return "redirect:/document";
		}

		model.addAttribute("document", document);
		logger.info("show: " + document);
		return "document/show";
	}
	
	@GetMapping("/_create")
	public String createGET(Principal principal, Model model) {
		if (principal==null) {
			logger.error("createGET:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return "redirect:/";
		}
		
		Document document = new Document();
		model.addAttribute("document", document);

		return "document/edit";
	}

	@GetMapping("/_edit/**")
	public String editGet(@PathVariable("id") String id, Principal principal, Authentication authentication, HttpServletRequest request, Model model) {
		if (principal==null) {
			logger.error("show:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return "redirect:/";
		}
		String path = getPath(request, "/__/");

		setupToken(principal, authentication);
		Document document = docClient.meta(path);
		
		if (document==null) {
			logger.error("show: " + HttpStatus.NOT_FOUND.getReasonPhrase() + ": " + id);			
			return "redirect:/document";
		}

		model.addAttribute("document", document);
		logger.info("show: " + document);

		return "document/edit";
	}

	@PostMapping("/__/**")
	public String createPOST(@ModelAttribute("document") Document document, BindingResult errors, HttpServletRequest request, Principal principal, Authentication authentication, Model model) {
		if (principal==null) {
			logger.error("createPOST:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return "redirect:/";
		}
		if (errors != null && errors.hasErrors()) {
			logger.error("createPOST:  " + HttpStatus.BAD_REQUEST.getReasonPhrase() + ":" + errors);
			return "document/edit";
		}
		String path = getPath(request, "/__/");

		boolean publicLink= true;
		
		
		URI uri = null;
		
		try {
			setupToken(principal, authentication);
			uri = docClient.upload(path, document, publicLink);			
			if (uri==null) {
				logger.error("createPOST: " + document);
				return "redirect:/document";
			}
		} catch (RuntimeException e) {
			logger.error("createPOST: " +document + " " + e);
			return "redirect:/document";
		}

		
		logger.info("createPOST: " + document);

		
		return "redirect:/document/" + document.getName();
	}
	
	
	
	
	@DeleteMapping("/__/**")
	public String remove(@PathVariable("id") String id, Principal principal, HttpServletRequest request, Model model) {
		if (principal==null) {
			logger.error("createPOST:  " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return "redirect:/";
		}
		String path = getPath(request, "/__/");

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
