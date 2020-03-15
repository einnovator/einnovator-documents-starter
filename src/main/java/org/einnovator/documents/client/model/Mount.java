package org.einnovator.documents.client.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.einnovator.util.model.ToStringCreator;
import org.springframework.core.convert.ConversionService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A {@code Mount}.
 *
 * @author support@einnovator.org
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Mount extends OwnedEntity {
	
	private StoreType type;

	private String path;
	
	private String root;

	private String name;

	private ScopeType scope;

	private String profiles;
	
	private String description;

	private String icon;

	private String img;

	private Boolean enabled;

	private Boolean readonly;

	private Boolean versioned;

	private Boolean autocreate;
	
	private int order;
	
	private Map<String, Object> properties;

	/**
	 * Create instance of {@code Mount}.
	 *
	 */
	public Mount() {	
	}
	
	/**
	 * Create instance of {@code Mount}.
	 *
	 * @param obj a prototype
	 */
	public Mount(Object obj) {
		super(obj);
	}

	
	//
	// Getters/Setters
	//

	/**
	 * Get the value of property {@code id}.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the value of property {@code id}.
	 *
	 * @param id the value of property id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * Get the value of property {@code path}.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the value of property {@code path}.
	 *
	 * @param path the value of property path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get the value of property {@code name}.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the value of property {@code name}.
	 *
	 * @param name the value of property name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the value of property {@code scope}.
	 *
	 * @return the scope
	 */
	public ScopeType getScope() {
		return scope;
	}

	/**
	 * Set the value of property {@code scope}.
	 *
	 * @param scope the value of property scope
	 */
	public void setScope(ScopeType scope) {
		this.scope = scope;
	}

	/**
	 * Get the value of property {@code profiles}.
	 *
	 * @return the profiles
	 */
	public String getProfiles() {
		return profiles;
	}

	/**
	 * Set the value of property {@code profiles}.
	 *
	 * @param profiles the value of property profiles
	 */
	public void setProfiles(String profiles) {
		this.profiles = profiles;
	}

	/**
	 * Get the value of property {@code properties}.
	 *
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Set the value of property {@code properties}.
	 *
	 * @param properties the value of property properties
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * Get the value of property {@code description}.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the value of property {@code description}.
	 *
	 * @param description the value of property description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the value of property {@code icon}.
	 *
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Set the value of property {@code icon}.
	 *
	 * @param icon the value of property icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Get the value of property {@code img}.
	 *
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Set the value of property {@code img}.
	 *
	 * @param img the value of property img
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * Get the value of property {@code enabled}.
	 *
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * Set the value of property {@code enabled}.
	 *
	 * @param enabled the value of property enabled
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Get the value of property {@code readonly}.
	 *
	 * @return the readonly
	 */
	public Boolean getReadonly() {
		return readonly;
	}

	/**
	 * Set the value of property {@code readonly}.
	 *
	 * @param readonly the value of property readonly
	 */
	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	/**
	 * Get the value of property {@code versioned}.
	 *
	 * @return the versioned
	 */
	public Boolean getVersioned() {
		return versioned;
	}

	/**
	 * Set the value of property {@code versioned}.
	 *
	 * @param versioned the value of property versioned
	 */
	public void setVersioned(Boolean versioned) {
		this.versioned = versioned;
	}

	/**
	 * Get the value of property {@code autocreate}.
	 *
	 * @return the autocreate
	 */
	public Boolean getAutocreate() {
		return autocreate;
	}

	/**
	 * Set the value of property {@code autocreate}.
	 *
	 * @param autocreate the value of property autocreate
	 */
	public void setAutocreate(Boolean autocreate) {
		this.autocreate = autocreate;
	}

	
	/**
	 * Get the value of property {@code order}.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Set the value of property {@code order}.
	 *
	 * @param order the value of property order
	 */
	public void setOrder(int order) {
		this.order = order;
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
	 * @param owner the value of property owner
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	
	//
	// With
	//


	/**
	 * Set the value of property {@code id}.
	 *
	 * @param id the value of property id
	 * @return this {@code Mount}
	 */
	public Mount withId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Set the value of property {@code type}.
	 *
	 * @param type the value of property type
	 * @return this {@code Mount}
	 */
	public Mount withType(StoreType type) {
		this.type = type;
		return this;
	}

	/**
	 * Set the value of property {@code path}.
	 *
	 * @param path the value of property path
	 * @return this {@code Mount}
	 */
	public Mount withPath(String path) {
		this.path = path;
		return this;
	}

	/**
	 * Set the value of property {@code name}.
	 *
	 * @param name the value of property name
	 * @return this {@code Mount}
	 */
	public Mount withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Set the value of property {@code scope}.
	 *
	 * @param scope the value of property scope
	 * @return this {@code Mount}
	 */
	public Mount withScope(ScopeType scope) {
		this.scope = scope;
		return this;
	}

	/**
	 * Set the value of property {@code profiles}.
	 *
	 * @param profiles the value of property profiles
	 * @return this {@code Mount}
	 */
	public Mount withProfiles(String profiles) {
		this.profiles = profiles;
		return this;
	}


	/**
	 * Set the value of property {@code root}.
	 *
	 * @param root the value of property root
	 * @return this {@code Mount}
	 */
	public Mount withRoot(String root) {
		this.root = root;
		return this;
	}

	
	/**
	 * Set the value of property {@code properties}.
	 *
	 * @param properties the value of property properties
	 * @return this {@code Mount}
	 */
	public Mount withProperties(Map<String, Object> properties) {
		this.properties = properties;
		return this;
	}

	/**
	 * Set the value of property {@code description}.
	 *
	 * @param description the value of property description
	 * @return this {@code Mount}
	 */
	public Mount withDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Set the value of property {@code icon}.
	 *
	 * @param icon the value of property icon
	 * @return this {@code Mount}
	 */
	public Mount withIcon(String icon) {
		this.icon = icon;
		return this;
	}

	/**
	 * Set the value of property {@code img}.
	 *
	 * @param img the value of property img
	 * @return this {@code Mount}
	 */
	public Mount withImg(String img) {
		this.img = img;
		return this;
	}

	/**
	 * Set the value of property {@code enabled}.
	 *
	 * @param enabled the value of property enabled
	 * @return this {@code Mount}
	 */
	public Mount withEnabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	/**
	 * Set the value of property {@code readonly}.
	 *
	 * @param readonly the value of property readonly
	 * @return this {@code Mount}
	 */
	public Mount withReadonly(Boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	/**
	 * Set the value of property {@code versioned}.
	 *
	 * @param versioned the value of property versioned
	 * @return this {@code Mount}
	 */
	public Mount withVersioned(Boolean versioned) {
		this.versioned = versioned;
		return this;
	}

	/**
	 * Set the value of property {@code autocreate}.
	 *
	 * @param autocreate the value of property autocreate
	 * @return this {@code Mount}
	 */
	public Mount withAutocreate(Boolean autocreate) {
		this.autocreate = autocreate;
		return this;
	}

	
	/**
	 * Set the value of property {@code order}.
	 *
	 * @param order the value of property order
	 * @return this {@code Mount}
	 */
	public Mount withOrder(int order) {
		this.order = order;
		return this;
	}

	
	//
	// Property utils
	//
	

	public Object getProperty(String name) {
		if (properties==null) {
			return null;
		}
		return properties.get(name);
	}


	@SuppressWarnings("unchecked")
	public <T> T getProperty(String name, Class<T> type, ConversionService conversionService) {
		if (properties==null) {
			return null;
		}
		Object value = properties.get(name);
		if (value!=null && conversionService!=null) {
			if (conversionService.canConvert(value.getClass(), type)) {
				return conversionService.convert(value, type);
			}
		}
		return (T)value;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String name, T defaultValue, ConversionService conversionService) {
		if (properties==null) {
			return null;
		}
		Object value = properties.get(name);
		if (value==null) {
			return defaultValue;
		}
		if (value!=null && conversionService!=null && defaultValue!=null) {
			if (conversionService.canConvert(value.getClass(), defaultValue.getClass())) {
				return (T) conversionService.convert(value, defaultValue.getClass());
			}
		}
		return (T)value;
	}
	
	public void setProperty(String name, Object value) {
		if (properties==null) {
			properties = new LinkedHashMap<String, Object>();
		}
		properties.put(name, value);
	}


	@Override
	public ToStringCreator toString0(ToStringCreator creator) {
		return super.toString0(creator)
				.append("type", type)
				.append("scope", scope)
				.append("path", path)
				.append("name", name)
				.append("root", root)
				.append("readonly", readonly)
				.append("versioned", versioned)
				.append("groupId", groupId)
				.append("properties", properties)
				.append("icon", icon)
				.append("img", img)
				.append("description", description)
				;
	}


	public static Mount findByName(String name, List<Mount> mounts) {
		if (name==null || name.isEmpty() || mounts==null) {
			return null;
		}
		for (Mount mount: mounts) {
			if (mount.getName()!=null && name.equals(mount.getName())) {
				return mount;
			}
		}
		return null;
	}

}
