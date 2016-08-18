package com.sss.controllers.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.sss.constants.Constants;
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
import com.sss.mdbc.MDBClientSingleton;

public class AnnounceControllerImpl implements Controller {

	private static final Logger logger = Logger
			.getLogger(AnnounceControllerImpl.class);

	private final MongoClient client;
	private final DB db;
	private final DBCollection adCollection;
	private final DBCollection trackingCollection;

	private static final String OPERATOR_OR = "$or";
	private static final String OPERATOR_SET = "$set";
	private static final String OPERATOR_PUSH = "$push";

	public AnnounceControllerImpl() {
		client = MDBClientSingleton.getInstance();
		db = client.getDB(DataBaseConstants.MOBILE_SERVICE_MDB);
		adCollection = db
				.getCollection(DataBaseConstants.ADS_COLLECTION);
		trackingCollection = db
				.getCollection(DataBaseConstants.TRACKING_COLLECTION);
	}

	@Override
	public void createDocument(final MultiMap map) {

		if ((map != null) && (!map.isEmpty())) {

			DBObject newAnnounce = new BasicDBObject();
			if (map.contains(Constants.TITLE)) {
				String title = map.get(Constants.TITLE);
				newAnnounce.put(Constants.TITLE, title);
			}
			if (map.contains(Constants.DESCRIPTION)) {
				String description = map.get(Constants.DESCRIPTION);
				newAnnounce.put(Constants.DESCRIPTION, description);
			}
			if (map.contains(Constants.PRICE)) {
				String price = map.get(Constants.PRICE);
				newAnnounce.put(Constants.PRICE, price);
			}

			if (map.contains(Constants.CATEGORIES)) {
				final BasicDBList categoryList = putCategoriesIntoMDBArray(map);
				newAnnounce.put(Constants.CATEGORIES, categoryList);
			}

			final DBObject owner = new BasicDBObject();
			owner.put(Constants.EMAIL, map.get(Constants.OWNER_EMAIL));

			if (map.contains(Constants.OWNER_FIRSTNAME)) {
				owner.put("firstname", map.get(Constants.OWNER_FIRSTNAME));
			}
			if (map.contains(Constants.OWNER_LASTNAME)) {
				owner.put("lastname", map.get(Constants.OWNER_LASTNAME));
			}

			final DBObject operatingPlace = new BasicDBObject();
			operatingPlace.put(Constants.CITY, map.get(Constants.OPERATINGPLACE_CITY));
			operatingPlace.put(Constants.ZIP, map.get(Constants.OPERATINGPLACE_ZIP));

			if (map.contains(Constants.OPERATINGPLACE_ADDRESS)) {
				operatingPlace.put(Constants.ADDRESS, map.get(Constants.OPERATINGPLACE_ADDRESS));
			}
			if (map.contains(Constants.OPERATINGPLACE_PERIMETER)) {
				operatingPlace.put(Constants.PERIMETER, map.get(Constants.OPERATINGPLACE_PERIMETER));
			}
			if (map.contains(Constants.OPERATINGPLACE_REMOTESERVICE)) {
				operatingPlace.put(Constants.REMOTESERVICE, map.get(Constants.OPERATINGPLACE_REMOTESERVICE));
			}

			newAnnounce.put("owner", owner);
			newAnnounce.put("operatingPlace", operatingPlace);

			adCollection.insert(newAnnounce);
		}

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

		DBCursor cursor = adCollection.find(query);

		BasicDBList dbObjectList = new BasicDBList();
		while (cursor.hasNext()) {
			dbObjectList.add(cursor.next());
		}

		return dbObjectList;
	}

	@Override
	public DBObject modifyDocument(final MultiMap map) {

		if (!map.contains(Constants.TITLE) ||
				!map.contains(Constants.OWNER_EMAIL)) {
			return null;
		}

		final DBObject searchQuery = new BasicDBObject();
		searchQuery.put(Constants.TITLE, map.get(Constants.TITLE));
		searchQuery.put("owner.email", map.get(Constants.OWNER_EMAIL));

		final DBObject newDocument = new BasicDBObject();
		final DBObject setDocument = new BasicDBObject();

		if (map.contains(Constants.DESCRIPTION)) {
			setDocument.put(Constants.DESCRIPTION,
								map.get(Constants.DESCRIPTION));
		}
		if (map.contains(Constants.PRICE)) {
			setDocument.put(Constants.PRICE,
								map.get(Constants.PRICE));
		}
		if (map.contains(Constants.CATEGORIES)) {
			final BasicDBList categoryList = putCategoriesIntoMDBArray(map);
			setDocument.put(Constants.CATEGORIES, categoryList);
		}

		newDocument.put(OPERATOR_SET, setDocument);
		adCollection.update(searchQuery, newDocument);

		return adCollection.findOne(searchQuery);
	}
	
