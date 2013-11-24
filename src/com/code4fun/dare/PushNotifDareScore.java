package com.code4fun.dare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.parse.ParseTwitterUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by vvu on 23/11/13.
 */
public abstract class PushNotifDareScore extends BroadcastReceiver {

	private final String TAG = "PushNotifDareScore";

	@Override
	public void onReceive(Context context, Intent intent) {

	}
}
