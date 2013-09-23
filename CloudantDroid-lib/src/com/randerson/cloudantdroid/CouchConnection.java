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

import java.io.BufferedInputStream;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

public final class CouchConnection {
	
	// default CouchDB method for making a post to the cloudant account
	public static String[] post(UsernamePasswordCredentials creds, String url, String jsonString)
	{
		// HTTP Response inner objects
		String statusText = "";
		String responseHeaders = "";
		String responseText = "";
		
			try {

				// create the default http client object
				DefaultHttpClient connection = new DefaultHttpClient();
				
				// get the http client's connection manager
				ClientConnectionManager conManager = connection.getConnectionManager();
				
				// get the http client's credentials provider
				CredentialsProvider credProvider = connection.getCredentialsProvider();
				
				// set the credentials to the provider
				credProvider.setCredentials(AuthScope.ANY, creds);
				
				// create the http posting object
				HttpPost postRequest = new HttpPost(url);
				
				// create a http string entity to pass the json string 
				StringEntity jsonEntity = new StringEntity(jsonString);
				
				// pass the http string entity to the post object
				postRequest.setEntity(jsonEntity);
				
				// set the post object headers
				postRequest.setHeader("Accept", "application/json");
				postRequest.setHeader("Content-type", "application/json");
				
				// close the previous get request connection
				conManager.closeExpiredConnections();
				
				// make the post request to the connection and capture the http response
			    HttpResponse handler = connection.execute(postRequest);
			    
			    // capture the response headers
			    Header[] headers = handler.getAllHeaders();
			    
			    // iterate through the returned headers and log them out
			    for (int i = 0; i < headers.length; i++)
			    {
			    	// set the stringed header to the responseHeader
			    	responseHeaders += headers[i].toString() + "\n";
			    }
			    
			    // log the headers
		    	Log.i("Response Header", responseHeaders);
		    	
			    // set the status code to the statusText object
			    statusText = handler.getStatusLine().toString();
			    
			    // log out the status code and message
			    Log.i("Status Code", handler.getStatusLine().toString());
			    
			    // retrieve the response entity
			    HttpEntity entity = handler.getEntity();
				
				// creates a new buffer input stream with the URL connection object
				BufferedInputStream bfStream = new BufferedInputStream(entity.getContent());
				
				// create a new byte array
				byte[] contentBytes = new byte[1024];
				
				// integer to handle the number of bytes read
				int readBytes = 0;
				
				// create a new string buffer object
				StringBuffer buffString = new StringBuffer();
				
				// loop to handle the appending of the string content to buffer
				while ((readBytes = bfStream.read(contentBytes)) != -1)
				{
					// set the response string to the string content read
					responseText = new String(contentBytes, 0, readBytes);
					
					// appends the new string content to the buffer string object
					buffString.append(responseText);
				}
				
				// set the responseText to the full buffered string content in buffer
				responseText = buffString.toString();
				
				// log out the status code and message
			    Log.i("Response Text", responseText);
				
			} catch (IOException e)
			{
				Log.e("URL POST ERROR", "Server failed to respond to output request");
				e.printStackTrace();
			} catch (Exception e) {
				Log.e("EXCEPTION", "Unexpected exception");
				e.printStackTrace();
			}
	
		// return the response object
		return new String[] {statusText, responseHeaders, responseText};
	}
	
