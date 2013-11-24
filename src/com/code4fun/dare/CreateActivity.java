package com.code4fun.dare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseTwitterUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateActivity extends Activity {
    private static final String TAG = "CreateActivity";
    ImageView imagePreview;
    String user;

    EditText descriptionText;
    EditText titleText;

    Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_story);

        user = ParseTwitterUtils.getTwitter().getScreenName();

        descriptionText = (EditText) findViewById(R.id.description);
        titleText = (EditText) findViewById(R.id.title);

        View v = findViewById(R.id.choose_picture);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
        imagePreview = (ImageView) findViewById(R.id.image_preview);

        v = findViewById(R.id.story_submit);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostComm post = new PostComm() {
                    @Override
                    protected void onPostExecute(String result) {
                        Log.d(TAG, result);
                        Util.inform(getApplicationContext(), "Dare created");
                        finish();
                    }
                };

                try {
                    InputStream pictureStream = null;
                    try {
                        pictureStream = getContentResolver().openInputStream(mImageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    final Bitmap scaledBitmap = Util.getScaledBitmap(getContentResolver(), mImageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                    byte[] b = baos.toByteArray();
                    String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

                    JSONObject jsonStory = new JSONObject();
                    jsonStory.put("creator", user);
                    jsonStory.put("name", titleText.getText());
                    jsonStory.put("description", descriptionText.getText());
                    jsonStory.put("base64img", imageEncoded);

                    post.execute("/dare/create", jsonStory.toString());

                    scaledBitmap.recycle();
                } catch (JSONException e) {
                    Util.inform(getApplicationContext(), "Could not create Dare.");
                    e.printStackTrace();
                } finally {
                    finish();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imagePreview.setImageURI(selectedImage);
                    mImageUri = selectedImage;
                }
                break;
        }
    }
}
