package com.example.maaster.itp939juniorproject;

import android.util.Log;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;

/**
 * Created by Administrator on 14/10/2559.
 */
public class MongoDBConnection {

    private String url;
    private String document;
    private String dbName;
    private DBCursor cursor;
    private MongoClient mongoClient;

    public MongoDBConnection(String url, String document, String dbName) {
        this.url = url;
        this.document = document;
        this.dbName = dbName;

        try {
            mongoClient = new MongoClient(new MongoClientURI(url));

        DB db = mongoClient.getDB(dbName);
        DBCollection dbCollection = db.getCollection(document);
        cursor = dbCollection.find();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public DBCursor getCursor() {
        return cursor;
    }

    public void closeDB() {
        mongoClient.close();
    }
}
