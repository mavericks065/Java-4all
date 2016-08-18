package com.sss.handlers;

import com.sss.VertxServer;
import org.vertx.java.core.http.HttpServerRequest;

public class Restriction {
	
	/**
	 * Check user is authorized & login in correctly
	 * by checking if session id is in server's chache
	 * @param request
	 * @return true if authorized
	 */
	public static boolean authorized(HttpServerRequest request) {
		String cookie = request.headers().get("sessionid");
		if (cookie == null || !VertxServer.sessions.containsKey(cookie)) {
			request.response().setStatusCode(400).end();
			return false;
		} 		
		return true;
		
	}
}
