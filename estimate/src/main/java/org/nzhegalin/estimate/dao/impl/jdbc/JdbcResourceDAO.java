package org.nzhegalin.estimate.dao.impl.jdbc;

import java.sql.SQLException;
import java.util.Collection;

import org.nzhegalin.estimate.dao.DataProvider;
import org.nzhegalin.estimate.dao.ResourceDAO;
import org.nzhegalin.estimate.entity.Resource;
import org.nzhegalin.estimate.entity.builder.ResourceBuilder;

public class JdbcResourceDAO implements ResourceDAO {

	private DataProvider dataProvider;

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	@Override
	public Collection<Resource> getAllResources() {
		try {
			return dataProvider
					.queryCollection(
							"SELECT code as resource_code, name as resource_name, measure_unit as resource_measure_unit, id as resource_id, type as resource_type FROM resource",
							new ResourceBuilder());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void createNewResource(Resource value) {

		try {
			String code = createStringParameter(value.getCode());
			String name = createStringParameter(value.getName());
			String measureUnit = createStringParameter(value.getMeasureUnit());
			String type = createStringParameter(String.valueOf(value.getType()));
			dataProvider.update("INSERT INTO resource(code, name, measure_unit, type) VALUES (" + code + "," + name
					+ "," + measureUnit + "," + type + ")");
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	private String createStringParameter(String parameter) {
		return String.format("'%s'", parameter == null ? "" : parameter.replace("'", "''"));
	}
}