	//  CouchDB method for making a get request to the cloudant account
	public static String[] get(UsernamePasswordCredentials creds, String url)
	{
		// HTTP Response inner objects
		String statusText = "";
		String responseHeaders = "";
		String responseText = "";
	
			try {
					// create the default http client object
					DefaultHttpClient connection = new DefaultHttpClient();
					
					// get the http client's connection manager
					ClientConnectionManager conManager = connection.getConnectionManager();
					
					// get the http client's credentials provider
					CredentialsProvider credProvider = connection.getCredentialsProvider();
					
					// set the credentials to the provider
					credProvider.setCredentials(AuthScope.ANY, creds);
					
					// create the http posting object
					HttpGet getRequest = new HttpGet(url);
					
					// set the post object headers
					getRequest.setHeader("Accept", "application/json");
					getRequest.setHeader("Content-type", "application/json");
					
					// close the previous get request connection
					conManager.closeExpiredConnections();
					
					// make the post request to the connection and capture the http response
				    HttpResponse handler = connection.execute(getRequest);
				    
				    // capture the response headers
				    Header[] headers = handler.getAllHeaders();
				    
				    // iterate through the returned headers and log them out
				    for (int i = 0; i < headers.length; i++)
				    {
				    	// set the stringed header to the responseHeader
				    	responseHeaders += headers[i].toString() + "\n";
				    }
				    
				    // log the headers
			    	Log.i("Response Header", responseHeaders);
				    
				    // set the status code to the statusText object
				    statusText = handler.getStatusLine().toString();
				    
				    // log out the status code and message
				    Log.i("Status Code", handler.getStatusLine().toString());
					
				    // retrieve the response entity
					HttpEntity entity = handler.getEntity();
					
					// creates a new buffer input stream with the URL connection object
					BufferedInputStream bfStream = new BufferedInputStream(entity.getContent());
					
					// create a new byte array
					byte[] contentBytes = new byte[1024];
					
					// integer to handle the number of bytes read
					int readBytes = 0;
					
					// create a new string buffer object
					StringBuffer buffString = new StringBuffer();
					
					// loop to handle the appending of the string content to buffer
					while ((readBytes = bfStream.read(contentBytes)) != -1)
					{
						// set the response string to the string content read
						responseText = new String(contentBytes, 0, readBytes);
						
						// appends the new string content to the buffer string object
						buffString.append(responseText);
					}
					
					// set the responseText to the full buffered string content in buffer
					responseText = buffString.toString();
					
					// log out the status code and message
				    Log.i("Response Text", responseText);
					
				} catch (IOException e) {
					Log.e("URL RESPONSE ERROR", "Server failed to respond to request");
					e.printStackTrace();
				} catch (Exception e) {
					Log.e("EXCEPTION", "Unexpected exception");
					e.printStackTrace();
				}
			
		// return the response object
		return new String[] {statusText, responseHeaders, responseText};
	}
	
	// session method for making a delete request to Cloudant
	public static String[] delete(UsernamePasswordCredentials creds, String url)
	{
		// HTTP Response inner objects
		String statusText = "";
		String responseHeaders = "";
		String responseText = "";
		
			try {

				// create the default http client object
				DefaultHttpClient connection = new DefaultHttpClient();
				
				// get the http client's connection manager
				ClientConnectionManager conManager = connection.getConnectionManager();
				
				// get the http client's credentials provider
				CredentialsProvider credProvider = connection.getCredentialsProvider();
				
				// set the credentials to the provider
				credProvider.setCredentials(AuthScope.ANY, creds);
				
				// create the http putting object
				HttpDelete deleteRequest = new HttpDelete(url);
				
				// set the put object headers
				deleteRequest.setHeader("Accept", "application/json");
				deleteRequest.setHeader("Content-type", "application/json");
				
				// close the previous get request connection
				conManager.closeExpiredConnections();
				
				// make the put request to the connection and capture the http response
			    HttpResponse handler = connection.execute(deleteRequest);
			    
			    // capture the response headers
			    Header[] headers = handler.getAllHeaders();
			    
			    // iterate through the returned headers and log them out
			    for (int i = 0; i < headers.length; i++)
			    {
			    	// set the stringed header to the responseHeader
			    	responseHeaders += headers[i].toString() + "\n";
			    }
			    
			    // log the headers
		    	Log.i("Response Header", responseHeaders);
			    
			    // set the status code to the statusText object
			    statusText = handler.getStatusLine().toString();
			    
			    // log out the status code and message
			    Log.i("Status Code", handler.getStatusLine().toString());
			    
			    // retrieve the response entity
			    HttpEntity entity = handler.getEntity();
				
				// creates a new buffer input stream with the URL connection object
				BufferedInputStream bfStream = new BufferedInputStream(entity.getContent());
				
				// create a new byte array
				byte[] contentBytes = new byte[1024];
				
				// integer to handle the number of bytes read
				int readBytes = 0;
				
				// create a new string buffer object
				StringBuffer buffString = new StringBuffer();
				
				// loop to handle the appending of the string content to buffer
				while ((readBytes = bfStream.read(contentBytes)) != -1)
				{
					// set the response string to the string content read
					responseText = new String(contentBytes, 0, readBytes);
					
					// appends the new string content to the buffer string object
					buffString.append(responseText);
				}
				
				// set the responseText to the full buffered string content in buffer
				responseText = buffString.toString();
				
				// log out the status code and message
			    Log.i("Response Text", responseText);
				
			} catch (IOException e)
			{
				Log.e("URL DELETE ERROR", "Server failed to respond to output request");
				e.printStackTrace();
			} catch (Exception e) {
				Log.e("EXCEPTION", "Unexpected exception");
				e.printStackTrace();
			}
	
		// return the response object
		return new String[] {statusText, responseHeaders, responseText};
	}
	
