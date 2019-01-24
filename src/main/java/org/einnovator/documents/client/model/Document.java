package org.einnovator.documents.client.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Document extends ObjectBase {

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
	
	public static final String ATTRIBUTE_UUID = "uuid";
	public static final String ATTRIBUTE_CREATION_DATE = "creation_date";


	private String uri;

	private String path;

	private String name;

	private Owner owner;

	private Map<String, Object> meta = new LinkedHashMap<>();

	@JsonIgnore
	private byte[] content;

	@JsonIgnore
	private InputStream inputStream;

	private Boolean folder;

	private Boolean hidden;

	private List<Permission> permissions;

	private Map<String, String> attributes = new LinkedHashMap<>();

	private List<Document> attachments = new ArrayList<>();

	private List<String> tags = new ArrayList<>();

	private String category;

	private String category2;

	private String description;

	private String template;

	private String icon;

	private String img;

	public Document() {
	}

	public Document(String path, Owner owner, Map<String, Object> meta, Map<String, String> attributes) {
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

	public Document(String path, Owner owner, byte[] content, Map<String, Object> meta, Map<String, String> attributes) {
		this(path, owner, meta, attributes);
		this.content = content;
		setOrUpdateSize(false);
	}

	public Document(String path, Owner owner, InputStream inputStream, Map<String, Object> meta, Map<String, String> attributes) {
		this(path, owner, meta, attributes);
		this.inputStream = inputStream;
	}

	public Document(String path, Owner owner, long size, InputStream inputStream, Map<String, Object> meta, Map<String, String> attributes) {
		this(path, owner, inputStream, meta, attributes);
		setContentLength(size);
	}

	public Document(Document document) {
		super(document);
		this.meta = document.getMeta() != null ? new LinkedHashMap<>(document.getMeta()) : null;
		this.attributes = document.getAttributes() != null ? new LinkedHashMap<>(document.getAttributes()) : null;
	}

	/**
	 * Create instance of {@code Document}.
	 *
	 * @param uri
	 * @param bytes
	 */
	public Document(URI uri, byte[] bytes) {
		setContent(bytes);
		setContentLength(bytes!=null ? bytes.length : 0);
		if (uri!=null) {
			this.uri = uri.toString();			
		}
		if (bytes!=null) {
			this.inputStream = new ByteArrayInputStream(bytes);			
		}
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
	/**
	 * Get the value of property {@code folder}.
	 *
	 * @return the folder
	 */
	public Boolean getFolder() {
		return folder;
	}

	/**
	 * Set the value of property {@code folder}.
	 *
	 * @param folder the folder to set
	 */
	public void setFolder(Boolean folder) {
		this.folder = folder;
	}

	/**
	 * Get the value of property {@code category}.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Set the value of property {@code category}.
	 *
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Get the value of property {@code category2}.
	 *
	 * @return the category2
	 */
	public String getCategory2() {
		return category2;
	}

	/**
	 * Set the value of property {@code category2}.
	 *
	 * @param category2 the category2 to set
	 */
	public void setCategory2(String category2) {
		this.category2 = category2;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	@JsonIgnore
	public String getContentType() {
		if (meta == null) {
			return null;
		}
		Object contentType = meta.get(CONTENT_TYPE);
		return contentType != null ? contentType.toString() : null;
	}

	public void setContentLength(long contentLength) {
		meta.put(CONTENT_LENGTH, contentLength);
	}

	@JsonIgnore
	public long getContentLength() {
		if (meta != null && meta.containsKey(CONTENT_LENGTH)) {
			if (meta.get(CONTENT_LENGTH) instanceof Number) {
				return ((Number) meta.get(CONTENT_LENGTH)).longValue();

			}
			return Long.parseLong((String) meta.get(CONTENT_LENGTH));
		}
		return -1;
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


	/* (non-Javadoc)
	 * @see org.einnovator.util.model.ObjectBase#toString0(org.einnovator.util.model.ToStringCreator)
	 */
	@Override
	public ToStringCreator toString0(ToStringCreator creator) {
		return creator
			.append("name", name)
			.append("path", path)
			.append("#content", content != null ? content.length : null)
			.append("inputStream", inputStream)
			.append("folder", folder)
			.append("hidden", hidden)
			.append("template", template)
			.append("permissions", permissions)
			.append("tags", tags)
			.append("category", category)
			.append("category2", category2)
			.append("description", description)
			.append("attributes", attributes)
			.append("#attachments", attachments!=null ? attachments.size() : null)
			.append("icon", icon)
			.append("img", img);
	}


	
}