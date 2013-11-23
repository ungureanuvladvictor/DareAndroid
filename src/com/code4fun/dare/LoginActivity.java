package com.code4fun.dare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

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


        /*
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("Dare", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("Dare", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("Dare", "User logged in through Facebook!");
                }
            }
        });
        */

        /*
        List<String> permissions = Arrays.asList("basic_info", "user_about_me",
                "user_relationships", "user_birthday", "user_location");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("Dare", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("Dare", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("Dare", "User logged in through Facebook!");
                }
            }
        });
        */
    }

	View.OnClickListener loginListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent mainScreen = new Intent(getApplicationContext(), MainMenuActivity.class);
			startActivity(mainScreen);
		}
	};

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
