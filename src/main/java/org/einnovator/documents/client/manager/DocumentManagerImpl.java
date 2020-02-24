package org.einnovator.documents.client.manager;

import java.net.URI;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.documents.client.DocumentsClient;
import org.einnovator.documents.client.config.DocumentsClientConfiguration;
import org.einnovator.documents.client.config.DocumentsClientContext;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.util.security.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;

public class DocumentManagerImpl extends ManagerBase implements DocumentManager {

	public static final String CACHE_DOCUMENT = "Document";
	public static final String CACHE_FOLDER = "Folder";

	private final Log logger = LogFactory.getLog(getClass());

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
	public URI write(Document document, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.write(document, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("write: %s %s %s",  e, document!=null ? document.getPath() : null, options));
			return null;
		}
	}

	@Override
	public Document read(String path, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.read(path, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("read: %s %s %s",  e, path, options));
			return null;
		}
	}

	
	@Override
	public Document read(URI uri, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.read(uri, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("read: %s %s %s",  e, uri, options));
			return null;
		}
	}



	@Override
	public byte[] content(URI uri, DocumentOptions options, String contentType, DocumentsClientContext context) {
		try {
			return client.content(uri, options, contentType, context);
		} catch (RuntimeException e) {
			logger.error(String.format("content: %s %s %s %s",  e, uri, options, contentType));
			return null;
		}
	}


	@Override
	public List<Document> list(String path, DocumentFilter filter, Pageable pageable, DocumentsClientContext context) {
		try {
			return client.list(path, filter, pageable, context);
		} catch (RuntimeException e) {
			logger.error(String.format("list: %s %s %s %s",  e, path, filter, pageable));
			return null;
		}
	}

	@Override
	public boolean delete(String path, DocumentOptions options, DocumentsClientContext context) {
		try {
			client.delete(path, options, context);
			return true;
		} catch (RuntimeException e) {
			logger.error(String.format("delete: %s %s %s",  e, path, options));
			return false;
		}
	}


	@Override
	public boolean delete(URI uri, DocumentOptions options, DocumentsClientContext context) {
		try {
			client.delete(uri, options, context);
			return true;
		} catch (RuntimeException e) {
			logger.error(String.format("delete: %s %s %s",  e, uri, options));
			return false;
		}
	}


	
	@Override
	public Document restore(String path, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.restore(path, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("restore: %s %s %s", e, path, options));
			return null;
		}	
	}

	@Override
	public Document restore(URI uri, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.restore(uri, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("restore: %s %s %s", e, uri, options));
			return null;
		}	
	}


	@Override
	public URI mkdir(String path, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.mkdir(path, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("mkdir: %s %s %s", e, path, options));
			return null;
		}	
	}


	@Override
	public URI copy(String path, String destPath, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.copy(path, destPath, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("copy: %s %s", e, path));
			return null;
		}	
	}


	@Override
	public URI move(String path, String destPath, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.move(path, destPath, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("move: %s %s %s %s", e, path, destPath, options));
			return null;
		}	
	}


	@Override
	public URI addAuthority(String path, Authority authority, DocumentOptions options, DocumentsClientContext context) {
		try {
			return client.addAuthority(path, authority, options, context);
		} catch (RuntimeException e) {
			logger.error(String.format("share: %s %s %s %s", e, path, authority, options));
			return null;
		}
	}

	@Override
	public boolean removeAuthority(String path, String id, DocumentOptions options, DocumentsClientContext context) {
		try {
			client.removeAuthority(path, id, options, context);
			return true;
		} catch (RuntimeException e) {
			logger.error(String.format("share: %s %s %s %s", e, path, id, options));
			return false;
		}
	}

	
	
	@Override
	public void onDocumentUpdate(String path, DocumentsClientContext context) {
		evictCaches(path);
	}

	@Override
	public void onFolderUpdate(String path, DocumentsClientContext context) {
		evictCaches(path);
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

	@EventListener
	@Override
	public void onEvent(ApplicationEvent event) {
		if (!(event instanceof PayloadApplicationEvent)) {
			return;
		}
		Document document = getNotificationSource(((PayloadApplicationEvent<?>)event).getPayload(), Document.class);
		if (document!=null) {
			evictCaches(document);				
		}		
	}

	private void evictCaches(Document document) {
		if (document==null) {
			return;
		}
		evictCaches(document.getPath());
	}
	
	private void evictCaches(String path) {
		if (path==null) {
			return;
		}
		Cache cache = getDocumentCache();
		if (cache!=null) {
			cache.evict(path);
		}
		Cache cache2 = getFolderCache();
		if (cache2!=null) {
			cache2.evict(path);
		}

	}

}
