package com.sss.handlers;

import java.util.Date;

import com.sss.controllers.impl.AnnounceControllerImpl;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;


public class DeleteStaticHandler extends AbstractHandler {
	
	private static final String DELETE = "DELETE";
	private static final String DEL = "delete";
	
	@Override
	public void handle(final HttpServerRequest request) {
		if (Restriction.authorized(request)){
			super.handle(request);
			if (request.method().equals(DELETE)) {
				request.expectMultiPart(true);
				request.bodyHandler(new Handler<Buffer>() {
					@Override
					public void handle(Buffer arg0) {
						attributes = request.headers();
						track();
						controller.deleteDocument(attributes);
					}
				});
		request.response().end();
			}
		}
	}
	
	/**
	 * check if it is an announce then if it is the case it adds 
	 * it calls the tracking process
	 */
	private void track() {
		if (controller instanceof AnnounceControllerImpl) {
			((AnnounceControllerImpl) controller).announceTracking(attributes.get("ownerEmail"), 
					attributes.get("title") ,
					attributes.get("lon"), 
					attributes.get("lat"), 
					new Date(),
					DEL);
		}
	}
}