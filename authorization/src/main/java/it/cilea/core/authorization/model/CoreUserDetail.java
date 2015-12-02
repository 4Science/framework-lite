package it.cilea.core.authorization.model;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface CoreUserDetail<R, A, I> extends UserDetails {

	CoreAuthorities<R, A, I> getAuthorities(R resourceIdentifier);

	boolean hasAuthorities(R resourceIdentifier);

	CoreAuthorities<R, A, I> getAuthorities(R resourceIdentifier, A authorityIdentifier);

	boolean hasAuthorities(R resourceIdentifier, A authorityIdentifier);

	boolean hasAuthorities(R resourceIdentifier, A authorityIdentifier, I infoIdentifier);

	void swithAuthority(A authorityIdentifier);

	A getCurrentAuthorityIdentifier();

	Integer getUserId();

	Map<String, String> getStringMap();

}
