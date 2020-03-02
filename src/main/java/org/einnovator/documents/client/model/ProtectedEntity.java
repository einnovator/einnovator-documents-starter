package org.einnovator.documents.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.einnovator.util.CollectionUtil;
import org.einnovator.util.model.ToStringCreator;
import org.einnovator.util.security.Authority;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract public class ProtectedEntity extends OwnedEntity {

	protected ShareType sharing;
	
	protected List<Authority> authorities;

	//
	// Constructors
	//
	
	/**
	 * Create instance of {@code ProtectedEntity}.
	 *
	 */
	public ProtectedEntity() {
	}


	/**
	 * Create instance of {@code ProtectedEntity}.
	 *
	 * @param obj a prototype
	 */
	public ProtectedEntity(Object obj) {
		super(obj);
	}

	//
	// Getters/Setters
	//
	
	/**
	 * Get the value of property {@code sharing}.
	 *
	 * @return the sharing
	 */
	public ShareType getSharing() {
		return sharing;
	}

	/**
	 * Set the value of property {@code sharing}.
	 *
	 * @param sharing the value of property sharing
	 */
	public void setSharing(ShareType sharing) {
		this.sharing = sharing;
	}

	/**
	 * Get the value of property {@code authorities}.
	 *
	 * @return the authorities
	 */
	public List<Authority> getAuthorities() {
		return authorities;
	}


	/**
	 * Set the value of property {@code authorities}.
	 *
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	//With
	
	/**
	 * Set the value of property {@code sharing}.
	 *
	 * @param sharing the value of property sharing
	 * @return this {@code ProtectedEntity}
	 */
	public ProtectedEntity withSharing(ShareType sharing) {
		this.sharing = sharing;
		return this;
	}

	/**
	 * Set the value of property {@code authorities}.
	 *
	 * @param authorities the authorities to with
	 * @return this {@code ProtectedEntity}
	 */
	public ProtectedEntity withAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
		return this;
	}
	
	//
	// Authorities
	//
	
	/**
	 * Add {@code Authority} to this {@code ProtectedEntity}.
	 * 
	 * @param authority the {@code Authority}
	 */
	public void addAuthority(Authority authority) {
		if (authorities==null) {
			authorities = new ArrayList<Authority>();
		}
		authorities.add(authority);
	}

	/**
	 * Remove {@code Authority} with specified index.
	 * 
	 * @param index the index of the  {@code Authority}
	 * @return the  {@code Authority} if found, null otherwise
	 */
	public Authority removeAuthority(int index) {
		List<Authority> authorities = getAuthorities();
		if (authorities==null || index <0 || index>= authorities.size()) {
			return null;
		}
		return authorities.remove(index);
	}
	
	/**
	 * Remove {@code Authority} with specified identifier.
	 * 
	 * @param id the identifier (UUID or ID)
	 * @return the  {@code Authority} if found, null otherwise
	 */
	public Authority removeAuthority(String id) {
		if (authorities!=null && id!=null) {
			for (int i=0; i<authorities.size(); i++) {
				Authority authority = authorities.get(i);
				if (id.equals(authority.getId()) || id.equals(authority.getUuid()) || id.equals(authority.getUsername()))   {
					authorities.remove(i);
					return authority;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get {@code Authority} with specified index.
	 * 
	 * @param index the index of the  {@code Authority}
	 * @return the  {@code Authority} if found, null otherwise
	 */
	public Authority getAuthority(int index) {
		List<Authority> authorities = getAuthorities();
		if (authorities==null || index <0 || index>= authorities.size()) {
			return null;
		}
		return authorities.get(index);
	}

	/**
	 * Find the specified {@code Authority} by matching equal identifier.
	 * 
	 * @param authority the  {@code Authority} to find
	 * @return the  {@code Authority} if found, null otherwise
	 */
	public Authority findAuthority(Authority authority) {
		List<Authority> authorities = getAuthorities();
		if (authority!=null && authorities!=null) {
			for (Authority authority2: authorities) {
				if ((authority2.getId()!=null && authority2.getId().equals(authority.getId())) || (authority2.getUuid()!=null && authority2.getUuid().equals(authority.getUuid()))) {
					return authority2;
				}
			}
		}
		return null;
	}

	/**
	 * Find the {@code Authority} with specified identifier.
	 * 
	 * @param id the identifier (UUID or ID)
	 * @return the  {@code Authority} if found, null otherwise
	 */
	public Authority findAuthority(String id) {
		List<Authority> authorities = getAuthorities();
		if (id!=null && authorities!=null) {
			for (Authority authority: authorities) {
				if (id.equals(authority.getId()) || id.equals(authority.getUuid()) || id.equals(authority.getUsername()))   {
					return authority;
				}
			}
		}
		return null;
	}

	public Authority removeAuthority(Authority authority) {
		List<Authority> authorities = getAuthorities();
		if (authority!=null && authorities!=null) {
			for (int i=0; i<authorities.size(); i++) {
				Authority authority2 = authorities.get(i);
				if ((authority2.getId()!=null && authority2.getId().equals(authority.getId())) 
						|| (authority2.getUuid()!=null && authority2.getUuid().equals(authority.getUuid()))) {
					return authorities.remove(i);
				}
			}
		}
		return null;
	}

	public boolean hasAuthorities() {
		return !CollectionUtil.isEmpty(authorities);
	}

	/**
	 * Check if {@code ProtectedEntity} is {@code PUBLIC} based on the value of property {@link #sharing}.
	 * 
	 * @return true if {@code PUBLIC}, false otherwise
	 */
	public boolean isPublic() {
		return sharing==ShareType.PUBLIC;
	}
	
	/**
	 * Check if {@code ProtectedEntity} is {@code PRIVATE} based on the value of property {@link #sharing}.
	 * 
	 * @return true if {@code PRIVATE}, false otherwise
	 */
	public boolean isPrivate() {
		return sharing==ShareType.PRIVATE;
	}


	public boolean canView(String username, List<String> groups, Collection<? extends GrantedAuthority> authorities) {
		return canView(username, groups, authorities);
	}

	public boolean canRead(String username, List<String> groups, Collection<? extends GrantedAuthority> authorities) {
		if (can(username)) {
			return true;
		}
		if (Authority.canRead(this.getAuthorities(), username, groups)) {
			return true;
		}
		return false;
	}

	public boolean canCreate(String username, List<String> groups, Collection<? extends GrantedAuthority> authorities) {
		return username!=null;
	}

	public boolean canEdit(String username, List<String> groups, Collection<? extends GrantedAuthority> authorities) {
		return canEdit(username, groups, authorities);
	}

	public boolean canDelete(String username, List<String> groups, Collection<? extends GrantedAuthority> authorities) {
		return canEdit(username, groups, authorities);
	}

	public boolean canPost(String username, List<String> groups, Collection<? extends GrantedAuthority> authorities) {
		return canWrite(username, groups, authorities);
	}

	public boolean canWrite(String username, List<String> groups, Collection<? extends GrantedAuthority> authorities) {
		if (can(username)) {
			return true;
		}
		if (Authority.canWrite(this.getAuthorities(), username, groups)) {
			return true;
		}
		return false;
	}
	
	public int canReadCount() {
		if (authorities==null) {
			return 0;
		}
		int n = 0;
		for (Authority authority: authorities) {
			if (Boolean.TRUE.equals(authority.getRead())) {
				n++;
			}
		}
		return n;
	}

	public int canWriteCount() {
		if (authorities==null) {
			return 0;
		}
		int n = 0;
		for (Authority authority: authorities) {
			if (Boolean.TRUE.equals(authority.getWrite())) {
				n++;
			}
		}
		return n;
	}

	public int canManageCount() {
		if (authorities==null) {
			return 0;
		}
		int n = 0;
		for (Authority authority: authorities) {
			if (Boolean.TRUE.equals(authority.getManage())) {
				n++;
			}
		}
		return n;
	}

	public boolean canManage(String username, List<String> groups, Collection<? extends GrantedAuthority> authorities) {
		if (can(username)) {
			return true;
		}
		if (Authority.canManage(this.getAuthorities(), username, groups)) {
			return true;
		}
		return false;
	}


	public boolean can(String username) {
		if (username!=null && username.equals(owner)) {
			return true;
		}
		return false;
	}
	

	@Override
	public ToStringCreator toString1(ToStringCreator creator) {
		return super.toString1(creator
				.append("sharing", sharing)
				.append("authorities", getAuthorities())
				);
	}
	
}
