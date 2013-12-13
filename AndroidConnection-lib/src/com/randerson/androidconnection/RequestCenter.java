package com.randerson.androidconnection;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class RequestCenter {
	
	
	private String URL_STRING = null;
	private UsernamePasswordCredentials CREDENTIALS = null;
	
	public RequestCenter(String username, String password, String url)
	{
		CREDENTIALS = new UsernamePasswordCredentials(username + ":" + password);
	};

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
	
}
