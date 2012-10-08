package org.nzhegalin.estimate.entity;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReportRow {

	private final long id;
	private String name;
	private String measureUnit;
	private String code;
	private Double actualMeasure;
	private final Map<Resource, Double> resources = new TreeMap<Resource, Double>();

	public ReportRow(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getActualMeasure() {
		return actualMeasure;
	}

	public void setActualMeasure(Double actualMeasure) {
		this.actualMeasure = actualMeasure;
	}

	public void setMeasure(Resource res, Double measure) {
		resources.put(res, measure);
	}

	public Set<Resource> getResources() {
		return Collections.unmodifiableSet(resources.keySet());
	}

	public Double getResourceMeasure(Resource resource) {
		return resources.get(resource);
	}

	public Double getReourceActualMeasure(Resource resource) {
		return resources.get(resource) == null ? null : resources.get(resource)
				* actualMeasure;
	}

}
