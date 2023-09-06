package com.google.android.apps.location.gps.gnsslogger.data;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.*;

public class LoginApiManager {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();
    private static Handler handler = new Handler(Looper.getMainLooper());

    public interface LoginCallback {
        void onSuccess(String response);
        void onFailure(Exception e);
    }

    public static void login(String username, String password, String macAddress, final LoginCallback callback) {
        String json = "{\"userName\": \""+username+"\", \n \"password\": \""+password+"\", \n \"macAddress\": \""+ macAddress +"\"}";
        String loginUrl = "http://10.40.10.27:14000/api/GNSS/Login";
        Log.println(Log.INFO,"1",json);
        RequestBody requestBody = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(loginUrl)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    final String responseBody = response.body().string();
                    Log.println(Log.INFO,"1",responseBody);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(responseBody);
                        }
                    });
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e);
                    }
                });
            }
        });
    }
}
