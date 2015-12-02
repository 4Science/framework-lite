package it.cilea.core.authorization.comparator;

import it.cilea.core.authorization.model.impl.ResourceLink;

import java.io.Serializable;
import java.util.Comparator;

public class ResourceLinkComparator implements Comparator<ResourceLink>, Serializable {

	public int compare(ResourceLink element1, ResourceLink element2) {
		if (!element1.getChild().getDescription().equals(element2.getChild().getDescription()))
			return element1.getChild().getDescription().compareTo(element2.getChild().getDescription());
		return element1.getParent().getDescription().compareTo(element2.getParent().getDescription());
	}
}
