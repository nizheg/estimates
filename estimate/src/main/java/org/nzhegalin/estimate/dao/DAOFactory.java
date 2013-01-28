package org.nzhegalin.estimate.dao;

import org.nzhegalin.estimate.manager.DictionaryProvider;
import org.nzhegalin.estimate.manager.DictionaryValueProvider;
import org.nzhegalin.estimate.manager.EstimatesProvider;
import org.nzhegalin.estimate.manager.ResourceProvider;

public interface DAOFactory {

	DictionaryProvider getDictionaryProvider();

	DictionaryValueProvider getDictionaryValueProvider();

	EstimatesProvider getEstimatesProvider();

	ResourceProvider getResourceProvider();

}
