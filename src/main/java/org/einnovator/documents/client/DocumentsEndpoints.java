package org.einnovator.documents.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.einnovator.util.UriUtils;


public class DocumentsEndpoints {
	
	private static String encodePath(String path) {
		return UriUtils.encodePath(path, DocumentsConfiguration.DEFAULT_ENCODING);
	}

	public static String upload(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/_upload/"+encodePath(path);
	}
	
	public static String download(String path, DocumentsConfiguration config) {
		return config.getUri() + "/_/" + encodePath(path);
	}
	
	public static String meta(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/_meta/" + encodePath(path);
	}
	
	public static String list(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/__/" + encodePath(path);
	}

	public static String delete(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/__/" + encodePath(path);
	}

	public static String share(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/_share/" + encodePath(path);
	}
	
	public static String deleteShare(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/_unshare/" + encodePath(path);
	}
	
	public static String create(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/_meta/" + encodePath(path);
	}
	
	public static String restore(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/_restore/" + encodePath(path);
	}
	
	public static String folder(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/__/" + encodePath(path);
	}

	public static String copy(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/_copy/" + encodePath(path);
	}
	
	public static String move(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/_move/" + encodePath(path);
	}
	
	public static String templates(DocumentsConfiguration config) {
		return config.getUri() + "/api/template";
	}
	
	public static String template(String path, DocumentsConfiguration config) {
		return config.getUri() + "/api/template/" + encodePath(path);
	}
	
	public static URI makeURI(String uri) {
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}		
	}

	

}
