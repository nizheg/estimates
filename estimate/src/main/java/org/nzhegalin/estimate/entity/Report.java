package org.nzhegalin.estimate.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import java.io.Serializable;

import org.nzhegalin.estimate.entity.Estimates.EstimatesItem;

public class Report implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<Resource> resources = new LinkedList<Resource>();;
	private final List<ReportRow> rows = new LinkedList<ReportRow>();

	public Report(Estimates estimates) {
		Set<Resource> resourceSet = new TreeSet<Resource>();
		for (EstimatesItem item : estimates.getValues()) {
			DictionaryValue dictionaryValue = item.getValue();
			for (DictionaryValueResource resource : dictionaryValue
					.getResources()) {
				if (resource.getResource() instanceof MaterialResource) {
					resourceSet.add(resource.getResource());
				}
			}
		}
		resources.addAll(resourceSet);
		for (EstimatesItem estimatesItem : estimates.getValues()) {
			DictionaryValue dictionaryValue = estimatesItem.getValue();

			ReportRow row = new ReportRow(estimatesItem.getId());
			row.setName(dictionaryValue.getName());
			row.setMeasureUnit(dictionaryValue.getMeasureUnit());
			row.setCode(dictionaryValue.getDictionary().getCode()
					+ dictionaryValue.getCode());
			row.setActualMeasure(estimatesItem.getMeasure());
			for (DictionaryValueResource resource : dictionaryValue
					.getResources()) {
				if (resource.getResource() instanceof MaterialResource) {
					row.setMeasure(resource.getResource(),
							resource.getMeasure());
				}
			}
			rows.add(row);
		}
	}

	public List<Resource> getResources() {
		return resources;
	}

	public List<ReportRow> getRows() {
		return rows;
	}

}
