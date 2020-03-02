package org.einnovator.documents.client.modelx;

import java.util.ArrayList;
import java.util.List;

import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.DocumentType;
import org.einnovator.util.model.ToStringCreator;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A filter for {@code Document}s.
 *
 * @author support@einnovator.org
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentFilter extends DocumentOptions {

	protected String q;

	protected String contentType;

	protected String category;
		
	protected DocumentType type;
	
	protected String marker;
	 
	//
	// Constructors
	//
	
	/**
	 * Create instance of {@code DocumentFilter}.
	 *
	 */
	public DocumentFilter() {
	}	

	//
	// Getters/Setters
	//

	/**
	 * Get the value of property {@code q}.
	 *
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * Set the value of property {@code q}.
	 *
	 * @param q the value of property q
	 */
	public void setQ(String q) {
		this.q = q;
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
	 * Get the value of property {@code marker}.
	 *
	 * @return the marker
	 */
	public String getMarker() {
		return marker;
	}

	/**
	 * Set the value of property {@code marker}.
	 *
	 * @param marker the value of property marker
	 */
	public void setMarker(String marker) {
		this.marker = marker;
	}

	//
	// With
	//

	/**
	 * Set the value of property {@code q}.
	 *
	 * @param q the value of property q
	 * @return this {@code DocumentFilter}
	 */
	public DocumentFilter withQ(String q) {
		this.q = q;
		return this;
	}

	/**
	 * Set the value of property {@code contentType}.
	 *
	 * @param contentType the value of property contentType
	 * @return this {@code DocumentFilter}
	 */
	public DocumentFilter withContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * Set the value of property {@code category}.
	 *
	 * @param category the value of property category
	 * @return this {@code DocumentFilter}
	 */
	public DocumentFilter withCategory(String category) {
		this.category = category;
		return this;
	}

	/**
	 * Set the value of property {@code type}.
	 *
	 * @param type the value of property type
	 * @return this {@code DocumentFilter}
	 */
	public DocumentFilter withType(DocumentType type) {
		this.type = type;
		return this;
	}

	/**
	 * Set the value of property {@code marker}.
	 *
	 * @param marker the value of property marker
	 * @return this {@code DocumentFilter}
	 */
	public DocumentFilter withMarker(String marker) {
		this.marker = marker;
		return this;
	}



	@Override
	public ToStringCreator toString0(ToStringCreator creator) {
		return super.toString0(creator)
				.append("q", q)
				.append("type", type)
				.append("contentType", contentType)
				.append("category", category)
				.append("marker", marker)
				;
	}
	
	//
	// Client side filtering
	//
	
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
			if (q!=null) {
				String q = this.q.toLowerCase();
				if (!name_.toLowerCase().contains(q)) {
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
