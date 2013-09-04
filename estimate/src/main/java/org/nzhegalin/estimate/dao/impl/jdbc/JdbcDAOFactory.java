package org.nzhegalin.estimate.dao.impl.jdbc;

import java.io.Serializable;

import org.nzhegalin.estimate.dao.DAOFactory;

public class JdbcDAOFactory implements DAOFactory, Serializable {

	private static final long serialVersionUID = 1L;

	private JdbcDictionaryDAO dictionaryProvider;
	private JdbcEstimatesDAO estimatesProvider;
	private JdbcResourceDAO resourceProvider;
	private JdbcDictionaryValueDAO dictionaryValueProvider;

	@Override
	public JdbcDictionaryDAO getDictionaryDAO() {
		return dictionaryProvider;
	}

	public void setDictionaryDAO(JdbcDictionaryDAO dictionaryProvider) {
		this.dictionaryProvider = dictionaryProvider;
	}

	@Override
	public JdbcEstimatesDAO getEstimatesDAO() {
		return estimatesProvider;
	}

	public void setEstimatesDAO(JdbcEstimatesDAO estimatesProvider) {
		this.estimatesProvider = estimatesProvider;
	}

	@Override
	public JdbcResourceDAO getResourceDAO() {
		return resourceProvider;
	}

	public void setResourceDAO(JdbcResourceDAO resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public JdbcDictionaryValueDAO getDictionaryValueDAO() {
		return dictionaryValueProvider;
	}

	public void setDictionaryValueDAO(JdbcDictionaryValueDAO dictionaryValueProvider) {
		this.dictionaryValueProvider = dictionaryValueProvider;
	}

	public void init() {
		this.estimatesProvider.setDictionaryValueDAO(getDictionaryValueDAO());
	}

}
