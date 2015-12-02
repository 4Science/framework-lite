package it.cilea.core.authorization.comparator;

import it.cilea.core.authorization.model.impl.Resource;

import java.io.Serializable;
import java.util.Comparator;

public class ResourceComparator implements Comparator<Resource>, Serializable {

	public int compare(Resource element1, Resource element2) {
		return element1.getDescription().compareTo(element2.getDescription());

	}
}
