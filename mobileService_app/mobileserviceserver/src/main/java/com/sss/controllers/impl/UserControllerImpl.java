package com.sss.controllers.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import com.sss.constants.Constants;
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
import com.sss.controllers.Controller;

public class UserControllerImpl implements Controller {

	private static final Logger logger = Logger
			.getLogger(UserControllerImpl.class);

	private final MongoClient client;
	private final DB db;
	private final DBCollection usersCollection;
	private final DBCollection trackingCollection;

	private String lon;
	private String lat;

	private static final String OPERATOR_PUSH = "$push";
	private static final String OPERATOR_SET = "$set";

	public UserControllerImpl() {
		client = MDBClientSingleton.getInstance();
		db = client.getDB(DataBaseConstants.MOBILE_SERVICE_MDB);
		usersCollection = db.getCollection(DataBaseConstants.USERS_COLLECTION);
		trackingCollection = db
				.getCollection(DataBaseConstants.TRACKING_COLLECTION);
	}

	@Override
	public void createDocument(final MultiMap map) {
		if ((map != null) && (!map.isEmpty())) {

			String email = "";
			String password = "";
			String firstname = "";
			String lastname = "";
			if (map.contains(Constants.EMAIL)) {
				email = map.get(Constants.EMAIL);
			}
			if (map.contains(Constants.PASSWORD)) {
				password = map.get(Constants.PASSWORD);
			}
			if (map.contains(Constants.LASTNAME)) {
				lastname = map.get(Constants.LASTNAME);
			}
			if (map.contains(Constants.FIRSTNAME)) {
				firstname = map.get(Constants.FIRSTNAME);
			}

			final DBObject newUser;

			if (!firstname.isEmpty() && !lastname.isEmpty()) {
				newUser = new BasicDBObject(Constants.EMAIL, email)
						.append(Constants.PASSWORD, password)
						.append(Constants.FIRSTNAME, firstname)
						.append(Constants.LASTNAME, lastname);
			} else if (firstname.isEmpty() && !lastname.isEmpty()) {
				newUser = new BasicDBObject(Constants.EMAIL, email).append(Constants.PASSWORD,
						password).append(Constants.LASTNAME, lastname);
			} else if (!firstname.isEmpty() && lastname.isEmpty()) {
				newUser = new BasicDBObject(Constants.EMAIL, email).append(Constants.PASSWORD,
						password).append(Constants.FIRSTNAME, firstname);
			} else {
				newUser = new BasicDBObject(Constants.EMAIL, email).append(Constants.PASSWORD,
						password);
			}

			usersCollection.insert((DBObject) newUser);
		}
	}

	@Override
	public DBObject modifyDocument(final MultiMap map) {
		if (!map.contains(Constants.EMAIL)) {
			return null;
		}

		final DBObject searchQuery = new BasicDBObject();
		searchQuery.put(Constants.EMAIL, map.get(Constants.EMAIL));

		final DBObject newDocument = new BasicDBObject();
		final DBObject setDocument = new BasicDBObject();

		setDocument.put(Constants.PASSWORD, map.get(Constants.PASSWORD));
		if (map.contains(Constants.FIRSTNAME)) {
			setDocument.put(Constants.FIRSTNAME, map.get(Constants.FIRSTNAME));
		}
		if (map.contains(Constants.LASTNAME)) {
			setDocument.put(Constants.LASTNAME, map.get(Constants.LASTNAME));
		}
		if (map.contains(Constants.SEXE)) {
			setDocument.put(Constants.SEXE, map.get(Constants.SEXE));
		}
		if (map.contains(Constants.ADDRESS)) {
			setDocument.put(Constants.ADDRESS, map.get(Constants.ADDRESS));
		}
		newDocument.put(OPERATOR_SET, setDocument);

		usersCollection.update(searchQuery, newDocument);
		return find(searchQuery, 0);
	}

	@Override
	public void deleteDocument(final MultiMap map) {
		// TODO Auto-generated method stub

	}

	@Override
	public DBObject findDocument(final String search, final int limit) {

		DBObject query = new BasicDBObject();
		if ((search != null) && (!search.isEmpty())) {
			query = createFindingQuery(search);
		}

		final BasicDBList dbObjectList = find(query, limit);

		return dbObjectList;

	}

	/**
	 * Method that just return the BasicDBList result of the query
	 */
	private BasicDBList find(final DBObject query, final int limit) {
		final DBCursor cursor;
		if (limit != 0) {
			cursor = usersCollection.find(query).limit(limit);
		} else {
			cursor = usersCollection.find(query);
		}

		final BasicDBList dbObjectList = new BasicDBList();
		while (cursor.hasNext()) {
			dbObjectList.add(cursor.next());
		}
		return dbObjectList;
	}

	/**
	 * create the finding query
	 * 
	 * @param search
	 * @return
	 */
	private DBObject createFindingQuery(String search) {

		try {
			search = URLDecoder.decode(search, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(
					"Error while decoding the search URL : " + e.getMessage(),
					e);
		}

		final Map<String, String> map = Splitter.on("&")
				.withKeyValueSeparator("=").split(search);

		lon = map.get(Constants.LON);
		lat = map.get(Constants.LAT);

		return new BasicDBObject(Constants.EMAIL, map.get(Constants.EMAIL));
	}
	
	/******************
	 * TRACKING PART
	 ******************/
	
	/**
	 * update the connection time for a user that we find in database because of
	 * the string search where is the email
	 * @param email
	 */
	public void addUserTracking(final String email) {

		final DBObject newUserObjectTracking = new BasicDBObject();
		newUserObjectTracking.put(Constants.EMAIL, email);

		trackingCollection.insert(newUserObjectTracking);
	}
	
	/**
	 * update the connection time for a user that we find in database because of
	 * the string search where is the email
	 * 
	 * @param search
	 * @param date
	 */
	public void updateUserTracking(final String search, final Date date) {
		DBObject query = new BasicDBObject();
		if ((search != null) && (!search.isEmpty())) {
			query = createFindingQuery(search);
		}
		final BasicDBList list = new BasicDBList();
		list.add(lon);
		list.add(lat);
		final DBObject trackingObject = trackingCollection.findOne(query);
		final DBObject trackingPart = new BasicDBObject(Constants.CONNECTIONS,
				new BasicDBObject(Constants.TIME, date).append(Constants.LOC, list));
		final DBObject pushPart = new BasicDBObject(OPERATOR_PUSH, trackingPart);

		trackingCollection.update(trackingObject, pushPart);
	}
	
}
