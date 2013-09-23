/*
 * project 		CloudantDroid
 * 
 * package 		com.randerson.couchdroid
 * 
 * @author 		Rueben Anderson
 * 
 * date			Sep 15, 2013
 * 
 */

/*
 	This file is part of CloudantDroid.
 	
 	CloudantDroid is a library for communicating with Cloudant (couchDB) servers
    Copyright (C) 2013  Rueben Anderson

    CloudantDroid is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.randerson.cloudantdroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public final class CouchJSONCenter {

	// method for creating a new base couch JSON object with user supplied id
	public static JSONObject getNewObject(String id)
	{
		// create a new JSON object
		JSONObject couchObject = new JSONObject();
		
		// set the id field
		try {
			couchObject.accumulate("_id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return couchObject;
	}
	
	// method for creating a new base couch object for updating
	public static JSONObject getUpdateObject(String id, String rev)
	{
		// create a new JSON object
		JSONObject couchObject = new JSONObject();
		
		// set the id field
		try {
			couchObject.accumulate("_id", id);
			couchObject.accumulate("_rev", rev);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return couchObject;
	}
	
	// method for creating a new couch deletion object
	public static JSONObject getDeleteObject(String rev)
	{
		// create a new JSON object
		JSONObject couchObject = new JSONObject();
		
		// set the id field
		try {
			couchObject.accumulate("_rev", rev);
			couchObject.accumulate("_deleted", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return couchObject;
	}
	
	// method for adding a new json field and string value
	public static JSONObject addObjectField(JSONObject json, String key, String value)
	{
		// add the key / value pair to the JSON object
		// replacing any duplicate fields
		try {
			json.accumulate(key, value);
		} catch (JSONException e) {
			Log.e("JSON EXCEPTION", "Exception adding key/value pair to JSON");
			e.printStackTrace();
		}
		
		return json;
	}
		
	// method for adding a new json field and integer value
	public static JSONObject addObjectField(JSONObject json, String key, int value)
	{
		// add the key / value pair to the JSON object
		// replacing any duplicate fields
		try {
			json.accumulate(key, value);
		} catch (JSONException e) {
			Log.e("JSON EXCEPTION", "Exception adding key/value pair to JSON");
			e.printStackTrace();
		}
		
		return json;
	}
	
	// method for adding a new json field and float value
	public static JSONObject addObjectField(JSONObject json, String key, float value)
	{
		// add the key / value pair to the JSON object
		// replacing any duplicate fields
		try {
			json.accumulate(key, value);
		} catch (JSONException e) {
			Log.e("JSON EXCEPTION", "Exception adding key/value pair to JSON");
			e.printStackTrace();
		}
		
		return json;
	}
	
	// method for adding a new json field and boolean value
	public static JSONObject addObjectField(JSONObject json, String key, boolean value)
	{
		// add the key / value pair to the JSON object
		// replacing any duplicate fields
		try {
			json.accumulate(key, value);
		} catch (JSONException e) {
			Log.e("JSON EXCEPTION", "Exception adding key/value pair to JSON");
			e.printStackTrace();
		}
		
		return json;
	}
	
	// method for adding a new json field and json object value
	public static JSONObject addObjectField(JSONObject json, String key, JSONObject value)
	{
		// add the key / value pair to the JSON object
		// replacing any duplicate fields
		try {
			json.accumulate(key, value);
		} catch (JSONException e) {
			Log.e("JSON EXCEPTION", "Exception adding key/value pair to JSON");
			e.printStackTrace();
		}
		
		return json;
	}
	
	// method for adding a new json array
	public static JSONObject addObjectField(JSONObject json, String key, JSONArray value)
	{
		// add the key / value pair to the JSON object
		// replacing any duplicate fields
		try {
			json.accumulate(key, value);
		} catch (JSONException e) {
			Log.e("JSON EXCEPTION", "Exception adding key/value pair to JSON");
			e.printStackTrace();
		}
		
		return json;
	}
	
	// method for creating a JSON object from stringified JSON
	public static JSONObject jsonifyString(String jsonString)
	{
		JSONObject json = null;
		
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			Log.e("JSON EXCEPTION", "Exception converting string into JSON object");
			e.printStackTrace();
		}
		
		return json;
	}
	
}
