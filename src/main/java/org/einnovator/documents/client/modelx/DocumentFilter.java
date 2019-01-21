package org.einnovator.documents.client.modelx;

import org.einnovator.util.model.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentFilter extends DocumentOptions {

	protected String q;
	
	protected Boolean strict;
	
	protected String category;
		
	protected String tags;

	protected Boolean folders;
	
	protected String marker;
	 
	public DocumentFilter() {
	}	

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
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

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}

	
	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	@Override
	public ToStringCreator toString0(ToStringCreator creator) {
		return super.toString0(creator)
				.append("q", q)
				.append("strict", strict)
				.append("category", category)
				.append("tags", tags)
				.append("folders", folders)
				.append("marker", marker)
				;
	}

}
