package org.nzhegalin.estimate.dao;

import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.nzhegalin.estimate.entity.builder.EntityBuilder;

public interface DataProvider {
	void setDataSource(DataSource dataSource);

	<T> T queryObject(String query, final EntityBuilder<T> builder) throws SQLException;

	<T> Collection<T> queryCollection(String query, final EntityBuilder<T> builder) throws SQLException;

	boolean update(String query) throws SQLException;

}
