package org.einnovator.documents.client.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Permission {

	private String group;
	private String user;
	private boolean allPerm;

	private List<PermissionType> permTypes = new ArrayList<>();


	public List<PermissionType> getPermTypes() {
		return permTypes;
	}

	public void setPermTypes(List<PermissionType> permTypes) {
		this.permTypes = permTypes;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

//	public List<PermissionType> getPermissions() {
//		return permTypes;
//	}
//
//	public void setPermissions(Set<PermissionType> permissions) {
//		this.permTypes = permissions;
//	}

//	public String getAllowedUser() {
//		if (allPerm) {
//			return "@";
//		}
//		if (StringUtils.hasText(group)) {
//			return group;
//		}
//		if (StringUtils.hasText(user)) {
//			return user;
//		}
//		return ".";
//	}

	public void setAllPerm() {
		this.allPerm = true;
	}
	
	public boolean getAllPerm() {
		return this.allPerm;
	}
//
//	public String toString() {
//		StringBuilder builder = new StringBuilder();
//		builder.append(getAllowedUser());
//		builder.append('#');
//		for (PermissionType p : permTypes) {
//			builder.append(p);
//		}
//
//		return builder.toString();
//	}

//	public static List<Permission> permissionFromString(String permissionsText) {
//		if (!StringUtils.hasText(permissionsText)) {
//			return new ArrayList<>();
//		}
//
//		String[] permissionTokens = permissionsText.split(",");
//		// Pattern pattern = Pattern.compile("(@\\w*|\\\\.|\\w+)\\\\.(\\w*)");
//		List<Permission> permissionsList = new ArrayList<>();
//
//		for (String p : permissionTokens) {
//			Permission userPermission = new Permission();
//			// Set<PermissionType> permTypes = new HashSet<>();
//			// Matcher matcher = pattern.matcher(p);
//			// String user = matcher.group(0);
//			// String permissionString = matcher.group(1);
//			String[] pTokens = p.split("#");
//			String user = pTokens[0];
//			String permissionString = pTokens[1];
//
//			if (user.equals("@")) {
//				userPermission.setAllPerm();
//			} else if (user.startsWith("@")) {
//				userPermission.setGroup(user);
//			} else if (!user.equals(".")) {
//				userPermission.setUser(user);
//			}
//
//			if (permissionString.contains("READ")) {
//				userPermission.addPermission(PermissionType.READ);
//			}
//			if (permissionString.contains("WRITE")) {
//				userPermission.addPermission(PermissionType.WRITE);
//			}
//			if (permissionString.contains("DOWNLOAD")) {
//				userPermission.addPermission(PermissionType.DOWNLOAD);
//			}
//			if (permissionString.contains("SHARE")) {
//				userPermission.addPermission(PermissionType.SHARE);
//			}
//			permissionsList.add(userPermission);
//		}
//		return permissionsList;
//	}

	public void addPermission(PermissionType permTypes) {
		this.permTypes.add(permTypes);
	}

	@Override
	public int hashCode() {
		StringBuilder builder = new StringBuilder();
		if (user != null) {
			builder.append(user.hashCode());
		}
		if (group != null) {
			builder.append(group.hashCode());
		}
		builder.append(Boolean.hashCode(allPerm));
		builder.append(permTypes.toString().hashCode());
		return builder.toString().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Permission) {
			Permission op = (Permission) other;
			if (this.allPerm == op.allPerm)
				if (StringUtils.hasText(user) && this.user.equals(op.user) || user == null && op.user == null)
					if (StringUtils.hasText(group) && this.group.equals(op.group)
							|| group == null && op.group == null) {
						for (PermissionType p : this.permTypes) {
							if (!op.permTypes.contains(p)) {
								return false;
							}
						}
						return true;
					}
		}
		return false;
	}

//	public boolean allowsAccess(Owner owner, PermissionType op) {
//		String allowedUser = getAllowedUser();
//		if (allowedUser.equals("@")) {
//			return (op.equals(PermissionType.ANY)) ? true : permTypes.contains(op);
//		}
//		if (allowedUser.charAt(0) == '@') {
//			if (ownerBelongsToGroup(owner, allowedUser)) {
//				return (op.equals(PermissionType.ANY)) ? true : permTypes.contains(op);
//			}
//		} else if (allowedUser.equals(owner.getUsername())) {
//			return (op.equals(PermissionType.ANY)) ? true : permTypes.contains(op);
//
//		}
//		return false;
//	}
//
//	private boolean ownerBelongsToGroup(Owner owner, String groupName) {
//		return false;
//	}

}
