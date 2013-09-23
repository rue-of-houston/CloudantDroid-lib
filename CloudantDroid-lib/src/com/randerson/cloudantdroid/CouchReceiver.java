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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//Session method for tracking connectivity
// register this receiver within the activity using the registerReceiver method
public final class CouchReceiver extends BroadcastReceiver {

	// the default connection status and type for the receiver
	boolean NETWORK_STATUS = false;
	String NETWORK_TYPE = "No Connection";
	Context CONTEXT = null;
	
	// constructor
	public CouchReceiver(Context context)
	{
		CONTEXT = context;
	};
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		ConnectivityManager cm = (ConnectivityManager) CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		
		// check the network status is connected
		if (network.isConnected())
		{
			// set the bool to true
			this.NETWORK_STATUS = true;
			
			// set the network type
			this.NETWORK_TYPE = network.getTypeName();
		}
		else
		{
			// set the bool to false
			this.NETWORK_STATUS = false;
			
			// set the network type to none
			this.NETWORK_TYPE = "No Connection";
		}
	} 
	
	// method for returning the network status
	public boolean getStatus()
	{
		// return the network status from the receiver;
		return this.NETWORK_STATUS;
	}
	
	// method for returning the network type
	public String getType()
	{
		// return the network type
		return this.NETWORK_TYPE;
	}
	
	// Session method for getting an intent filter for registration along with the receiver
	public static IntentFilter getFilter()
	{
		// return an intent filter
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		
		return filter;
	}

}
