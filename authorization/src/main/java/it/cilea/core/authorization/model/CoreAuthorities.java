package it.cilea.core.authorization.model;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

public interface CoreAuthorities<R, A, I> extends GrantedAuthority {

	A getAuthorityIdentifier();

	R getResourceIdentifier();

	Set<I> getInfoIdentifierSet();

	Set<CoreIdentity> getIdentitySet();

	Set<String> getInfoIdentifierStringSet();

}
