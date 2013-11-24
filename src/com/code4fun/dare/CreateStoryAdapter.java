package com.code4fun.dare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CreateStoryAdapter extends ArrayAdapter<Story> {
    public static View createStoryView;

    public CreateStoryAdapter(Context context) {
        super(context, 0, 0);

        if (createStoryView == null) {
            createStoryView = LayoutInflater.from(getContext()).inflate(R.layout.create_story, null);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createStoryView;
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
        return 1;
    }
}
