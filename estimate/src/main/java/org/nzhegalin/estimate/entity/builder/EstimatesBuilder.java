package org.nzhegalin.estimate.entity.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nzhegalin.estimate.entity.Estimates;

public class EstimatesBuilder implements EntityBuilder<Estimates> {

	@Override
	public Estimates createInstance(ResultSet rs) throws SQLException {
		Estimates estimates = new Estimates();
		estimates.setId(rs.getLong("estimates_id"));
		estimates.setName(rs.getString("estimates_name"));
		return estimates;
	}

}
