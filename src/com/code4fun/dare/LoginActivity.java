package com.code4fun.dare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import com.code4fun.dare.PostComm;
import com.parse.PushService;

public class LoginActivity extends Activity {
	final String TAG = "LoginActivity";

    final View.OnClickListener mLoginTwitterListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            twitterLogin(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException err) {
                    if (user == null) {
                        Log.d("Dare", "Uh oh. The user cancelled the Facebook login.");
                        view.setEnabled(true);
                    } else if (user.isNew()) {
                        Log.d("Dare", "User signed up and logged in through Facebook!");
                        Intent mainScreen = new Intent(getApplicationContext(), MainMenuActivity.class);
						/*ParseInstallation.getCurrentInstallation().saveInBackground();
						PushService.setDefaultPushCallback(getApplicationContext(), MainMenuActivity.class, R.drawable.launcher);
						PushService.subscribe(getApplicationContext(), ParseTwitterUtils.getTwitter().getScreenName(), MainMenuActivity.class);
						*/startActivity(mainScreen);
                    } else {
						Log.d("Dare", "User logged in through Facebook!");
                        Intent mainScreen = new Intent(getApplicationContext(), MainMenuActivity.class);
						/*ParseInstallation.getCurrentInstallation().saveInBackground();
						PushService.setDefaultPushCallback(getApplicationContext(), MainMenuActivity.class, R.drawable.launcher);
						PushService.subscribe(getApplicationContext(), ParseTwitterUtils.getTwitter().getScreenName(), MainMenuActivity.class);
						*/startActivity(mainScreen);
                    }
                }
            });
            view.setEnabled(false);
        }
    };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
		findViewById(R.id.loginButton).setOnClickListener(mLoginTwitterListener);
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

    private void twitterLogin(LogInCallback logInCallback) {
        ParseTwitterUtils.logIn(this, logInCallback);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
