package com.kingkingyyk.chi.connection;

import com.datastax.driver.core.ResultSet;
import com.kingkingyyk.chi.Chi;
import com.kingkingyyk.chi.config.Config;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class REST extends Thread {
	private Vertx vertx;
	private Router router;
	
	public REST() {
		this.vertx=Vertx.vertx(new VertxOptions().setMaxWorkerExecuteTime(Long.MAX_VALUE));
		this.router=Router.router(vertx);
		
		router.route().handler(BodyHandler.create().setBodyLimit(5*1024*1024));
		
		router.get("/users").handler((rc)-> this.getUsers(rc));
	}
	
	public void getUsers(RoutingContext rc) {
		vertx.executeBlocking(future -> {
		    ResultSet rs=Chi.session.execute("select json * from chi.users");
		    JsonArray ary=new JsonArray();
		    while (!rs.isExhausted()) ary.add(new JsonObject(rs.one().getString(0)));
		    future.complete(ary);
		}, false, res -> {
			if (!rc.response().closed()) rc.response().putHeader("content-type", "application/json").end(res.result().toString());
		});
	}
	
	@Override
	public void run () {
		vertx.createHttpServer().requestHandler(router::accept).listen(Config.REST_PORT);
	}
}
