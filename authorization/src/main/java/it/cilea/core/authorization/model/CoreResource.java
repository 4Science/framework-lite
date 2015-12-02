package it.cilea.core.authorization.model;

public interface CoreResource<R> {
	String getDiscriminator();

	R getIdentifier();
	
	String getDataAccessLogicBeanIdentifier();

}
