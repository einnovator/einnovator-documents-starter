package org.einnovator.documents.client.modelx;


public class TemplateFilter {
	
	private String subject;
		
	private String category;
	
	private String organization;
	
	private Boolean global = true;

	private Boolean local = true;
	
	private Boolean shared = true;
	
	public TemplateFilter() {
	}	
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}
	
	

	public Boolean getLocal() {
		return local;
	}

	public void setLocal(Boolean local) {
		this.local = local;
	}

	public Boolean getShared() {
		return shared;
	}

	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	@Override
	public String toString() {
		return "TemplateFilter [subject=" + subject + ", category=" + category + ", organization=" + organization
				+ ", global=" + global + ", local=" + local + ", shared=" + shared + "]";
	}

	
}
