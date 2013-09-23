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
 	This file is part of CloudantDroid
 	
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

import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public final class CouchSession {

	// User credential data
	private String USERNAME = null;
	private String PASSWORD = null;
	
	// Session context
	private Activity CONTEXT = null;
	
	// Handler messenger
	private Messenger MESSENGER = null;
	
	// Cloudant server URL
	private String COUCH_SERVER_URL;
	
	// User supplied Couch databases
	// Session space for storing and retrieving couch database names easily
	public HashMap<String, String> DATABASES = new HashMap<String, String>();
	
	// Session Handler
	private Handler HANDLER = null;
	
	// Request code
	private int REQUEST_ID = 0;
	
	// default session constructor
	// serverURL should be the default home cloudant server link
	public CouchSession(Activity context, String serverURL)
	{
		// set the singleton context
		CONTEXT = context;
	
		// set the base couch URL
		COUCH_SERVER_URL = serverURL;
		
		Log.i("NOTICE", "CloudantDroid  Copyright (C) 2013  Rueben Anderson \n" +
				"This program comes with ABSOLUTELY NO WARRANTY. \n" +
				"This is free software, and you are welcome to redistribute it \n" +
				"under certain conditions; see <http://www.gnu.org/licenses/>");
	};
	
	// session method for setting couchDB credentials
	public void setCredentials(String username, String password)
	{
		USERNAME = username;
		PASSWORD = password;
	}
	
	// session method for setting the messenger
	public void setMessenger(Messenger messenger)
	{
		MESSENGER = messenger;
	}
	
	// session method for query a database
	// a custom query can be sent by passing false for the allDocs argument
	// and sending a query url string with the parameters needed
	public void queryDB(String dbName, boolean allDocs)
	{
		// create the URL to the DB to query
		String newDBurl = COUCH_SERVER_URL + dbName;
		
		// check if the user wants all docs supplied or not
		// if not it is assumed the query methods will be supplied instead
		if (allDocs == true)
		{
			newDBurl = newDBurl + "/_all_docs";
		}
		
		// create the intent service object
		Intent request = new Intent(CONTEXT, CouchService.class);
		
		// verify that the intent is valid
		if (request != null)
		{
			request.putExtra(CouchService.METHOD_KEY, CouchService.GET);
			request.putExtra(CouchService.USER_KEY, USERNAME);
			request.putExtra(CouchService.AUTH_KEY, PASSWORD);
			request.putExtra(CouchService.MESSENGER_KEY, MESSENGER);
			request.putExtra(CouchService.URL_KEY, newDBurl);
			request.putExtra(CouchService.REQUEST_KEY, REQUEST_ID);
			
			// start the service from the application context
			getActivityInterface().startCouchService(request);
		}
	}
	
	// session method for querying a document
	public void queryDB(String dbName, String docName)
	{
		// create the URL to the DB to query
		String newDBurl = COUCH_SERVER_URL + dbName + "/" + docName;
		
		// create the intent service object
		Intent request = new Intent(CONTEXT, CouchService.class);
		
		// verify that the intent is valid
		if (request != null)
		{
			request.putExtra(CouchService.METHOD_KEY, CouchService.GET);
			request.putExtra(CouchService.USER_KEY, USERNAME);
			request.putExtra(CouchService.AUTH_KEY, PASSWORD);
			request.putExtra(CouchService.MESSENGER_KEY, MESSENGER);
			request.putExtra(CouchService.URL_KEY, newDBurl);
			request.putExtra(CouchService.REQUEST_KEY, REQUEST_ID);
			
			// start the service from the application context
			getActivityInterface().startCouchService(request);
		}
	}
	
	// session method for creating a new database
	public void createDB(String dbName)
	{
		// create the URL to the new DB to create
		String newDBurl = COUCH_SERVER_URL + dbName;
		
		// create the intent service object
		Intent request = new Intent(CONTEXT, CouchService.class);
		
		// verify that the intent is valid
		if (request != null)
		{
			request.putExtra(CouchService.METHOD_KEY, CouchService.PUT);
			request.putExtra(CouchService.USER_KEY, USERNAME);
			request.putExtra(CouchService.AUTH_KEY, PASSWORD);
			request.putExtra(CouchService.MESSENGER_KEY, MESSENGER);
			request.putExtra(CouchService.URL_KEY, newDBurl);
			request.putExtra(CouchService.REQUEST_KEY, REQUEST_ID);
			
			// start the service from the application context
			getActivityInterface().startCouchService(request);
		}
	}
	
	// session method for deleting a database
	public void deleteDB(String dbName)
	{
		// create the URL to the DB to delete
		String deleteDBurl = COUCH_SERVER_URL + dbName;
		
		// create the intent service object
		Intent request = new Intent(CONTEXT, CouchService.class);
		
		// verify that the intent is valid
		if (request != null)
		{
			request.putExtra(CouchService.METHOD_KEY, CouchService.DELETE);
			request.putExtra(CouchService.USER_KEY, USERNAME);
			request.putExtra(CouchService.AUTH_KEY, PASSWORD);
			request.putExtra(CouchService.MESSENGER_KEY, MESSENGER);
			request.putExtra(CouchService.URL_KEY, deleteDBurl);
			request.putExtra(CouchService.REQUEST_KEY, REQUEST_ID);
			
			// start the service from the application context
			getActivityInterface().startCouchService(request);
		}
	}
	
	// session method for creating or updating a document
	// when updating a document the "_rev" and "_id" fields of the document to update must be supplied
	public void createUpdateDocument(String dbName, JSONObject data)
	{
		// create the URL to the DB to delete
		String createDocUrl = COUCH_SERVER_URL + dbName;
		
		// create the intent service object
		Intent request = new Intent(CONTEXT, CouchService.class);
		
		// verify that the intent is valid
		if (request != null)
		{
			request.putExtra(CouchService.METHOD_KEY, CouchService.POST);
			request.putExtra(CouchService.USER_KEY, USERNAME);
			request.putExtra(CouchService.AUTH_KEY, PASSWORD);
			request.putExtra(CouchService.MESSENGER_KEY, MESSENGER);
			request.putExtra(CouchService.URL_KEY, createDocUrl);
			request.putExtra(CouchService.JSON_KEY, data.toString());
			request.putExtra(CouchService.REQUEST_KEY, REQUEST_ID);
			
			// start the service from the application context
			getActivityInterface().startCouchService(request);
		}
	}
	
	// session method for deleting a document
	public void deleteDocument(String docName, String dbName, JSONObject data)
	{
		// create the URL to the DB to delete
		String deleteDocUrl = COUCH_SERVER_URL + dbName + "/" + docName;
		
		// create the intent service object
		Intent request = new Intent(CONTEXT, CouchService.class);
		
		// verify that the intent is valid
		if (request != null)
		{
			request.putExtra(CouchService.METHOD_KEY, CouchService.PUT);
			request.putExtra(CouchService.USER_KEY, USERNAME);
			request.putExtra(CouchService.AUTH_KEY, PASSWORD);
			request.putExtra(CouchService.MESSENGER_KEY, MESSENGER);
			request.putExtra(CouchService.URL_KEY, deleteDocUrl);
			request.putExtra(CouchService.JSON_KEY, data.toString());
			request.putExtra(CouchService.REQUEST_KEY, REQUEST_ID);
			
			// start the service from the application context
			getActivityInterface().startCouchService(request);
		}
	}
	
	// singleton method for accessing the parent activity method for starting couch service
	private CouchSessionInterface getActivityInterface()
	{
		// create a null instance of the interface object
		CouchSessionInterface sessionInterface = null;
		
		// verify that the context is implementing the interface
		if (CONTEXT instanceof CouchSessionInterface)
		{
			sessionInterface = (CouchSessionInterface) CONTEXT;
		}
		else
		{
			// throw exception
			throw new ClassCastException(CONTEXT.toString() + " must implement required methods");
		}
		
		return sessionInterface;
	}
	
	// method for allowing for the use of the built-in session handling
	// removing the need for supplying a handler and messenger object
	@SuppressLint("HandlerLeak")
	public void useSessionHandler()
	{
		
		// create a new handler object for the couch session
		HANDLER = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// verify that the handled message is returned successfully
				if (msg.arg1 == -1)
				{
					// receive the msg object and assign its contents to the
					// response object for processing
					String[] response = (String[]) msg.obj;
					
					// pass the response object to the parent activity
					getActivityInterface().returnServiceResponse(response, msg.arg2);
				}
			}
		};
		
		// setup the messenger object for using the handler
		MESSENGER = new Messenger(HANDLER);

	}
	
	// method for setting the request code for the impending request
	// this code can be used to manage the returned data
	public void setRequesetID(int id)
	{
		REQUEST_ID = id;
	}
	
}
