package org.nzhegalin.estimate.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Estimates implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;

	private final Map<Long, EstimatesItem> values = new TreeMap<Long, EstimatesItem>();

	public void addItem(EstimatesItem item) {
		values.put(item.getId(), item);
	}

	public void deleteItem(EstimatesItem item) {
		values.remove(item.getId());
	}

	public void addItems(Collection<? extends EstimatesItem> items) {
		for (EstimatesItem item : items) {
			values.put(item.getId(), item);
		}
	}

	public Collection<EstimatesItem> getValues() {
		return Collections.unmodifiableCollection(values.values());
	}

	public EstimatesItem getItemById(Long id) {
		return values.get(id);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static class EstimatesItem implements Comparable<EstimatesItem>,
			Serializable {

		private static final long serialVersionUID = 1L;
		private long id;
		private int number;
		private DictionaryValue value;
		private Double measure;

		public EstimatesItem() {
		}

		public DictionaryValue getValue() {
			return value;
		}

		public void setValue(DictionaryValue value) {
			this.value = value;
		}

		public Double getMeasure() {
			return measure;
		}

		public void setMeasure(Double measure) {
			this.measure = measure;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public int getNumber() {
			return number;
		}

		@Override
		public int compareTo(EstimatesItem another) {
			return this.number - another.number;
		}
	}

}
