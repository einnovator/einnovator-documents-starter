package org.einnovator.documents.client.config;

import org.einnovator.util.config.ConnectionConfiguration;
import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("documents")
public class DocumentsConfiguration extends ObjectBase {

	public final static String DEFAULT_ENCODING = "UTF-8";

	public final static String DEFAULT_SERVER = "http://localhost:2020";

	private String DEFAULT_LOCAL_ROOT = "/data";

	private String server = DEFAULT_SERVER;
	
	@NestedConfigurationProperty
	private FilesConfiguration files = new FilesConfiguration();

	@NestedConfigurationProperty
	private ConnectionConfiguration connection = new ConnectionConfiguration();


	private Boolean local = false;
	
	private String localRoot = DEFAULT_LOCAL_ROOT;
	
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
	 * Get the value of property {@code files}.
	 *
	 * @return the files
	 */
	public FilesConfiguration getFiles() {
		return files;
	}

	/**
	 * Set the value of property {@code files}.
	 *
	 * @param files the files to set
	 */
	public void setFiles(FilesConfiguration files) {
		this.files = files;
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

	
	/**
	 * Get the value of property {@code localRoot}.
	 *
	 * @return the localRoot
	 */
	public String getLocalRoot() {
		return localRoot;
	}

	/**
	 * Set the value of property {@code localRoot}.
	 *
	 * @param localRoot the localRoot to set
	 */
	public void setLocalRoot(String localRoot) {
		this.localRoot = localRoot;
	}
	

	/**
	 * Get the value of property {@code local}.
	 *
	 * @return the local
	 */
	public Boolean getLocal() {
		return local;
	}

	/**
	 * Set the value of property {@code local}.
	 *
	 * @param local the local to set
	 */
	public void setLocal(Boolean local) {
		this.local = local;
	}

	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator
				.append("server", server)
				.append("files", files)
				.append("connection", connection)
				.append("local", local)
				.append("localRoot", localRoot)
				;
	}
}
