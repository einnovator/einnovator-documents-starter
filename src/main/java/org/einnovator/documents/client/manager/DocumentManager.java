package org.einnovator.documents.client.manager;

import java.net.URI;
import java.util.List;

import org.einnovator.documents.client.config.DocumentsClientContext;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.modelx.DocumentFilter;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.util.security.Authority;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestClientException;

public interface DocumentManager {

	
	//
	//Documents and Folders
	//

	/**
	 * Write a {@code Document}.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner of {@code Document} folder.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document} folder.

	 * @param document the {@code Document}
	 * @param options optional {@code DocumentOptions}
	 * @param context optional {@code DocumentsClientContext}
	 * @return the location {@code URI} for the written {@code Document}, or null if request fails
	 */
	URI write(Document document, DocumentOptions options, DocumentsClientContext context);

	/**
	 * Read a {@code Document}.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Member of {@code Document} group if sharing type is {@code RESTRICTED}.
	 * 
	 * @param path the {@code Document} path in the tree of the user (the principal, by default)
	 * @param options optional {@code DocumentOptions}
	 * @param context optional {@code DocumentsClientContext}
	 * @return the {@code Document}, or null if request fails
	 */
	Document read(String path, DocumentOptions options, DocumentsClientContext context);

	/**
	 * Read a {@code Document}.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Member of {@code Document} group if sharing type is {@code RESTRICTED}.
	 * 
	 * @param uri the {@code URI} of the {@code Document}
	 * @param options optional {@code DocumentOptions}
	 * @param context optional {@code DocumentsClientContext}
	 * @return the {@code Document}, or null if request fails
	 */
	Document read(URI uri, DocumentOptions options, DocumentsClientContext context);
	/**
	 * Read content of a {@code Document}.
	 * 
	 * @param uri the {@code URI} of the {@code Document}
	 * @param options optional {@code DocumentOptions}
	 * @param contentType the requested content type
	 * @param context optional {@code DocumentsClientContext}
	 * @return the content, or null if request fails
	 */
	byte[] content(URI uri, DocumentOptions options, String contentType, DocumentsClientContext context);

	
	/**
	 * List {@code Document}s in a folder.
	 * 
	 * <p><b>Required Security Credentials</b>: Matching {@link Document#getSharing()} and {@link Document#getAuthorities()}.
	 * 
	 * @param path the folder path in the tree of the user (the principal, by default)
	 * @param filter a {@code DocumentFilter}
	 * @param pageable a {@code Pageable} (optional)
	 * @param context optional {@code DocumentsClientContext}
	 * @throws RestClientException if request fails
	 * @return a {@code List} with {@code Document}s, or null if request fails
	 */
	List<Document> list(String path, DocumentFilter filter, Pageable pageable, DocumentsClientContext context);

	/**
	 * Delete or moves recycle-bin/trash to existing {@code Document}
	 * 
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param path the {@code Document} path in the tree of the user (the principal, by default)
	 * @param options optional {@code RequestOptions}
	 * @param context optional {@code SocialClientContext}
	 * @return true if {@code Document} is deleted or move to recycle-bin/trash, false if request fails
	 */
	boolean delete(String path, DocumentOptions options, DocumentsClientContext context);

	/**
	 * Delete or moves recycle-bin/trash to existing {@code Document}
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param uri the {@code URI} of the {@code Document}
	 * @param options optional {@code RequestOptions}
	 * @param context optional {@code SocialClientContext}
	 * @return true if {@code Document} is deleted or move to recycle-bin/trash, false if request fails
	 */
	boolean delete(URI uri, DocumentOptions options, DocumentsClientContext context);

	/**
	 * Restore previous delete {@code Document} if found in recycle-bin/trash folder.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param path the {@code Document} path in the tree of the user (the principal, by default)
	 * @param options optional {@code RequestOptions}
	 * @param context optional {@code SocialClientContext}
	 * @return the restored {@code Document} meta-data, or null if request fails
	 * @return true if {@code Document} is restored, false if request fails
	 */
	Document restore(String path, DocumentOptions options, DocumentsClientContext context);
	
