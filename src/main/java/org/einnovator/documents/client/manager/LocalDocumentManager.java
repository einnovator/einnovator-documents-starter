package org.einnovator.documents.client.manager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.documents.client.config.DocumentsClientConfiguration;

import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.DocumentType;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.util.FileUtil;
import org.einnovator.util.PathUtil;
import org.einnovator.util.UriUtils;
import org.einnovator.util.format.RichDateFormatter;
import org.einnovator.util.security.Authority;
import org.einnovator.util.security.SecurityUtil;
import org.einnovator.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

public class LocalDocumentManager extends ManagerBase implements DocumentManager {

	public static final String CACHE_DOCUMENT = "Document";
	public static final String CACHE_FOLDER = "Folder";

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private DocumentsClientConfiguration config;
	
	protected Formatter<Date> dateFormatter = new RichDateFormatter();

	private CacheManager cacheManager;
	
	@Autowired
	public LocalDocumentManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public LocalDocumentManager() {
	}

	public LocalDocumentManager(DocumentsClientConfiguration config) {
		this.config = config;
	}
	
	@Override
	public URI write(Document document, DocumentOptions options) {
		try {
			String lpath = getLocalPath(document);
			byte[] content = document.getOrReadContent();
			try {
				File file = new File(lpath);
				file.getParentFile().mkdirs();
				FileUtils.writeByteArrayToFile(file, content);
			} catch (IOException e) {
				throw new RuntimeException(lpath, e);
			}		
			String url = makeUrl(document.getPath());
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("write: %s %s %s", document.getPath(), lpath, url));				
			}

