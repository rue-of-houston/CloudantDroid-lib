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

import org.apache.http.auth.UsernamePasswordCredentials;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public final class CouchService extends IntentService {

	// service keys
	public static final String URL_KEY = "url";
	public static final String JSON_KEY = "json";
	public static final String MESSENGER_KEY = "messenger";
	public static final String METHOD_KEY = "method";
	public static final String USER_KEY = "username";
	public static final String AUTH_KEY = "password";
	public static final String REQUEST_KEY = "request_code";
	
	// request methods
	public static final String GET = "get";
	public static final String PUT = "put";
	public static final String DELETE = "delete";
	public static final String POST = "post";
	
	// CouchDB credentials object
	private UsernamePasswordCredentials CREDENTIALS;
	
	public CouchService() {
		super("CouchService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		// verify that the intent is valid
		if (intent != null)
		{
			// Response data
			String[] response = null;
			
			// Get the intent extras
			Bundle bundle = intent.getExtras();
			
			//  The Session strings
			String url = null;
			String json = null;
			String method = null;
			String username = null;
			String password = null;
			
			// Session code
			int code = -1;
			
			// Verify the objects are available and set the data
			// retrieve and set the url from the bundle
			if (bundle != null && bundle.containsKey(URL_KEY))
			{
				url = bundle.getString(URL_KEY);
			}
			
			// retrieve and set the json string from the bundle
			if (bundle != null && bundle.containsKey(JSON_KEY))
			{
				json = bundle.getString(JSON_KEY);
			}
			
			// retrieve and set the method from the bundle
			if (bundle != null && bundle.containsKey(METHOD_KEY))
			{
				method = bundle.getString(METHOD_KEY);
			}
			
			// retrieve and set the username from the bundle
			if (bundle != null && bundle.containsKey(USER_KEY))
			{
				username = bundle.getString(USER_KEY);
			}
			
			// retrieve and set the password from the bundle
			if (bundle != null && bundle.containsKey(AUTH_KEY))
			{
				password = bundle.getString(AUTH_KEY);
			}
			
			// retrieve and set the method from the bundle
			if (bundle != null && bundle.containsKey(REQUEST_KEY))
			{
				code = bundle.getInt(REQUEST_KEY);
			}
			
			// Set the session credentials
			CREDENTIALS = new UsernamePasswordCredentials(username, password);
			
			// Retrieve the messenger
			Messenger messenger = (Messenger) bundle.get(MESSENGER_KEY);
			
			// Determine which service is being requested
			// Make the request and capture the returned response object
			if (method.equals(GET))
			{
				response = CouchConnection.get(CREDENTIALS, url);
			}
			else if (method.equals(POST))
			{
				response = CouchConnection.post(CREDENTIALS, url, json);
			}
			else if (method.equals(PUT))
			{
				response = CouchConnection.put(CREDENTIALS, url, json);
			}
			else if (method.equals(DELETE))
			{
				response = CouchConnection.delete(CREDENTIALS, url);
			}
			
			// Retrieve a system message
			Message message = Message.obtain();
			
			// Set the OK result & response text
			message.arg1 = Activity.RESULT_OK;
			message.arg2 = code;
			message.obj = response;
			
			// Check that there has been a valid messenger object supplied
			if (messenger != null)
			{
				// Send the message
				try {
					messenger.send(message);
				} catch (RemoteException e) {
					Log.e("RemoteException", "Exception trying to send service return message");
					e.printStackTrace();
				}
			}
			
		}
	}

}
