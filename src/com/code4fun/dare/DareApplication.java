package com.code4fun.dare;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import android.app.Application;

public class DareApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

        /*Parse.initialize(this, "P81h4XXXtSPxb4KJjpMCPecsw4IjRYEUKBXeLrOT",
                "0dQAkL28N3U8ZXS8RDsZs6l125kjQljvM1S7jhoS");

        ParseFacebookUtils.initialize("135724333240350");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);*/
	}



}
