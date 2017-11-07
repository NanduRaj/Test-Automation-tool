package com.auxolabs.testAutomationTool;

import com.auxolabs.testAutomationTool.configuration.TestAutomationToolConfiguration;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MongoConnector {
    private MongoDatabase database;
    public MongoConnector(TestAutomationToolConfiguration configuration){
        MongoClient mongoClient = new MongoClient(configuration.getMongoHost(),configuration.getMongoPort());
        database = mongoClient.getDatabase(configuration.getMongoDatabase());
    }

    public void insert(String collectionName, Document toInsert){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(toInsert);
    }

    public long delete(String collectionName, Document toDelete){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        DeleteResult deleteResult = collection.deleteOne(toDelete);
        return deleteResult.getDeletedCount();
    }

    public Document getDocument(String collectionName,Document query){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        FindIterable<Document> iterDoc = collection.find(query);
        Iterator it = iterDoc.iterator();
        if(it.hasNext())
            return (Document)it.next();
        return null;
    }

    public void update(String collectionName, Document query, Document updated) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        UpdateResult updateResult = null;
        try{
            updateResult = collection.updateMany(query, updated);
        }catch (Exception e){
            System.out.println("couldnt update");
        }
        if (updateResult != null) {
            updateResult.getMatchedCount();
        }
    }

    public List<Document> getAllDocuments(String collectionName){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        MongoCursor<Document> cursor = collection.find().iterator();
        List<Document> allDocuments = new ArrayList<Document>();
        try {
            while (cursor.hasNext()) {
                allDocuments.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return allDocuments;
    }
}
