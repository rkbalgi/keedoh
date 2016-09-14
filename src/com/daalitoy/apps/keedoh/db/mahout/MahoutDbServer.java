package com.daalitoy.apps.keedoh.db.mahout;

import org.apache.log4j.Logger;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;

public class MahoutDbServer {

	private static final Logger log = Logger.getLogger(MahoutDbServer.class);
	private static MahoutDbServer instance = new MahoutDbServer();
	private Server server = null;

	public static MahoutDbServer getInstance() {
		return (instance);
	}

	private MahoutDbServer() {

		HsqlProperties p = new HsqlProperties();
		p.setProperty("server.database.0", "file:/../db/mahout");
		p.setProperty("server.dbname.0", "mahout");
		server = new Server();
		try {
			server.setProperties(p);
		} catch (Exception e) {
			log.error("error configuring mahout", e);
		}
		server.setLogWriter(null); // can use custom writer
		server.setErrWriter(null); // can use custom writer

	}

	public void start() {
		server.start();
	}

	public void shutdown() {
		server.shutdown();
	}

}
