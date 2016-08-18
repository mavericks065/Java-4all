package com.sss;

import java.util.HashMap;

import com.sss.handlers.AbstractHandler;
import com.sss.handlers.DeleteStaticHandler;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import com.sss.handlers.AddingStaticHandler;
import com.sss.handlers.ListStaticHandler;
import com.sss.handlers.LoginStaticHandler;
import com.sss.handlers.UpdateStaticHandler;

public class VertxServer extends Verticle {

	private static final Integer PORT_NUMBER = 8888;
	/** key = sessionid, value = user login **/
	public static HashMap<String, String> sessions = new HashMap<>();

	public void start() {
		
		final AbstractHandler addingStaticHandler = new AddingStaticHandler();
		final AbstractHandler listStaticHandler = new ListStaticHandler();
		final AbstractHandler loginStaticHandler = new LoginStaticHandler();
		final AbstractHandler updateStaticHandler = new UpdateStaticHandler();
		final AbstractHandler deleteStaticHandler = new DeleteStaticHandler();

		final RouteMatcher routeMatcher = new RouteMatcher();
		routeMatcher.post("/update/:collection", updateStaticHandler);
		routeMatcher.put("/add/:collection/", addingStaticHandler);
		routeMatcher.put("/add/:collection", addingStaticHandler);
		routeMatcher.delete("/delete/:collection/", deleteStaticHandler);
		routeMatcher.delete("/delete/:collection", deleteStaticHandler);
		routeMatcher.get("/list/:collection/:search", listStaticHandler);
		routeMatcher.get("/list/:collection/", listStaticHandler);
		routeMatcher.get("/list/:collection", listStaticHandler);
		routeMatcher.get("/list/:collection/:embeddedDocument/:field", listStaticHandler);
		routeMatcher.get("/login/:collection/:search", loginStaticHandler);
		vertx.createHttpServer().requestHandler(routeMatcher)
				.listen(PORT_NUMBER);

		container.logger().info("Webserver started, listening on port: 8888");
	}
}
