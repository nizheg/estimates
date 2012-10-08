package org.nzhegalin.estimate.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public abstract class Resource implements Comparable<Resource>, Serializable {

	private static final long serialVersionUID = 1L;

	protected char type;
	private long id;
	private String code;
	private String name;
	private String measureUnit;
	private static final List<Character> types = Arrays.asList(new Character[] {
			'h', 'g', 'm' });

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public char getType() {
		return type;
	}

	@Override
	public int compareTo(Resource anotherResource) {
		int anotherResIndex = types.indexOf(anotherResource.type);
		int thisResIndex = types.indexOf(type);
		int typesDifference = thisResIndex - anotherResIndex;
		return typesDifference == 0 ? (int) (this.code
				.compareTo(anotherResource.code)) : typesDifference;
	}

	@Override
	public String toString() {
		return code + ": " + name;
	}
}
