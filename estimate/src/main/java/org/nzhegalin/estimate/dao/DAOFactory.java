package org.nzhegalin.estimate.dao;

public interface DAOFactory {

	DictionaryDAO getDictionaryDAO();

	DictionaryValueDAO getDictionaryValueDAO();

	EstimatesDAO getEstimatesDAO();

	ResourceDAO getResourceDAO();

}
