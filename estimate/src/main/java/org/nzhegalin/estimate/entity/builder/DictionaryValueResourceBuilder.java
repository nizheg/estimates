package org.nzhegalin.estimate.entity.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nzhegalin.estimate.entity.DictionaryValueResource;
import org.nzhegalin.estimate.entity.Resource;

public class DictionaryValueResourceBuilder implements
		EntityBuilder<DictionaryValueResource> {

	public static final String RESOURCE_MEASURE = "dictionary_value_resource_measure";

	@Override
	public DictionaryValueResource createInstance(ResultSet rs)
			throws SQLException {
		DictionaryValueResource dictionaryValueResource = new DictionaryValueResource();
		ResourceBuilder resourceBuilder = new ResourceBuilder();
		Resource resource = resourceBuilder.createInstance(rs);
		dictionaryValueResource.setResource(resource);
		dictionaryValueResource.setMeasure(rs.getDouble(RESOURCE_MEASURE));
		return dictionaryValueResource;
	}

}
