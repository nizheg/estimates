package org.nzhegalin.estimate.entity.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nzhegalin.estimate.entity.Dictionary;

public class DictionaryBuilder implements EntityBuilder<Dictionary> {

	@Override
	public Dictionary createInstance(ResultSet rs) throws SQLException {
		Dictionary dictionary = new Dictionary();
		dictionary.setId(rs.getLong("dictionary_id"));
		dictionary.setCode(rs.getString("dictionary_code"));
		return dictionary;
	}

}
