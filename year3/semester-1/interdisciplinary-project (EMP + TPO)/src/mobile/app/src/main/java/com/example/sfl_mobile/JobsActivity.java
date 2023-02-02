package com.example.sfl_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sfl_mobile.data.Result;
import com.example.sfl_mobile.data.model.LoggedInUser;
import com.example.sfl_mobile.databinding.ActivityJobsBinding;
import com.mysql.cj.core.exceptions.WrongArgumentException;

import org.w3c.dom.Text;

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

public class JobsActivity extends AppCompatActivity {
    TextView userData;
    ImageView logoView;

    Button pendingButton;
    Button completedButton;

    ScrollView jobsView;
    LinearLayout jobsLayout;

    ActivityJobsBinding binding;

    ArrayList<String> pendingJobsData = new ArrayList<>();
    ArrayList<String> completedJobsData = new ArrayList<>();

    String displayName;
    String username;
    String password;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        binding = ActivityJobsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getBindingViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String res = "\n" + extras.getString("displayName");
            displayName = extras.getString("displayName");
            username = extras.getString("username");
            password = extras.getString("password");
            role = extras.getString("role");
            userData.setText(extras.getString("displayName"));
            userData.setTextSize(17.5f);
            userData.setTypeface(null, Typeface.BOLD);
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

        fillLayout(pendingJobsData);

        pendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillLayout(pendingJobsData);
            }
        });

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillLayout(completedJobsData);
            }
        });
    }

    private void getBindingViews() {
        userData = binding.userTextView;
        logoView = binding.logoImageView;
        pendingButton = binding.buttonPending;
        completedButton = binding.buttonDone;
        jobsView = binding.jobsView;
        jobsLayout = binding.jobsLayout;
    }

    private void fillLayout(ArrayList<String> data) {
        jobsLayout.removeAllViews();

        RelativeLayout rl = new RelativeLayout(JobsActivity.this);
        LinearLayout ll = new LinearLayout(JobsActivity.this);
        TextView id = new TextView(JobsActivity.this);
        TextView dm = new TextView(JobsActivity.this);
        TextView wg = new TextView(JobsActivity.this);
        TextView st = new TextView(JobsActivity.this);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(40, 0, 0, 0);
        ll.setLayoutParams(llp);

        rl.setBackgroundColor(Color.LTGRAY);

        id.setText("ParcelID");
        dm.setText("Dimensions (cm)");
        wg.setText("Weight (kg)");

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

        for (String jobData : data) {
            RelativeLayout relativeLayout = new RelativeLayout(JobsActivity.this);
            LinearLayout textLayout = new LinearLayout(JobsActivity.this);

            TextView jobIDView = new TextView(JobsActivity.this);
            TextView dimensionsView = new TextView(JobsActivity.this);
            TextView weightView = new TextView(JobsActivity.this);

            Button button = new Button(JobsActivity.this);

            // Set text.
            String[] jobAttributes = jobData.split(" ");
            jobIDView.setText(jobAttributes[4]);
            dimensionsView.setText(jobAttributes[1]);
            weightView.setText(jobAttributes[2]);

            // Implement button functionality.
            button.setText("SCAN");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(JobsActivity.this, PictureBarcodeActivity.class);
                    i.putExtra("displayName", displayName);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    i.putExtra("jobID", jobAttributes[0]);
                    i.putExtra("parcelID", jobAttributes[4]);
                    i.putExtra("role", role);
                    startActivity(i);
                }
            });

            // Style text.
            jobIDView.setPadding(10, 45, 25, 0);
            jobIDView.setTextSize(15f);
            jobIDView.setTypeface(null, Typeface.BOLD);
            jobIDView.setWidth(250);
            jobIDView.setGravity(Gravity.CENTER);

            dimensionsView.setPadding(10, 15, 25, 0);
            dimensionsView.setTextSize(15f);
            dimensionsView.setTypeface(null, Typeface.BOLD);
            dimensionsView.setWidth(400);
            dimensionsView.setGravity(Gravity.CENTER);

            weightView.setPadding(10, 15, 25, 0);
            weightView.setTextSize(15f);
            weightView.setTypeface(null, Typeface.BOLD);
            weightView.setGravity(Gravity.CENTER);

            // Add text views to text layout.
            textLayout.addView(jobIDView);
            textLayout.addView(dimensionsView);
            textLayout.addView(weightView);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(40, 0, 0, 0);
            textLayout.setLayoutParams(lp);

            // Add text layout and button to relative layout.
            relativeLayout.addView(textLayout);
            // Only add button if job is pending.
            if (jobAttributes[3].toLowerCase(Locale.ROOT).equals("pending")) {
                relativeLayout.addView(button);
                // Align button to the right side of page.
                RelativeLayout.LayoutParams buttonParams =
                        (RelativeLayout.LayoutParams) button.getLayoutParams();
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                buttonParams.rightMargin = 40;
            }

            // Align text to the left side of page.
            RelativeLayout.LayoutParams textViewLayoutParams =
                    (RelativeLayout.LayoutParams) textLayout.getLayoutParams();
            textViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            jobsLayout.addView(relativeLayout);
        }
    }

    private void populateTestData() {
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");
        pendingJobsData.add("13 4x3x2 23.6 Pending");

        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
        completedJobsData.add("13 4x3x2 23.6 Completed");
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
            String[] jobs = content.toString().split(";");
            pendingJobsData.clear();
            completedJobsData.clear();

            for (String job : jobs) {
                if (job.equals("success")) {
                    continue;
                }
                else if (job.equals("failure")) {
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

                if (jobComponents[2].equals("pending")) {
                    pendingJobsData.add(result);
                }
                else {
                    completedJobsData.add(result);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}