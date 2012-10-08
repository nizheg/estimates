package org.nzhegalin.estimate.entity.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nzhegalin.estimate.entity.Dictionary;
import org.nzhegalin.estimate.entity.DictionaryValue;

public class DictionaryValueBuilder implements EntityBuilder<DictionaryValue> {

	public DictionaryValueBuilder() {
	}

	@Override
	public DictionaryValue createInstance(ResultSet rs) throws SQLException {
		DictionaryValue value = new DictionaryValue();
		value.setId(rs.getLong("dictionary_value_id"));
		value.setCode(rs.getString("dictionary_value_code"));
		value.setMeasureUnit(rs.getString("dictionary_value_measure_unit"));
		value.setName(rs.getString("dictionary_value_name"));

		DictionaryBuilder dictionaryBuilder = new DictionaryBuilder();
		Dictionary dictionary = dictionaryBuilder.createInstance(rs);
		value.setDictionary(dictionary);

		return value;
	}
}
