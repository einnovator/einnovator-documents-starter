package org.einnovator.documents.client.model;

import org.einnovator.util.SecurityUtil;
import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Owner extends ObjectBase {

	public static final String OWNER_TYPE_USER = "user";
	public static final String OWNER_TYPE_GROUP = "group";
	public static final String OWNER_TYPE_USER_CONNECTION = "user-connection";
	public static final String OWNER_TYPE_GROUP_CONNECTION = "group-connection";
	
	private String type;

	private String subtype;

	private String id;

	private String name;

	private String xid;

	private String xname;

	public Owner() {
	}

	public Owner(String id) {
		this(OWNER_TYPE_USER, id);
	}

	public Owner(String type, String id) {
		this.type = type;
		this.id = id;
	}

	public Owner(String type, String id, String name) {
		this.type = type;
		this.id = id;
	}

	public Owner(String type, String id, String name, String xid, String xname) {
		this(type, id, name);
		this.xid = id;
		this.xname = xname;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getXname() {
		return xname;
	}

	public void setXname(String xname) {
		this.xname = xname;
	}

	/**
	 * @return
	 */
	public boolean isUser() {
		return OWNER_TYPE_USER.equals(type);
	}

	@Override
	public ToStringCreator toString0(ToStringCreator creator) {
		return super.toString0(creator)
				.append("type", type)
				.append("id", id)
				.append("name", name)
				;
	}

	
	public String serialize() {
		if (id==null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if (type == null || type==OWNER_TYPE_USER) {
			sb.append(id);
		} else if (type==OWNER_TYPE_GROUP) {
			sb.append("@");
			sb.append(id);			
		} else if (type==OWNER_TYPE_USER_CONNECTION) {
			sb.append(id);			
			sb.append("|");
			sb.append(subtype);
		} else if (type==OWNER_TYPE_GROUP_CONNECTION) {
			sb.append("@");
			sb.append(id);			
			sb.append("|");
			sb.append(subtype);
		}
		return sb.toString();
	}


	public static Owner user(String username) {
		return new Owner(OWNER_TYPE_USER, username, null);
	}

	public static Owner principal() {
		return user(SecurityUtil.getPrincipalName());
	}

	public static Owner group(String group) {
		return new Owner(OWNER_TYPE_GROUP, null,  group);
	}

	
}
