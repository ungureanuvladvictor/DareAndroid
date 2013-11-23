package com.code4fun.dare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainMenuActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

        ListView listView = (ListView) findViewById(R.id.app_inner);
        listView.setAdapter(new StoryAdapter(getApplicationContext(), mocks()));
		//setupFonts();
	}

	private void setupFonts() {
		Button scoreButton = (Button) findViewById(R.id.scoreButton);
		Button inboxButton = (Button) findViewById(R.id.inboxButton);
		Typeface font = Typeface.createFromAsset(getAssets(), "Aaargh.ttf");
		scoreButton.setTypeface(font);
		inboxButton.setTypeface(font);
	}

    ArrayList<Story> mocks() {
        ArrayList<Story> stories = new ArrayList<Story>();

        final Story s = new Story();

        s.author = "Dominik Kundel";

        s.imageUrl = "http://funlava.com/wp-content/uploads/2013/10/Apple-Mac-OS-X-Lion-Aqua-Wallpaper.jpg";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(s.imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    final InputStream input = connection.getInputStream();
                    final Bitmap image = BitmapFactory.decodeStream(input);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            s.image = image;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        s.title = "My momma!!!";
        s.description = "Somebody should do something!";

        for (int i = 0; i < 20; i++) {
            stories.add(s);
        }

        return stories;
    }

	public void onBackPressed(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you dare to exit ?").setPositiveButton("I dare !", dialogClickListener)
				.setNegativeButton("No, scared !", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
			}
		}
	};
}
