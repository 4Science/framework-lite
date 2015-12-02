package it.cilea.core.authorization.model;

import java.util.Set;

public interface CoreIdentity {

	Boolean hasAuthorities();

	Set<? extends CoreAuthorities> getAuthorities();
}
