package com.sss.handlers;

import java.util.Date;

import com.sss.controllers.impl.AnnounceControllerImpl;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

import com.sss.controllers.impl.UserControllerImpl;

public class AddingStaticHandler extends AbstractHandler {

	private static final String PUT = "PUT";
	private static final String ADD = "add";

	@Override
	public void handle(final HttpServerRequest request) {
		if (Restriction.authorized(request)){
			super.handle(request);
			if (request.method().equals(PUT)) {
				request.expectMultiPart(true);
				request.bodyHandler(new Handler<Buffer>() {
					@Override
					public void handle(Buffer arg0) {
						attributes = request.formAttributes();
						controller.createDocument(attributes);
	
						if (controller instanceof UserControllerImpl) {
							((UserControllerImpl) controller).addUserTracking(attributes.get("email")); 
						} else if (controller instanceof AnnounceControllerImpl) {
							updateNewAnnounceTracking();
						}
					}
				});
			}
			request.response().end();
		}
	}

	/**
	 * method that add the tracking of this action into the tracking collection
	 */
	private void updateNewAnnounceTracking() {
		((AnnounceControllerImpl) controller).announceTracking(attributes.get("ownerEmail"), 
				attributes.get("title") ,
				attributes.get("lon"), 
				attributes.get("lat"), 
				new Date(),
				ADD);
	}
	}