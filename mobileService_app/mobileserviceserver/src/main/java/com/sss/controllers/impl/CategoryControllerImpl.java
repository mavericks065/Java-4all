package com.sss.controllers.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import com.sss.controllers.Controller;
import com.sss.mdbc.MDBClientSingleton;
import org.apache.log4j.Logger;
import org.vertx.java.core.MultiMap;

import com.google.common.base.Splitter;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import com.sss.constants.DataBaseConstants;

public class CategoryControllerImpl  implements Controller {
	
	private static final Logger logger = Logger.getLogger(CategoryControllerImpl.class);
	
	private final MongoClient client;
	private final DB db;
	private final DBCollection categoriesCollection;
	
	public CategoryControllerImpl() {
		client = MDBClientSingleton.getInstance();
		db = client.getDB(DataBaseConstants.MOBILE_SERVICE_MDB);
		categoriesCollection =db.getCollection(DataBaseConstants.CATEGORIES_COLLECTION);
	}

	@Override
	public void createDocument(final MultiMap map) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * find announces in mongoDB with or without arguments
	 */
	@Override
	public DBObject findDocument(String search, final int limit) {
		DBObject query = new BasicDBObject();
		
		if ((search != null) && (!search.isEmpty())) {
			query = createFindingQuery(search);
		}
		
		DBCursor cursor = categoriesCollection.find(query);
		
		BasicDBList dbObjectList = new BasicDBList();
		while (cursor.hasNext()) {
			dbObjectList.add(cursor.next());
		}
		
		return dbObjectList;
	}

	/**
	 * create the finding query
	 * @param search
	 * @return
	 */
	private DBObject createFindingQuery(String search) {
		
		try {
			search = URLDecoder.decode(search, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error while decoding the search URL : " + e.getMessage(), e);
		}
		
		final Map<String, String> map = Splitter.on("&") 
		        .withKeyValueSeparator("=")
		        .split(search);
		
		return new BasicDBObject(map);
	}

	@Override
	public DBObject modifyDocument(final MultiMap map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDocument(final MultiMap map) {
		// TODO Auto-generated method stub
		
	}}