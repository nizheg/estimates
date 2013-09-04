package org.nzhegalin.estimate.dao.impl.jdbc;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;

import org.nzhegalin.estimate.dao.DataProvider;
import org.nzhegalin.estimate.dao.DictionaryDAO;
import org.nzhegalin.estimate.entity.Dictionary;
import org.nzhegalin.estimate.entity.builder.DictionaryBuilder;

public class JdbcDictionaryDAO implements DictionaryDAO, Serializable {

	private static final long serialVersionUID = 1L;
	private DataProvider dataProvider;

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	@Override
	public Dictionary getDictionary(long id) {
		try {
			return dataProvider.queryObject(
					"SELECT id as dictionary_id, code as dictionary_code FROM dictionary WHERE dictionary_id = " + id,
					new DictionaryBuilder());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Collection<Dictionary> getAllDictionaries() {
		try {
			return dataProvider.queryCollection("SELECT id as dictionary_id, code as dictionary_code FROM dictionary",
					new DictionaryBuilder());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public boolean updateDictionary(Dictionary item) {
		try {
			return dataProvider.update(String.format("UPDATE dictionary SET code=%s WHERE id = " + item.getId(),
					item.getCode()));
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}

	}
}
