package org.einnovator.documents.client.model;

import java.util.Collection;

public enum PermissionType {
	ALL("ALL"),
	READ("Read"),
	WRITE("Write"),
	DOWNLOAD("Download"),
	SHARE("Share"),
	CREATE("Create"),
	DELETE("Delete"),
	ROLE("Role"),
	PERMISSION("Permission");
	
	private final String displayName;

	PermissionType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public static PermissionType parse(String s) {
		for (PermissionType e: PermissionType.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}

	public static PermissionType parse(String s, PermissionType defaultValue) {
		PermissionType value = parse(s, defaultValue);
		return value!=null ? value: defaultValue;
	}

	public boolean includes(PermissionType type) {
		switch (type) {
			case ALL: return true;
			default: return this==type;
		}
	}
	
	public boolean includesAny(Collection<PermissionType> types) {
		if (types!=null) {
			for (PermissionType type: types) {
				if (includes(type)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if this {@code PermissionType} is present in array
	 * 
	 * @param types the array of {@code PermissionType}
	 * @return true, if present; false, otherwise.
	 */
	public boolean includesAny(PermissionType[] types) {
		if (types!=null) {
			for (PermissionType type: types) {
				if (includes(type)) {
					return true;
				}
			}
		}
		return false;
	}

}
