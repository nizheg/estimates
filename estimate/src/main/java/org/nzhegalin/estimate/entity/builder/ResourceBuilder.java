package org.nzhegalin.estimate.entity.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nzhegalin.estimate.entity.MachineResource;
import org.nzhegalin.estimate.entity.ManhourResource;
import org.nzhegalin.estimate.entity.MaterialResource;
import org.nzhegalin.estimate.entity.Resource;

public class ResourceBuilder implements EntityBuilder<Resource> {

	public static final String RESOURCE_ID = "resource_id";
	public static final String RESOURCE_TYPE = "resource_type";
	public static final String RESOURCE_CODE = "resource_code";
	public static final String RESOURCE_MEASURE_UNIT = "resource_measure_unit";
	public static final String RESOURCE_NAME = "resource_name";
	public static final String RESOURCE_MEASURE = "resource_measure";

	@Override
	public Resource createInstance(ResultSet rs) throws SQLException {
		Resource resource = newResource(rs.getString(RESOURCE_TYPE));
		resource.setId(rs.getLong(RESOURCE_ID));
		resource.setCode(rs.getString(RESOURCE_CODE));
		resource.setMeasureUnit(rs.getString(RESOURCE_MEASURE_UNIT));
		resource.setName(rs.getString(RESOURCE_NAME));
		return resource;
	}

	protected Resource newResource(String typeString) {
		char type = typeString.charAt(0);
		switch (type) {
		case 'm':
			return new MaterialResource();
		case 'h':
			return new ManhourResource();
		case 'g':
			return new MachineResource();
		default:
			throw new IllegalStateException("Type of resource is not defined");
		}
	}

}
