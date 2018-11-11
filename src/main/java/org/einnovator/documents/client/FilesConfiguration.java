package org.einnovator.documents.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

public class FilesConfiguration {
	
	public static final String DEFAULT_UPLOAD_FOLDER = "/.upload/";

	public static final String DEFAULT_TMP_FOLDER = "/.tmp/";

	public static final  Map<String, List<String>> DEFAULT_EXTS = new HashMap<>();

	public static final String CONTENT_TYPE_IMG = "img";
	
	public static final String CONTENT_TYPE_TEXT = "text";

	public static final List<String> DEFAULT_EXTS_IMG = Arrays.asList("jpg", "jpeg", "png", "gif");
	
	public static final List<String> DEFAULT_EXTS_TEXT =  Arrays.asList("pdf", "txt", "docx", "doc", "odf");

	
	static {
		DEFAULT_EXTS.put(CONTENT_TYPE_IMG, DEFAULT_EXTS_IMG);
		DEFAULT_EXTS.put(CONTENT_TYPE_TEXT, DEFAULT_EXTS_TEXT);
	}
		
	protected String root = DEFAULT_UPLOAD_FOLDER;
	
	private String tmp = DEFAULT_TMP_FOLDER;
	
	
	protected Map<String, String> locations = new HashMap<>();
	
	protected Map<String, String> folders = new HashMap<>();

	protected Map<String, List<String>> exts =  DEFAULT_EXTS;

	protected Map<String, Boolean> useTmpFor =  new HashMap<>();

	protected boolean useTmp;
	
	public FilesConfiguration() {
	}
	
	public String getRoot() {
		return root;
	}
	
	public void setRoot(String root) {
		this.root = root;
	}
	

	public Map<String, String> getLocations() {
		return locations;
	}

	public void setLocations(Map<String, String> locations) {
		this.locations = locations;
	}

	public Map<String, String> getFolders() {
		return folders;
	}

	public void setFolders(Map<String, String> folders) {
		this.folders = folders;
	}

	public Map<String, List<String>> getExts() {
		return exts;
	}

	public List<String> getExts(String key) {
		if (exts!=null) {
			return exts.get(key);
		}
		return null;
	}

	public void setExts(Map<String, List<String>> exts) {
		this.exts = exts;
	}

	public String getLocation(String key) {
		if(key!=null && locations.containsKey(key)) {
			return root + locations.get(key);
		}
		return null;
	}

	public String getFolder(String key) {
		if(key!=null && folders.containsKey(key)) {
			return root + folders.get(key);
		}
		return null;
	}

	
	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}


	public Map<String, Boolean> getUseTmpFor() {
		return useTmpFor;
	}

	public void setUseTmpFor(Map<String, Boolean> useTmpFor) {
		this.useTmpFor = useTmpFor;
	}

	public boolean isUseTmp() {
		return useTmp;
	}

	public void setUseTmp(boolean useTmp) {
		this.useTmp = useTmp;
	}

	@Override
	public String toString() {
		return "FilesConfiguration [" 
				+ (root != null ? "root=" + root + ", " : "")
				+ (locations != null ? "locations=" + locations + ", " : "")
				+ (exts != null ? "exts=" + exts + ", " : "")
				+ (tmp != null ? "tmp=" + tmp + ", " : "")
				+ ("useTmp=" + useTmp + ", ")
				+ (useTmpFor != null ? "useTmpFor=" + useTmpFor + ", " : "")
				+ "]";
	}

	public boolean useTmpFor(String key) {
		if (useTmpFor!=null && !StringUtils.isEmpty(key) && Boolean.TRUE.equals(useTmpFor.get(key))) {
			return true;
		}
		return useTmp;
	}
	
	public void addExts(String key, List<String> exts) {
		this.exts.put(key, exts);
	}

	public void addLocation(String key, String location) {
		this.locations.put(key, location);
	}

	public void addFolder(String key, String folder) {
		this.folders.put(key, folder);
	}

}
