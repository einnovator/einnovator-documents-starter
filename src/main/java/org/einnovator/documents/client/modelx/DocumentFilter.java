package org.einnovator.documents.client.modelx;

import java.util.ArrayList;
import java.util.List;

import org.einnovator.documents.client.model.Document;
import org.einnovator.util.model.ToStringCreator;
import org.springframework.util.StringUtils;

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
	
	public boolean filter(Document document) {
		String name = document.getRequiredName();
		if (!filter(name)) {
			return false;
		}
		return true;
	}
	
	public boolean filter(String name) {
		if (name!=null) {
			if (StringUtils.hasText(marker)) {
				if (name.compareTo(marker)<0) {
					return false;
				}
			}
			String name_ = name.toLowerCase();
			boolean strict = Boolean.TRUE.equals(this.strict);
			if (q!=null) {
				String q = this.q.toLowerCase();
				if (!strict && !name_.toLowerCase().contains(q)) {
					return false;
				}
				if (strict && !name_.toLowerCase().startsWith(q)) {
					return false;
				}
			}					
		}
		return true;
	}

	public List<Document> filter(List<Document> documents) {
		if (documents==null) {
			return null;
		}
		List<Document> documents2 = new ArrayList<>();
		for (Document document: documents) {
			if (document!=null && filter(document)) {
				documents2.add(document);
			}
		}
		return documents2;
	}
	
	public static List<Document> filter(DocumentFilter filter, List<Document> documents) {
		if (filter==null) {
			return documents;
		}
		return filter.filter(documents);
	}

}
