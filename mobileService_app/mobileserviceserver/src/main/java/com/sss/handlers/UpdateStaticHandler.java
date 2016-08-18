package com.sss.handlers;

import java.util.Date;

import com.sss.controllers.impl.AnnounceControllerImpl;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

import com.mongodb.DBObject;

public class UpdateStaticHandler extends AbstractHandler {

	private static final String POST = "POST";
	private static final String UPDATE = "update";

	@Override
	public void handle(final HttpServerRequest request) {
		if (Restriction.authorized(request)){
			super.handle(request);
			if (request.method().equals(POST)) {
				request.expectMultiPart(true);
				request.bodyHandler(new Handler<Buffer>() {
					@Override
					public void handle(Buffer arg0) {
						attributes = request.formAttributes();
						final DBObject responseObject = controller
								.modifyDocument(attributes);
	
						updateTracking(responseObject);
	
						request.response().end(responseObject.toString());
					}
				});
			}
		}
	}

	/**
	 * mathod that add the tracking of this action into the tracking collection
	 * @param responseObject
	 */
	private void updateTracking(final DBObject responseObject) {
		if (controller instanceof AnnounceControllerImpl) {
			((AnnounceControllerImpl) controller).updateAnnounceTracking(attributes.get("ownerEmail"), 
					responseObject.get("_id").toString(), 
					attributes.get("lon"), 
					attributes.get("lat"), 
					new Date(),
					UPDATE);
		}
	}
}