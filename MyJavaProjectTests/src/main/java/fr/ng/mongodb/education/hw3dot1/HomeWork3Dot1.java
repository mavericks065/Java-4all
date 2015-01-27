package fr.ng.mongodb.education.hw3dot1;

import java.net.UnknownHostException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class HomeWork3Dot1 {

	private static final String mySchoolDB = "school";
	private static final String myStudentCollection = "students";

	public static void main(String[] args) throws UnknownHostException {

		final MongoClient client = new MongoClient();
		final DB db = client.getDB(mySchoolDB);
		final DBCollection collection = db.getCollection(myStudentCollection);
		final DBCursor cursor = collection.find();

		try {

			while (cursor.hasNext()) {
				final DBObject currentStudent = cursor.next();
				// I get the documents within the array
				// documentation at :
				// http://api.mongodb.org/java/current/com/mongodb/BasicDBList.html
				final BasicDBList studentScores = (BasicDBList) currentStudent
						.get("scores");

				BasicDBObject homeworkToDelete = null;
				Double lowerValue = null;
				for (int i = 0; i < studentScores.size(); i++) {
					BasicDBObject currentScore = ((BasicDBObject) studentScores
							.get(i));
					if (currentScore.get("type").equals("homework")) {
						if (homeworkToDelete == null
								|| (Double) currentScore.get("score") < lowerValue) {
							lowerValue = (Double) currentScore.get("score");
							homeworkToDelete = currentScore;
						}
					}
				}

				// documentation at :
				// http://docs.mongodb.org/manual/reference/operator/update/pull/
				collection.update(currentStudent, new BasicDBObject("$pull",
						new BasicDBObject("scores", homeworkToDelete)));
			}
		} finally {
			cursor.close();
		}
	}

}
