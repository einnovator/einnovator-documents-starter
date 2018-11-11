package org.einnovator.documents.client.manager;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import org.einnovator.documents.client.DocumentFilter;
import org.einnovator.documents.client.DocumentsClient;
import org.einnovator.documents.client.TemplateFilter;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.Permission;
import org.einnovator.documents.client.model.Template;

public class DocumentManagerImpl implements DocumentManager {

	public static final String CACHE_DOCUMENT = "Document";
	public static final String CACHE_FOLDER = "Folder";

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private DocumentsClient client;
	
	private CacheManager cacheManager;
	
	@Autowired
	public DocumentManagerImpl(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public DocumentManagerImpl(DocumentsClient client, CacheManager cacheManager) {
		this.client = client;
		this.cacheManager = cacheManager;
	}


	@Override
	public URI upload(Document document, String username) {
		try {
			return client.upload(document, username);
		} catch (RuntimeException e) {
			logger.error("upload:" + e + " " + document + " " + username);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public URI upload(Document document) {
		try {
			return client.upload(document);
		} catch (RuntimeException e) {
			logger.error("upload:" + e + " " + document);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public URI upload(Document document, String username, boolean publicLink) {
		try {
			return client.upload(document, username, publicLink);
		} catch (RuntimeException e) {
			logger.error("upload:" + e + " " + document + " " + username + " " + publicLink);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public URI upload(Document document, boolean publicLink) {
		try {
			return client.upload(document, publicLink);
		} catch (RuntimeException e) {
			logger.error("upload:" + e + " " + document + " " + publicLink);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public URI upload(String location, Document document, boolean publicLink) {
		try {
			return client.upload(location, document, publicLink);
		} catch (RuntimeException e) {
			logger.error("upload:" + e + " " + location + " " + document + " " + publicLink);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public URI upload(String location, Document document, String username, boolean publicLink) {
		try {
			return client.upload(location, document, username, publicLink);
		} catch (RuntimeException e) {
			logger.error("upload:" + e + " " + location + " " + document + " " + username + " " + publicLink);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public URI upload(String location, Document document, String username, boolean publicLink, boolean updateMeta) {
		try {
			return client.upload(location, document, username, publicLink, updateMeta);
		} catch (RuntimeException e) {
			logger.error("upload:" + e + " " + location + " " + document + " " + username + " " + publicLink + " " + updateMeta);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document meta(String path, String username) {
		try {
			return client.meta(path, username);
		} catch (RuntimeException e) {
			logger.error("meta:" + e + " " + path + " " + username);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document meta(URI uri, String username) {
		try {
			return client.meta(uri, username);
		} catch (RuntimeException e) {
			logger.error("meta:" + e + " " + uri + " " + username);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document meta(String path) {
		try {
			return client.meta(path);
		} catch (RuntimeException e) {
			logger.error("meta:" + e + " " + path);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document meta(URI uri) {
		try {
			return client.meta(uri);
		} catch (RuntimeException e) {
			logger.error("meta:" + e + " " + uri);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(String path) {
		try {
			return client.download(path);
		} catch (RuntimeException e) {
			logger.error("download:" + e);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(String path, Principal principal) {
		try {
			return client.download(path, principal);
		} catch (RuntimeException e) {
			logger.error("download:" + e + " " + principal);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(String path, String username, boolean content) {
		try {
			return client.download(path, username, content);
		} catch (RuntimeException e) {
			logger.error("download:" + e + " " + path + " " + username + " " + content);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(URI uri, boolean content) {
		try {
			return client.download(uri, content);
		} catch (RuntimeException e) {
			logger.error("meta:" + e + " " + uri + " " + content);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(String path, boolean content) {
		try {
			return client.download(path, content);
		} catch (RuntimeException e) {
			logger.error("download:" + e + " " + content);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(URI uri, String username, boolean content) {
		try {
			return client.download(uri, username, content);
		} catch (RuntimeException e) {
			logger.error("download:" + e + " " + uri + " " + username + " " + content);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(URI uri) {
		try {
			return client.download(uri);
		} catch (RuntimeException e) {
			logger.error("download:" + e + " " + uri);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(URI uri, String username, String contentType) {
		try {
			return client.download(uri, username, contentType);
		} catch (RuntimeException e) {
			logger.error("download:" + e + " " + uri + " " + username + " " + contentType);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Document download(URI uri, String contentType) {
		try {
			return client.download(uri, contentType);
		} catch (RuntimeException e) {
			logger.error("download:" + e + " " + uri + " " + contentType);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public byte[] content(URI uri, String username, String contentType) {
		try {
			return client.content(uri, username, contentType);
		} catch (RuntimeException e) {
			logger.error("content:" + e + " " + uri + " " + username + " " + contentType);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Document> list(String path) {
		try {
			return client.list(path);
		} catch (RuntimeException e) {
			logger.error("list:" + e + " " + path);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Document> list(String path, String username) {
		try {
			return client.list(path, username);
		} catch (RuntimeException e) {
			logger.error("list:" + e + " " + path + " " + username);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Document> list(String path, DocumentFilter filter) {
		try {
			return client.list(path, filter);
		} catch (RuntimeException e) {
			logger.error("list:" + e + " " + filter);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Document> list(String path, String username, DocumentFilter filter) {
		try {
			return client.list(path, username, filter);
		} catch (RuntimeException e) {
			logger.error("list:" + e + " " + username + " " + filter);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public void delete(String path, boolean force) {
		try {
			client.delete(path, force);
		} catch (RuntimeException e) {
			logger.error("delete:" + e + " " + path + " " + force);
			e.printStackTrace();
		}
	}


	@Override
	public void delete(String path, String username, boolean force) {
		try {
			client.delete(path, username, force);
		} catch (RuntimeException e) {
			logger.error("delete:" + e + " " + path + " " + username + " " + force);
			e.printStackTrace();
		}
	}


	@Override
	public void delete(URI uri, String username, boolean force) {
		try {
			client.delete(uri, username, force);
		} catch (RuntimeException e) {
			logger.error("delete:" + e + " " + uri + " " + username + " " + force);
			e.printStackTrace();
		}
	}


	@Override
	public void share(String path, String username, List<Permission> permissions) {
		try {
			client.share(path, username, permissions);
		} catch (RuntimeException e) {
			logger.error("share:" + e + " " + path + " " + username + " " + permissions);
			e.printStackTrace();
		}
	}


	@Override
	public void share(String path, List<Permission> permissions) {
		try {
			client.share(path, permissions);
		} catch (RuntimeException e) {
			logger.error("share:" + e + " " + path + " " + permissions);
			e.printStackTrace();
		}	
	}


	@Override
	public void shareDelete(String path, String username, List<Permission> permissions) {
		try {
			client.shareDelete(path, username, permissions);
		} catch (RuntimeException e) {
			logger.error("shareDelete:" + e + " " + path + " " + username + " " + permissions);
			e.printStackTrace();
		}	
	}


	@Override
	public void shareDelete(String path, List<Permission> permissions) {
		try {
			client.shareDelete(path, permissions);
		} catch (RuntimeException e) {
			logger.error("shareDelete:" + e + " " + path + " " + permissions);
			e.printStackTrace();
		}	
	}


	@Override
	public Document restore(String path, String username) {
		try {
			return client.restore(path, username);
		} catch (RuntimeException e) {
			logger.error("restore:" + e + " " + path + " " + username);
			e.printStackTrace();
			return null;
		}	
	}


	@Override
	public Document restore(String path) {
		try {
			return client.restore(path);
		} catch (RuntimeException e) {
			logger.error("restore:" + e + " " + path);
			e.printStackTrace();
			return null;
		}	
	}


	@Override
	public URI newFolder(String path, String username) {
		try {
			return client.newFolder(path, username);
		} catch (RuntimeException e) {
			logger.error("newFolder:" + e + " " + path + " " + username);
			e.printStackTrace();
			return null;
		}	
	}


	@Override
	public URI newFolder(String path) {
		try {
			return client.newFolder(path);
		} catch (RuntimeException e) {
			logger.error("newFolder:" + e + " " + path);
			e.printStackTrace();
			return null;
		}	
	}


	@Override
	public URI copy(String path, String username) {
		try {
			return client.copy(path, username);
		} catch (RuntimeException e) {
			logger.error("copy:" + e + " " + path + " " + username);
			e.printStackTrace();
			return null;
		}	
	}


	@Override
	public URI copy(String path) {
		try {
			return client.copy(path);
		} catch (RuntimeException e) {
			logger.error("copy:" + e + " " + path);
			e.printStackTrace();
			return null;
		}	
	}


	@Override
	public URI move(String path, String newLocation, String username) {
		try {
			return client.move(path, newLocation, username);
		} catch (RuntimeException e) {
			logger.error("move:" + e + " " + path + " " + newLocation + " " + username);
			e.printStackTrace();
			return null;
		}	
	}


	@Override
	public URI move(String path, String newLocation) {
		try {
			return client.move(path, newLocation);
		} catch (RuntimeException e) {
			logger.error("move:" + e + " " + path + " " + newLocation);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Template> listTemplates(TemplateFilter filter) {
		try {
			return client.listTemplates(filter);
		} catch (RuntimeException e) {
			logger.error("listTemplates:" + e + " " + filter);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Template> listTemplates(String username, TemplateFilter filter) {
		try {
			return client.listTemplates(username, filter);
		} catch (RuntimeException e) {
			logger.error("listTemplates:" + e + " " + username + " " + filter);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Template getTemplate(String id) {
		try {
			return client.getTemplate(id);
		} catch (RuntimeException e) {
			logger.error("getTemplate:" + e + " " + id);
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Template getTemplate(String username, String id) {
		try {
			return client.getTemplate(username, id);
		} catch (RuntimeException e) {
			logger.error("getTemplate:" + e + " " + username + " " + id);
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public void onDocumentUpdate(String path) {
		if (path==null) {
			return;
		}
		try {
			Cache cache = cacheManager.getCache(DocumentManagerImpl.CACHE_DOCUMENT);
			if (cache!=null) {
				cache.evict(path);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error("onDocumentUpdate: " + e);
		}
	}

	@Override
	public void onFolderUpdate(String path) {
		if (path==null) {
			return;
		}
		try {
			Cache cache = cacheManager.getCache(DocumentManagerImpl.CACHE_FOLDER);
			if (cache!=null) {
				cache.evict(path);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error("onFolderUpdate: " + e);
		}
	}


	@Override
	public void clearCaches() {
		clearDocumentCache();
		clearFolderCache();
	}

	@Override
	public void clearDocumentCache() {
		Cache cache = getDocumentCache();
		if (cache!=null) {
			cache.clear();
		}
	}

	@Override
	public void clearFolderCache() {
		Cache cache = getFolderCache();
		if (cache!=null) {
			cache.clear();
		}
	}

	@Override
	public Cache getDocumentCache() {
		Cache cache = cacheManager.getCache(CACHE_DOCUMENT);
		return cache;
	}


	@Override
	public Cache getFolderCache() {
		Cache cache = cacheManager.getCache(CACHE_DOCUMENT);
		return cache;
	}

}
