package org.nzhegalin.estimate.entity;

import java.io.Serializable;

public class DictionaryValueResource implements
		Comparable<DictionaryValueResource>, Serializable {

	private static final long serialVersionUID = 1L;
	private Resource resource;
	private Double measure;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Double getMeasure() {
		return measure;
	}

	public void setMeasure(Double measure) {
		this.measure = measure;
	}

	@Override
	public int compareTo(DictionaryValueResource another) {
		return this.resource.compareTo(another.resource);
	}
}
