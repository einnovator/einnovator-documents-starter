package org.einnovator.documents.client.model;

import java.util.ArrayList;
import java.util.List;

public class AttributeTemplate {
	
	public static enum AttributeType {
		CHECKBOX("Checkbox"),
		DATE("Date"),
		FILE("File"),
		NUMBER("Number"),
		RADIO_BUTTON("Radio Button"),
		SELECT_LIST("Select List"), 
		NAMED_LIST("Named List"),
		TEXT("TextBox"),
		USER("User"),
		SWITCH("Switch");

		private String displayName;

		private AttributeType(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	public static enum AttributeConstraint {
		NOT_NULL("Attribute cannot be null"),
		REQUIRED_IF_PARENT_NOT_NULL ("Attribute cannot be null"), 
		REQUIRED_IF_PARENT_TRUE ("Attribute cannot be null"), 
		SWITCH_ON("Attribute cannot be null");

		private String displayName;

		private AttributeConstraint(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}
	
	private String parentName;

	private AttributeConstraint parentConstraint;

	private AttributeConstraint constraint;

	private String name;

	private String description;

	private AttributeType type;
	
	private String list;

	private boolean required;
	
	private boolean searchable;

	private int maxLength;

	private int minLength;
	
	private int order;
	
	private List<String> optionsNames = new ArrayList<String>();
	
	private List<String> optionsValues = new ArrayList<String>();

	public AttributeTemplate() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	
	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public int getMinLength() {
		return minLength;
	}

	public String getParentName() {
		return this.parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public AttributeConstraint getParentConstraint() {
		return parentConstraint;
	}

	public void setParentConstraint(AttributeConstraint parentConstraint) {
		this.parentConstraint = parentConstraint;
	}

	public AttributeConstraint getConstraint() {
		return constraint;
	}

	public void setConstraint(AttributeConstraint constraint) {
		this.constraint = constraint;
	}
	
	public List<String> getOptionNames() {
		return optionsNames;
	}

	public void setOptionNames(List<String> options) {
		this.optionsNames = options;
	}
	
	public List<String> getOptionValues() {
		return optionsValues;
	}

	public void setOptionValues(List<String> options) {
		this.optionsValues = options;
	}


	public void addOptionName(String name) {
		optionsNames.add(name);
	}
	
	public void addOptionValue(String value) {
		optionsValues.add(value);
	}	

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public List<String> getOptionsNames() {
		return optionsNames;
	}

	public void setOptionsNames(List<String> optionsNames) {
		this.optionsNames = optionsNames;
	}

	public List<String> getOptionsValues() {
		return optionsValues;
	}

	public void setOptionsValues(List<String> optionsValues) {
		this.optionsValues = optionsValues;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	
	@Override
	public String toString() {
		return "AttributeTemplate [parentName=" + parentName + ", parentConstraint=" + parentConstraint
				+ ", constraint=" + constraint + ", name=" + name + ", description=" + description + ", type=" + type
				+ ", required=" + required + ", maxLength=" + maxLength + ", minLength=" + minLength + ", order="
				+ order + ", optionsNames=" + optionsNames + ", optionsValues=" + optionsValues + "]";
	}


}
