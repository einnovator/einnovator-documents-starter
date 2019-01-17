package org.einnovator.documents.client.config;

import org.einnovator.util.config.ConnectionConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("documents")
public class DocumentsConfiguration {

	public static final String DEFAULT_PERSONAL_PATH = "Personal Documents/";
	
	public static final String DEFAULT_ORGANIZATIONS_PATH = "Organization Documents/";

	public final static String DEFAULT_ENCODING = "UTF-8";

	public final static String DEFAULT_SERVER = "http://localhost:2020";

	private String server = DEFAULT_SERVER;
	
	@NestedConfigurationProperty
	private ConnectionConfiguration connection = new ConnectionConfiguration();
	
	public DocumentsConfiguration() {
	}

	/**
	 * Get the value of property {@code server}.
	 *
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * Set the value of property {@code server}.
	 *
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * Get the value of property {@code connection}.
	 *
	 * @return the connection
	 */
	public ConnectionConfiguration getConnection() {
		return connection;
	}

	/**
	 * Set the value of property {@code connection}.
	 *
	 * @param connection the connection to set
	 */
	public void setConnection(ConnectionConfiguration connection) {
		this.connection = connection;
	}

	
}
