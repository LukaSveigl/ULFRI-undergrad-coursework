package com.example.sfl_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sfl_mobile.databinding.ActivityEmployeeJobsBinding;
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

public class EmployeeJobsActivity extends AppCompatActivity {

    TextView userData;
    ImageView logoView;

    Button backButton;

    ScrollView jobsView;
    LinearLayout jobsLayout;

    ActivityEmployeeJobsBinding binding;

    ArrayList<String> jobsData = new ArrayList<>();

    String displayName;
    String staffUsername;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_jobs);

        binding = ActivityEmployeeJobsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getBindingViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            displayName = extras.getString("displayName");
            staffUsername = extras.getString("staffUsername");
            username = extras.getString("username");
            password = extras.getString("password");
            userData.setText(extras.getString("displayName"));
            userData.setTextSize(17.5f);
            userData.setTypeface(null, Typeface.BOLD);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EmployeeJobsActivity.this, ManagerActivity.class);
                i.putExtra("displayName", displayName);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                EmployeeJobsActivity.this.finish();
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
                } else {
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
        jobsView = binding.jobsView;
        jobsLayout = binding.jobsLayout;
    }

    private void fillLayout() {
        jobsLayout.removeAllViews();

        RelativeLayout rl = new RelativeLayout(EmployeeJobsActivity.this);
        LinearLayout ll = new LinearLayout(EmployeeJobsActivity.this);
        TextView id = new TextView(EmployeeJobsActivity.this);
        TextView dm = new TextView(EmployeeJobsActivity.this);
        TextView wg = new TextView(EmployeeJobsActivity.this);
        TextView st = new TextView(EmployeeJobsActivity.this);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(40, 0, 0, 0);
        ll.setLayoutParams(llp);

        rl.setBackgroundColor(Color.LTGRAY);

        id.setText("JobID");
        dm.setText("ParcelID");
        wg.setText("Status");

        id.setPadding(10, 15, 25, 0);
        id.setTextSize(16f);
        id.setTypeface(null, Typeface.BOLD);
        id.setWidth(250);
        id.setHeight(120);
        id.setTextColor(Color.BLACK);
        id.setGravity(Gravity.CENTER);

        dm.setPadding(10, 15, 25, 0);
        dm.setTextSize(16f);
        dm.setTypeface(null, Typeface.BOLD);
        dm.setWidth(400);
        dm.setHeight(120);
        dm.setTextColor(Color.BLACK);
        dm.setGravity(Gravity.CENTER);

        wg.setPadding(10, 15, 25, 0);
        wg.setTextSize(16f);
        wg.setTypeface(null, Typeface.BOLD);
        id.setWidth(250);
        id.setHeight(120);
        wg.setTextColor(Color.BLACK);
        wg.setGravity(Gravity.CENTER);

        st.setPadding(10, 15, 25, 0);
        st.setTextSize(16f);
        st.setTypeface(null, Typeface.BOLD);
        st.setTextColor(Color.BLACK);
        st.setGravity(Gravity.CENTER);

        ll.addView(id);
        ll.addView(dm);
        ll.addView(wg);

        rl.addView(ll);

        jobsLayout.addView(rl);

        for (String jobData : jobsData) {
            RelativeLayout relativeLayout = new RelativeLayout(EmployeeJobsActivity.this);
            LinearLayout textLayout = new LinearLayout(EmployeeJobsActivity.this);

            TextView jobIDView = new TextView(EmployeeJobsActivity.this);
            TextView parcelIDView = new TextView(EmployeeJobsActivity.this);
            TextView weightView = new TextView(EmployeeJobsActivity.this);
            TextView statusView = new TextView(EmployeeJobsActivity.this);

            // Set text.
            String[] jobAttributes = jobData.split(" ");
            jobIDView.setText(jobAttributes[0]);
            parcelIDView.setText(jobAttributes[4]);
            statusView.setText(jobAttributes[3]);

            // Style text.
            jobIDView.setPadding(10, 45, 25, 0);
            jobIDView.setTextSize(15f);
            jobIDView.setTypeface(null, Typeface.BOLD);
            jobIDView.setWidth(250);
            jobIDView.setGravity(Gravity.CENTER);

            parcelIDView.setPadding(10, 15, 25, 0);
            parcelIDView.setTextSize(15f);
            parcelIDView.setTypeface(null, Typeface.BOLD);
            parcelIDView.setWidth(400);
            parcelIDView.setGravity(Gravity.CENTER);

            statusView.setPadding(10, 15, 25, 0);
            statusView.setTextSize(15f);
            statusView.setTypeface(null, Typeface.BOLD);

            if (jobAttributes[3].toLowerCase(Locale.ROOT).equals("pending")) {
                statusView.setTextColor(Color.GREEN);
            } else {
                statusView.setTextColor(Color.RED);
            }

            // Add text views to text layout.
            textLayout.addView(jobIDView);
            textLayout.addView(parcelIDView);
            textLayout.addView(statusView);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(40, 0, 0, 0);
            textLayout.setLayoutParams(lp);

            // Add text layout and button to relative layout.
            relativeLayout.addView(textLayout);

            // Align text to the left side of page.
            RelativeLayout.LayoutParams textViewLayoutParams =
                    (RelativeLayout.LayoutParams) textLayout.getLayoutParams();
            textViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            jobsLayout.addView(relativeLayout);
        }
    }

    private void populateTestData() {
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Completed");
        jobsData.add("13 4x3x2 23.6 Completed");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Completed");
        jobsData.add("13 4x3x2 23.6 Completed");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Completed");
        jobsData.add("13 4x3x2 23.6 Completed");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Pending");
        jobsData.add("13 4x3x2 23.6 Completed");
    }

    private void populateData() {
        try {
            StringBuilder content = new StringBuilder();
            // Create connection to web api servlet via post
            URL openUrl = new URL(Common.connectionStringJobs);
            HttpURLConnection connection = (HttpURLConnection) openUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(1000000);
            connection.setReadTimeout(5000000);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Send login credentials to servlet
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            String params = "username=" + staffUsername + "&password=" + password;
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
            String[] jobs = content.toString().split(";");
            jobsData.clear();

            for (String job : jobs) {
                if (job.equals("success")) {
                    continue;
                } else if (job.equals("failure")) {
                    return;
                }

                String[] jobComponents = job.split(",");

                String result =
                        jobComponents[0] +
                                " " +
                                jobComponents[5] +
                                " " +
                                jobComponents[4] +
                                " " +
                                jobComponents[2].substring(0, 1).toUpperCase(Locale.ROOT) + jobComponents[2].substring(1) +
                                " " +
                                jobComponents[3];

                jobsData.add(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}