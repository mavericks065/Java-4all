package com.sss.handlers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

import com.sss.VertxServer;
import org.apache.log4j.Logger;
import org.vertx.java.core.http.HttpServerRequest;

import com.google.common.base.Splitter;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import com.sss.controllers.impl.UserControllerImpl;

public class LoginStaticHandler extends AbstractHandler {

	private static final Logger logger = Logger
			.getLogger(LoginStaticHandler.class);

	@Override
	public void handle(HttpServerRequest request) {
		super.handle(request);
		final String search = params.get("search");

		DBObject user = (DBObject) ((BasicDBList) controller
				.findDocument(search, 0)).get(0);

		boolean loginOK = checkLogin(search, user);

		if (loginOK) {
			String sessionId = hexEncode(createSessionID());
			request.response().putHeader("Set-Cookie", "sessionid="+sessionId);
			VertxServer.sessions.put(sessionId, String.valueOf(user.get("email")));
			
			if (controller instanceof UserControllerImpl) {
				((UserControllerImpl) controller).updateUserTracking(search,
						new Date());
			}

			user.put("toLog", "true");

			request.response().end(user.toString());
		} else {
			request.response().end(
					new BasicDBObject("toLog", "false").toString());
		}

	}

	/**
	 * create sessionID we send to the client when he cnnects to the application
	 * @return
	 */
	private byte[] createSessionID() {
		byte[] result = null;
		try {
			SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
			
			String randomNum = new Integer(prng.nextInt()).toString();

			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			result = sha.digest(randomNum.getBytes());
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * transform the byte tab into a string the example of David Flanagan's book
	 * "Java In A Nutshell" (hex string)
	 * @param aInput
	 * @return
	 */
	private String hexEncode(final byte[] aInput){
	    StringBuilder result = new StringBuilder();
	    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
	    for (int idx = 0; idx < aInput.length; ++idx) {
	      byte b = aInput[idx];
	      result.append(digits[ (b&0xf0) >> 4 ]);
	      result.append(digits[ b&0x0f]);
	    }
	    return result.toString();
	  }

	/**
	 * test if the user has entered the right pwd
	 * 
	 * @param search
	 * @param responseObject
	 * @param toLog
	 * @return
	 */
	private boolean checkLogin(final String search, DBObject responseObject) {
		boolean loginOK = false;
		
		try {
			final String searchDecoded = URLDecoder.decode(search, "UTF-8");
			final Map<String, String> map = Splitter.on("&")
					.withKeyValueSeparator("=").split(searchDecoded);

			if (responseObject != null
					&& responseObject.get("password").equals(
							map.get("password"))) {
				loginOK = true;
			}

		} catch (UnsupportedEncodingException e) {
			logger.error(
					"Error while decoding the search URL : " + e.getMessage(),
					e);
		}
		return loginOK;
	}
}