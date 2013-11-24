package com.code4fun.dare;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseTwitterUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StoryDetailAdapter extends ArrayAdapter<Story> {

    public StoryDetailAdapter(Context context, List<Story> objects) {
        super(context, 0, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_detail_view, null);
        }

        final Story story = getItem(position);

        final ImageView image = (ImageView) convertView.findViewById(R.id.image);
        final TextView title = (TextView) convertView.findViewById(R.id.title);
        final TextView description = (TextView) convertView.findViewById(R.id.description);
        final TextView author = (TextView) convertView.findViewById(R.id.author);

        final ImageView accept = (ImageView) convertView.findViewById(R.id.accept);
        final ImageView favorite = (ImageView) convertView.findViewById(R.id.favorite);
        final ImageView reject = (ImageView) convertView.findViewById(R.id.reject);

        if (story.starred) {
            favorite.setImageResource(R.drawable.star_active);
        } else {
            favorite.setImageResource(R.drawable.star_inactive);
        }

        if (story.image != null) {
            image.setImageBitmap(story.image);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                reject.setEnabled(true);

                PostComm post = new PostComm() {
                    @Override
                    protected void onPostExecute(String result) {
                        Log.d(TAG, result);
                        Util.inform(getContext(), "Dare accepted");
                    }
                };

                try {
                    final Story story = getItem(0);

                    JSONObject jsonStory = new JSONObject();
                    jsonStory.put("dareid", story.id);
                    jsonStory.put("username", ParseTwitterUtils.getTwitter().getScreenName());

                    post.execute("/dare/accept", jsonStory.toString());
                } catch (JSONException e) {
                    Util.inform(getContext(), "Could not accept Dare.");
                    v.setEnabled(true);
                    e.printStackTrace();
                }
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv = (ImageView) v;
                final String url;
                final String message;
                if (story.starred) {
                    url = "/user/unstar";
                    message = "Dare unfavorited";
                    iv.setImageResource(R.drawable.star_inactive);
                } else {
                    url = "/user/star";
                    message = "Dare favorited";
                    iv.setImageResource(R.drawable.star_active);
                }

                PostComm post = new PostComm() {
                    @Override
                    protected void onPostExecute(String result) {
                        Log.d(TAG, result);
                        Util.inform(getContext(), message);
                    }
                };

                try {
                    final Story story = getItem(0);

                    JSONObject jsonStory = new JSONObject();
                    jsonStory.put("dareid", story.id);
                    jsonStory.put("username", ParseTwitterUtils.getTwitter().getScreenName());

                    post.execute(url, jsonStory.toString());
                } catch (JSONException e) {
                    Util.inform(getContext(), "Could not accept Dare.");
                    v.setEnabled(true);
                    e.printStackTrace();
                }
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                accept.setEnabled(true);

                PostComm post = new PostComm() {
                    @Override
                    protected void onPostExecute(String result) {
                        Log.d(TAG, result);
                        Util.inform(getContext(), "Dare rejected");
                    }
                };

                try {
                    final Story story = getItem(0);

                    JSONObject jsonStory = new JSONObject();
                    jsonStory.put("dareid", story.id);
                    jsonStory.put("username", ParseTwitterUtils.getTwitter().getScreenName());

                    post.execute("/dare/reject", jsonStory.toString());
                } catch (JSONException e) {
                    Util.inform(getContext(), "Could not reject Dare.");
                    v.setEnabled(true);
                    e.printStackTrace();
                }
            }
        });

        title.setText(story.title);
        description.setText(story.description);
		author.setText("by " + story.author);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
