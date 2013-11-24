package com.code4fun.dare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.content.DialogInterface;
import android.util.Base64;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.PushService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class MainMenuActivity extends Activity {
	final String TAG = "MainMenuActivity";

    BaseAdapter mCreateStoryAdapter;
    BaseAdapter mFeedAdapter;
    BaseAdapter mFavoritesAdapter;
    BaseAdapter mStoryDetailAdapter;

    ImageButton mCreateButton;
    ImageButton mFeedButton;
    ImageButton mFavoritesButton;

    Story mStoryDetail;

    Uri mImageUri;

    ListView mListView;

    enum State {
        STATE_CREATE,
        STATE_FEED,
        STATE_FAVORITES,
        STATE_STORY_DETAIL,
    };

    State mState;

    BaseAdapter mAdapter;
    String mUser;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.code4fun.dare.DARE_SCORE")) {
				GetComm fetch = new GetComm() {
					@Override
					protected void onPostExecute(String result) {
						try {
							JSONObject answer = new JSONObject(result);
							final String score = answer.getString("score");
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Button scoreButton = (Button) findViewById(R.id.scoreButton);
									scoreButton.setText("   " + score);
								}
							});

						} catch (JSONException e1) {
							e1.printStackTrace();
						}
					}
				};
				fetch.execute("/user/" + ParseTwitterUtils.getTwitter().getScreenName());
			}
		}
	};

	public void updateStatus() {
		GetComm fetch = new GetComm() {
			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject answer = new JSONObject(result);
					Log.d(TAG, "score: " + answer.get("score"));
					final String score = answer.getString("score");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Button scoreButton = (Button) findViewById(R.id.scoreButton);
							scoreButton.setText("   " + score);
						}
					});

				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		};

		fetch.execute("/user/" + ParseTwitterUtils.getTwitter().getScreenName());
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

        mUser = ParseTwitterUtils.getTwitter().getScreenName();
        mListView = (ListView) findViewById(R.id.app_inner);

		updateStatus();
		IntentFilter filterScore = new IntentFilter("com.code4fun.dare.DARE_SCORE");
		registerReceiver(mReceiver, filterScore);

        mCreateButton = (ImageButton) findViewById(R.id.createButton);
        mFeedButton = (ImageButton) findViewById(R.id.discoverButton);
        mFavoritesButton = (ImageButton) findViewById(R.id.starButton);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(State.STATE_CREATE);
            }
        });
        mFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(State.STATE_FEED);
            }
        });
        mFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(State.STATE_FAVORITES);
            }
        });

		findViewById(R.id.logoutButton).setOnClickListener(logoutClick);
        updateState(State.STATE_FEED);
 	}

    private void initCreateStoryView() {
        View createStoryView = CreateStoryAdapter.createStoryView;

        final EditText descriptionText = (EditText) createStoryView.findViewById(R.id.description);
        final EditText titleText = (EditText) createStoryView.findViewById(R.id.title);
        final EditText targetText = (EditText) createStoryView.findViewById(R.id.target);

        View v = createStoryView.findViewById(R.id.choose_picture);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        v = createStoryView.findViewById(R.id.story_submit);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostComm post = new PostComm() {
                    @Override
                    protected void onPostExecute(String result) {
                        Log.d(TAG, result);
                        Util.inform(getApplicationContext(), "Dare created");
                    }
                };

                try {
                    final Bitmap scaledBitmap = Util.getScaledBitmap(getContentResolver(), mImageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                    byte[] b = baos.toByteArray();
                    String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

                    JSONObject jsonStory = new JSONObject();
                    jsonStory.put("creator", mUser);
                    jsonStory.put("name", titleText.getText());
                    jsonStory.put("description", descriptionText.getText());
                    jsonStory.put("target", targetText.getText());
                    jsonStory.put("base64img", imageEncoded);

                    post.execute("/dare/create", jsonStory.toString());

                    scaledBitmap.recycle();
                } catch (JSONException e) {
                    Util.inform(getApplicationContext(), "Could not create Dare.");
                    e.printStackTrace();
                } finally {
                    updateState(State.STATE_FEED);
                    Util.inform(getApplicationContext(), "Uploading Dare...");
                    mCreateStoryAdapter = null;
                    CreateStoryAdapter.createStoryView = null;
                }
            }
        });
    }

    private void initLoader() {
        String url = null;
        switch (mState) {
            case STATE_FEED:
                url = "/feed/latest/" + mUser;
                break;
            case STATE_FAVORITES:
                url = "/feed/starred/" + mUser;
                break;
            default:
                break;
        }
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
                        story.id = storyJSON.getString("id");
                        story.starred = storyJSON.getBoolean("starred");

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
                                            switch (mState) {
                                                case STATE_FEED:
                                                    mFeedAdapter.notifyDataSetChanged();
                                                    break;
                                                case STATE_FAVORITES:
                                                    mFavoritesAdapter.notifyDataSetChanged();
                                                    break;
                                                default:
                                                    break;
                                            }
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

        switch (mState) {
            case STATE_FEED:
            case STATE_FAVORITES:
                retriever.execute(url);
                break;
            default:
                break;
        }
    }

	View.OnClickListener logoutClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Set<String> setOfAllSubscriptions = PushService.getSubscriptions(getApplicationContext());
			for (String s : setOfAllSubscriptions)
				PushService.unsubscribe(getApplicationContext(), s);

			ParseUser.logOut();
			Intent mainScreen = new Intent(getApplicationContext(), LoginActivity.class);
			mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainScreen);
		}
	};

	@Override
	public void onPause() {
		super.onPause();

		unregisterReceiver(mReceiver);
	}

	@Override
	public void onResume(){
		super.onResume();

		IntentFilter filterScore = new IntentFilter("com.code4fun.dare.DARE_SCORE");
		registerReceiver(mReceiver, filterScore);
	}

    private void onLoadFinished(ArrayList<Story> stories) {
        switch (mState) {
            case STATE_FEED:
                mFeedAdapter = new StoryAdapter(getApplicationContext(), stories);
            case STATE_FAVORITES:
                mFavoritesAdapter = new StoryAdapter(getApplicationContext(), stories);
                break;
            default:
                break;
        }

        switch (mState) {
            case STATE_CREATE:
                mListView.setAdapter(mCreateStoryAdapter);
                break;
            case STATE_FEED:
                mListView.setAdapter(mFeedAdapter);
                break;
            case STATE_FAVORITES:
                mListView.setAdapter(mFavoritesAdapter);
                break;
            case STATE_STORY_DETAIL:
                mListView.setAdapter(mStoryDetailAdapter);
                break;
        }
    }

    private void updateState(State state) {
        if (mState == null || mState != state) {
            mCreateButton.setEnabled(true);
            mFeedButton.setEnabled(true);
            mFavoritesButton.setEnabled(true);
            switch (state) {
                case STATE_CREATE:
                    mCreateButton.setEnabled(false);
                    if (mCreateStoryAdapter == null) {
                        mCreateStoryAdapter = new CreateStoryAdapter(getApplicationContext());
                    }
                    mListView.setAdapter(mCreateStoryAdapter);
                    initCreateStoryView();
                    break;
                case STATE_FEED:
                    mFeedButton.setEnabled(false);
                    if (mFeedAdapter != null) {
                        mListView.setAdapter(mFeedAdapter);
                    }
                    break;
                case STATE_FAVORITES:
                    mFavoritesButton.setEnabled(false);
                    if (mFavoritesAdapter != null) {
                        mListView.setAdapter(mFavoritesAdapter);
                    }
                    break;
                case STATE_STORY_DETAIL:
                    final ArrayList<Story> stories = new ArrayList<Story>();
                    stories.add(mStoryDetail);
                    mStoryDetailAdapter = new StoryDetailAdapter(getApplicationContext(), stories);
                    mListView.setAdapter(mStoryDetailAdapter);
                    break;
            }

            switch (state) {
                case STATE_FAVORITES:
                case STATE_FEED:
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mStoryDetail = (Story) mListView.getAdapter().getItem(position);
                            updateState(State.STATE_STORY_DETAIL);
                        }
                    });
                    break;
                default:
                    break;
            }

            mState = state;

            initLoader();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    final ImageView imagePreview =(ImageView) CreateStoryAdapter.createStoryView.
                            findViewById(R.id.image_preview);
                    Uri selectedImage = imageReturnedIntent.getData();
                    imagePreview.setImageURI(selectedImage);
                    mImageUri = selectedImage;
                }
                break;
        }
    }
}
