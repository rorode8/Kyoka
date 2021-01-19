package Kyoka.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class MongoDB {
	
	private static MongoClient mongoClient;
	private static MongoDatabase  database;
	private static MongoCollection<Document> users;
	
	
	
	public static void connect() {
		
		String ur = DataLoader.getString("connection");
		mongoClient = MongoClients.create(ur);
		
		database = mongoClient.getDatabase("discord");
		users = database.getCollection("users");
		
		System.out.println(database.getName());
		System.out.println(users.countDocuments());		
		
		
	}
	
	public static boolean userExists(long id) {
		BsonDocument b;
		return users.find(new Document("_id",id)).cursor().hasNext();		
		
	}
	
	public static void close() {
		mongoClient.close();
		
	}
	
	public static void createDocument(long id) {
		//List<Document> docs = new ArrayList<Document>();
		users.insertOne(new Document("_id",id)
				.append("money", 0D)
				.append("objects", new ArrayList<Document>())
				.append("bag", new ArrayList<Document>())
				.append("lastUpdate", new Date()));
		
	}
	
	public static void pushIntoArray(String key ,long id, Document doc) {
		//Document chill = new Document("itemId",0).append("type", "sword");
		Document item = new Document(key, doc);
		Document update = new Document("$push", item);
		System.out.println(users.updateOne(new Document("_id",id),update));
		
		
		
		
	}
	
	public static int countItems(long id, String field) {
		List<Document> list = users.find(new Document("_id",id)).cursor().next().getList(field, Document.class);
		if(list==null) {
			return 0;
		}else {
			return list.size();
		}
		

	}
	
	public static void updateDB(long id, double inc) {
		Document doc = new Document("findAndModify","users")
				.append("query", new Document("_id", id))
				.append("sort", new Document("money",1))
				.append("update", new Document("$inc", new Document("money", inc)));
		
		System.out.println(database.runCommand(doc).toString());
		
	}
	
	public static void substract(long id, double subs) {
		Document doc = new Document("findAndModify","users")
				.append("query", new Document("_id", id))
				.append("sort", new Document("money",1))
				.append("update", new Document("$inc", new Document("money", -subs)));
		System.out.println(database.runCommand(doc).toString());
		
	}
	
	
	public static double sumArray(long id) {
		List<Document> pipeline = new ArrayList<Document>();
		pipeline.add(new Document("$match", new Document("_id", id)));
		pipeline.add(new Document("$project", new Document("total", new Document("$sum", "$bag.sell value"))));
		
		//System.out.println(users.aggregate(pipeline).cursor().next() );
		return  (double)users.aggregate(pipeline).cursor().next().getInteger("total") ;
	}
	public static void purgeArray(long id) {
		users.findOneAndUpdate(new Document("_id",id), new Document("$set", new Document("bag", new ArrayList<Document>())));
	}
	
	public static Document getDoc(long id) {
		return users.find(new Document("_id",id)).cursor().next();
	}
	
	public static List<Document> retrieveDocs(long id, String field) {

		
		for(Document fish: users.find(new Document("_id",id)).cursor().next().getList(field, Document.class)) {
			System.out.println(fish.toString());
			System.out.println(fish.getString("name"));
		}
		
		return users.find(new Document("_id",id)).cursor().next().getList(field, Document.class);
		
	}
	
	public static MongoCollection<Document> getUsers(){
		return users;
	}
	
	public static void removeFromArray(int i, long id, String field) {
		users.findOneAndUpdate(new Document("_id", id), new Document("$unset", new Document(field+"."+String.valueOf(i), 1)));
		users.findOneAndUpdate(new Document("_id", id), new Document("$pull", new Document(field, null)));
		
	}
	
	

}
