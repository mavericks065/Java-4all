package com.sss.controllers;

import org.vertx.java.core.MultiMap;

import com.mongodb.DBObject;

/**
 * It will be exposed to the client and it will do the link 
 * between client and server
 */
public interface Controller {
	
	public void createDocument(final MultiMap map);
	
	public DBObject findDocument(final String search, final int limit);
	
	public DBObject modifyDocument(final MultiMap map);
	
	public void deleteDocument(final MultiMap map);

}
