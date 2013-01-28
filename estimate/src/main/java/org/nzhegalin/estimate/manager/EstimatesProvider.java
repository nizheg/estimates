package org.nzhegalin.estimate.manager;

import java.sql.SQLException;
import java.util.Collection;

import org.nzhegalin.estimate.entity.DictionaryValue;
import org.nzhegalin.estimate.entity.DictionaryValueResource;
import org.nzhegalin.estimate.entity.Estimates;
import org.nzhegalin.estimate.entity.Estimates.EstimatesItem;
import org.nzhegalin.estimate.entity.builder.EstimatesBuilder;
import org.nzhegalin.estimate.entity.builder.EstimatesItemBuilder;

public class EstimatesProvider {

	private DataProvider dataProvider;
	private DictionaryValueProvider dictionaryValueProvider;

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public void setDictionaryValueProvider(DictionaryValueProvider dictionaryValueProvider) {
		this.dictionaryValueProvider = dictionaryValueProvider;
	}

	public Estimates getEstimates(long id) {
		try {

			Estimates estimates = dataProvider.queryObject(
					"SELECT id as estimates_id, name as estimates_name FROM estimates WHERE estimates.id = " + id,
					new EstimatesBuilder());
			Collection<EstimatesItem> estimatesItems = dataProvider.queryCollection("SELECT "
					+ "  estimates_value.id as estimates_value_id, "
					+ "  estimates_value.measure as estimates_value_measure, "
					+ "  estimates_value.number as estimates_value_number, "
					+ "  dictionary_value.code as dictionary_value_code, "
					+ "  dictionary_value.name as dictionary_value_name, "
					+ "  dictionary_value.measure_unit as dictionary_value_measure_unit, "
					+ "  dictionary_value.id as dictionary_value_id, " + "  dictionary.id as dictionary_id, "
					+ "  dictionary.code as dictionary_code " + "  FROM estimates_value "
					+ "  INNER JOIN dictionary_value ON dictionary_value.id = estimates_value.dictionary_value_id "
					+ "  INNER JOIN dictionary ON dictionary.id = dictionary_value.dictionary_id "
					+ "  WHERE estimates_value.estimates_id = " + estimates.getId(), new EstimatesItemBuilder());
			for (EstimatesItem estimatesItem : estimatesItems) {
				DictionaryValue value = estimatesItem.getValue();
				Collection<DictionaryValueResource> dictionaryValueResources = dictionaryValueProvider
						.getDictionaryValueResources(value);
				value.getResources().addAll(dictionaryValueResources);
			}
			estimates.addItems(estimatesItems);
			return estimates;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	public Collection<Estimates> getAllEstimates() {
		try {
			return dataProvider.queryCollection("SELECT id as estimates_id, name as estimates_name FROM estimates",
					new EstimatesBuilder());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	public boolean addEstimateItem(Estimates estimates, EstimatesItem item) {
		try {
			return dataProvider
					.update(String
							.format("INSERT INTO estimates_value(measure, estimates_id, dictionary_value_id, number) "
									+ "  SELECT "
									+ "    %s,"
									+ "    estimates_id.id,"
									+ "    %s,"
									+ "    (SELECT coalesce(max(number) + 1, 1) FROM estimates_value WHERE estimates_id = estimates_id.id)"
									+ "  FROM (SELECT estimates.id FROM estimates WHERE id = %s) as estimates_id(id)",
									item.getMeasure(), item.getValue().getId(), estimates.getId()));
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}

	}

	public boolean updateEstimateItem(EstimatesItem item) {
		try {
			return dataProvider.update(String.format("UPDATE estimates_value SET measure=%s, number=%s"
					+ "  WHERE id = " + item.getId(), item.getMeasure(), item.getNumber()));
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}

	}

	public boolean deleteEstimateItem(EstimatesItem item) {
		try {
			return dataProvider.update("DELETE FROM estimates_value WHERE id = " + item.getId());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}

	}

	public boolean createNewEstimates(String name) {
		try {
			return dataProvider.update("INSERT INTO estimates(name) VALUES('" + name.replace("'", "''") + "')");
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
}
