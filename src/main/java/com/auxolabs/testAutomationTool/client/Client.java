package com.auxolabs.testAutomationTool.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Client {
    private static HashMap<String,Object> responseData;
    public Client(){
        responseData = new HashMap<String, Object>();
    }

    public HashMap<String, Object> getResponseData() {
        return responseData;
    }

    private static Object getArray(Object object2) throws ParseException {

        JSONArray jsonArr = (JSONArray) object2;

        for (int k = 0; k < jsonArr.length(); k++) {

            if (jsonArr.get(k) instanceof JSONObject) {
                parseJson((JSONObject) jsonArr.get(k));
            } else {
                System.out.println(jsonArr.get(k));
                return jsonArr.get(k);
            }
        }
        return null;
    }

    private static HashMap<String,Object> parseJson(JSONObject jsonObject) throws ParseException {
        Iterator<Object> iterator = jsonObject.keys();
        while (iterator.hasNext()){
            Object value = iterator.next();
            if (value instanceof JSONArray){
                System.out.println(value.toString());
                responseData.put(value.toString(),getArray(jsonObject.get((String) value)));
            }
            else {
                if (jsonObject.get((String) value) instanceof JSONObject){
                    parseJson((JSONObject) jsonObject.get((String)value));
                }
                else {
                    responseData.put(value.toString(),jsonObject.get((String) value));
                    System.out.println(value.toString() + " : " + jsonObject.get((String) value));
                }
            }
        }
        System.out.println("Response data : "+ responseData);
        return responseData;
    }

    public HashMap<String,Object> getResponse(String url, Map<String,Object> requestParams) throws UnirestException, ParseException {

        String key = requestParams.keySet().toArray()[0].toString();
        String value = requestParams.get(key).toString();
        HttpResponse<JsonNode> tagResponse = Unirest.get(url).
                header("accept", "application/json").
                routeParam(key,value).
                asJson();
        JSONObject jsonObject = tagResponse.getBody().getObject();
        return parseJson(jsonObject);
    }
}