	@Override
	public void deleteDocument(final MultiMap map) {
		final DBObject docToRemove = new BasicDBObject(Constants.TITLE,
												map.get(Constants.TITLE));

		if (map.get(Constants.DESCRIPTION) != null
				&& !map.get(Constants.DESCRIPTION).isEmpty()) {
			docToRemove.put(Constants.DESCRIPTION,
								map.get(Constants.DESCRIPTION));
		}

		if (map.get(Constants.OWNER_EMAIL) != null
				&& !map.get(Constants.OWNER_EMAIL).isEmpty()) {
			docToRemove.put("owner.email", map.get(Constants.OWNER_EMAIL));
		}

		adCollection.remove(docToRemove);
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

		if (map.containsKey("query")) {
			final Pattern query = Pattern.compile(".*".concat(map.get("query"))
					.concat(".*"), Pattern.CASE_INSENSITIVE);
			List<BasicDBObject> list = new ArrayList<>();
			list.add(new BasicDBObject(Constants.TITLE, query));
			list.add(new BasicDBObject(Constants.DESCRIPTION, query));
			list.add(new BasicDBObject(Constants.CATEGORIES, query));
			list.add(new BasicDBObject("owner.email", query));
			list.add(new BasicDBObject("owner.firstname", query));
			list.add(new BasicDBObject("owner.lastname", query));

			return new BasicDBObject(OPERATOR_OR, list);
		}

		return new BasicDBObject(map);
	}

	/**
	 * factorization of used code
	 * 
	 * @param map
	 * @return
	 */
	private BasicDBList putCategoriesIntoMDBArray(final MultiMap map) {
		final String categories = map.get(Constants.CATEGORIES);
		final BasicDBList categoryList = new BasicDBList();
		categoryList.addAll(Splitter.on(";").splitToList(categories));
		return categoryList;
	}

	/******************
	 * TRACKING PART
	 ******************/
	
	/**
	 * 
	 * @param mail
	 * @param date
	 * @param action
	 */
	public void updateAnnounceTracking(final String mail, final String announceID,
			final String lon, final String lat, final Date date,
			final String action) {
		final DBObject query = new BasicDBObject();
		query.put(Constants.EMAIL, mail);
		final DBObject trackingObject = trackingCollection.findOne(query);
		
		final BasicDBList list = new BasicDBList();
		list.add(lon);
		list.add(lat);

		final DBObject myAnnounceActivityObject = new BasicDBObject();
		myAnnounceActivityObject.put("time", date);
		myAnnounceActivityObject.put("loc", list);
		myAnnounceActivityObject.put("action", action);
		myAnnounceActivityObject.put("announce_id", announceID);

		final DBObject trackingPart = new BasicDBObject(
				"my_announces_activity", myAnnounceActivityObject);

		final DBObject pushPart = new BasicDBObject(OPERATOR_PUSH, trackingPart);

		trackingCollection.update(trackingObject, pushPart);
	}

	/**
	 * Add the add tracking into collection tracking
	 * @param mail
	 * @param date
	 * @param lon 
	 * @param lat
	 * @param action
	 */
	public void announceTracking(final String mail, final String announceTitle,
			final String lon, final String lat, final Date date,
			final String action) {

		// get the document to modify with the corresponding email of the user
		final DBObject query = new BasicDBObject();
		query.put(Constants.EMAIL, mail);

		final DBObject trackingObject = trackingCollection.findOne(query);

		// create the tab of longitude and latitude
		final BasicDBList list = new BasicDBList();
		list.add(lon);
		list.add(lat);

		// create the second part of the request that will be used in the first part
		final DBObject announceObject = adCollection
				.findOne(new BasicDBObject("owner.email", mail).append(Constants.TITLE,
						announceTitle));

		final DBObject myAnnounceActivityObject = new BasicDBObject();
		myAnnounceActivityObject.put("time", date);
		myAnnounceActivityObject.put("loc", list);
		myAnnounceActivityObject.put("action", action);
		myAnnounceActivityObject.put("announce_id", announceObject.get("_id"));

		// create the final request
		final DBObject trackingPart = new BasicDBObject(
				"my_announces_activity", myAnnounceActivityObject);

		final DBObject pushPart = new BasicDBObject(OPERATOR_PUSH, trackingPart);

		trackingCollection.update(trackingObject, pushPart);
	}
}