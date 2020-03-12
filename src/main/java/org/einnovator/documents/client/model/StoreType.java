package org.einnovator.documents.client.model;

/**
 * Enum for a type of {@code Store}.
 *
 * @author support@einnovator.org
 */
public enum StoreType {
	FS("FS"),
	FEDERATION("Federation"),
	S3("S3"),
	DROPBOX("Dropbox"),
	GDRIVE("GDrive"),
	GCLOUD("GCloudStorage"),
	AZURE("Azure"),
	BLACKBLAZE("Backblaze"),
	RACKSPACE_US("RackspaceUS"),
	RACKSPACE_UK("RackspaceUK"),
	SFTP("SFTP")
	;
	
	private final String displayValue;

	/**
	 * Create instance of {@code StoreType}.
	 *
	 * @param displayValue
	 */
	StoreType(String displayValue) {
		this.displayValue = displayValue;
	}

	/**
	 * Get the value of property {@code displayValue}.
	 *
	 * @return the displayValue
	 */
	public String getDisplayValue() {
		return displayValue;
	}

	/**
	 * Get the value of property {@code displayValue}.
	 *
	 * @return the displayValue
	 */
	public String getDisplayName() {
		return displayValue;
	}
}
