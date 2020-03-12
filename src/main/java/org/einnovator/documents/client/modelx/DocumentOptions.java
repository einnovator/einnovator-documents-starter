package org.einnovator.documents.client.modelx;

import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.ShareType;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.model.EntityOptions;
import org.einnovator.util.model.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentOptions extends EntityOptions<Document> {

	//read
	
	public static final DocumentOptions META_ONLY = new DocumentOptions().withMeta(true);

	public static final DocumentOptions CONTENT_ONLY = new DocumentOptions().withContent(true);

	public static final DocumentOptions CONTENT_AND_META = new DocumentOptions().withContent(true).withMeta(true);

	public static final DocumentOptions WITH_ATTACHMENTS = new DocumentOptions().withContent(true).withMeta(true).withAttachments(true);

	public static final DocumentOptions WITH_VERSIONS = new DocumentOptions().withContent(true).withMeta(true).withVersions(true);

	public static final DocumentOptions WITH_VERSIONS_AND_ATTACHEMENTS = new DocumentOptions().withContent(true).withMeta(true).withVersions(true).withAttachments(true);

	public static final DocumentOptions PUBLIC = new DocumentOptions().withSharing(ShareType.PUBLIC);
	public static final DocumentOptions PRIVATE = new DocumentOptions().withSharing(ShareType.PRIVATE);
	public static final DocumentOptions RESTRICTED = new DocumentOptions().withSharing(ShareType.RESTRICTED);
	public static final DocumentOptions ROLE = new DocumentOptions().withSharing(ShareType.ROLE);

	//write
	
	public static final DocumentOptions CACHE = new DocumentOptions().withCache(true);

	public static final DocumentOptions ASYNC = new DocumentOptions().withAsync(true);

	public static final DocumentOptions SYNC1 = new DocumentOptions().withAsync(true).withTsync(1000L);

	public static final DocumentOptions SYNC10 = new DocumentOptions().withAsync(true).withTsync(10000L);

	public static final DocumentOptions SYNC60 = new DocumentOptions().withAsync(true).withTsync(60000L);


	//copy, move
	
	public static final DocumentOptions RECURSE = new DocumentOptions().withRecurse(true);

	public static final DocumentOptions OVERWRITE = new DocumentOptions().withOverwrite(true);

	public static final DocumentOptions RECURSE_OVERWRITE = new DocumentOptions().withRecurse(true).withOverwrite(true);

	//delete

	public static final DocumentOptions RECURSE_FORCE = new DocumentOptions().withRecurse(true).withForce(true);

	public static final DocumentOptions FORCE = new DocumentOptions().withForce(true);

	
	//read
	protected Boolean content;
	
	protected Boolean meta;

	protected Boolean attachments;

	protected Boolean versions;

	protected String encoding;
	
	//write

	protected Boolean cache;
	
	protected Boolean async;

	protected Long tsync;


	//copy, move, delete

	protected Boolean recurse;
	
	protected Boolean overwrite;

	//delete
	
	protected Boolean force;
	
	// sharing
	
	protected ShareType sharing;

	//
	// Constructors
	//
	
	/**
	 * Create instance of {@code DocumentOptions}.
	 *
	 */
	public DocumentOptions() {
	}
	
	/**
	 * Create instance of {@code DocumentOptions}.
	 *
	 * @param obj a prototype
	 */
	public DocumentOptions(Object obj) {
		super(obj);
	}

	/**
	 * Create instance of {@code DocumentOptions}.
	 *
	 * @param options a variadic array of {@code DocumentOptions} to merge settings
	 */
	public DocumentOptions(DocumentOptions... options) {
		for (DocumentOptions options1: options) {
			MappingUtils.updateObjectFromNonNull(this,options1);
		}
	}

	//
	// Getter Setters
	//

	
	/**
	 * Get the value of property {@code content}.
	 *
	 * @return the content
	 */
	public Boolean getContent() {
		return content;
	}

	/**
	 * Set the value of property {@code content}.
	 *
	 * @param content the value of property content
	 */
	public void setContent(Boolean content) {
		this.content = content;
	}

	/**
	 * Get the value of property {@code meta}.
	 *
	 * @return the meta
	 */
	public Boolean getMeta() {
		return meta;
	}

	/**
	 * Set the value of property {@code meta}.
	 *
	 * @param meta the value of property meta
	 */
	public void setMeta(Boolean meta) {
		this.meta = meta;
	}

	/**
	 * Get the value of property {@code attachments}.
	 *
	 * @return the attachments
	 */
	public Boolean getAttachments() {
		return attachments;
	}

	/**
	 * Set the value of property {@code attachments}.
	 *
	 * @param attachments the value of property attachments
	 */
	public void setAttachments(Boolean attachments) {
		this.attachments = attachments;
	}

	/**
	 * Get the value of property {@code versions}.
	 *
	 * @return the versions
	 */
	public Boolean getVersions() {
		return versions;
	}

	/**
	 * Set the value of property {@code versions}.
	 *
	 * @param versions the value of property versions
	 */
	public void setVersions(Boolean versions) {
		this.versions = versions;
	}

	/**
	 * Get the value of property {@code encoding}.
	 *
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Set the value of property {@code encoding}.
	 *
	 * @param encoding the value of property encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Get the value of property {@code cache}.
	 *
	 * @return the cache
	 */
	public Boolean getCache() {
		return cache;
	}

	/**
	 * Set the value of property {@code cache}.
	 *
	 * @param cache the value of property cache
	 */
	public void setCache(Boolean cache) {
		this.cache = cache;
	}

	/**
	 * Get the value of property {@code async}.
	 *
	 * @return the async
	 */
	public Boolean getAsync() {
		return async;
	}

	/**
	 * Set the value of property {@code async}.
	 *
	 * @param async the value of property async
	 */
	public void setAsync(Boolean async) {
		this.async = async;
	}

	/**
	 * Get the value of property {@code tsync}.
	 *
	 * @return the tsync
	 */
	public Long getTsync() {
		return tsync;
	}

	/**
	 * Set the value of property {@code tsync}.
	 *
	 * @param tsync the value of property tsync
	 */
	public void setTsync(Long tsync) {
		this.tsync = tsync;
	}

	/**
	 * Get the value of property {@code recurse}.
	 *
	 * @return the recurse
	 */
	public Boolean getRecurse() {
		return recurse;
	}

	/**
	 * Set the value of property {@code recurse}.
	 *
	 * @param recurse the value of property recurse
	 */
	public void setRecurse(Boolean recurse) {
		this.recurse = recurse;
	}

	/**
	 * Get the value of property {@code overwrite}.
	 *
	 * @return the overwrite
	 */
	public Boolean getOverwrite() {
		return overwrite;
	}

	/**
	 * Set the value of property {@code overwrite}.
	 *
	 * @param overwrite the value of property overwrite
	 */
	public void setOverwrite(Boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * Get the value of property {@code force}.
	 *
	 * @return the force
	 */
	public Boolean getForce() {
		return force;
	}

	/**
	 * Set the value of property {@code force}.
	 *
	 * @param force the value of property force
	 */
	public void setForce(Boolean force) {
		this.force = force;
	}	

	/**
	 * Get the value of property {@code sharing}.
	 *
	 * @return the sharing
	 */
	public ShareType getSharing() {
		return sharing;
	}

	/**
	 * Set the value of property {@code sharing}.
	 *
	 * @param sharing the value of property sharing
	 */
	public void setSharing(ShareType sharing) {
		this.sharing = sharing;
	}

	//
	// With
	//

	/**
	 * Set the value of property {@code content}.
	 *
	 * @param content the value of property content
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withContent(Boolean content) {
		this.content = content;
		return this;
	}

	/**
	 * Set the value of property {@code meta}.
	 *
	 * @param meta the value of property meta
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withMeta(Boolean meta) {
		this.meta = meta;
		return this;
	}

	/**
	 * Set the value of property {@code attachments}.
	 *
	 * @param attachments the value of property attachments
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withAttachments(Boolean attachments) {
		this.attachments = attachments;
		return this;
	}

	/**
	 * Set the value of property {@code versions}.
	 *
	 * @param versions the value of property versions
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withVersions(Boolean versions) {
		this.versions = versions;
		return this;
	}

	/**
	 * Set the value of property {@code encoding}.
	 *
	 * @param encoding the value of property encoding
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	/**
	 * Set the value of property {@code cache}.
	 *
	 * @param cache the value of property cache
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withCache(Boolean cache) {
		this.cache = cache;
		return this;
	}

	/**
	 * Set the value of property {@code async}.
	 *
	 * @param async the value of property async
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withAsync(Boolean async) {
		this.async = async;
		return this;
	}

	/**
	 * Set the value of property {@code tsync}.
	 *
	 * @param tsync the value of property tsync
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withTsync(Long tsync) {
		this.tsync = tsync;
		return this;
	}

	/**
	 * Set the value of property {@code recurse}.
	 *
	 * @param recurse the value of property recurse
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withRecurse(Boolean recurse) {
		this.recurse = recurse;
		return this;
	}

	/**
	 * Set the value of property {@code overwrite}.
	 *
	 * @param overwrite the value of property overwrite
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withOverwrite(Boolean overwrite) {
		this.overwrite = overwrite;
		return this;
	}

	/**
	 * Set the value of property {@code force}.
	 *
	 * @param force the value of property force
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withForce(Boolean force) {
		this.force = force;
		return this;
	}


	/**
	 * Set the value of property {@code sharing}.
	 *
	 * @param sharing the value of property sharing
	 * @return this {@code DocumentOptions}
	 */
	public DocumentOptions withSharing(ShareType sharing) {
		this.sharing = sharing;
		return this;
	}


	
	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return super.toString(creator)
				.append("content", content)
				.append("meta", meta)
				.append("versions", versions)
				.append("attachments", attachments)
				.append("recurse", recurse)
				.append("overwrite", overwrite)
				.append("sharing", sharing)
				;
	}

	
	//
	// Static utils
	//
	
	
	//
	// Statuc util
	//
	
	public static boolean isContent(DocumentOptions options) {
		if (options==null || options.content==null || Boolean.TRUE.equals(options.getContent())) {
			return true;
		}
		return false;
	}
	
	public static boolean isMeta(DocumentOptions options)  {
		return options==null || options.meta==null || Boolean.TRUE.equals(options.meta);
	}

	public static boolean isVersions(DocumentOptions options)  {
		return options!=null && Boolean.TRUE.equals(options.versions);
	}

	public static boolean isAttachments(DocumentOptions options)  {
		return options!=null && Boolean.TRUE.equals(options.attachments);
	}
	
	public static boolean isRecurse(DocumentOptions options)  {
		return options==null || !Boolean.FALSE.equals(options.recurse);
	}

	public static boolean isOverwrite(DocumentOptions options)  {
		return options!=null && Boolean.TRUE.equals(options.overwrite);
	}

	public static boolean isCache(DocumentOptions options)  {
		return options==null || !Boolean.FALSE.equals(options.cache);
	}

	public static boolean isAsync(DocumentOptions options)  {
		return options!=null && Boolean.TRUE.equals(options.async);
	}

	public static Long isTsync(DocumentOptions options)  {
		return options==null || options.tsync==null || options.tsync<=0 ? 0: options.tsync;
	}

	public static boolean isForce(DocumentOptions options)  {
		return options!=null && Boolean.TRUE.equals(options.force);
	}

	public static DocumentOptions options(DocumentOptions... options) {
		return new DocumentOptions(options);
	}


}
