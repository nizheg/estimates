package org.nzhegalin.estimate.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.nzhegalin.estimate.entity.DictionaryValue;
import org.nzhegalin.estimate.entity.DictionaryValueResource;
import org.nzhegalin.estimate.entity.builder.DictionaryValueBuilder;
import org.nzhegalin.estimate.entity.builder.DictionaryValueResourceBuilder;
import org.nzhegalin.estimate.entity.builder.EntityBuilder;

public class DictionaryValueProvider {

	private DataProvider dataProvider;

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public DictionaryValue getDictionaryValue(long id) {
		try {

			DictionaryValue dictionaryValue = dataProvider.queryObject("SELECT "
					+ "  dictionary_value.code as dictionary_value_code, " + "  name as dictionary_value_name, "
					+ "  measure_unit as dictionary_value_measure_unit, "
					+ "  dictionary_value.id as dictionary_value_id, " + "  dictionary.id as dictionary_id, "
					+ "  dictionary.code as dictionary_code " + "  FROM dictionary_value "
					+ "  INNER JOIN dictionary ON dictionary.id = dictionary_value.dictionary_id "
					+ "  WHERE dictionary_value.id = " + id, new DictionaryValueBuilder());

			return dictionaryValue;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	public DictionaryValue getFullDictionaryValue(long id) {
		DictionaryValue dictionaryValue = getDictionaryValue(id);
		dictionaryValue.getResources().addAll(getDictionaryValueResources(dictionaryValue));
		return dictionaryValue;

	}

	public Collection<DictionaryValueResource> getDictionaryValueResources(DictionaryValue value) {
		try {
			Collection<DictionaryValueResource> dictionaryValueResources = dataProvider.queryCollection("SELECT "
					+ "  dictionary_value_resource.measure as dictionary_value_resource_measure, "
					+ "  resource.id as resource_id, " + "  resource.code as resource_code, "
					+ "  resource.name as resource_name, " + "  resource.measure_unit as resource_measure_unit, "
					+ "  resource.type as resource_type " + "  FROM dictionary_value_resource "
					+ "  INNER JOIN resource ON resource.id = dictionary_value_resource.resource_id "
					+ "  WHERE dictionary_value_id = " + value.getId(), new DictionaryValueResourceBuilder());
			return dictionaryValueResources;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}

	}

	public Collection<DictionaryValue> getAllDictionaryValues() {
		try {
			return dataProvider.queryCollection("SELECT " + "  dictionary_value.code as dictionary_value_code, "
					+ "  name as dictionary_value_name, " + "  measure_unit as dictionary_value_measure_unit, "
					+ "  dictionary_value.id as dictionary_value_id, " + "  dictionary.id as dictionary_id, "
					+ "  dictionary.code as dictionary_code " + "  FROM dictionary_value "
					+ "  INNER JOIN dictionary ON dictionary.id = dictionary_value.dictionary_id",
					new DictionaryValueBuilder());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	public void createNewDictionaryValue(DictionaryValue dictionaryValue) {
		try {
			dataProvider.beginTransaction();
			long dictionaryId = dictionaryValue.getDictionary().getId();
			String code = createStringParameter(dictionaryValue.getCode());
			String name = createStringParameter(dictionaryValue.getName());
			String measureUnit = createStringParameter(dictionaryValue.getMeasureUnit());
			dataProvider.update("INSERT INTO dictionary_value (dictionary_id, code, name, measure_unit) VALUES ("
					+ dictionaryId + "," + code + "," + name + "," + measureUnit + ")");

			String dictionaryValueIdQuery = String.format(
					"SELECT id  FROM dictionary_value where code=%s and name=%s and measure_unit=%s LIMIT 1", code,
					name, measureUnit);
			for (DictionaryValueResource resource : dictionaryValue.getResources()) {
				dataProvider
						.update("INSERT INTO dictionary_value_resource (dictionary_value_id, resource_id, measure) VALUES ("
								+ "("
								+ dictionaryValueIdQuery
								+ ")"
								+ ","
								+ resource.getResource().getId()
								+ ","
								+ resource.getMeasure() + ")");

			}
			dataProvider.commitTransaction();
		} catch (Exception e) {
			if (dataProvider != null) {
				try {
					dataProvider.rollbackTransaction();
				} catch (SQLException ex) {
					throw new IllegalStateException(ex.getMessage(), e);
				}
			}
			throw new IllegalStateException(e);
		}
	}

	private String createStringParameter(String parameter) {
		return String.format("'%s'", parameter == null ? "" : parameter.replace("'", "''"));
	}

	public void updateDictionaryValue(DictionaryValue dictionaryValue) {
		if (dictionaryValue.getId() == 0) {
			throw new IllegalArgumentException("Only existent dictionary value can be updated");
		}
		try {
			dataProvider.beginTransaction();
			long dictionaryId = dictionaryValue.getDictionary().getId();
			String code = createStringParameter(dictionaryValue.getCode());
			String name = createStringParameter(dictionaryValue.getName());
			String measureUnit = createStringParameter(dictionaryValue.getMeasureUnit());
			dataProvider.update(String.format(
					"UPDATE dictionary_value SET dictionary_id=%s, code=%s, name=%s, measure_unit=%s WHERE id = "
							+ dictionaryValue.getId(), dictionaryId, code, name, measureUnit));
			Collection<Long> existentResourceIds = dataProvider.queryCollection(
					"SELECT resource_id as id FROM dictionary_value_resource WHERE dictionary_value_id = "
							+ dictionaryValue.getId(), new EntityBuilder<Long>() {
						@Override
						public Long createInstance(ResultSet rs) throws SQLException {
							return rs.getLong("id");
						}
					});

			for (DictionaryValueResource resource : dictionaryValue.getResources()) {
				if (existentResourceIds.contains(resource.getResource().getId())) {
					dataProvider.update("UPDATE dictionary_value_resource SET measure = " + resource.getMeasure()
							+ " WHERE resource_id = " + resource.getResource().getId() + " AND "
							+ "dictionary_value_id = " + dictionaryValue.getId());
					existentResourceIds.remove(resource.getResource().getId());
				} else {
					dataProvider
							.update("INSERT INTO dictionary_value_resource (dictionary_value_id, resource_id, measure) VALUES ("
									+ dictionaryValue.getId()
									+ ","
									+ resource.getResource().getId()
									+ ","
									+ resource.getMeasure() + ")");
				}
			}
			StringBuilder resourceIdsBuilder = new StringBuilder();
			for (Long id : existentResourceIds) {
				if (resourceIdsBuilder.length() > 0) {
					resourceIdsBuilder.append(", ");
				}
				resourceIdsBuilder.append(id);
			}
			if (resourceIdsBuilder.length() > 0) {
				dataProvider.update(String.format(
						"DELETE FROM dictionary_value_resource WHERE dictionary_value_id=%s AND resource_id IN (%s)",
						dictionaryValue.getId(), resourceIdsBuilder.toString()));
			}
			dataProvider.commitTransaction();
		} catch (Exception e) {
			if (dataProvider != null) {
				try {
					dataProvider.rollbackTransaction();
				} catch (SQLException ex) {
					throw new IllegalStateException(ex.getMessage(), e);
				}
			}
			throw new IllegalStateException(e);
		}

	}
}
