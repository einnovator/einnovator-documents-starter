package org.einnovator.documents.client.model;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.einnovator.util.model.ToStringCreator;
import org.einnovator.util.security.SecurityUtil;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A {@code Document}.
 *
 * @author support@einnovator.org
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Document extends ProtectedEntity {

	public static final String DELIMITER = "/";

	private String uri;

	private String path;
	
	private DocumentType type;
	
	private String name;

	@JsonIgnore
	private byte[] content;

	@JsonIgnore
	private InputStream inputStream;

	private boolean folder;

	private Boolean hidden;

	private Boolean system;

	private Boolean head;

	private String version;

	@JsonIgnore
	private Date versionDate;

	private Long contentLength;
	
	private String contentType;
	
	private String contentEncoding;
	
	private String contentDisposition;
	
	private String contentLanguage;
	
	private String contentMD5;
	
	private Long[] contentRange;
	
	private String etag;
	
	private String cacheControl;
	
	private Long expires;
	
	private Date expireDate;
	
	@JsonIgnore
	private String expireDateFormatted;
	
	private String storageClass;
	
	private Map<String, String> attributes = new LinkedHashMap<>();

	private List<Document> attachments = new ArrayList<>();

	private List<String> tags = new ArrayList<>();

	private String category;
	private String info;
	private String template;
	private String icon;
	private String img;

	private String link;

	private String channelId;


	//
	// Constructors
	//
	
	/**
	 * Create instance of {@code Document}.
	 *
	 */
	public Document() {
	}

	/**
	 * Create instance of {@code Document}.
	 *
	 * @param path the path
	 */
	public Document(String path) {
		this(path, null);
	}

	/**
	 * Create instance of {@code Document}.
	 *
	 * @param path the path
	 * @param owner the owner username
	 */
	public Document(String path, String owner) {
		this.path = path;
		this.owner = owner;
	}

	/**
	 * Create instance of {@code Document}.
	 *
	 * @param path the path
	 * @param owner the owner username
	 * @param content the byte content
	 */
	public Document(String path, String owner, byte[] content) {
		this(path, owner);
		this.content = content;
		setOrUpdateSize(false);
	}

	/**
	 * Create instance of {@code Document}.
	 *
	 * @param path the path
	 * @param owner the owner username
	 * @param inputStream the {@code InputStream} for the content
	 */
	public Document(String path, String owner, InputStream inputStream) {
		this(path, owner);
		this.inputStream = inputStream;
	}

	/**
	 * Create instance of {@code Document}.
	 *
	 * @param path the path
	 * @param owner the owner username
	 * @param size the {@code Document} size
	 * @param inputStream the {@code InputStream} for the content
	 */
	public Document(String path, String owner, long size, InputStream inputStream) {
		this(path, owner, inputStream);
		setContentLength(size);
	}

	/**
	 * Create instance of {@code Document}.
	 *
	 * @param path the path
	 * @param owner the owner username
	 * @param size the {@code Document} size
	 * @param inputStream the {@code InputStream} for the content
	 * @param contentType the content type
	 */
	public Document(String path, String owner, long size, InputStream inputStream, String contentType) {
		this(path, owner, inputStream);
		setContentLength(size);
		setContentType(contentType);
	}
	
	/**
	 * Create instance of {@code Document}.
	 *
	 * @param path the path
	 * @param owner the owner username
	 * @param content the byte content
	 * @param contentType the content type
	 */
	public Document(String path, String owner, byte[] content, String contentType) {
		this(path, owner, content);
		setContentType(contentType);
	}


	/**
	 * Create instance of {@code Document}.
	 *
	 * @param obj a prototype
	 */
	public Document(Object obj) {
		super(obj);
		if (obj instanceof Document) {
			Document document = (Document) obj;
			this.attributes = document.getAttributes() != null ? new LinkedHashMap<>(document.getAttributes()) : null;
		}
	}

	/**
	 * Create instance of {@code Document}.
	 *
	 * @param uri the uri
	 * @param bytes the content
	 */
	public Document(URI uri, byte[] bytes) {
		setContent(bytes);
		setContentLength(bytes!=null ? bytes.length : 0L);
		if (uri!=null) {
			this.uri = uri.toString();			
		}
		if (bytes!=null) {
			this.inputStream = new ByteArrayInputStream(bytes);			
		}
	}


	//
	// Getter/Setters
	//
	
	/**
	 * Get the value of property {@code inputStream}.
	 *
	 * @return the inputStream
	 */
	@JsonIgnore
	public InputStream getInputStream() {
		return inputStream;
	}
	
	/**
	 * Set the value of property {@code inputStream}.
	 *
	 * @param inputStream the value of property inputStream
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	/**
	 * Get the value of property {@code content}.
	 *
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * Set the value of property {@code content}.
	 *
	 * @param content the value of property content
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * Get the value of property {@code path}.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the value of property {@code path}.
	 *
	 * @param path the value of property path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get the value of property {@code name}.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the value of property {@code name}.
	 *
	 * @param name the value of property name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the value of property {@code type}.
	 *
	 * @return the type
	 */
	public DocumentType getType() {
		return type;
	}

	/**
	 * Set the value of property {@code type}.
	 *
	 * @param type the value of property type
	 */
	public void setType(DocumentType type) {
		this.type = type;
	}

	/**
	 * Get the value of property {@code head}.
	 *
	 * @return the head
	 */
	public Boolean getHead() {
		return head;
	}

	/**
	 * Get the value of property {@code folder}.
	 *
	 * @return the folder
	 */
	public boolean isFolder() {
		return DocumentType.FOLDER==type || folder;
	}

	/**
	 * Set the value of property {@code folder}.
	 *
	 * @param folder the value of property folder
	 */
	public void setFolder(boolean folder) {
		this.folder = folder;
	}


	/**
	 * Get the value of property {@code version}.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the value of property {@code version}.
	 *
	 * @param version the value of property version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Get the value of property {@code versionDate}.
	 *
	 * @return the versionDate
	 */
	public Date getVersionDate() {
		return versionDate;
	}

	/**
	 * Set the value of property {@code versionDate}.
	 *
	 * @param versionDate the value of property versionDate
	 */
	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
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
	 * @param category the value of property category
	 */
	public void setCategory(String category) {
		this.category = category;
	}


	/**
	 * Get the value of property {@code attributes}.
	 *
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * Set the value of property {@code attributes}.
	 *
	 * @param attributes the value of property attributes
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Get the value of property {@code tags}.
	 *
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * Set the value of property {@code tags}.
	 *
	 * @param tags the value of property tags
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * Get the value of property {@code info}.
	 *
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Set the value of property {@code info}.
	 *
	 * @param info the value of property info
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * Get the value of property {@code contentLength}.
	 *
	 * @return the contentLength
	 */
	public Long getContentLength() {
		return contentLength;
	}

	/**
	 * Set the value of property {@code contentLength}.
	 *
	 * @param contentLength the value of property contentLength
	 */
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * Get the value of property {@code contentType}.
	 *
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Set the value of property {@code contentType}.
	 *
	 * @param contentType the value of property contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	
	/**
	 * Get the value of property {@code contentEncoding}.
	 *
	 * @return the contentEncoding
	 */
	public String getContentEncoding() {
		return contentEncoding;
	}

	/**
	 * Set the value of property {@code contentEncoding}.
	 *
	 * @param contentEncoding the value of property contentEncoding
	 */
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	/**
	 * Get the value of property {@code contentDisposition}.
	 *
	 * @return the contentDisposition
	 */
	public String getContentDisposition() {
		return contentDisposition;
	}

	/**
	 * Set the value of property {@code contentDisposition}.
	 *
	 * @param contentDisposition the value of property contentDisposition
	 */
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	/**
	 * Get the value of property {@code contentLanguage}.
	 *
	 * @return the contentLanguage
	 */
	public String getContentLanguage() {
		return contentLanguage;
	}

	/**
	 * Set the value of property {@code contentLanguage}.
	 *
	 * @param contentLanguage the value of property contentLanguage
	 */
	public void setContentLanguage(String contentLanguage) {
		this.contentLanguage = contentLanguage;
	}

	/**
	 * Get the value of property {@code contentMD5}.
	 *
	 * @return the contentMD5
	 * @return this {@code Document}
	 */
	public String getContentMD5() {
		return contentMD5;
	}

	/**
	 * Set the value of property {@code contentMD5}.
	 *
	 * @param contentMD5 the value of property contentMD5
	 */
	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}

	/**
	 * Get the value of property {@code contentRange}.
	 *
	 * @return the contentRange
	 */
	public Long[] getContentRange() {
		return contentRange;
	}

	/**
	 * Set the value of property {@code contentRange}.
	 *
	 * @param contentRange the value of property contentRange
	 */
	public void setContentRange(Long[] contentRange) {
		this.contentRange = contentRange;
	}

	/**
	 * Get the value of property {@code etag}.
	 *
	 * @return the etag
	 */
	public String getETag() {
		return etag;
	}

	/**
	 * Set the value of property {@code etag}.
	 *
	 * @param etag the value of property etag
	 */
	public void setETag(String etag) {
		this.etag = etag;
	}

	/**
	 * Get the value of property {@code cacheControl}.
	 *
	 * @return the cacheControl
	 */
	public String getCacheControl() {
		return cacheControl;
	}

	/**
	 * Set the value of property {@code cacheControl}.
	 *
	 * @param cacheControl the value of property cacheControl
	 */
	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}

	/**
	 * Get the value of property {@code expires}.
	 *
	 * @return the expires
	 */
	public Long getExpires() {
		return expires;
	}

	/**
	 * Set the value of property {@code expires}.
	 *
	 * @param expires the value of property expires
	 */
	public void setExpires(Long expires) {
		this.expires = expires;
	}

	/**
	 * Get the value of property {@code expireDate}.
	 *
	 * @return the expireDate
	 */
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * Set the value of property {@code expireDate}.
	 *
	 * @param expireDate the value of property expireDate
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * Get the value of property {@code storageClass}.
	 *
	 * @return the storageClass
	 */
	public String getStorageClass() {
		return storageClass;
	}

	/**
	 * Set the value of property {@code storageClass}.
	 *
	 * @param storageClass the value of property storageClass
	 */
	public void setStorageClass(String storageClass) {
		this.storageClass = storageClass;
	}

	/**
	 * Get the value of property {@code expireDateFormatted}.
	 *
	 * @return the expireDateFormatted
	 */
	public String getExpireDateFormatted() {
		return expireDateFormatted;
	}

	/**
	 * Set the value of property {@code expireDateFormatted}.
	 *
	 * @param expireDateFormatted the value of property expireDateFormatted
	 */
	public void setExpireDateFormatted(String expireDateFormatted) {
		this.expireDateFormatted = expireDateFormatted;
	}

	/**
	 * Get the value of property {@code uri}.
	 *
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Set the value of property {@code uri}.
	 *
	 * @param uri the value of property uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Get the value of property {@code hidden}.
	 *
	 * @return the hidden
	 */
	public Boolean getHidden() {
		return hidden;
	}

	/**
	 * Set the value of property {@code hidden}.
	 *
	 * @param hidden the value of property hidden
	 */
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * Get the value of property {@code system}.
	 *
	 * @return the system
	 */
	public Boolean getSystem() {
		return system;
	}

	/**
	 * Set the value of property {@code system}.
	 *
	 * @param system the value of property system
	 */
	public void setSystem(Boolean system) {
		this.system = system;
	}

	/**
	 * Set the value of property {@code head}.
	 *
	 * @param head the value of property head
	 */
	public void setHead(Boolean head) {
		this.head = head;
	}

	/**
	 * Get the value of property {@code template}.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Set the value of property {@code template}.
	 *
	 * @param template the value of property template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Get the value of property {@code icon}.
	 *
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Set the value of property {@code icon}.
	 *
	 * @param icon the value of property icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Get the value of property {@code link}.
	 *
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Set the value of property {@code link}.
	 *
	 * @param link the value of property link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Get the value of property {@code channelId}.
	 *
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * Set the value of property {@code channelId}.
	 *
	 * @param channelId the value of property channelId
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * Get the value of property {@code img}.
	 *
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Set the value of property {@code img}.
	 *
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}
	
	/**
	 * Get the value of property {@code attachments}.
	 *
	 * @return the attachments
	 */
	public List<Document> getAttachments() {
		return attachments;
	}

	/**
	 * Set the value of property {@code attachments}.
	 *
	 * @param attachments the value of property attachments
	 */
	public void setAttachments(List<Document> attachments) {
		this.attachments = attachments;
	}

	//
	// With
	//
	
	/**
	 * Set the value of property {@code inputStream}.
	 *
	 * @param inputStream the value of property inputStream
	 * @return this {@code Document}
	 */
	public Document withInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	/**
	 * Set the value of property {@code name}.
	 *
	 * @param name the value of property name
	 * @return this {@code Document}
	 */
	public Document withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Set the value of property {@code folder}.
	 *
	 * @param folder the value of property folder
	 * @return this {@code Document}
	 */
	public Document withName(Boolean folder) {
		this.folder = folder;
		return this;
	}
	
	/**
	 * Set the value of property {@code etag}.
	 *
	 * @param etag the value of property etag
	 * @return this {@code Document}
	 */
	public Document withEtag(String etag) {
		this.etag = etag;
		return this;
	}

	
	/**
	 * Set the value of property {@code content}.
	 *
	 * @param content the value of property content
	 * @return this {@code Document}
	 */
	public Document withContent(byte[] content) {
		this.content = content;
		return this;
	}

	/**
	 * Set the value of property {@code category}.
	 *
	 * @param category the value of property category
	 * @return this {@code Document}
	 */
	public Document withCategory(String category) {
		this.category = category;
		return this;
	}
	
	/**
	 * Set the value of property {@code type}.
	 *
	 * @param type the value of property type
	 * @return this {@code Document}
	 */
	public Document withType(DocumentType type) {
		this.type = type;
		return this;
	}

	/**
	 * Set the value of property {@code path}.
	 *
	 * @param path the value of property path
	 * @return this {@code Document}
	 */
	public Document withPath(String path) {
		this.path = path;
		return this;
	}

	/**
	 * Set the value of property {@code folder}.
	 *
	 * @param folder the value of property folder
	 * @return this {@code Document}
	 */
	public Document withFolder(Boolean folder) {
		this.folder = folder;
		return this;
	}

	/**
	 * Set the value of property {@code hidden}.
	 *
	 * @param hidden the value of property hidden
	 * @return this {@code Document}
	 */
	public Document withHidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}


	/**
	 * Set the value of property {@code version}.
	 *
	 * @param version the value of property version
	 * @return this {@code Document}
	 */
	public Document withVersion(String version) {
		this.version = version;
		return this;
	}

	/**
	 * Set the value of property {@code versionDate}.
	 *
	 * @param versionDate the value of property versionDate
	 * @return this {@code Document}
	 */
	public Document withVersionDate(Date versionDate) {
		this.versionDate = versionDate;
		return this;
	}


	/**
	 * Set the value of property {@code attributes}.
	 *
	 * @param attributes the value of property attributes
	 * @return this {@code Document}
	 */
	public Document withAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
		return this;
	}

	/**
	 * Set the value of property {@code tags}.
	 *
	 * @param tags the value of property tags
	 * @return this {@code Document}
	 */
	public Document withTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	/**
	 * Set the value of property {@code info}.
	 *
	 * @param info the value of property info
	 * @return this {@code Document}
	 */
	public Document withInfo(String info) {
		this.info = info;
		return this;
	}

	/**
	 * Set the value of property {@code contentLength}.
	 *
	 * @param contentLength the value of property contentLength
	 * @return this {@code Document}
	 */
	public Document withContentLength(Long contentLength) {
		this.contentLength = contentLength;
		return this;
	}

	/**
	 * Set the value of property {@code contentType}.
	 *
	 * @param contentType the value of property contentType
	 * @return this {@code Document}
	 */
	public Document withContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * Set the value of property {@code contentEncoding}.
	 *
	 * @param contentEncoding the value of property contentEncoding
	 * @return this {@code Document}
	 */
	public Document withContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
		return this;
	}

	/**
	 * Set the value of property {@code contentDisposition}.
	 *
	 * @param contentDisposition the value of property contentDisposition
	 * @return this {@code Document}
	 */
	public Document withContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
		return this;
	}


	/**
	 * Set the value of property {@code contentLanguage}.
	 *
	 * @param contentLanguage the value of property contentLanguage
	 * @return this {@code Document}
	 */
	public Document withContentLanguage(String contentLanguage) {
		this.contentLanguage = contentLanguage;
		return this;
	}


	/**
	 * Set the value of property {@code contentMD5}.
	 *
	 * @param contentMD5 the value of property contentMD5
	 * @return this {@code Document}
	 */
	public Document withContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
		return this;
	}

	/**
	 * Set the value of property {@code contentRange}.
	 *
	 * @param contentRange the value of property contentRange
	 * @return this {@code Document}
	 */
	public Document withContentRange(Long[] contentRange) {
		this.contentRange = contentRange;
		return this;
	}


	/**
	 * Set the value of property {@code etag}.
	 *
	 * @param etag the value of property etag
	 * @return this {@code Document}
	 */
	public Document withETag(String etag) {
		this.etag = etag;
		return this;
	}

	/**
	 * Set the value of property {@code cacheControl}.
	 *
	 * @param cacheControl the value of property cacheControl
	 * @return this {@code Document}
	 */
	public Document withCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
		return this;
	}

	/**
	 * Set the value of property {@code expires}.
	 *
	 * @param expires the value of property expires
	 * @return this {@code Document}
	 */
	public Document withExpires(Long expires) {
		this.expires = expires;
		return this;
	}

	/**
	 * Set the value of property {@code expireDate}.
	 *
	 * @param expireDate the value of property expireDate
	 * @return this {@code Document}
	 */
	public Document withExpireDate(Date expireDate) {
		this.expireDate = expireDate;
		return this;
	}

	/**
	 * Set the value of property {@code storageClass}.
	 *
	 * @param storageClass the value of property storageClass
	 * @return this {@code Document}
	 */
	public Document withStorageClass(String storageClass) {
		this.storageClass = storageClass;
		return this;
	}

	/**
	 * Set the value of property {@code expireDateFormatted}.
	 *
	 * @param expireDateFormatted the value of property expireDateFormatted
	 * @return this {@code Document}
	 */
	public Document withExpireDateFormatted(String expireDateFormatted) {
		this.expireDateFormatted = expireDateFormatted;
		return this;
	}

	/**
	 * Set the value of property {@code uri}.
	 *
	 * @param uri the value of property uri
	 * @return this {@code Document}
	 */
	public Document withUri(String uri) {
		this.uri = uri;
		return this;
	}


	/**
	 * Set the value of property {@code hidden}.
	 *
	 * @param hidden the value of property hidden
	 * @return this {@code Document}
	 */
	public Document withHidden(Boolean hidden) {
		this.hidden = hidden;
		return this;
	}


	/**
	 * Set the value of property {@code head}.
	 *
	 * @param head the value of property head
	 * @return this {@code Document}
	 */
	public Document withHead(Boolean head) {
		this.head = head;
		return this;
	}

	/**
	 * Set the value of property {@code template}.
	 *
	 * @param template the value of property template
	 * @return this {@code Document}
	 */
	public Document withTemplate(String template) {
		this.template = template;
		return this;
	}

	/**
	 * Set the value of property {@code icon}.
	 *
	 * @param icon the value of property icon
	 * @return this {@code Document}
	 */
	public Document withIcon(String icon) {
		this.icon = icon;
		return this;
	}


	/**
	 * Set the value of property {@code link}.
	 *
	 * @param link the value of property link
	 * @return this {@code Document}
	 */
	public Document withLink(String link) {
		this.link = link;
		return this;
	}

	/**
	 * Set the value of property {@code channelId}.
	 *
	 * @param channelId the value of property channelId
	 * @return this {@code Document}
	 */
	public Document withChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}


	/**
	 * Set the value of property {@code img}.
	 *
	 * @param img the img to with
	 * @return this {@code Document}
	 */
	public Document withImg(String img) {
		this.img = img;
		return this;
	}

	//
	// Attributes
	//
	
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

	//
	// Util
	//
	
	@JsonIgnore
	public Long size() { // alias
		return getContentLength();
	}

	@JsonIgnore
	public String getFormatedSize() { //alias
		return getFormatedContentLength();
	}
	
	private void setOrUpdateSize(boolean force) {
		if (content != null && (force || contentLength==null)) {
			contentLength = (long)content.length;
		}
	}

	@JsonIgnore
	public String getFormatedContentLength() {
		Long bytes = size();
		if (bytes==null) {
			return null;
		}
		int unit = 1000;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = ("kMGTPE").charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
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
	
	@JsonIgnore
	public InputStream getOrCreateInputStream() {
		if (inputStream == null) {
			if (content == null) {
				content = new byte[0];
				setContentLength(0L);
			}
			inputStream = new ByteArrayInputStream(content);
		}
		return inputStream;
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

	@JsonIgnore
	public byte[] getOrReadContent() {
		if (content == null && inputStream != null) {
			try {
				content = StreamUtils.copyToByteArray(inputStream);
				inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return content;
	}
	
	@JsonIgnore
	public String getRequiredName() {
		return getRequiredName(DELIMITER);
	}

	@JsonIgnore
	public String getFilename() {
		return getRequiredName();
	}


	public String getRequiredName(String delimiter) {
		if (this.name==null) {
			this.name = getNameFromPath(delimiter);
		}
		return this.name;
	}

	public String getNameFromPath(String delimiter) {
		String name;
		if (path != null) {
			int i = path.lastIndexOf(delimiter);
			if (i > 0) {
				if (path.endsWith(delimiter)) {
					folder = true;
					String path2 = path.substring(0, path.length() - 1);
					i = path2.lastIndexOf(delimiter);
					if (i > 0) {
						name = path2.substring(i + 1);
					} else {
						name = path2;
					}
				} else {
					name = path.substring(i + 1);
				}
			} else {
				name = path;
			}
		} else {
			name = null;
		}
		return name;
	}

	public String setNameFromPath(String delimiter) {
		String name = getNameFromPath(delimiter);
		this.name = name;
		return name;
	}

	@Override
	public ToStringCreator toString0(ToStringCreator creator) {
		return creator
			.append("name", name)
			.append("path", path)
			.append("uri", uri)
			.append("folder", folder)
			.append("hidden", hidden)
			.append("version", version)
			.append("versionDate", versionDate)
			.append("head", head)
			.append("#content", content != null ? content.length : null)
			.append("inputStream", inputStream)
			.append("folder", folder)
			.append("hidden", hidden)
			.append("template", template)
			.append("tags", tags)
			.append("category", category)
			.append("info", info)
			.append("#info", info!=null && !info.isEmpty() ? info.length() : null)
			.append("attributes", attributes)
			.append("#attachments", attachments!=null ? attachments.size() : null)
			.append("icon", icon)
			.append("img", img);
	}

	//
	// Static util
	//
	
	public static Document makeFolder(String path, Document document) {
		if (document==null) {
			document = new Document();
		}
		path = normalizePath(path);
		if (!path.endsWith(DELIMITER)) {
			path += DELIMITER;
		}
		document
			.withPath(path)
			.withType(DocumentType.FOLDER)
			.withContentLength(0L);
		document.setNameFromPath(DELIMITER);
		return document;
	}
	
	public static String normalizePath(String path) {
		if (!StringUtils.hasText(path)) {
			path = DELIMITER;
		} else {
			path = path.trim();
		}
		if (!path.startsWith(DELIMITER)) {
			path = DELIMITER + path;
		}
		return path;
	}
	
	
	public static Document makeDocument(MultipartFile file, String path, Document document) {
		if (document==null) {
			document = new Document();
		}
		if (document.getOwner()==null) {
			document.withOwner(SecurityUtil.getPrincipalName());
		}
		String contentType = getContentType(file);
		document.withPath(path).withContentType(contentType);
		try {
			document.withContentLength(file.getSize());
			document.inputStream = file.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return document;
	}
	
	public static String getContentType(MultipartFile file) {
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
			String contentType = URLConnection.guessContentTypeFromStream(stream);
			if (!StringUtils.hasText(contentType)) {
				contentType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());
			}
			return StringUtils.hasText(contentType) ? contentType : file.getContentType();			
		} catch (IOException e) {
			return null;
		}
	}
	
}