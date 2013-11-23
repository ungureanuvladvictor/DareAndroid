package com.code4fun.dare;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by vvu on 23/11/13.
 */
public class PostComm extends AsyncTask<String, Void, String> {
	final String TAG = "PostComm";
	private String POSTResult;

	@Override
	protected String doInBackground(String... strings) {
		HttpClient Client = new DefaultHttpClient();
		InputStream inputStream = null;
		HttpPost httpPost = null;
		URI link = null;
		try {
			link = new URI("http://172.16.2.113:6969" + strings[0]);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		httpPost = new HttpPost(link);
		StringEntity se = null;
		try {
			se = new StringEntity(strings[1]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setEntity(se);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		HttpResponse httpResponse = null;
		try {
			httpResponse = Client.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inputStream = httpResponse.getEntity().getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(inputStream == null)
			try {
				throw new IOException("shit got wrong!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		else try {
			this.POSTResult = convertInputStreamToString(inputStream);
			return this.POSTResult;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(String result) {
		//PRODE IMPLEMENT POST CALLBACK
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}
}
