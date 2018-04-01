package com.kingkingyyk.chi.connection;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.datastax.driver.core.ResultSet;
import com.kingkingyyk.chi.Chi;
import com.kingkingyyk.chi.data.db.Database;
import com.kingkingyyk.chi.utils.Utils;

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
	private int port;
	private AtomicInteger streamID=new AtomicInteger(0);
	
	public static void initialize(com.kingkingyyk.chi.config.REST rest) {
		new REST(rest).start();
	}
	
	private REST(com.kingkingyyk.chi.config.REST rest) {
		this.port=rest.PORT;
		this.vertx=Vertx.vertx(new VertxOptions().setMaxWorkerExecuteTime(Long.MAX_VALUE));
		this.router=Router.router(vertx);
		
		router.route().handler(BodyHandler.create().setBodyLimit(rest.MAX_PACKET_SIZE*1024));
		router.get("/admin/shutdown").handler((rc)-> this.shutdown(rc));
		router.get("/users").handler((rc)-> this.getUsers(rc));
	}
	
	private void shutdown(RoutingContext rc) {
		int sid=this.printStreamInfo(rc);
		vertx.executeBlocking(future -> {
			JsonObject obj=new JsonObject();
			obj.put("result", Chi.shutdown());
		    future.complete(obj);
		}, false, res -> this.respondToRequest(rc, sid, res.result().toString()) );
	}
	
	private void getUsers(RoutingContext rc) {
		int sid=this.printStreamInfo(rc);
		vertx.executeBlocking(future -> {
		    long start=System.currentTimeMillis();
		    ResultSet rs=Database.getSession().execute("select json * from chi.users");
		    JsonArray ary=new JsonArray();
		    while (!rs.isExhausted()) ary.add(new JsonObject(rs.one().getString(0)));
		    long end=System.currentTimeMillis();
		    Logger.getLogger(this.getClass().getName()).info("Stream "+sid+" - Query took "+(end-start)+"ms. ("+ary.size()+" rows)");
		    
		    future.complete(ary);
		}, false, res -> this.respondToRequest(rc, sid, res.result().toString()) );
	}
	
	private int printStreamInfo(RoutingContext rc) {
		int sid = this.streamID.incrementAndGet();
		Logger.getLogger(this.getClass().getName()).info("Stream "+sid+" - ["+rc.request().remoteAddress().toString()+"] - "+rc.normalisedPath());
		return sid;
	}
	
	private void respondToRequest(RoutingContext rc, int streamID, String value) {
		if (!rc.response().closed()) {
			rc.response().putHeader("content-type", "application/json").end(value);
			Logger.getLogger(this.getClass().getName()).info("Stream "+streamID+" - Returned "+Utils.formatBytes(rc.response().bytesWritten())+".");
		}
	}
	
	@Override
	public void run () {
		vertx.createHttpServer().requestHandler(router::accept).listen(this.port);
	}
}
