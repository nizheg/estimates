package org.nzhegalin.estimate.dao;

import java.util.Collection;

import org.nzhegalin.estimate.entity.Resource;

public interface ResourceDAO {
	Collection<Resource> getAllResources();

	void createNewResource(Resource value);

}
