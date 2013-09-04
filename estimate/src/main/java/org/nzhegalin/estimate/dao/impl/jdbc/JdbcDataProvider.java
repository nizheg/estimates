package org.nzhegalin.estimate.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.nzhegalin.estimate.dao.DataProvider;
import org.nzhegalin.estimate.entity.builder.EntityBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcDataProvider implements DataProvider {

	private JdbcTemplate template;

	@Override
	public void setDataSource(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public <T> T queryObject(String query, final EntityBuilder<T> builder) throws SQLException {
		return template.queryForObject(query, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				return builder.createInstance(rs);
			}
		});

	}

	@Override
	public <T> Collection<T> queryCollection(String query, final EntityBuilder<T> builder) throws SQLException {
		return template.query(query, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				return builder.createInstance(rs);
			}
		});
	}

	@Override
	public boolean update(String query) throws SQLException {
		return template.update(query) > 0;
	}

}
