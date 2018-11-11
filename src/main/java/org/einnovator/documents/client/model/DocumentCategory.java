package org.einnovator.documents.client.model;

public enum DocumentCategory {
	CERTIFICATE("Certificate", false),
	AUDITOR_QUALIFICATION("Auditor Qualification", true, true),
	PERSONAL_QUALIFICATION("Personal Qualification", true),
	PERSONAL_IDENTIFICATION("Personal Identification", true),
	ORGANIZATION_IDENTIFICATION("Identification"),
	AUDITREPORT("Audit Report"),
	SPECIFICATION("Specification"),
	REPORT("Report"),
	REVIEW("Review"),
	EVALUATION("Evaluation"),
	INVOICE("Invoice"),
	QUOTE("Quote"),
	RFQ("Request For Quote"),
	LETTER("Letter"),
	TECHNICAL("Technical"),
	SURVEY("Survey"),
	BUSINESS("Business"),	
	MARKETING("Marketing"),
	MINUTES("Minutes"),
	OTHER_PERSONAL("Other Personal"),
	OTHER_ORGANIZATION("Other Organizational");
	
	public boolean personal;

	public boolean auditor;

	private String displayName;

	private DocumentCategory(String displayName) {
		this.displayName = displayName;
	}
	
	private DocumentCategory(String displayName, boolean personal) {
		this.displayName = displayName;
		this.personal = personal;
	}

	
	private DocumentCategory(String displayName, boolean personal, boolean auditor) {
		this.displayName = displayName;
		this.personal = personal;
		this.auditor = auditor;
	}

	public String getDisplayName() {
		return displayName;
	}

	
	public boolean isPersonal() {
		return personal;
	}
	
	public boolean isAuditor() {
		return personal;
	}

	public static DocumentCategory parse(String s) {
		for (DocumentCategory e: DocumentCategory.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}

	public boolean isOther() {
		return this==OTHER_PERSONAL || this==OTHER_ORGANIZATION;
	}
}
