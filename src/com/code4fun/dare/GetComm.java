package com.code4fun.dare;

import android.os.AsyncTask;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by vvu on 23/11/13.
 */
public abstract class GetComm extends AsyncTask<String, Void, String> {

    final static public String HOST = "http://172.16.2.113:6969";

	final String TAG = "GetComm";
	String GETAnswer;

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	@Override
	protected String doInBackground(String... strings) {
		HttpClient Client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(HOST + strings[0]);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			String answer = Client.execute(httpget, responseHandler);
			this.GETAnswer = answer;
			return this.GETAnswer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract void onPostExecute(String result);


}
