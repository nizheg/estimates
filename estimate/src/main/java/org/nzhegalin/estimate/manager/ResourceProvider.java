package org.nzhegalin.estimate.manager;

import java.sql.SQLException;
import java.util.Collection;

import org.nzhegalin.estimate.entity.Resource;
import org.nzhegalin.estimate.entity.builder.ResourceBuilder;

public class ResourceProvider {

	public Collection<Resource> getAllResources() {
		try {
			DataProvider dataProvider = DataProvider.instance();
			return dataProvider
					.queryCollection(
							"SELECT code as resource_code, name as resource_name, measure_unit as resource_measure_unit, id as resource_id, type as resource_type FROM resource",
							new ResourceBuilder());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	public void createNewResource(Resource value) {

		try {
			DataProvider dataProvider = DataProvider.instance();
			String code = createStringParameter(value.getCode());
			String name = createStringParameter(value.getName());
			String measureUnit = createStringParameter(value.getMeasureUnit());
			String type = createStringParameter(String.valueOf(value.getType()));
			dataProvider
					.update("INSERT INTO resource(code, name, measure_unit, type) VALUES ("
							+ code
							+ ","
							+ name
							+ ","
							+ measureUnit
							+ ","
							+ type + ")");
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	private String createStringParameter(String parameter) {
		return String.format("'%s'",
				parameter == null ? "" : parameter.replace("'", "''"));
	}
}
