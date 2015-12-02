package it.cilea.core.authorization.model;

public interface CoreAuthority<A> {

	String getDescription();

	Boolean useInfos();

	A getIdentifier();

	Integer getPriority();
}
