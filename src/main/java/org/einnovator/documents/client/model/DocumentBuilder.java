package org.einnovator.documents.client.model;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentBuilder {
	
	private String path;
	
	private Owner owner;
	
	private Map<String, Object> meta = new LinkedHashMap<>();

	private byte[] content;
	
	private InputStream inputStream;

	private String name;
	
	private Long contentLength;

	private String contentType;

	private String category;

	public DocumentBuilder() {
	}

	
	public DocumentBuilder path(String path) {
		this.path = path;
		return this;
	}


	public DocumentBuilder owner(Owner owner) {
		this.owner = owner;
		return this;
	}


	public DocumentBuilder meta(Map<String, Object> meta) {
		this.meta = meta;
		return this;
	}


	public DocumentBuilder content(byte[] content) {
		this.content = content;
		return this;
	}

	public DocumentBuilder inputStream(InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}
	
	public DocumentBuilder name(String name) {
		this.name = name;
		return this;
	}
	

	public DocumentBuilder contentLength(Long contentLength) {
		this.contentLength = contentLength;
		return this;
	}

	public DocumentBuilder contentLength(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public DocumentBuilder category(String category) {
		this.category = category;
		return this;
	}


	public Document build() {
		Document document = new Document();
		document.setPath(path);
		document.setMeta(meta);
		document.setName(name);
		document.setInputStream(inputStream);
		document.setContent(content);
		document.setOwner(owner);
		document.setContentLength(contentLength);
		document.setContentType(contentType);
		document.setCategory(category);
		return document;

	}
}
