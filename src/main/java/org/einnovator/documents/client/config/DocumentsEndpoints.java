package org.einnovator.documents.client.config;

import org.einnovator.util.PathUtil;
import org.einnovator.util.UriUtils;


public class DocumentsEndpoints {
	
	private static String encodePath(String path) {
		return UriUtils.encodePath(path, DocumentsClientConfiguration.DEFAULT_ENCODING);
	}

	public static String upload(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_upload/", encodePath(path));
	}
	
	public static String download(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/_/", encodePath(path));
	}
	
	public static String meta(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_meta/", encodePath(path));
	}
	
	public static String list(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/__/", encodePath(path));
	}

	public static String delete(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/__/", encodePath(path));
	}

	public static String share(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_share/", encodePath(path));
	}
	
	public static String unshare(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_unshare/", encodePath(path));
	}
	
	public static String create(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_meta/", encodePath(path));
	}
	
	public static String restore(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_restore/", encodePath(path));
	}
	
	public static String folder(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/__/", encodePath(path));
	}

	public static String copy(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_copy/", encodePath(path));
	}
	
	public static String move(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_move/", encodePath(path));
	}
	
	public static String authorities(String path, DocumentsClientConfiguration config) {
		return config.getServer() + PathUtil.concat("/api/_auth/", encodePath(path));
	}

	
}