			return UriUtils.makeURI(url);
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error(String.format("write: %s %s %s",  e, document!=null ? document.getPath() : null, options));
			return null;
		}
	}
	
	private String makeUrl(String path) {
		String baseUrl = getBaseUrl();
		String principalName = SecurityUtil.getPrincipalName();
		String princialRoot =  principalName!=null ? "~" + principalName : "";
		String url = PathUtil.concatAll(baseUrl, "/api/_/", princialRoot, path);
		return url;
	}
	private String getLocalPath(Document document) {
		return getLocalPath(document.getPath());
	}

	public String getLocalPath(String path) {
		String principalName = SecurityUtil.getPrincipalName();
		String princialRoot =  principalName!=null ? principalName : "";
		if (path.startsWith("http:") || path.startsWith("https:") || path.startsWith("file:")) {
			int i = path.indexOf("~");
			if (i>0) {
				path = path.substring(i);
			} else {
				i = path.indexOf("/", 8);
				if (i>0) {
					path = path.substring(i);					
				}
			}
		}
		if (path.startsWith("/api/_/")) {
			path = path.substring("/api/_/".length());
		}
		if (path.startsWith("~")) {
			int i = path.indexOf("/");
			if (i>0) {
				princialRoot= path.substring(1, i).trim();
				if (i+1<path.length()) {
					path = path.substring(i+1);					
				}
			}
		}
		return PathUtil.concatAll(config.getLocalRoot(), config.getFiles().getRoot(), princialRoot, path);
	}

	@Override
	public Document read(String path, DocumentOptions options) {
		try {
			String lpath = getLocalPath(path);
			File file = new File(lpath);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("read: %s %s", path, lpath, options));			
			}
			InputStream inputStream = null;
			if (DocumentOptions.isContent(options)) {
				inputStream = new FileInputStream(file);				
			}
			if (Boolean.TRUE.equals(options.getMeta())) {
				
			}
			if (Boolean.TRUE.equals(options.getAttachments())) {	
			}

			Document document = makeDocument(file, path);
			document.setInputStream(inputStream);
			return document;
		} catch (IOException | RuntimeException e) {
			logger.error(String.format("read: %s %s %s",  e, path, options));
			return null;
		}
	}

	
	@Override
	public Document read(URI uri, DocumentOptions options) {
		try {
			String path = uri.getPath();
			return read(path, options);
		} catch (RuntimeException e) {
			logger.error(String.format("read: %s %s %s",  e, uri, options));
			return null;
		}
	}



	@Override
	public byte[] content(URI uri, DocumentOptions options, String contentType) {
		Document document = read(uri, options);
		return document!=null ? document.getOrReadContent() : null;
	}


	@Override
	public List<Document> list(String path, DocumentFilter filter, Pageable pageable) {
		try {
			String lpath = getLocalPath(path);
			List<Document> documents = new ArrayList<>();
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("list: %s %s %s %s %s", path, lpath, filter, pageable));			
			}
			File dir = new File(lpath);
			File[] files = dir.listFiles(new FileFilter() {	
				@Override
				public boolean accept(File file0) {
					if (filter!=null) {
						if (!filter.filter(file0.getName())) {
							return false;
						}
					}
					return true;
				}
			});
			if (files==null) {
				return null;
			}
			
			int n = 0;
			for (File file: files) {
				if (pageable!=null) {
					if (documents.size()>=pageable.getPageSize()) {
						break;
					}
					if (n<pageable.getOffset()) {
						n++;
						continue;
					}
				}
				n++;
				Document document =makeDocument(file, path);
				documents.add(document);
			}
			return documents;
			
		} catch (RuntimeException e) {
			logger.error(String.format("list: %s %s %s",  e, path, filter));
			return null;
		}
	}

	protected Document makeDocument(File file, String path) {
		Document document = (Document)new Document()
				.withName(file.getName())
				.withPath(PathUtil.concat(path, file.getName()))
				.withUri(makeUrl(path))
				.withContentLength(file.length())
				.withType(file.isDirectory() ? DocumentType.FILE : file.isFile() ? DocumentType.FILE : null)
				.withFolder(file.isDirectory())
				.withContentType(getMimeTypeFromExt(file.getName()))
				.withLastModified(new Date(file.lastModified()))
				;
		Date lastModified = document.getLastModified();
		if (lastModified!=null) {
			document.setLastModifiedFormatted(dateFormatter.print(lastModified, Locale.getDefault()));										
		}
		return document;
	}
	
	@Override
	public boolean delete(String path, DocumentOptions options) {
		try {
			String lpath = getLocalPath(path);
			File file = new File(lpath);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("delete: %s %s", path, lpath, options));			
			}
			try {
				if (!file.delete()) {
					return false;
				}			
			} catch (SecurityException e) {
				return false;
			}
			return true;
		} catch (RuntimeException e) {
			logger.error(String.format("delete: %s %s %s",  e, path, options));
			return false;
		}
	}


	@Override
	public boolean delete(URI uri, DocumentOptions options) {
		return delete(getPath(uri), options);
	}

	
	public String getPath(URI uri) {
		return uri.toString();
	}

	
	@Override
	public Document restore(String path, DocumentOptions options) {
		try {
			return null;
		} catch (RuntimeException e) {
			logger.error(String.format("restore: %s %s %s", e, path, options));
			return null;
		}	
	}

	@Override
	public Document restore(URI uri, DocumentOptions options) {
		return restore(getPath(uri), options);
	}


	@Override
	public URI mkdir(String path, DocumentOptions options) {
		try {
			String lpath = getLocalPath(path);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("mkdir: %s %s %s %s", path, lpath, options));			
			}
			File file = new File(lpath);
			try {
				if (!file.mkdirs()) {
					return null;
				}			
			} catch (SecurityException e) {
				return null;
			}
			Document document = Document.makeFolder(path, null);
			return document!=null ? makeURI(document.getPath()) : null;
		} catch (RuntimeException e) {
			logger.error(String.format("mkdir: %s %s %s", e, path, options));
			return null;
		}	
	}


	/**
	 * @param path
	 * @return
	 */
	private URI makeURI(String path) {
		try {
			String host = UriUtils.makeURI(config.getServer()).getHost();
			URI uri = new URI("file", host, path, null);
			return uri;
		} catch (URISyntaxException e) {
			logger.error(String.format("mkdir: %s %s", e, path));
			return null;
		}
	}

	@Override
	public URI copy(String path, String destPath, DocumentOptions options) {
		try {
			Document document = read(path, options);
			if (document==null) {
				return null;
			}
			document.setPath(destPath);
			return write(document, options);
		} catch (RuntimeException e) {
			logger.error(String.format("copy: %s %s", e, path));
			return null;
		}	
	}


	@Override
	public URI move(String path, String destPath, DocumentOptions options) {
		try {
			Document document = read(path, options);
			if (document==null) {
				return null;
			}
			document.setPath(destPath);
			URI uri = write(document, options);
			if (uri!=null) {
				return null;
			}
			if (!delete(path, options)) {
				return null;
			}
			return uri;
		} catch (RuntimeException e) {
			logger.error(String.format("copy: %s %s", e, path));
			return null;
		}
	}


	@Override
	public URI addAuthority(String path, Authority authority, DocumentOptions options) {
		try {
			return null;
		} catch (RuntimeException e) {
			logger.error(String.format("addAuthority: %s %s %s %s", e, path, authority, options));
			return null;
		}
	}
	
	@Override
	public boolean removeAuthority(String path, String id, DocumentOptions options) {
		try {
			return false;
		} catch (RuntimeException e) {
			logger.error(String.format("removeAuthority: %s %s %s %s", e, path, id, options));
			return false;
		}
	}
	
	@Override
	public void onDocumentUpdate(String path) {
		evictCaches(path);
	}

	@Override
	public void onFolderUpdate(String path) {
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

	public static String getMimeTypeFromExt(String filename) {
		String ext = FileUtil.getExtention(filename);
		if (!StringUtils.hasText(ext)) {
			return null;
		}
		String mimeType = URLConnection.guessContentTypeFromName(filename);
		if (mimeType!=null) {
			return mimeType;
		}
		MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
		mimeType = fileTypeMap.getContentType(filename);
		if (mimeType!=null) {
			return mimeType;
		}
		return null;
	}
	
	protected String getBaseUrl() {
		String baseUrl = WebUtil.getBaseUrl();
		if (baseUrl==null) {
			baseUrl = "http://localhost";
		}		
		return baseUrl;
	}
}
