package org.einnovator.documents.client.model;

import org.einnovator.util.model.EntityBase;
import org.einnovator.util.model.OwnerType;
import org.einnovator.util.model.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OwnedEntity extends EntityBase {

	protected OwnerType ownerType;
	
	protected String owner;

	protected Object user;

	protected Object group;


	
	/**
	 * Create instance of {@code OwnedEntity}.
	 *
	 */
	public OwnedEntity() {
	}

	
	public OwnedEntity(String name) {
		super(name);
	}

	public OwnedEntity(Object prototype) {
		super(prototype);
	}

	/**
	 * Get the value of property {@code owner}.
	 *
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Set the value of property {@code owner}.
	 *
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	

	/**
	 * Get the value of property {@code ownerType}.
	 *
	 * @return the ownerType
	 */
	public OwnerType getOwnerType() {
		return ownerType;
	}

	/**
	 * Set the value of property {@code ownerType}.
	 *
	 * @param ownerType the ownerType to set
	 */
	public void setOwnerType(OwnerType ownerType) {
		this.ownerType = ownerType;
	}

	/**
	 * Get the value of property {@code user}.
	 *
	 * @return the user
	 */
	public Object getUser() {
		return user;
	}

	/**
	 * Set the value of property {@code user}.
	 *
	 * @param user the user to set
	 */
	public void setUser(Object user) {
		this.user = user;
	}

	/**
	 * Get the value of property {@code group}.
	 *
	 * @return the group
	 */
	public Object getGroup() {
		return group;
	}

	/**
	 * Set the value of property {@code group}.
	 *
	 * @param group the group to set
	 */
	public void setGroup(Object group) {
		this.group = group;
	}

	// With
	
	/**
	 * Set the value of property {@code owner}.
	 *
	 * @param owner the owner to with
	 */
	public OwnedEntity withOwner(String owner) {
		this.owner = owner;
		return this;
	}


	/**
	 * Set the value of property {@code ownerType}.
	 *
	 * @param ownerType the ownerType to with
	 */
	public OwnedEntity withOwnerType(OwnerType ownerType) {
		this.ownerType = ownerType;
		return this;
	}


	/**
	 * Set the value of property {@code user}.
	 *
	 * @param user the user to with
	 */
	public OwnedEntity withUser(Object user) {
		this.user = user;
		return this;
	}


	/**
	 * Set the value of property {@code group}.
	 *
	 * @param group the group to with
	 */
	public OwnedEntity withGroup(Object group) {
		this.group = group;
		return this;
	}


	@Override
	public ToStringCreator toString2(ToStringCreator creator) {
		return super.toString2(creator
				.append("owner", owner)
				.append("ownerType", ownerType)
				);
	}

	public boolean isOwnerUser() {
		return ownerType==null || ownerType==OwnerType.USER;
	}

	public boolean isOwnerGroup() {
		return ownerType==OwnerType.GROUP;
	}

}
