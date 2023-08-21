package com.google.android.apps.location.gps.gnsslogger.data;

import static android.provider.Settings.Secure.ANDROID_ID;

import android.content.Context;
import android.os.Debug;
import android.provider.Settings;
import android.util.Log;

import com.google.android.apps.location.gps.gnsslogger.HttpPost;
import com.google.android.apps.location.gps.gnsslogger.R;
import com.google.android.apps.location.gps.gnsslogger.data.model.LoggedInUser;
import com.google.android.apps.location.gps.gnsslogger.ui.login.LoginActivity;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource  implements HttpPost.OnResponseReceivedListener{
    String loginToken ="";
    boolean accepted = false;
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            String macAddress = HttpPost.getMacAddress(LoginActivity.android_device_id);

            String json = "{\"userName\": \""+username+"\"} {\"password\": \""+password+"\"} {\"macAddress\": \""+ macAddress +"\"} "; // JSON data
            Log.println(Log.INFO,"1",json);
             HttpPost postTask = new HttpPost();
            Log.println(Log.INFO,"1","1");
            postTask.(
                   "http://localhost:49478/api/GNSS/Login", json);
            Log.println(Log.INFO,"1","1");
           LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            Log.println(Log.INFO,"1","1");

            if (accepted){
                return new Result.Success<>(fakeUser);
            }
            else{

                return new Result.Error(new IOException("Error logging in"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    @Override
    public void onResponseReceived(String token) {
        accepted = true;
        Log.println(Log.DEBUG, "Token: " , token);
    }
}