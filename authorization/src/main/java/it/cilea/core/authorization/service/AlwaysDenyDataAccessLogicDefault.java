package it.cilea.core.authorization.service;

import org.springframework.stereotype.Service;

@Service("alwaysDenyDataAccessLogicDefault")
public class AlwaysDenyDataAccessLogicDefault implements DataAccessLogic<String, String> {

	@Override
	public boolean check(String resource, String parameters) {

		return false;
	}

}
