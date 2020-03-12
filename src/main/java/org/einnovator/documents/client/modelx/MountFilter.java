package org.einnovator.documents.client.modelx;

import org.einnovator.documents.client.model.StoreType;
import org.einnovator.util.model.ToStringCreator;

public class MountFilter extends MountOptions {

	protected String q;
		
	protected StoreType type;
	 
	protected String scope;

	/**
	 * Create instance of {@code MountFilter}.
	 *
	 */
	public MountFilter() {
	}
	
	
	/**
	 * Get the value of property {@code q}.
	 *
	 * @return the q
	 */
	public String getQ() {
		return q;
	}


	/**
	 * Set the value of property {@code q}.
	 *
	 * @param q the value of property q
	 */
	public void setQ(String q) {
		this.q = q;
	}


	/**
	 * Get the value of property {@code type}.
	 *
	 * @return the type
	 */
	public StoreType getType() {
		return type;
	}


	/**
	 * Set the value of property {@code type}.
	 *
	 * @param type the value of property type
	 */
	public void setType(StoreType type) {
		this.type = type;
	}


	/**
	 * Get the value of property {@code scope}.
	 *
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}


	/**
	 * Set the value of property {@code scope}.
	 *
	 * @param scope the value of property scope
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}


	@Override
	public ToStringCreator toString0(ToStringCreator creator) {
		return super.toString0(creator)
				.append("q", q)
				.append("type", type)
				.append("scope", scope)
				;
	}

	
}
