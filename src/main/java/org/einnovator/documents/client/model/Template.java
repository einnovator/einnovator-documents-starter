package org.einnovator.documents.client.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

public class Template {

	@NotEmpty
	private String name;

	private String uuid;

	private String uri;
	
	private String path;

	private Owner owner;
	
	private SubjectType subject;
	
	private DocumentCategory category;
	
	private String otherCategory;
	
	private String description;

	private String additionalInfo;

	private List<AttributeTemplate> attributes = new ArrayList<AttributeTemplate>();

	private List<Permission> permissions = new ArrayList<>();
	
	private Map<String, Object> meta = new LinkedHashMap<>();
	
	private List<String> tags = new ArrayList<>();

	private String icon;
	
	private String imgUri;
	
	public Template() {
	}

	public Template(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SubjectType getSubject() {
		return subject;
	}

	public void setSubject(SubjectType subject) {
		this.subject = subject;
	}

	public DocumentCategory getCategory() {
		return category;
	}

	public void setCategory(DocumentCategory category) {
		this.category = category;
	}

	public String getOtherCategory() {
		return otherCategory;
	}

	public void setOtherCategory(String otherCategory) {
		this.otherCategory = otherCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public List<AttributeTemplate> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeTemplate> attributeTemplates) {
		this.attributes = attributeTemplates;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public String getId() {
		String id = "";
		if(getName() == null) {
			return id;
		}
		id = getName().toString().replaceAll("\\s+", "_");
		id = id.replaceAll("\\.", "__");
		return id;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [" 
				+ (name != null ? "name=" + name + ", " : "") 
				+ (uri != null ? "uri=" + uri + ", " : "")
				+ (subject != null ? "subject=" + subject + ", " : "")
				+ (category != null ? "category=" + category + ", " : "")
				+ (otherCategory != null ? "otherCategory=" + otherCategory + ", " : "")
				+ (icon != null ? "icon=" + icon + ", " : "")
				+ (owner != null ? "owner=" + owner + ", " : "") 
				+ (imgUri != null ? "imgUri=" + imgUri + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (additionalInfo != null ? "additionalInfo=" + additionalInfo + ", " : "")
				+ (attributes != null ? "attributes=" + attributes + ", " : "")
				+ (permissions != null ? "permissions=" + permissions + ", " : "")
				+ (meta != null ? "meta=" + meta + ", " : "") 
				+ (tags != null ? "tags=" + tags : "") + "]";
	}

	public AttributeTemplate getAttribute(String attribute) {
		for (AttributeTemplate attrTmpl : attributes) {
			if (attrTmpl.getName().equals(attribute)) {
				return attrTmpl;
			}
		}
		return null;
	}
	


}
