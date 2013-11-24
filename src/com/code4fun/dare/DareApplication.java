package com.code4fun.dare;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.PushService;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DareApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

        Parse.initialize(this, "P81h4XXXtSPxb4KJjpMCPecsw4IjRYEUKBXeLrOT",
                "0dQAkL28N3U8ZXS8RDsZs6l125kjQljvM1S7jhoS");

        ParseTwitterUtils.initialize("ZpidyjsOOKQijCOV4muFDA",
                "utlv1MXV7n3KepTiwl0mbqxMT5pWSxRl9istTBS8sPY");
	}
}
