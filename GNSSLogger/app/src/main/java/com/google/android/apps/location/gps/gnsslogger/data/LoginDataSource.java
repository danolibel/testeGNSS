package com.google.android.apps.location.gps.gnsslogger.data;

import android.util.Log;

import com.google.android.apps.location.gps.gnsslogger.HttpPost;
import com.google.android.apps.location.gps.gnsslogger.data.model.LoggedInUser;
import com.google.android.apps.location.gps.gnsslogger.ui.login.LoginActivity;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource  implements HttpPost.OnResponseReceivedListener{
    static boolean loginToken = false;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            String macAddress = HttpPost.getMacAddress(LoginActivity.android_device_id);

           /* String json = "{\"userName\": \""+username+"\"} {\"password\": \""+password+"\"} {\"macAddress\": \""+ macAddress +"\"} "; // JSON data
            Log.println(Log.INFO,"1",json);

            Log.println(Log.INFO,"1","1");
            new loginRetrievePost().execute(
                   "http://localhost:49478/api/GNSS/Login", json); */
            LoginApiManager.login(username,password,macAddress,new LoginApiManager.LoginCallback() {
                @Override
                public void onSuccess(String response) {
                    // Handle successful login response
                    loginToken = true;
                }

                @Override
                public void onFailure(Exception e) {
                    // Handle login failure or network error
                    loginToken = false;
                }
            });
            Log.println(Log.INFO,"1","1");
           LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            Log.println(Log.INFO,"1","1");

            if (loginToken){
                return new Result.Success<>(fakeUser);
            }
            else{

                return new aResult.Error(new IOException("Error logging in"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
    public static void changeLogin(String string){

    }
    @Override
    public void onResponseReceived(String token) {

        Log.println(Log.DEBUG, "Token: " , token);
    }
}