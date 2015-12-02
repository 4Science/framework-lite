package it.cilea.core.authorization.comparator;

import it.cilea.core.authorization.model.CoreAuthority;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class AuthorityComparator implements Comparator<CoreAuthority>, Serializable {

	public int compare(CoreAuthority element1, CoreAuthority element2) {
		if (element1.getPriority().equals(element2.getPriority()))
			element1.getDescription().compareTo(element2.getDescription());
		return element1.getPriority().compareTo(element2.getPriority());

	}
}
