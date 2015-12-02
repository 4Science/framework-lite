package it.cilea.core.authorization.service;

public interface DataAccessLogic<R, H> {

	boolean check(R resource, H request);
}
