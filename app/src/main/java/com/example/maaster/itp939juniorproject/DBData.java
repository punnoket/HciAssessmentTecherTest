package com.example.maaster.itp939juniorproject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;



/**
 * Created by Administrator on 11/10/2559.
 */
public class DBData {

    private DBCursor cursor;

    public DBData() {
        try {

            String dbURI = "mongodb://192.168.1.35:27017";
            MongoClient mongoClient = new MongoClient(new MongoClientURI(dbURI));

            DB db = mongoClient.getDB("test");
            DBCollection dbCollection = db.getCollection("Student");
            cursor = dbCollection.find();


            mongoClient.close();
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    public DBCursor getCursor() {
        return cursor;
    }
}
