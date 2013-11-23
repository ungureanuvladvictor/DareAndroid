package com.code4fun.dare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.content.DialogInterface;

/**
 * Created by vvu on 23/11/13.
 */

public class MainMenuActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
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
