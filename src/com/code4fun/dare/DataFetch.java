package com.code4fun.dare; /**
 * Created by vvu on 23/11/13.
 */
import android.util.Log;

import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import org.apache.http.*;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class DataFetch {
	ParseUser user;
	String JSON_data;
	private final String TAG = "DataFetch";

	DataFetch(ParseUser user) {
		this.user = user;
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

	public void getData() throws IOException {
		Runnable runnable = new Runnable()  {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet verifyGet = new HttpGet(
						"https://api.twitter.com/1.1/users/show.json?screen_name=" + ParseTwitterUtils.getTwitter().getScreenName());
				ParseTwitterUtils.getTwitter().signRequest(verifyGet);
				try {
					HttpResponse response = client.execute(verifyGet);
					JSON_data = EntityUtils.toString(response.getEntity());
					Log.d(TAG, JSON_data);
					HttpClient httpclient = new DefaultHttpClient();
					InputStream inputStream = null;
					HttpPost httpPost = null;
					URI link = null;
					try {
						link = new URI("http://172.16.2.113:6969/user/create");
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					httpPost = new HttpPost(link);
					StringEntity se = new StringEntity(JSON_data);
					httpPost.setEntity(se);
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("Content-type", "application/json");
					HttpResponse httpResponse = httpclient.execute(httpPost);
					inputStream = httpResponse.getEntity().getContent();
					if(inputStream == null)
						throw new IOException("shit got wrong!");
					else Log.d(TAG, convertInputStreamToString(inputStream));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread mythread = new Thread(runnable);
		mythread.start();
	}
}
