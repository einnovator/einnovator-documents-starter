package org.einnovator.documents.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("documents")
public class DocumentsConfiguration {

	public static final String DEFAULT_PERSONAL_PATH = "Personal Documents/";
	
	public static final String DEFAULT_ORGANIZATIONS_PATH = "Organization Documents/";

	public final static String DEFAULT_ENCODING = "UTF-8";

	
	private String uri = "http://localhost:9595";
	
	@NestedConfigurationProperty
	private ConnectionConfiguration connection = new ConnectionConfiguration();
	
	public DocumentsConfiguration() {
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ConnectionConfiguration getConnection() {
		return connection;
	}

	public void setConnection(ConnectionConfiguration connectionConfig) {
		this.connection = connectionConfig;
	}
	
	
}
