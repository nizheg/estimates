package org.nzhegalin.estimate.dao;

import java.util.Collection;

import org.nzhegalin.estimate.entity.Dictionary;

public interface DictionaryDAO {
	Dictionary getDictionary(long id);

	Collection<Dictionary> getAllDictionaries();

	boolean updateDictionary(Dictionary item);

}
