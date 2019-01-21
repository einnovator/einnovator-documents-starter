package org.einnovator.documents.client.manager;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Pageable;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.Permission;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;

public interface DocumentManager {

	//Documents and Folders

	/**
	 * Write (upload) document content, and attributes, and optionally attachments.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param document a document to be uploaded
	 * @param options the {@code DocumentOptions}
	 * @return URI the direct URI to the document uploaded
	 */
	URI write(Document document, DocumentOptions options);

	Document read(String path, DocumentOptions options);
	
	byte[] content(URI uri, String username, String contentType);

	List<Document> list(String path, DocumentFilter filter, Pageable pageable);

	URI mkdir(String path, DocumentOptions options);

	URI copy(String path, String destPath, DocumentOptions options);
	URI move(String path, String destPath, DocumentOptions options);

	
	void delete(String path, DocumentOptions options);
	void delete(URI uri, DocumentOptions options);


	Document restore(String path, DocumentOptions options);
	Document restore(URI uri);
	

	void share(String path, List<Permission> permissions);
	void share(URI uri, List<Permission> permissions);
	
	void unshare(String path, List<Permission> permissions);
	void unshare(URI uri, List<Permission> permissions);

	void onFolderUpdate(String path);

	void onDocumentUpdate(String path);

	void clearCaches();
	void clearDocumentCache();
	void clearFolderCache();
	Cache getDocumentCache();
	Cache getFolderCache();
	
	void onEvent(ApplicationEvent event);
}
