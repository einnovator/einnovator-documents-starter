package org.einnovator.documents.client.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {

	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_RANGE = "Content-Range";
	public static final String CONTENT_MD5 = "Content-MD5";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_LANGUAGE = "Content-Language";
	public static final String DATE = "Date";
	public static final String ETAG = "ETag";
	public static final String LAST_MODIFIED = "Last-Modified";
	public static final String SERVER = "Server";
	public static final String CONNECTION = "Connection";
	private static final String ATTRIBUTE_AUTHENTICATION_REQUIRED = "Authentication";
	
	public static final String ATTRIBUTE_CATEGORY = "category";
	public static final String ATTRIBUTE_CATEGORY_OTHER = "otherCategory";
	public static final String ATTRIBUTE_SUBJECT = "subject";
	public static final String ATTRIBUTE_TEMPLATE = "template";
	public static final String ATTRIBUTE_ICON = "icon";
	public static final String ATTRIBUTE_IMG = "img";
	public static final String ATTRIBUTE_ORGANIZATION = "organization";
	public static final String ATTRIBUTE_PRODUCT_NAME = "product name";
	public static final String ATTRIBUTE_DESCRIPTION = "description";
	public static final String ATTRIBUTE_INFO = "additional info";
	public static final String ATTRIBUTE_OWNER = "owner";
	public static final String ATTRIBUTE_ATTACHMENTS = "attachments";
	public static final String ATTRIBUTE_REFERENCE = "reference";
	public static final String ATTRIBUTE_PERMISSIONS = "permissions";
	public static final String ATTRIBUTE_TAGS = "document tags";
	public static final String ATTRIBUTE_UUID = "uuid";
	public static final String ATTRIBUTE_CREATION_DATE = "creation_date";


	private String path;

	private Owner owner;

	private Map<String, Object> meta = new LinkedHashMap<>();

	private byte[] content;

	private InputStream inputStream;

	private String name;

	private boolean folder;

	private List<Permission> permissions;

	private String description;

	private String additionalInfo;

	private String uri;

	private String organization;

	private Map<String, String> attributes = new LinkedHashMap<>();

	private List<Document> attachments = new ArrayList<>();

	private SubjectType subject;

	private DocumentCategory category;

	private String otherCategory;

	private String template;

	private List<String> tags = new ArrayList<>();

	private Boolean hidden;

	private String icon;

	private String imgUri;

	public Document() {
	}

	public Document(String volume, String path, Owner owner, Map<String, Object> meta, Map<String, String> attributes) {
		super();
		this.path = path;
		this.owner = owner;
		if (meta != null) {
			this.meta = meta;
		}
		if (attributes != null) {
			this.attributes = attributes;
		}
	}

	public Document(String volume, String path, Owner owner, byte[] content, Map<String, Object> meta,
			Map<String, String> attributes) {
		this(volume, path, owner, meta, attributes);
		this.content = content;
		setOrUpdateSize(false);
	}

	public Document(String volume, String path, Owner owner, InputStream inputStream, Map<String, Object> meta,
			Map<String, String> attributes) {
		this(volume, path, owner, meta, attributes);
		this.inputStream = inputStream;
	}

	public Document(String volume, String path, Owner owner, long size, InputStream inputStream,
			Map<String, Object> meta, Map<String, String> attributes) {
		this(volume, path, owner, inputStream, meta, attributes);
		setContentLength(size);
	}

	public Document(Document document) {
		this.folder = document.isFolder();
		this.name = document.getName();
		this.path = document.getPath();
		this.meta = document.getMeta() != null ? new LinkedHashMap<>(document.getMeta()) : null;
		this.attributes = document.getAttributes() != null ? new LinkedHashMap<>(document.getAttributes()) : null;
		this.owner = document.getOwner();
		this.permissions = document.getPermissions();
		this.content = document.getContent();
		this.inputStream = document.getInputStream();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
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

	public String getCategoryAsString() {
		if (category!=null) {
			return category.toString();
		}
		return getAttribute(ATTRIBUTE_CATEGORY);
	}

	public String getOtherCategory() {
		return otherCategory;
	}

	public void setOtherCategory(String otherCategory) {
		this.otherCategory = otherCategory;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(String name, String value) {
		if (value==null) {
			return;
		}
		if (this.attributes==null) {
			this.attributes = new LinkedHashMap<>();
		}
		this.attributes.put(name, value);
	}
	
	public void setAttribute(String name, Object value) {
		if (value!=null) {
			setAttribute(name, value.toString());
		}
	}
	
	public void setAttribute(String name, Date date) {
		if (date!=null) {
			setAttribute(name, Long.toString(date.getTime()));
		}
	}

	public String getAttribute(String name) {
		if (attributes==null) {
			return null;
		}
		return attributes.get(name);
	}

	public Date getAttributeAsDate(String name) {
		Long value = getAttributeAsLong(name);
		if (value==null) {
			return null;
		}
		return new Date(value);
	}
	
	public Boolean getAttributeAsBoolean(String name) {
		String value = getAttribute(name);
		if (value==null) {
			return null;
		}
		return Boolean.parseBoolean(value);
	}

	public Long getAttributeAsLong(String name) {
		String value = getAttribute(name);
		if (value==null) {
			return null;
		}
		try {
			return Long.parseLong(value);			
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public List<Document> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Document> attachments) {
		this.attachments = attachments;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public byte[] loadContent() {
		if (content != null) {
			return content;
		}
		if (inputStream == null) {
			return null;
		}
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			StreamUtils.copy(inputStream, out);
			content = out.toByteArray();
			return content;
		} catch (IOException e) {
			return null;
		}
	}

	public String loadTextContent(String encoding) {
		byte[] bytes = loadContent();
		if (bytes == null) {
			return null;
		}
		try {
			return new String(bytes, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String loadTextContent() {
		byte[] bytes = loadContent();
		if (bytes == null) {
			return null;
		}
		return new String(bytes);
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [" + (name != null ? "name=" + name + ", " : "")
				+ (path != null ? "path=" + path + ", " : "") + (uri != null ? "uri=" + uri + ", " : "")
				+ (owner != null ? "owner=" + owner + ", " : "")
				+ (content != null ? ", content=#" + content.length : "")
				+ (inputStream != null ? "inputStream=" + inputStream + ", " : "") + "folder=" + folder + ", hidden="
				+ hidden + ", " + (subject != null ? "subject=" + subject + ", " : "")
				+ (icon != null ? "icon=" + icon + ", " : "") + (meta != null ? "meta=" + meta + ", " : "")
				+ (template != null ? "template=" + template : "")
				+ (permissions != null ? "permissions=" + permissions + ", " : "")
				+ (tags != null ? "tags=" + tags + ", " : "") + (category != null ? "category=" + category + ", " : "")
				+ (otherCategory != null ? "otherCategory=" + otherCategory + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (additionalInfo != null ? "additionalInfo=" + additionalInfo + ", " : "") + "writePermission="
				+ (attributes != null ? "attributes=" + attributes + ", " : "")
				+ (attachments != null ? "|attachments|=" + attachments.size() + ", " : "")
				+ (imgUri != null ? "imgUri=" + imgUri + ", " : "") + "]";
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setContentType(String contentType) {
		if (meta == null) {
			meta = new LinkedHashMap<>();
		}
		meta.put(CONTENT_TYPE, contentType);
	}

	public String getContentType() {
		if (meta == null) {
			return null;
		}
		Object contentType = meta.get(CONTENT_TYPE);
		return contentType != null ? contentType.toString() : null;
	}

	public Boolean getAuthenticationRequired() {
		return getAttributeAsBoolean(ATTRIBUTE_AUTHENTICATION_REQUIRED);
	}

	public void setContentLength(long contentLength) {
		meta.put(CONTENT_LENGTH, contentLength);
	}

	public static class MetaBuilder {
		private Map<String, Object> meta = new LinkedHashMap<>();

		public static MetaBuilder meta() {
			return new MetaBuilder();
		}

		public MetaBuilder length(String contentLength) {
			meta.put(CONTENT_LENGTH, contentLength);
			return this;
		}

		public MetaBuilder length(Integer contentLength) {
			return length(contentLength != null ? contentLength.toString() : null);

		}

		public MetaBuilder contentType(String contentType) {
			meta.put(CONTENT_TYPE, contentType);
			return this;
		}

		public MetaBuilder encoding(String contentEncoding) {
			meta.put(CONTENT_ENCODING, contentEncoding);
			return this;
		}

		public MetaBuilder language(String contentLanguage) {
			meta.put(CONTENT_LANGUAGE, contentLanguage);
			return this;
		}

		public Map<String, Object> build() {
			return meta;
		}

	}

	@JsonIgnore
	public String getCategoryDisplayName() {
		if (category != null) {
			if (category.isOther() && StringUtils.hasText(otherCategory)) {
				return otherCategory;
			}
			return category.getDisplayName();
		} else {
			if (StringUtils.hasText(otherCategory)) {
				return otherCategory;
			}
		}
		return null;
	}

	public String getSubjectDisplayName() {
		return subject != null ? subject.getDisplayName() : null;
	}

	private void setOrUpdateSize(boolean force) {
		if (content != null && (force || !meta.containsKey(CONTENT_LENGTH))) {
			meta.put(CONTENT_LENGTH, content.length);
		}
	}

	public String getExtention() {
		if (path == null) {
			return null;
		}
		int i = path.lastIndexOf(".");
		if (i < 0 || i >= path.length() - 1) {
			return null;
		}
		return path.substring(i + 1);
	}

	public String getFilename() {
		if (path==null) {
			return null;
		}
		String[] ss = path.split("/");
		return ss.length > 0 ? ss[ss.length - 1] : null;
	}

	
}