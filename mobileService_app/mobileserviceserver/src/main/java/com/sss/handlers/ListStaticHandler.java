package com.sss.handlers;

import org.vertx.java.core.http.HttpServerRequest;

import com.mongodb.DBObject;


public class ListStaticHandler extends AbstractHandler {
	
	@Override
	public void handle(HttpServerRequest request) {
		if (Restriction.authorized(request)){
			super.handle(request);
	
			String search = "";
			if (params.get("search") != null && !params.get("search").equals("")) {
				search = params.get("search");
			} else if (params.get("embeddedDocument") != null && !params.get("embeddedDocument").equals("")) {
				search = params.get("embeddedDocument").concat(".").concat(params.get("field"));
			}
			DBObject responseObject = controller.findDocument(search, 0);
			request.response().end(responseObject.toString());
		}
	}
}