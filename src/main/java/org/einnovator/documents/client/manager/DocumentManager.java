package org.einnovator.documents.client.manager;

import java.net.URI;
import java.util.List;

import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.Permission;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Pageable;

public interface DocumentManager {

	//Documents and Folders

	/**
	 * Write (upload) document content, and attributes, and optionally attachments and versions.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param document  a document to be uploaded
	 * @param options  the {@code DocumentOptions}
	 * @return the direct URI to the document uploaded
	 */
	URI write(Document document, DocumentOptions options);

	/**
	 * Read (download) document content, and attributes, and optionally attachments and versions.
	 * 
	 * Return the {@code Document}, with the following component parts:
	 * <ul>
	 *   <li>byte content, except if option {@code content} is false
	 *   <li>meta attributes, if options {@code meta} is false (content length and content type always available)
	 * </ul>  
	 * @param path path of document is tree of principal or specified user
	 * @param options the {@code DocumentOptions}
	 * @return the {@code Document}
	 */
	Document read(String path, DocumentOptions options);

	/**
	 * Read (download) document content, and attributes, and optionally attachments and versions.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param uri URI of document content
	 * @param options the {@code DocumentOptions}
	 * @return URI the direct URI to the document uploaded
	 */
	Document read(URI uri, DocumentOptions options);

	/**
	 * Read (download) document content as byte array.
	 * 
	 * Return the byte array with binary content.
	 * 
	 * @param document a document to be uploaded
	 * @param options the {@code DocumentOptions}
	 * @return URI the direct URI to the document uploaded
	 */
	byte[] content(URI uri, DocumentOptions options, String contentType);

	/**
	 * List entries in specified folder.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param uri URI of document content
	 * @param options the {@code DocumentOptions}
	 * @return URI the direct URI to the document uploaded
	 */
	
	/**
	 * List entries in specified folder.
	 *
	 * @param path the folder path
	 * @param filter a {@code DocumentFilter}
	 * @param pageable a {@code Pageable}
	 * @return the list of entries in folder as {@code Document}
	 */
	List<Document> list(String path, DocumentFilter filter, Pageable pageable);

	/**
	 * Delete the document or folder in specified path.
	 * 
	 * @param path the path
	 * @param options optional {@code DocumentOptions}
	 * @return true, if operation is successful; false, otherwise.
	 */
	boolean delete(String path, DocumentOptions options);

	/**
	 * Delete the document or folder in specified URI.
	 * 
	 * @param uri the {@code URI}
	 * @param options optional {@code DocumentOptions}
	 * @return true, if operation is successful; false, otherwise.
	 */
	boolean delete(URI uri, DocumentOptions options);

	Document restore(String path, DocumentOptions options);
	Document restore(URI uri, DocumentOptions options);


	URI mkdir(String path, DocumentOptions options);

	URI copy(String path, String destPath, DocumentOptions options);
	URI move(String path, String destPath, DocumentOptions options);

	
	

	boolean share(String path, List<Permission> permissions, DocumentOptions options);
	boolean share(URI uri, List<Permission> permissions, DocumentOptions options);
	
	boolean unshare(String path, List<Permission> permissions, DocumentOptions options);
	boolean unshare(URI uri, List<Permission> permissions, DocumentOptions options);

	// Caching
	
	void onFolderUpdate(String path);
	void onDocumentUpdate(String path);

	void clearCaches();
	void clearDocumentCache();
	void clearFolderCache();
	Cache getDocumentCache();
	Cache getFolderCache();
	
	void onEvent(ApplicationEvent event);
}
