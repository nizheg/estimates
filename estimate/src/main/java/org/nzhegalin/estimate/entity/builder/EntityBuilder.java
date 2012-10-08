package org.nzhegalin.estimate.entity.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityBuilder<T> {

	T createInstance(ResultSet rs) throws SQLException;

}
