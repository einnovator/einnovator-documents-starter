package org.einnovator.documents.client.config;

import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;

/**
 * Files upload configuration.
 *
 * @author support@einnovator.org
 *
 */
public class FilesConfiguration extends ObjectBase {
	
	public static final String DEFAULT_UPLOAD_FOLDER = "/.Apps/";

	public static final String DEFAULT_TMP_FOLDER = "/.Tmp/";

	protected String root = DEFAULT_UPLOAD_FOLDER;
	
	private String tmp = DEFAULT_TMP_FOLDER;
	
	
	public FilesConfiguration() {
	}

	/**
	 * Get the value of property {@code root}.
	 *
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * Set the value of property {@code root}.
	 *
	 * @param root the value of property root
	 */
	public void setRoot(String root) {
		this.root = root;
	}

	/**
	 * Get the value of property {@code tmp}.
	 *
	 * @return the tmp
	 */
	public String getTmp() {
		return tmp;
	}

	/**
	 * Set the value of property {@code tmp}.
	 *
	 * @param tmp the value of property tmp
	 */
	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return super.toString(creator)
				.append("root", root)
				.append("tmp", tmp)
				;
	}


}
