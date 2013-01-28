package org.nzhegalin.estimate.dao.impl;

import java.io.Serializable;

import org.nzhegalin.estimate.dao.DAOFactory;
import org.nzhegalin.estimate.manager.DictionaryProvider;
import org.nzhegalin.estimate.manager.DictionaryValueProvider;
import org.nzhegalin.estimate.manager.EstimatesProvider;
import org.nzhegalin.estimate.manager.ResourceProvider;

public class JdbcDAOFactory implements DAOFactory, Serializable {

	private static final long serialVersionUID = 1L;

	private DictionaryProvider dictionaryProvider;
	private EstimatesProvider estimatesProvider;
	private ResourceProvider resourceProvider;
	private DictionaryValueProvider dictionaryValueProvider;

	@Override
	public DictionaryProvider getDictionaryProvider() {
		return dictionaryProvider;
	}

	public void setDictionaryProvider(DictionaryProvider dictionaryProvider) {
		this.dictionaryProvider = dictionaryProvider;
	}

	@Override
	public EstimatesProvider getEstimatesProvider() {
		return estimatesProvider;
	}

	public void setEstimatesProvider(EstimatesProvider estimatesProvider) {
		this.estimatesProvider = estimatesProvider;
	}

	@Override
	public ResourceProvider getResourceProvider() {
		return resourceProvider;
	}

	public void setResourceProvider(ResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public DictionaryValueProvider getDictionaryValueProvider() {
		return dictionaryValueProvider;
	}

	public void setDictionaryValueProvider(DictionaryValueProvider dictionaryValueProvider) {
		this.dictionaryValueProvider = dictionaryValueProvider;
	}

	public void init() {
		this.estimatesProvider.setDictionaryValueProvider(getDictionaryValueProvider());
	}

}
