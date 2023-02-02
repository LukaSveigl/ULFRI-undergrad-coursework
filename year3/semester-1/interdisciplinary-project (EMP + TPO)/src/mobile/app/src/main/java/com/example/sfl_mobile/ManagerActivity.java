package com.example.sfl_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sfl_mobile.databinding.ActivityManagerBinding;
import com.example.sfl_mobile.ui.login.LoginActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ManagerActivity extends AppCompatActivity {

    // Header
    ImageView logoImageView;
    TextView userTextView;

    // Body
    ImageView totalImageView;
    TextView totalTextView;
    String total = "";

    ImageView pendingImageView;
    TextView pendingTextView;
    String pending = "";

    ImageView completedImageView;
    TextView completedTextView;
    String completed = "";

    ImageView driversImageView;
    TextView driversTextView;
    String drivers = "";

    ImageView agentsImageView;
    TextView agentsTextView;
    String agents = "";

    // Footer
    Button signOutButton;

    ActivityManagerBinding binding;

    String displayName = "";
    String username = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getBindingViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            displayName = extras.getString("displayName");
            username = extras.getString("username");
            password = extras.getString("password");
            userTextView.setText(extras.getString("displayName"));
            userTextView.setTextSize(17.5f);
            userTextView.setTypeface(null, Typeface.BOLD);
        }

        populateTestData();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::populateData);
        if (!executorService.isTerminated()) {
            executorService.shutdown();
            try {
                if (executorService.awaitTermination(20, TimeUnit.SECONDS)) {
                    System.out.println("Service terminated successfully");
                }
                else {
                    System.out.println("Service terminated unsuccessfully");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setTextOnViews();

        driversImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManagerActivity.this, EmployeesActivity.class);
                i.putExtra("employeeType", "driver");
                i.putExtra("displayName", displayName);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                ManagerActivity.this.finish();
            }
        });

        agentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManagerActivity.this, EmployeesActivity.class);
                i.putExtra("employeeType", "agent");
                i.putExtra("displayName", displayName);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                ManagerActivity.this.finish();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManagerActivity.this, LoginActivity.class);
                startActivity(i);
                ManagerActivity.this.finish();
            }
        });

    }

    private void getBindingViews() {
        logoImageView = binding.logoImageView;
        userTextView = binding.userTextView;
        totalImageView = binding.parcelsImageView;
        totalTextView = binding.totalTextView;
        pendingImageView = binding.pendingImageView;
        pendingTextView = binding.pendingTextView;
        completedImageView = binding.completedImageView;
        completedTextView = binding.completedTextView;
        driversImageView = binding.driversImageView;
        driversTextView = binding.driversTextView;
        agentsImageView = binding.agentsImageView;
        agentsTextView = binding.agentsTextView;
        signOutButton = binding.signOutButton;
    }

    private void setTextOnViews() {
        totalTextView.setText(total);
        pendingTextView.setText(pending);
        completedTextView.setText(completed);
        driversTextView.setText(drivers);
        agentsTextView.setText(agents);
    }

    private void populateTestData() {
        total = "145";
        pending = "73";
        completed = Integer.toString(145 - 73);
        drivers = "10";
        agents = "12";
    }

    private void populateData() {
        try {
            StringBuilder content = new StringBuilder();
            // Create connection to web api servlet via post
            URL openUrl = new URL(Common.connectionStringStatistics);
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
            String[] stats = content.toString().split(";");
            if (!stats[0].equals("success")) {
                return;
            }

            String[] statsComponents = stats[1].split(",");
            total = statsComponents[0];
            pending = statsComponents[1];
            completed = statsComponents[2];
            drivers = statsComponents[3];
            agents = statsComponents[4];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}