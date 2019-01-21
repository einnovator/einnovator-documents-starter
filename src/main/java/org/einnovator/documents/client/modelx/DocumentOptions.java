package org.einnovator.documents.client.modelx;

import java.net.URI;

import org.einnovator.documents.client.model.Document;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentOptions extends ObjectBase {

	//read
	
	public static final DocumentOptions META_ONLY = new DocumentOptions().meta(true);

	public static final DocumentOptions CONTENT_ONLY = new DocumentOptions().content(true);

	public static final DocumentOptions CONTENT_AND_META = new DocumentOptions().content(true).meta(true);

	public static final DocumentOptions WITH_ATTACHMENTS = new DocumentOptions().content(true).meta(true).attachments(true);

	public static final DocumentOptions WITH_VERSIONS = new DocumentOptions().content(true).meta(true).versions(true);

	public static final DocumentOptions WITH_VERSIONS_AND_ATTACHEMENTS = new DocumentOptions().content(true).meta(true).versions(true).attachments(true);

	//write
	
	public static final DocumentOptions CACHE = new DocumentOptions().cache(true);

	public static final DocumentOptions ASYNC = new DocumentOptions().async(true);

	public static final DocumentOptions SYNC1 = new DocumentOptions().async(true).tsync(1000L);

	public static final DocumentOptions SYNC10 = new DocumentOptions().async(true).tsync(10000L);

	public static final DocumentOptions SYNC60 = new DocumentOptions().async(true).tsync(60000L);


	//copy, move
	
	public static final DocumentOptions RECURSE = new DocumentOptions().recurse(true);

	public static final DocumentOptions OVERWRITE = new DocumentOptions().overwrite(true);

	public static final DocumentOptions RECURSE_OVERWRITE = new DocumentOptions().recurse(true).overwrite(true);

	//delete

	public static final DocumentOptions RECURSE_FORCE = new DocumentOptions().recurse(true).force(true);

	public static final DocumentOptions FORCE = new DocumentOptions().force(true);

	
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
	
	//identify

	protected String username;

	 
	//notifications
	
	protected Boolean publish;

	//permission 
	
	protected boolean publicLink;
	
	protected boolean publicShare;


	public DocumentOptions() {
	}


	public DocumentOptions(Boolean content, Boolean meta) {
		this.content = content;
		this.meta = meta;
	}

	public DocumentOptions(Boolean content, Boolean meta, Boolean attachments, Boolean versions) {
		this.content = content;
		this.meta = meta;
		this.attachments = attachments;
		this.versions = versions;
	}

	public DocumentOptions(DocumentOptions... options) {
		for (DocumentOptions options1: options) {
			MappingUtils.updateObjectFromNonNull(this,options1);
		}
	}

	public Boolean getContent() {
		return content;
	}

	public void setContent(Boolean content) {
		this.content = content;
	}

	public Boolean getMeta() {
		return meta;
	}

	public void setMeta(Boolean meta) {
		this.meta = meta;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public Boolean getAttachments() {
		return attachments;
	}

	public void setAttachments(Boolean attachments) {
		this.attachments = attachments;
	}

	public Boolean getVersions() {
		return versions;
	}

	public void setVersions(Boolean versions) {
		this.versions = versions;
	}

	public Boolean getCache() {
		return cache;
	}

	public void setCache(Boolean cache) {
		this.cache = cache;
	}

	public Boolean getAsync() {
		return async;
	}

	public void setAsync(Boolean async) {
		this.async = async;
	}
	
	public Boolean getOverwrite() {
		return overwrite;
	}

	public void setOverwrite(Boolean overwrite) {
		this.overwrite = overwrite;
	}

	public Long getTsync() {
		return tsync;
	}

	public void setTsync(Long tsync) {
		this.tsync = tsync;
	}

	public Boolean getRecurse() {
		return recurse;
	}

	public void setRecurse(Boolean recurse) {
		this.recurse = recurse;
	}
	public Boolean getForce() {
		return force;
	}

	public void setForce(Boolean force) {
		this.force = force;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}


	public DocumentOptions content(Boolean content) {
		this.content = content;
		return this;
	}

	public DocumentOptions meta(Boolean meta) {
		this.meta = meta;
		return this;
	}

	public DocumentOptions versions(Boolean versions) {
		this.versions = versions;
		return this;
	}

	public DocumentOptions attachments(Boolean attachments) {
		this.attachments = attachments;
		return this;
	}

	public DocumentOptions recurse(Boolean recurse)  {
		this.recurse = recurse;
		return this;
	}

	public DocumentOptions overwrite(Boolean overwrite)  {
		this.overwrite = overwrite;
		return this;
	}
	
	public DocumentOptions cache(Boolean cache)  {
		this.cache = cache;
		return this;
	}

	public DocumentOptions async(Boolean async)  {
		this.async = async;
		return this;
	}

	public DocumentOptions tsync(Long tsync)  {
		this.tsync = tsync;
		return this;
	}

	public DocumentOptions force(Boolean force)  {
		this.force = force;
		return this;
	}

	public DocumentOptions username(String username)  {
		this.username = username;
		return this;
	}

	public DocumentOptions publish(Boolean publish)  {
		this.publish = publish;
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
				.append("username", username)
				;
	}
	
}
