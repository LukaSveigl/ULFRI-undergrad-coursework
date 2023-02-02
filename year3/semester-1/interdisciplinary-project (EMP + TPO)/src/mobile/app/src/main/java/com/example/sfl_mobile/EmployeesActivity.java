package com.example.sfl_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sfl_mobile.databinding.ActivityEmployeesBinding;
import com.example.sfl_mobile.databinding.ActivityJobsBinding;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class EmployeesActivity extends AppCompatActivity {

    TextView userData;
    ImageView logoView;

    Button backButton;

    ScrollView employeesView;
    LinearLayout employeesLayout;

    ActivityEmployeesBinding binding;

    ArrayList<String> employeesData = new ArrayList<>();
    String employeesType;

    String displayName;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        binding = ActivityEmployeesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getBindingViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            displayName = extras.getString("displayName");
            employeesType = extras.getString("employeeType");
            username = extras.getString("username");
            password = extras.getString("password");
            userData.setText(extras.getString("displayName"));
            userData.setTextSize(17.5f);
            userData.setTypeface(null, Typeface.BOLD);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EmployeesActivity.this, ManagerActivity.class);
                i.putExtra("displayName", displayName);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                EmployeesActivity.this.finish();
            }
        });

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

        fillLayout();
    }

    private void getBindingViews() {
        userData = binding.userTextView;
        logoView = binding.logoImageView;
        backButton = binding.backButton;
        employeesView = binding.employeesView;
        employeesLayout = binding.employeesLayout;
    }

    private void fillLayout() {
        employeesLayout.removeAllViews();

        for (String employeeData : employeesData) {
            RelativeLayout relativeLayout = new RelativeLayout(EmployeesActivity.this);
            LinearLayout textLayout = new LinearLayout(EmployeesActivity.this);

            TextView usernameView = new TextView(EmployeesActivity.this);
            TextView firstNameView = new TextView(EmployeesActivity.this);
            TextView lastNameView = new TextView(EmployeesActivity.this);

            Button button = new Button(EmployeesActivity.this);

            // Set text.
            String [] employeeAttributes = employeeData.split(" ");
            usernameView.setText(employeeAttributes[0]);
            firstNameView.setText(employeeAttributes[1]);

            // Build last name from rest of attributes - in case of middle names.
            StringBuilder lastName = new StringBuilder();
            for (int i = 2; i < employeeAttributes.length; i++) {
                lastName.append(employeeAttributes[i]);
                if (i != employeeAttributes.length - 1) {
                    lastName.append(" ");
                }
            }
            lastNameView.setText(lastName.toString());

            // Implement button functionality.
            button.setText("JOBS");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(EmployeesActivity.this, EmployeeJobsActivity.class);
                    i.putExtra("displayName", displayName);
                    i.putExtra("staffUsername", employeeAttributes[0]);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);
                }
            });

            // Style text.
            usernameView.setPadding(10, 15, 25, 0);
            usernameView.setTextSize(15f);
            usernameView.setTypeface(null, Typeface.BOLD);

            firstNameView.setPadding(10, 15, 25, 0);
            firstNameView.setTextSize(15f);
            firstNameView.setTypeface(null, Typeface.BOLD);

            lastNameView.setPadding(10, 15, 25, 0);
            lastNameView.setTextSize(15f);
            lastNameView.setTypeface(null, Typeface.BOLD);

            // Add text views to text layout.
            //textLayout.addView(usernameView);
            textLayout.addView(firstNameView);
            textLayout.addView(lastNameView);

            // Add text layout and button to relative layout.
            relativeLayout.addView(textLayout);
            relativeLayout.addView(button);

            // Align button to the right side of page.
            RelativeLayout.LayoutParams buttonParams =
                        (RelativeLayout.LayoutParams) button.getLayoutParams();
            buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            // Align text to the left side of page.
            RelativeLayout.LayoutParams textViewLayoutParams =
                    (RelativeLayout.LayoutParams) textLayout.getLayoutParams();
            textViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            employeesLayout.addView(relativeLayout);
        }
    }

    private void populateTestData() {
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
        employeesData.add("sheshut51 Shem Shutler");
    }

    private void populateData() {
        try {
            StringBuilder content = new StringBuilder();
            // Create connection to web api servlet via post
            URL openUrl = new URL(Common.connectionStringEmployees);
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
            String[] employees = content.toString().split(";");
            employeesData.clear();

            for (String employee : employees) {
                if (employee.equals("success")) {
                    System.out.println("success!");
                    continue;
                }
                else if (employee.equals("failure")) {
                    System.out.println("failure");
                    return;
                }

                System.out.println("EmployeeType: " + employeesType);

                String[] employeeComponents = employee.split(",");

                String result =
                        employeeComponents[0] +
                                " " +
                                employeeComponents[1] +
                                " " +
                                employeeComponents[2];

                if (employeeComponents[3].split(" ")[1].equals(employeesType)) {
                    employeesData.add(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}