	/**
	 * Restore previous delete {@code Document} if found in recycle-bin/trash folder.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param uri the {@code URI} of the {@code Document}
	 * @param options optional {@code RequestOptions}
	 * @param context optional {@code SocialClientContext}
	 * @return the restored {@code Document} meta-data, or null if request fails
	 */
	Document restore(URI uri, DocumentOptions options, DocumentsClientContext context);


	/**
	 * Make a new directory/folder.
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner of parent folder.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the parent folder.

	 * @param path the folder path in the tree of the user (the principal, by default)
	 * @param options optional {@code DocumentOptions}
	 * @param context optional {@code DocumentsClientContext}
	 * @return the location {@code URI} for the written folder, or null if request fails 
	 */
	URI mkdir(String path, DocumentOptions options, DocumentsClientContext context);

	/**
	 * Copy {@code Document}
	 * 
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param path the source {@code Document} path, in the tree of the user (the principal, by default)
	 * @param destPath the path where to write the {@code Document} copy path, in the tree of the user (the principal, by default)
	 * @param options optional {@code RequestOptions}
	 * @param context optional {@code SocialClientContext}
	 * @return the location {@code URI} for the {@code Document} copy, or null if request fails 
	 */
	URI copy(String path, String destPath, DocumentOptions options, DocumentsClientContext context);

	/**
	 * Move {@code Document}
	 * 
	 * 
	 * <p><b>Required Security Credentials</b>: Client, Admin (global role ADMIN), or owner.
	 * <br>Role {@code DOCUMENT_MANAGER} if role-based access-control is enabled for the {@code Document}.
	 * 
	 * @param path the original {@code Document} path, in the tree of the user (the principal, by default)
	 * @param destPath the path where to move the {@code Document}, in the tree of the user (the principal, by default)
	 * @param options optional {@code RequestOptions}
	 * @param context optional {@code SocialClientContext}
	 * @return the new location {@code URI} for the {@code Document}, or null if request fails 
	 */
	URI move(String path, String destPath, DocumentOptions options, DocumentsClientContext context);

	
	//
	// Authorities
	//

	/**
	 * Add {@code Authority} to {@code Document} in the specified path.
	 * 
	 * @param path the {@code Document} path, in the tree of the user (the principal, by default)
	 * @param authority the  {@code Authority}
	 * @param options optional {@code RequestOptions}
	 * @param context optional {@code SocialClientContext}
	 * @return the {@code URI} that uniquely identifies the {@code Authority}, or null if request fails 
	 */
	URI addAuthority(String path, Authority authority, DocumentOptions options, DocumentsClientContext context);
	
	
	/**
	 * Remove {@code Authority} from {@code Document} in the specified path.
	 * 
	 * @param path the {@code Document} path, in the tree of the user (the principal, by default)
	 * @param id the  {@code Authority} identifier (UUID)
	 * @param options optional {@code RequestOptions}
	 * @param context optional {@code SocialClientContext}
	 * @return true if {@code Authority} is removed, false if request fails
	 */
	boolean removeAuthority(String path, String id, DocumentOptions options, DocumentsClientContext context);

	// Caching
	
	void onFolderUpdate(String path, DocumentsClientContext context);
	void onDocumentUpdate(String path, DocumentsClientContext context);

	/**
	 * Clear all caches related to {@code Document}s.
	 * 
	 */
	void clearCaches();

	/**
	 * Clear {@code Document} file cache.
	 * 
	 */
	void clearDocumentCache();

	/**
	 * Clear {@code Document} folder cache.
	 * 
	 */
	void clearFolderCache();
	
	
	/**
	 * Get the cache for file {@code Document}s.
	 * 
	 * @return the {@code Cache}
	 */
	Cache getDocumentCache();

	/**
	 * Get the cache for folder {@code Document}s.
	 * 
	 * @return the {@code Cache}
	 */
	Cache getFolderCache();
	
	/**
	 * Handle {@code ApplicationEvent}.
	 * 
	 * Updates the {@code Document}s in caches if event of relevant type.
	 * 
	 * @param event an {@code ApplicationEvent}
	 */
	void onEvent(ApplicationEvent event);
}
