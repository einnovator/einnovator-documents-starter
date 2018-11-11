package org.einnovator.documents.client;


public class DocumentFilter {

	private String subject;

	private String category;
	
	private String template;
	
	private String q;
	
	private String encoding;
	
	private String organization;
	
	private String products;
	
	private String location;

	private String tags;
	
	private Boolean onlyFeatured;

	
	public DocumentFilter() {
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public void setEncodingType(String encoding) {
		this.encoding = encoding;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	
	public Boolean getOnlyFeatured() {
		return onlyFeatured;
	}

	public void setOnlyFeatured(Boolean onlyFeatured) {
		this.onlyFeatured = onlyFeatured;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " ["
				+ (subject != null ? "subject=" + subject + ", " : "")
				+ (category != null ? "category=" + category + ", " : "")
				+ (template != null ? "template=" + template + ", " : "")
				+ (q != null ? "q=" + q + ", " : "")
				+ (encoding != null ? "encoding=" + encoding + ", " : "")
				+ (organization != null ? "organization=" + organization + ", " : "")
				+ (products != null ? "products=" + products + ", " : "")
				+ (location != null ? "location=" + location + ", " : "")
				+ (onlyFeatured != null ? "onlyFeatured=" + onlyFeatured + ", " : "") 
				+ (tags != null ? "tags=" + tags : "") 
				+ "]";
	}



	
}
