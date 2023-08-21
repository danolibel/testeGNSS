package com.google.android.apps.location.gps.gnsslogger.data;

import com.google.android.apps.location.gps.gnsslogger.HttpPost;
import com.google.android.apps.location.gps.gnsslogger.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource implements HttpPost.OnResponseReceivedListener{
    String loginToken ="";
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            String macAddress = HttpPost.getMacAddress();
            String json = "{\"userName\": \""+username+"\"} {\"password\": \""+password+"\"} {\"macAddress\": \""+macAddress+"\"} "; // JSON data

            HttpPost postTask = new HttpPost();
           String response =  postTask.post("@string/url_login", json);
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            if (loginToken != null){
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

    public void onResponseReceived(String token) {
        // Handle the token here
        loginToken = token;
    }
}