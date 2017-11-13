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

    /**
     * function to insert a document to a collection in a database
     * @param collectionName, collection in which document to be inserted
     * @param toInsert, document to be inserted
     */
    public void insert(String collectionName, Document toInsert){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(toInsert);
    }

    /**
     * function to delete a document in a collection from a database
     * @param collectionName, the collection where document to be deleted
     * @param toDelete,,document to be deletes
     * @return count of documents deleted
     */
    public long delete(String collectionName, Document toDelete){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        DeleteResult deleteResult = collection.deleteOne(toDelete);
        return deleteResult.getDeletedCount();
    }

    /**
     * function to fetch a document from a collection in a database
     * @param collectionName,the collection from which the document is to be fetched
     * @param query, the query document which matches the document to be found
     * @return document fetched
     */
    public Document getDocument(String collectionName,Document query){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        FindIterable<Document> iterDoc = collection.find(query);
        Iterator it = iterDoc.iterator();
        if(it.hasNext())
            return (Document)it.next();
        return null;
    }

    /**
     * function to update a document
     * @param collectionName, the collection in which the document is to be updated
     * @param query, the query document which matches the document to be found
     * @param updated, the updated document
     */
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

    /**
     * function to get all documents in a collection
     * @param collectionName, the collection from which documents to be found
     * @return list of the documents present in the collection
     */
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
