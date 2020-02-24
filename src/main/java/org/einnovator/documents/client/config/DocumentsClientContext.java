/**
 * 
 */
package org.einnovator.documents.client.config;

import org.einnovator.util.web.ClientContext;

/**
 *
 */
public class DocumentsClientContext extends ClientContext {
	
	private DocumentsClientConfiguration config;
	
	/**
	 * Create instance of {@code DocumentsClientContext}.
	 *
	 */
	public DocumentsClientContext() {
	}

	/**
	 * Get the value of property {@code config}.
	 *
	 * @return the config
	 */
	public DocumentsClientConfiguration getConfig() {
		return config;
	}

	/**
	 * Set the value of property {@code config}.
	 *
	 * @param config the value of property config
	 */
	public void setConfig(DocumentsClientConfiguration config) {
		this.config = config;
	}
	
}
