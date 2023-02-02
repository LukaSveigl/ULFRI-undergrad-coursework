package com.example.sfl_mobile.data;

import com.example.sfl_mobile.Common;
import com.example.sfl_mobile.data.model.LoggedInUser;
import com.mysql.cj.core.exceptions.WrongArgumentException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        try {
            StringBuilder content = new StringBuilder();
            // Create connection to web api servlet via post
            URL openUrl = new URL(Common.connectionStringLogin);
            HttpURLConnection connection = (HttpURLConnection) openUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(1000000);
            connection.setReadTimeout(5000000);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Send login credentials to servlet
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            String params = "username=" + username + "&password=" + password;
            out.writeBytes(params);
            out.flush();
            out.close();

            // Receive success state from login servlet
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Close connection to servlet
            connection.disconnect();

            // Generate result based on servlet response content
            String[] results = content.toString().split(",");
            if (results[0].equals("success")) {
                LoggedInUser user = new LoggedInUser(
                        username,
                        results[1],
                        results[2]
                );
                return new Result.Success<>(user);
            } else {
                return new Result.Error(new WrongArgumentException("Wrong username or password."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}