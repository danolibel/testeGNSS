package com.google.android.apps.location.gps.gnsslogger;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;


public class HttpPost extends AsyncTask<String, Void, String> {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private OnResponseReceivedListener listener;

    public static String httpToken ="";

    public void MyHttpPostTask(OnResponseReceivedListener listener) {
        this.listener = listener;
    }



    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
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

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            // Parse the response to extract the token
            // For demonstration purposes, let's assume the response is in JSON format
            try {
                // Replace "token" with the actual field name in your JSON response
                String token = new JSONObject(response).getString("token");

                // Notify the listener with the token
                if (listener != null) {
                    listener.onResponseReceived(token);
                }
            } catch (JSONException e) {
                Log.e("HTTP_POST", "Error parsing JSON: " + e.getMessage());
            }
        }
    }

    public interface OnResponseReceivedListener {
        void onResponseReceived(String token);
    }

    public void onResponseReceived(String token) {
        // Handle the token here
        httpToken = token;
        Log.d("TOKEN_RECEIVED", "Token: " + token);
    }
    public static String getToken(){
        return httpToken;
    }
    public static String getMacAddress(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
}