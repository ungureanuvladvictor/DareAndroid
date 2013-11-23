package com.code4fun.dare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class StoryAdapter extends ArrayAdapter<Story> {

    public StoryAdapter(Context context, int resource) {
        super(context, resource);
    }

    public StoryAdapter(Context context, int resource, int textViewResourceId, List<Story> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public Story getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_item_view, null);
        }

        // TODO Bind elements

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

    @Override
    public int getCount() {
        return 20;
    }
}
