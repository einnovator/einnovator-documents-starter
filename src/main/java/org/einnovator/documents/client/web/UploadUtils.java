/**
 * 
 */
package org.einnovator.documents.client.web;

import org.einnovator.documents.client.config.FilesConfiguration;
import org.einnovator.util.PathUtil;
import org.springframework.util.StringUtils;

/**
 * @author support@einnovator.org
 *
 */
public class UploadUtils {

	public static String getTmpResourcePath(String key, String name, String originalName, boolean unique, FilesConfiguration config) {
		if (config != null) {
			String folder = null;
			if (config != null) {
				folder = config.getTmp();
			}
			if (folder != null) {
				return PathUtil.concat(folder, getResourceName(key, name, originalName, unique, config));
			}
			return PathUtil.concat(config.getRoot(), getResourceName(key, name, originalName, unique, config));
		}
		return null;
	}

	public static String getResourcePath(String key, String folder, String name, String originalName, boolean unique, FilesConfiguration config) {
		if (config != null) {
			if (StringUtils.hasText(folder)) {
				return PathUtil.concat(config.getRoot(),
						PathUtil.concat(folder, getResourceName(key, name, originalName, unique, config)));
			}
			return PathUtil.concat(config.getRoot(), getResourceName(key, name, originalName, unique, config));
		}
		return null;
	}

	public static String getResourceName(String key, String name, String originalName, boolean unique, FilesConfiguration config) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(key) && !StringUtils.hasText(name) && unique) {
			sb.append(key.trim());
		}
		String ext = null;

		if (originalName==null || originalName.isEmpty()) {
			originalName = name;
		}
		if (name==null || name.isEmpty()) {
			name = originalName;
		}
		if (name==null || name.isEmpty()) {
			name = key;
		}
		if (name!=null) {
			int i0 = name.lastIndexOf("/");
			if (i0!=-1 && i0<name.length()-1) {
				name = name.substring(i0+1);
			}			
		}
		if (StringUtils.hasText(originalName)) {
			int i= originalName.indexOf(".");
			if (i>0 && i<originalName.length()-1) {
				ext = originalName.substring(i+1);
			}
		}
		if (StringUtils.hasText(name)) {
			int i= name.indexOf(".");
			if (i>0 && i<name.length()-1) {
				name = name.substring(0, i);
			}
			if (sb.length() > 0) {
				sb.append("-");
			}
			sb.append(name.trim());
		}
		if (unique) {
			String uuid = generateUUID(key, name, config);
			if (StringUtils.hasText(uuid) && unique) {
				if (sb.length() > 0) {
					sb.append("-");
				}
				sb.append(uuid);
			}			
		}
		if (StringUtils.hasText(ext)) {
			sb.append(".");
			sb.append(ext);
		}
		return sb.toString();
	}
	
	public static String generateUUID(String key, String name, FilesConfiguration config) {
		return Long.toString(System.currentTimeMillis()/1000);
	}
}
