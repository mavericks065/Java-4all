package com.sss.handlers;

import com.sss.controllers.impl.AnnounceControllerImpl;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.http.HttpServerRequest;

import com.sss.constants.DataBaseConstants;
import com.sss.controllers.Controller;
import com.sss.controllers.impl.CategoryControllerImpl;
import com.sss.controllers.impl.UserControllerImpl;

public abstract class AbstractHandler implements Handler<HttpServerRequest> {

	protected Controller controller;
	protected MultiMap params;
	protected MultiMap attributes;
	
	private static final String COLLECTION = "collection";
	
	@Override
	public void handle(final HttpServerRequest request) {
		params = request.params();
		final String collection = params.get(COLLECTION);

		if (DataBaseConstants.USERS_COLLECTION.equals(collection)) {
			controller = new UserControllerImpl();
		} else if (DataBaseConstants.ADS_COLLECTION.equals(collection)) {
			controller = new AnnounceControllerImpl();
		} else if (DataBaseConstants.CATEGORIES_COLLECTION.equals(collection)) {
			controller = new CategoryControllerImpl();
		}
	}

}