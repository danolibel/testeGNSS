package com.google.android.apps.location.gps.gnsslogger;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;


public class HttpPost extends AsyncTask<String, Void, String> {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private OnResponseReceivedListener listener;

    public static String httpToken = "";

    public void HttpPost(OnResponseReceivedListener listener) {
        this.listener = listener;
    }


   /* public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    } */

    @Override
    public  String doInBackground(String... params) {
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
    public String sendPostRequest(String url, String json) {
        RequestBody requestBody = RequestBody.create(json, JSON);
        final String[] r = {""};
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                r[0] = responseBody;
                // For example, update UI or process the response data
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network or other errors here
            }

        });
        return r[0];
    }
    public interface OnResponseReceivedListener {
        void onResponseReceived(String token);
    }



    public static String getMacAddress(String uniqueId) {
        uniqueId = MD5(uniqueId);
        int i = 0;
        while(i<uniqueId.length())
        {
            if(i%5==0)
            {
                uniqueId = new StringBuilder(uniqueId).insert(i, "-").toString();
            }
            i++;
        }
        uniqueId = uniqueId.substring(1);
        return uniqueId;

    }
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch(UnsupportedEncodingException ex){
        }
        return null;
    }
}