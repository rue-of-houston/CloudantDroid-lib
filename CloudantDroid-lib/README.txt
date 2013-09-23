**********************************************************************************
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
 **********************************************************************************
 
 
 
 ABOUT THE LIBRARY
 
 CloudantDroid is a lightweight Java Android library written in Eclipse to fill 
 the gap in communicating with Cloudant (couchDB) servers from within the Android
 environment. I designed CloudantDroid to need no dependencies unlike some of the
 other Java options that are available. The library allows for all of the CRUD
 operations that Cloudant allows - Creating of databases & documents, Reading from
 the database, Updating the database & documents, and Deleting the database & documents.
 
 
 
 GETTING STARTED
 
 To get up and running with CloudantDroid there are a few steps that need to be taken.
 First, you'll need to download the library and import it into your workspace. Next
 you'll want to add the library to your application resources. In Eclipse this is done 
 by right-clicking your project: >> PROPERTIES >> ANDROID and adding it under the library
 section. CloudantDroid operates fully encapsulated couch functionality and so you must also 
 add the background service (CouchService) to your application manifest. If you want to use 
 the library's predefined network receiver you'll also need to add that receiver (CouchReceiver) 
 to your manifest file. If you don't see it in your project you will need to have the menu's 
 checkbox unchecked (the one about displaying only project services/receivers). Lastly, add the 
 uses-permissions for accessing NETWORK-STATE, and INTERNET.

 Now that the application is configured to use the library you will also need to make any
 activity that will be making requests implement the CouchSessionInterface. This will allow
 the session objects to communicate to the activity to make the requests. Next you have to add 
 the unimplemented methods. There are two methods, one for starting the service and the other 
 is for returning the service response data.

 1.
	@Override
	public void startCouchService(Intent intent)
	{
		startService(intent);
	}
	
	You will need to add the 'startService(intent)' portion and that's all you need to start 
	making requests to your cloudant account.

 2.
    @Override
	public void returnServiceResponse(String[] response, int id)
	{
		// your response handling code here
	}
	
	You can handle the response data of diverse request sessions by utilizing the request id.
	
	
	
	CAPTURING THE RESPONSES
 	
 	To get the status text:
 	
 		String status = response[0];
 		
 		This is the response status line.
 	
 	
 	To get the request headers:
 	
 		String[] headers = response[1].split("\n");
 		
 		This gets the headers string and splits it on each new line to effectively separate
 		each header into a string array.
 	
 	
 	To get the response text:
 	
 		String text = response[2];
 		
 		This is the actual returned JSON data strings.



 USING THE RECEIVER

 To use the built-in network receiver add this code to your activity:

	CouchReceiver receiver = new CouchReceiver(this);
	IntentFilter filter = CouchReceiver.getFilter();
	this.registerReceiver(receiver, filter);


 Now you can check the status boolean of the receiver when you need to verify if there is
 a network connection available like this:

	boolean isConnected = receiver.getStatus();


 You can similarly receive the type of connection like so:

	String networkType = receiver.getType();
	
	
	CouchReceiver example:
	
	if (receiver != null && receiver.getStatus() == true)
		{
			Toast toast = Toast.makeText(this, receiver.getType() + " Ready!", Toast.LENGTH_SHORT);
			
			if (toast != null)
			{
				toast.show();
			}
		}
		else
		{
			Toast toast = Toast.makeText(this, receiver.getType(), Toast.LENGTH_SHORT);
			
			if (toast != null)
			{
				toast.show();
			}
		}



 CREATING A SESSION INSTANCE

 First, you will need to create an instance of the CouchSession singleton like this:

	CouchSession session = new CouchSession(this, "https://[username].cloudant.com/");


 Next, you will have to set the session authorization credentials like this:
 (note that the credentials may also use the generated api key and api password)

	session.setCredentials([username], [password]);
	

 Lastly, if you want to receive the request response data you will need to supply a 
 valid Messenger and Handler object, to the CouchSession singleton. CloudantDroid
 provides a built in Messenger and Handler for returning the response data or you 
 may choose to use your own.



 USING THE BUILT-IN MESSENGER & HANDLER:
 	
 	session.useSessionHandler();
 	
 	This will automatically set-up a Messenger and Handler for you. Upon receiving
 	the response data it will return the response object to the parent activity via
 	the provided interface method 'returnServiceResponse()' that you added previously
 	in the set-up process.
 	
 	public void returnServiceResponse(String[] response, int id)
	{
		if (response != null)
		{
			String json = (String) response[2];
			
			Log.i("RESPONSE JSON", json);
		}
	}
 	
 	In this example the response is given a null check before it retrieves the response
 	text from the string array. Finally the response text is logged.
 	
 	

 SUPPLYING YOUR OWN MESSENGER & HANDLER:
 
	session.setMessenger(new Messenger([Handler]));

	This will set the messenger object that is part of the CouchSession class to the
	Messenger/Handler combination that you have chosen to supply.
	


 SESSION C.R.U.D OPERATIONS

 To create a new database:
	
	session.createDB([database name]);
	

 To read a database:

	session.queryDB([database name]);
	
	
 To create or update a document:

	session.createUpdateDocument([document name], [JSON object]);
	
	Supplying an id for the JSON object is optional when creating a new document
	but the JSON object must contain the document id "_id" and revision "_rev"
	field and values when updating the document. This can be accomplished easily 
	using the built in CouchJSONCenter class. See USING CouchJSONCenter below.
	Furthermore, remember when updating a document all of the fields must be supplied
	not just the fields being updated.
	
	
 To delete a document:

	session.deleteDocument([document name], [database name], [JSON object]);
	
	The JSON object must contain the document revision "_rev" field and value
	as well as the special deleted field "_deleted" with a true value. This
	can be accomplished easily using the built in CouchJSONCenter class.
	See USING CouchJSONCenter below.
	
	
 To delete a database:

	session.deleteDB([database name]);
	
	
	
 USING CouchJSONCenter CLASS

 The CouchJSONCenter is a built-in class that can be used to simplify the creation
 and handling of the JSON objects that are required with some requests when
 communicating with the Cloudant service. These methods handle the ugly try/catch
 cases that are required in Java.



 Get a new JSONObject with user-supplied id field:

	JSONObject doc = CouchJSONCenter.getNewObject([id value]);
	
	This object should be used in conjunction with the addObjectField method
	to add all of the document fields required.
	
	
	
 Add a data field to the JSONObject:
	
	CouchJSONCenter.addObjectField([JSON object], [key], [value]);
	
	The key is always a string but supported value types are 
	boolean, int, float, string, JSONObject, JSONArray
	
	example:
	doc = CouchJSONCenter.addObjectField(doc, "hp", 180);
	doc = CouchJSONCenter.addObjectField(doc, "type", "fire");
	doc = CouchJSONCenter.addObjectField(doc, "poisoned", true);
	
	
	
 Create a JSON object from a JSON string:

	CouchJSONCenter.jsonifyString([JSON string]);
	
	Use this method to convert your query strings from a JSON formatted
	string into a full fledged JSON object.
	
	
	
 Get a Couch delete object:

	CouchJSONCenter.getDeleteObject([document revision]);
	
	This object should be used when deleting a document. All required
	JSON fields will be supplied.
	
	
	
 Get a Couch update object:

	CouchJSONCenter.getUpdateObject([id value], [document revision]);
	
	This object should be used in conjunction with the addObjectField method
	to add all of the document fields. This is important because any 
	missing fields will be removed from the object. Alternatively, you can
	retrieve the current object with a get request and use the addObjectField
	method to update the fields that have been changed.
 
 
 
 MANAGING YOUR OWN REQUESTS
 
 If you need to use your own service to make customized requests you can do so
 by bypassing the use of the CouchSession object and using the CouchConnection 
 class directly within your service. This class is static and
 so you need only call the object and method you need.
 
 
 Making post requests:
 
 	CouchConnection.post([credentials], [url], [JSON string]);
 
 
 Making put requests:
 
 	CouchConnection.put([credentials], [url], [JSON string]);
 
 
 Making get requests:
 
 	CouchConnection.get([credentials], [url]);
 
 
 Making delete requests:
 
 	CouchConnection.delete([credentials], [url]);
 	
 	
 
 USING THE SESSION DATABASES OBJECT
 
 To help with managing of a CouchSession object with multiple database calls,
 a HashMap has been added to the CouchSession class. This is available
 publicly to allow for dynamic adding and removing of database name keys to lessen
 the chance of coding errors from supplying hard-coded string values.
 
 You may use this object after initializing the class like this:
 
 	Adding values:
 	
 	session.DATABASES.put("user_accounts", "userDB_");
 	
 	
 	Getting values:
 	
 	session.DATABASES.get("user_accounts");
 	
 Since this is fully a user manipulated object and has no built-in functionality
 provided automatically, I have made it a public method to allow for full unfettered access
 for manipulation. So you may also choose to iterate through it, copy it, nullify it,
 and assign a full HashMap<string, string> as all functionality must be supplied.
 
 	session.DATABASE = new HashMap<string, string>();
 	session.DATABASE = null;
 	
 	
 Suggested usage is like this:
 
 	String db = (String) session.DATABASES.get("user_accounts");
 	session.queryDB(db);