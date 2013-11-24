package com.code4fun.dare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.DialogInterface;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.parse.ParseTwitterUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainMenuActivity extends Activity {
	final String TAG = "MainMenuActivity";

    BaseAdapter mAdapter;
    String user;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
        user = ParseTwitterUtils.getTwitter().getScreenName();

        initLoader();
 	}

    private void initLoader() {
        GetComm retriever = new GetComm() {
            @Override
            protected void onPostExecute(String result) {
                try {
                    final ArrayList<Story> stories = new ArrayList<Story>();
                    JSONArray response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        final JSONObject storyJSON = response.getJSONObject(i);

                        final Story story = new Story();
                        story.author = storyJSON.getString("creator");
                        story.imageUrl = storyJSON.getString("image");
                        story.title = storyJSON.getString("name");
                        story.description = storyJSON.getString("description");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (story.imageUrl.charAt(0) == '/') {
                                        story.imageUrl = GetComm.HOST + story.imageUrl;
                                    }
                                    URL url = new URL(story.imageUrl);
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setDoInput(true);
                                    connection.connect();
                                    final InputStream input = connection.getInputStream();
                                    final Bitmap image = BitmapFactory.decodeStream(input);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            story.image = image;
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        stories.add(story);
                    }
                    onLoadFinished(stories);
                } catch (NullPointerException e) {
                    Util.inform(getApplicationContext(), "Stories cannot be retrieved at this time");
                    e.printStackTrace();
                } catch (JSONException e){
                    Util.inform(getApplicationContext(), "Stories cannot be retrieved at this time");
                    e.printStackTrace();
                }
            }
        };

        retriever.execute("/feed/latest");
    }

    private void onLoadFinished(ArrayList<Story> stories) {
        mAdapter = new StoryAdapter(getApplicationContext(), stories);
        ListView listView = (ListView) findViewById(R.id.app_inner);
        listView.setAdapter(mAdapter);
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
