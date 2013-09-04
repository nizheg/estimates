package org.nzhegalin.estimate.dao;

import java.util.Collection;

import org.nzhegalin.estimate.entity.DictionaryValue;
import org.nzhegalin.estimate.entity.DictionaryValueResource;

public interface DictionaryValueDAO {

	DictionaryValue getDictionaryValue(long id);

	DictionaryValue getFullDictionaryValue(long id);

	Collection<DictionaryValueResource> getDictionaryValueResources(DictionaryValue value);

	Collection<DictionaryValue> getAllDictionaryValues();

	void createNewDictionaryValue(DictionaryValue dictionaryValue);

	void updateDictionaryValue(DictionaryValue dictionaryValue);

}
