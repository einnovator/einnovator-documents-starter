package org.einnovator.documents.client.model;


public enum DocumentType {
	FILE("File"),
	FOLDER("Folder"),
	LINK("Link");
	
	private final String displayValue;

	DocumentType(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public String getDisplayName() {
		return displayValue;
	}

	public static DocumentType parse(String s) {
		for (DocumentType e: DocumentType.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}
	
	public static DocumentType parse(String s, DocumentType defaultValue) {
		DocumentType value = parse(s);
		return value!=null ? value: defaultValue;
	}
	

}
