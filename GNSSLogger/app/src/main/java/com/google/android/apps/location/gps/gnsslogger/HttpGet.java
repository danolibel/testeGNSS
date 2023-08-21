package com.google.android.apps.location.gps.gnsslogger;
import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
public class HttpGet extends AsyncTask<String, Void, String> {

    private OkHttpClient client = new OkHttpClient();

    private OnResponseReceivedListener listener;

    public HttpGet(OnResponseReceivedListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("HTTP_GET", "Error sending GET request: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            // Notify the listener with the response
            if (listener != null) {
                listener.onResponseReceived(response);
            }
        }
    }

    public interface OnResponseReceivedListener {
        void onResponseReceived(String response);
    }
}
