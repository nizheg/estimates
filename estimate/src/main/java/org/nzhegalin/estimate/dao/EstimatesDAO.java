package org.nzhegalin.estimate.dao;

import java.util.Collection;

import org.nzhegalin.estimate.entity.Estimates;
import org.nzhegalin.estimate.entity.Estimates.EstimatesItem;

public interface EstimatesDAO {
	Estimates getEstimates(long id);

	Collection<Estimates> getAllEstimates();

	boolean addEstimateItem(Estimates estimates, EstimatesItem item);

	boolean updateEstimateItem(EstimatesItem item);

	boolean deleteEstimateItem(EstimatesItem item);

	boolean createNewEstimates(String name);
}
