package org.einnovator.documents.client.model;


/**
 * Enum for the scope of a {@code Mount}.
 *
 * @author support@einnovator.org
 */
public enum ScopeType {
	GLOBAL("Global"),
	USER("User"),
	GROUP("Group")
	;
	
	private final String displayValue;

	/**
	 * Create instance of {@code ScopeType}.
	 *
	 * @param displayValue
	 */
	ScopeType(String displayValue) {
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
