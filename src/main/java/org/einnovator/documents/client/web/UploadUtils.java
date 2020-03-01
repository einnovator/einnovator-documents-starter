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

	public static String getTmpResourcePath(String key, String name, boolean appendUuid, FilesConfiguration config) {
		if (config != null) {
			String location = config.getLocation(key);
			if (location != null) {
				return location;
			}
			String folder = null;
			if (config != null) {
				folder = config.getTmp();
			}
			if (folder != null) {
				return folder + getResourceName(key, name, appendUuid, config);
			}
			return config.getRoot() + getResourceName(key, name, appendUuid, config);
		}
		return null;
	}

	public static String getResourcePath(String key, String folder, String name, boolean appendUuid, FilesConfiguration config) {
		if (config != null) {
			String location = config.getLocation(key);
			if (location != null) {
				return location;
			}
			if (StringUtils.hasText(folder)) {
				return PathUtil.concat(config.getRoot(),
						PathUtil.concat(folder, getResourceName(key, name, appendUuid, config)));
			} else if (StringUtils.hasText(key)) {
				key = key.trim();
				folder = config.getFolder(key);
				if (folder != null) {
					return PathUtil.concat(folder, getResourceName(key, name, appendUuid, config));
				}
			}
			return PathUtil.concat(config.getRoot(), getResourceName(key, name, appendUuid, config));
		}
		return null;
	}

	public static String getResourceName(String key, String name, boolean appendUuid, FilesConfiguration config) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(key) && appendUuid) {
			sb.append(key.trim());
		}
		String ext = null;
		int i0 = name.lastIndexOf("/");
		if (i0!=-1 && i0<name.length()-1) {
			name = name.substring(i0+1);
		}
		if (StringUtils.hasText(name)) {
			int i= name.indexOf(".");
			if (i>0 && i<name.length()-1) {
				ext = name.substring(i+1);
				name = name.substring(0, i);
			}
			if (sb.length() > 0) {
				sb.append("-");
			}
			sb.append(name.trim());
		}
		if (appendUuid) {
			String uuid = generateUUID(key, name, config);
			if (StringUtils.hasText(uuid) && appendUuid) {
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