	// default session method for making a put request to Cloudant
	public static String[] put(UsernamePasswordCredentials creds, String url, String jsonString)
	{
		// HTTP Response inner objects
		String statusText = "";
		String responseHeaders = "";
		String responseText = "";
		
			try {

				// create the default http client object
				DefaultHttpClient connection = new DefaultHttpClient();
				
				// get the http client's connection manager
				ClientConnectionManager conManager = connection.getConnectionManager();
				
				// get the http client's credentials provider
				CredentialsProvider credProvider = connection.getCredentialsProvider();
				
				// set the credentials to the provider
				credProvider.setCredentials(AuthScope.ANY, creds);
				
				// create the http putting object
				HttpPut putRequest = new HttpPut(url);
				
				// check if there is json to send
				if (jsonString != null)
				{
					// create a http string entity to pass the json string 
					StringEntity jsonEntity = new StringEntity(jsonString);
					
					// pass the http string entity to the post object
					putRequest.setEntity(jsonEntity);
				}
				
				// set the put object headers
				putRequest.setHeader("Accept", "application/json");
				putRequest.setHeader("Content-type", "application/json");
				
				// close the previous get request connection
				conManager.closeExpiredConnections();
				
				// make the put request to the connection and capture the http response
			    HttpResponse handler = connection.execute(putRequest);
			    
			    // capture the response headers
			    Header[] headers = handler.getAllHeaders();
			    
			    // iterate through the returned headers and log them out
			    for (int i = 0; i < headers.length; i++)
			    {
			    	// set the stringed header to the responseHeader
			    	responseHeaders += headers[i].toString() + "\n";
			    }
			    
			    // log the headers
		    	Log.i("Response Header", responseHeaders);
			    
			    // set the status code to the statusText object
			    statusText = handler.getStatusLine().toString();
			    
			    // log out the status code and message
			    Log.i("Status Code", handler.getStatusLine().toString());
			    
			    // retrieve the response entity
			    HttpEntity entity = handler.getEntity();
				
				// creates a new buffer input stream with the URL connection object
				BufferedInputStream bfStream = new BufferedInputStream(entity.getContent());
				
				// create a new byte array
				byte[] contentBytes = new byte[1024];
				
				// integer to handle the number of bytes read
				int readBytes = 0;
				
				// create a new string buffer object
				StringBuffer buffString = new StringBuffer();
				
				// loop to handle the appending of the string content to buffer
				while ((readBytes = bfStream.read(contentBytes)) != -1)
				{
					// set the response string to the string content read
					responseText = new String(contentBytes, 0, readBytes);
					
					// appends the new string content to the buffer string object
					buffString.append(responseText);
				}
				
				// set the responseText to the full buffered string content in buffer
				responseText = buffString.toString();
				
				// log out the status code and message
			    Log.i("Response Text", responseText);
			    
				
			} catch (IOException e)
			{
				Log.e("URL PUT ERROR", "Server failed to respond to output request");
				e.printStackTrace();
			} catch (Exception e) {
				Log.e("EXCEPTION", "Unexpected exception");
				e.printStackTrace();
			}
	
		// return the response object
		return new String[] {statusText, responseHeaders, responseText};
	}

}
