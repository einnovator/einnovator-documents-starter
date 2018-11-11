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


	public DocumentBuilder content(InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	
	public DocumentBuilder name(InputStream inputStream) {
		this.inputStream = inputStream;
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
		return document;
	}
}
