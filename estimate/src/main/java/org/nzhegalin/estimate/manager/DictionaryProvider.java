package org.nzhegalin.estimate.manager;

import java.sql.SQLException;
import java.util.Collection;

import org.nzhegalin.estimate.entity.Dictionary;
import org.nzhegalin.estimate.entity.builder.DictionaryBuilder;

public class DictionaryProvider {

	public Dictionary getDictionary(long id) {
		try {
			DataProvider dataProvider = DataProvider.instance();
			return dataProvider
					.queryObject(
							"SELECT id as dictionary_id, code as dictionary_code FROM dictionary WHERE dictionary_id = "
									+ id, new DictionaryBuilder());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	public Collection<Dictionary> getAllDictionaries() {
		try {
			DataProvider dataProvider = DataProvider.instance();
			return dataProvider
					.queryCollection(
							"SELECT id as dictionary_id, code as dictionary_code FROM dictionary",
							new DictionaryBuilder());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	public boolean updateDictionary(Dictionary item) {
		DataProvider dataProvider;
		try {
			dataProvider = DataProvider.instance();
			return dataProvider.update(String.format(
					"UPDATE dictionary SET code=%s WHERE id = " + item.getId(),
					item.getCode()));
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}

	}
}
