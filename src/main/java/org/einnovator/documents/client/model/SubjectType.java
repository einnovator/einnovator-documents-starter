package org.einnovator.documents.client.model;

public enum SubjectType {
	PERSONAL("Personal"),
	ORGANIZATIONAL("Organizational"),
	TEAM("Team"),
	THIRD_PARTY("Third Party"),
	OTHER("Other");
	
	private String displayName;

	private SubjectType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public static SubjectType parse(String s) {
		for (SubjectType e: SubjectType.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}
}
