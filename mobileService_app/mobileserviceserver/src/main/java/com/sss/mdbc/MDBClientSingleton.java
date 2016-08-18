package com.sss.mdbc;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.MongoClient;

/**
 * Singleton MongoClient for being sure there is just one instance
 */
public class MDBClientSingleton {
	
	private static final Logger logger = Logger.getLogger(MDBClientSingleton.class);
	
	private static MongoClient client;
	
	private MDBClientSingleton() {
	}
	
	public static MongoClient getInstance(){
		if (client == null){
			try {
				client = new MongoClient();
			} catch (UnknownHostException e) {
				logger.error("Error while connecting to MongoDB ", e);
			}
		}
		return client;
	}

}
