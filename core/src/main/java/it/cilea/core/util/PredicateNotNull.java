package it.cilea.core.util;

import org.apache.commons.collections.Predicate;

public class PredicateNotNull implements Predicate {

	public boolean evaluate(Object arg0) {
		if (arg0 == null)
			return false;
		return true;
	}

}
