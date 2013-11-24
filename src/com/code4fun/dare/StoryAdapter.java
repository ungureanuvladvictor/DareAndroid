package com.code4fun.dare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StoryAdapter extends ArrayAdapter<Story> {

    public StoryAdapter(Context context, List<Story> objects) {
        super(context, 0, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_item_view, null);
        }

        final Story story = getItem(position);

        final ImageView image = (ImageView) convertView.findViewById(R.id.image);
        final TextView title = (TextView) convertView.findViewById(R.id.title);
        final TextView description = (TextView) convertView.findViewById(R.id.description);
        final TextView author = (TextView) convertView.findViewById(R.id.author);

        if (story.image != null) {
            image.setImageBitmap(story.image);
        }

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
