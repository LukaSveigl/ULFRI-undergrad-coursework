package com.example.sfl_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sfl_mobile.databinding.ActivityJobsBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.mlkit.vision.barcode.*;
import com.google.mlkit.vision.common.InputImage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScannedBarcodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView textBarcodeValue;
    Button completeButton;

    private BarcodeDetector barcodeDetector;
    private BarcodeScanner barcodeScanner;
    private CameraSource cameraSource;

    private static final int REQUEST_CAMERA_PERMISSION = 201;

    String intentData = "";

    String displayName = "";
    String username = "";
    String password = "";
    String parcelID = "";
    String jobID = "";

    boolean succesfull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);

        surfaceView = findViewById(R.id.surfaceView);
        textBarcodeValue = findViewById(R.id.txtBarcodeValue);
        completeButton = findViewById(R.id.btnAction);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            displayName = extras.getString("displayName");
            username = extras.getString("username");
            password = extras.getString("password");
            parcelID = extras.getString("parcelID");
            jobID = extras.getString("jobID");
        }

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intentData.length() > 0) {
                    if (!intentData.trim().equals(parcelID)) {
                        return;
                    }

                    System.out.println("Intent Data: " + intentData);

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            if (verifyParcelId()) {
                                succesfull = true;
                            }
                        }
                    });

                    if (!executorService.isTerminated()) {
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

                    if (succesfull) {
                        Intent i = new Intent(ScannedBarcodeActivity.this, JobsActivity.class);
                        i.putExtra("displayName", displayName);
                        i.putExtra("username", username);
                        i.putExtra("password", password);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        barcodeScanner = BarcodeScanning.getClient(options);
        /*BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        barcodeDetector = BarcodeScanning.getClient();*/

        /*barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            ScannedBarcodeActivity.
                                    this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    }
                    else {
                        ActivityCompat.requestPermissions(
                                ScannedBarcodeActivity.this,
                                new String[] {Manifest.permission.CAMERA},
                                REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                // Ignore
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(),
                        "To prevent memory leaks, barcode scanner has been stopped",
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    textBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            intentData = barcodes.valueAt(0).displayValue;
                            textBarcodeValue.setText(intentData);
                        }
                    });
                }
            }
        });*/
    }

    private class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
        @Override
        public void analyze(ImageProxy imageProxy) {
            @SuppressLint("UnsafeOptInUsageError") Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            }

        }
    }

    private boolean verifyParcelId() {
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
            String params = "username=" + username + "&password=" + password +
                    //"&jobID=" + jobID + "&newStatus=2";
                    "&jobID=37&newStatus=2";
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
            String[] result = content.toString().split(";");

            if (result[0].equals("success")) {
                System.out.println("success!");
                return true;
            } else if (result[0].equals("failure")) {
                System.out.println("failure");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}