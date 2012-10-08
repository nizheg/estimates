package org.nzhegalin.estimate.entity.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nzhegalin.estimate.entity.DictionaryValue;
import org.nzhegalin.estimate.entity.Estimates.EstimatesItem;

public class EstimatesItemBuilder implements EntityBuilder<EstimatesItem> {

	@Override
	public EstimatesItem createInstance(ResultSet rs) throws SQLException {
		EstimatesItem item = new EstimatesItem();
		item.setId(rs.getLong("estimates_value_id"));
		item.setMeasure(rs.getDouble("estimates_value_measure"));
		item.setNumber(rs.getInt("estimates_value_number"));

		DictionaryValueBuilder dictionaryValueBuilder = new DictionaryValueBuilder();
		DictionaryValue dictionaryValue = dictionaryValueBuilder
				.createInstance(rs);
		item.setValue(dictionaryValue);

		return item;
	}

}
