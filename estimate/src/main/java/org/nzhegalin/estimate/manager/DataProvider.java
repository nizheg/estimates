package org.nzhegalin.estimate.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.nzhegalin.estimate.entity.builder.EntityBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DataProvider {

	private JdbcTemplate template;

	public void setDataSource(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	public <T> T queryObject(String query, final EntityBuilder<T> builder) throws SQLException {
		return template.queryForObject(query, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				return builder.createInstance(rs);
			}
		});

	}

	public <T> Collection<T> queryCollection(String query, final EntityBuilder<T> builder) throws SQLException {
		return template.query(query, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				return builder.createInstance(rs);
			}
		});
	}

	public boolean update(String query) throws SQLException {
		return template.update(query) > 0;
	}

}
