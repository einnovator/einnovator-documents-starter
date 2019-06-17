package org.einnovator.documents.client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permission {

	public static final Permission PUBLIC = new Permission(true);

	public static final Permission ROLE = new Permission(PermissionType.ROLE, PermissionType.PERMISSION);

	private Boolean publik;

	private List<Owner> owners;
	
	private List<PermissionType> allow;
	
	private List<PermissionType> deny;

	public Permission() {
	}

	public Permission(PermissionType... allow) {
		this.allow = new ArrayList<>(Arrays.asList(allow));
	}

	public Permission(List<Owner> owners, PermissionType... allow) {
		this.owners = owners;
		this.allow = new ArrayList<>(Arrays.asList(allow));
	}

	public Permission(Owner[] owners, PermissionType... allow) {
		this.owners = new ArrayList<>(Arrays.asList(owners));
		this.allow = new ArrayList<>(Arrays.asList(allow));
	}

	public Permission(Owner owner, PermissionType... allow) {
		this(new Owner[] {owner}, allow);
	}

	public Permission(String username, PermissionType... allow) {
		this(Owner.user(username), allow);
	}

	public Permission(Boolean publik) {
		this.publik = publik;
		if (Boolean.TRUE.equals(publik)) {
			allow = new ArrayList<>();
			allow.add(PermissionType.READ);			
		}
	}
	
	public Boolean getPublic() {
		return publik;
	}

	public void setPublic(boolean publik) {
		this.publik = publik;
	}


	public Boolean getPublik() {
		return publik;
	}

	public void setPublik(Boolean publik) {
		this.publik = publik;
	}

	public List<Owner> getOwners() {
		return owners;
	}

	public void setOwners(List<Owner> owners) {
		this.owners = owners;
	}

	public List<PermissionType> getAllow() {
		return allow;
	}

	public void setAllow(List<PermissionType> allow) {
		this.allow = allow;
	}

	public List<PermissionType> getDeny() {
		return deny;
	}

	public void setDeny(List<PermissionType> deny) {
		this.deny = deny;
	}

	public boolean refers(String username) {
		if (Boolean.TRUE.equals(publik)) {
			return true;
		}
		if (owners!=null) {
			for (Owner owner2: owners) {
				if (owner2!=null && owner2.getId()!=null) {
					if (Owner.OWNER_TYPE_USER.equals(owner2.getType()) && owner2.getId().equals(username)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean has(PermissionType type) {
		boolean found = false;
		if (allow!=null) {
			for (PermissionType type2: allow) {
				if (type2.includes(type)) {
					found = true;
					break;
				}
			}
		}
		if (deny!=null) {
			for (PermissionType type2: allow) {
				if (type2.includes(type)) {
					return false;
				}
			}			
		}
		return found;
	}
		
	public boolean isAllowed(String username, PermissionType type) {
		if (!refers(username)) {
			return false;
		}
		if (Boolean.TRUE.equals(publik)) {
			return true;
		}
		return has(type);
	}


	public void addOwner(Owner owner) {
		if (this.owners==null) {
			this.owners = new ArrayList<>();
		}
		this.owners.add(owner);
	}

	public void addAllow(PermissionType type) {
		if (this.allow==null) {
			this.allow = new ArrayList<>();
		}
		this.allow.add(type);
	}

	public void addDeny(PermissionType type) {
		if (this.deny==null) {
			this.deny = new ArrayList<>();
		}
		this.deny.add(type);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (allow!=null) {
			for (PermissionType type: allow) {
				if (sb.length()>0) {
					sb.append(" ");
				}
				sb.append("+");
				sb.append(type);	
			}			
		}
		if (deny!=null) {
			for (PermissionType type: allow) {
				sb.append("-");
				sb.append(type);	
			}
		}
		if (owners!=null) {
			if (sb.length()>0) {
				sb.append(" ");
			}
			sb.append("#");
			for (Owner owner: owners) {
				sb.append(owner.serialize());
			}
		}
		return sb.toString();
	}

	public boolean valid() {
		return true;
	}
	
	public static List<Permission> filterValid(List<Permission> permissions) {
		if (permissions==null) {
			return null;
		}
		List<Permission> permissions2 = new ArrayList<>();
		for (Permission permission: permissions) {
			if (permission!=null && permission.valid()) {
				permissions2.add(permission);
			}
		}
		return permissions2;
	}


	public static List<Owner> getOwners(List<Permission> permissions) {
		if (permissions==null) {
			return null;
		}
		List<Owner> owners = new ArrayList<>();
		for (Permission permission: permissions) {
			if (permission!=null && permission.getOwners()!=null) {				
				owners.addAll(permission.getOwners());
			}
		}
		return owners;
	}

	public static boolean isPublic(List<Permission> permissions) {
		if (permissions!=null) {
			for (Permission permission : permissions) {
				if (Boolean.TRUE.equals(permission.getPublic())) {
					return true;
				}
			}
		}
		return false;
	}

	public static int count(List<Permission> permissions, PermissionType type) {
		int n = 0;
		if (permissions!=null) {
			for (Permission permission: permissions) {
				if (permission!=null && permission.has(type)) {
					if (permission.owners!=null) {
						n += permission.owners.size();
					}
				}
			}			
		}
		return n;
	}

	/**
	 * Make {@code Permission} list for users with specified {@code PermissionType}
	 * 
	 * @param type the {@code PermissionType}
	 * @param usernames a variadic array of users
	 * @return the {@code Permission} list 
	 */
	public static List<Permission> make(PermissionType type, String... usernames) {
		List<Permission> permissions = new ArrayList<>();
		for (String username: usernames) {
			permissions.add(new Permission(Owner.user(username), type));
		}
		return permissions;
	}

}
