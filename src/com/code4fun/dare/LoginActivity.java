package com.code4fun.dare;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity {
	final String TAG = "LoginActivity";
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginListener);
	}

	View.OnClickListener loginListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent mainScreen = new Intent(getApplicationContext(), MainMenuActivity.class);
			startActivity(mainScreen);
		}
	};
		/*try {
			PackageInfo info = getPackageManager().getPackageInfo("com.code4fun.dare", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e(TAG, "hash key: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e1) {
			Log.e(TAG, "name not found : " + e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "no such an algorithm" + e.toString());
		} catch (Exception e) {
			Log.e(TAG, "exception" + e.toString());
		}

		ParseAnalytics.trackAppOpened(getIntent());

        List<String> permissions = Arrays.asList("basic_info", "user_about_me",
                "user_relationships", "user_birthday", "user_location");

        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(TAG, "User signed up and logged in through Facebook!");
                } else {
                    Log.d(TAG, "User logged in through Facebook!");
                }
            }
        });
	}*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }*/
}
