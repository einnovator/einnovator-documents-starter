package org.einnovator.documents.client.model;


/**
 * Enum for a type of {@code Document} sharing.
 *
 * @author support@einnovator.org
 */
public enum ShareType {
	PRIVATE("Private"),
	PUBLIC("Public"),
	RESTRICTED("Restricted"),
	ROLE("Role");
	
	private final String displayValue;

	/**
	 * Create instance of {@code ShareType}.
	 *
	 * @param displayValue
	 */
	ShareType(String displayValue) {
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
