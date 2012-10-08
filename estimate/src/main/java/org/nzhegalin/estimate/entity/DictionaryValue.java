package org.nzhegalin.estimate.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class DictionaryValue implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private Dictionary dictionary;
	private String code;
	private String name;
	private String measureUnit;
	private Set<DictionaryValueResource> resources;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Set<DictionaryValueResource> getResources() {
		if (this.resources == null) {
			this.resources = new TreeSet<DictionaryValueResource>();
		}
		return this.resources;
	}

	public void setResources(Set<DictionaryValueResource> resources) {
		this.resources = resources;
	}

	@Override
	public String toString() {
		return getCode() + ": " + getName();
	}

}
