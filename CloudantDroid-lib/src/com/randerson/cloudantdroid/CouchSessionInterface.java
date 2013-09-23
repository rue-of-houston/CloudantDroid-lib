/*
 * project 		CloudantDroid
 * 
 * package 		com.randerson.cloudantdroid
 * 
 * @author 		Rueben Anderson
 * 
 * date			Sep 16, 2013
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

import android.content.Intent;

public interface CouchSessionInterface {

	public void startCouchService(Intent intent);
	
	public void returnServiceResponse(String[] response, int id);
	
}
