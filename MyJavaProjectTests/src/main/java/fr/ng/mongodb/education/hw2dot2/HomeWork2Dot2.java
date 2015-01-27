package fr.ng.mongodb.education.hw2dot2;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class HomeWork2Dot2 {

	private static final String myStudentDB = "students";
	private static final String myCollection = "grades";
	
	public static void main(String[] args) throws UnknownHostException {
		
		final MongoClient client = new MongoClient();
		final DB db = client.getDB(myStudentDB);
		final DBCollection collection = db.getCollection(myCollection);
		
		// I want to retrieve data that have type : homework
		final DBCursor cursor = collection.find(new BasicDBObject().append("type", "homework"));
		
		// I sort them by student id and score, both lowest to highest
		cursor.sort(new BasicDBObject().append("student_id", 1).append("score", 1));

		List<DBObject> recordsToBeDeleted = new ArrayList<DBObject>();
		
		try {
			int previousRecordStudentId = -1;

			while (cursor.hasNext()) {

				DBObject record = cursor.next();
				int currentRecordStudentId = (Integer) record.get("student_id");

				if (previousRecordStudentId != currentRecordStudentId) {
					recordsToBeDeleted.add(record);
				}
				previousRecordStudentId = currentRecordStudentId;

			}
		} finally {
			cursor.close();
		}

		// I remove the students first time 
		for (DBObject record : recordsToBeDeleted) {
			collection.remove(record);
		}
	}

}
