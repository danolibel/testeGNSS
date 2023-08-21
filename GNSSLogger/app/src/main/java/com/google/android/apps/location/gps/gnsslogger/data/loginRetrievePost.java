package com.google.android.apps.location.gps.gnsslogger.data;

import static com.google.android.apps.location.gps.gnsslogger.HttpPost.JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.apps.location.gps.gnsslogger.HttpPost;
import com.google.android.apps.location.gps.gnsslogger.data.LoginDataSource;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginRetrievePost extends AsyncTask<String, Void, String> {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private HttpPost.OnResponseReceivedListener listener;
    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String json = params[1];

        RequestBody requestBody = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("HTTP_POST", "Error sending POST request: " + e.getMessage());
        }

        return null;
    }
    protected void onPostExecute(String string) {
        // TODO: check this.exception
        // TODO: do something with the feed
        LoginDataSource.changeLogin(string);
    }
}
