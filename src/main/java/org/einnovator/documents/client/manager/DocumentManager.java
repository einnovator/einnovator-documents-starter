package org.einnovator.documents.client.manager;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.cache.Cache;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.Permission;
import org.einnovator.documents.client.model.Template;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.TemplateFilter;

public interface DocumentManager {

	/**
	 * Method to upload document to store.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param document a document to be uploaded
	 * @param username a string containing the user id (email)
	 * @return URI the direct URI to the document uploaded
	 */
	URI upload(Document document, String username);
	
	/**
	 * Method to upload document to store.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param document a document to be uploaded
	 * @param username a string containing the user id (email)
	 * @return URI the direct URI to the document uploaded
	 */
	URI upload(Document document);

	/**
	 * Method to upload document to store.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param document a document to be uploaded
	 * @param username a string containing the user id (email)
	 * @param publicLink a boolean that if set TRUE will make the document public
	 * @return URI the direct URI to the document uploaded
	 */
	URI upload(Document document, String username, boolean publicLink);
	
	/**
	 * Method to upload document to store.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param document a document to be uploaded
	 * @return URI the direct URI to the document uploaded
	 */
	URI upload(Document document, boolean publicLink);
	
	/**
	 * Method to upload document to store.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param document a document to be uploaded
	 * @param publicLink a boolean that if set TRUE will make the document public
	 * @return URI the direct URI to the document uploaded
	 */
	URI upload(String location, Document document, boolean publicLink);

		/**
	 * Method to upload document to store.
	 * 
	 * Return the URI for the uploaded document.
	 * 
	 * @param document a document to be uploaded
	 * @param publicLink a boolean that if set TRUE will make the document public
	 * @return URI the direct URI to the document uploaded
	 */
	URI upload(String location, Document document, String username, boolean publicLink);

	URI upload(String location, Document document, String username, boolean publicLink, boolean updateMeta);

	
	Document meta(String path, String username);
	
	Document meta(URI uri, String username);
	
	Document meta(String path);
	
	Document meta(URI uri);

	
	Document download(String path);
	
	Document download(String path, Principal principal);
	
	Document download(String path, String username, boolean content);
	
	Document download(URI uri, boolean content);
	
	Document download(String path, boolean content);
	
	Document download(URI uri, String username, boolean content);
	
	Document download(URI uri);
	
	Document download(URI uri, String username, String contentType);
	
	Document download(URI uri, String contentType);

	byte[] content(URI uri, String username, String contentType);
	
	List<Document> list(String path);
	
	List<Document> list(String path, String username);
	
	List<Document> list(String path, DocumentFilter filter);
	
	List<Document> list(String path, String username, DocumentFilter filter);
	
	
	void delete(String path, boolean force/* , boolean deleteOriginal */);
	
	void delete(String path, String username, boolean force/* , boolean deleteOriginal */);
	
	void delete(URI uri, String username, boolean force/* , boolean deleteOriginal */);
	
	
	void share(String path, String username, List<Permission> permissions);
	
	void share(String path, List<Permission> permissions);
	
	void shareDelete(String path, String username, List<Permission> permissions);
	void shareDelete(String path, List<Permission> permissions);
	
	Document restore(String path, String username);
	Document restore(String path);
	
	URI newFolder(String path, String username);
	URI newFolder(String path);
	URI copy(String path, String username);
	URI copy(String path);
	URI move(String path, String newLocation, String username);
	URI move(String path, String newLocation);

	List<Template> listTemplates(TemplateFilter filter);
	List<Template> listTemplates(String username, TemplateFilter filter);
	
	Template getTemplate(String id);
	Template getTemplate(String username, String id);

	void onFolderUpdate(String path);

	void onDocumentUpdate(String path);

	void clearCaches();
	void clearDocumentCache();
	void clearFolderCache();
	Cache getDocumentCache();
	Cache getFolderCache();
}
