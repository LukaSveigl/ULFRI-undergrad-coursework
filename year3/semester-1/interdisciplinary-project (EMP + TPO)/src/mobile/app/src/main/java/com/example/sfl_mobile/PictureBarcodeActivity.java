package com.example.sfl_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.camera.lifecycle.ProcessCameraProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

//import com.google.android.gms.vision.barcode.Barcode;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PictureBarcodeActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST_PERMISSION = 201;
    private final String TAG = "MLKit Barcode";

    private PreviewView previewView;
    private CameraSelector cameraSelector;
    private ProcessCameraProvider cameraProvider;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;

    private boolean success;

    private String displayName = "";
    private String username = "";
    private String password = "";
    private String parcelID = "";
    private String jobID = "";
    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_barcode);

        previewView = findViewById(R.id.previewView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            displayName = extras.getString("displayName");
            username = extras.getString("username");
            password = extras.getString("password");
            parcelID = extras.getString("parcelID");
            jobID = extras.getString("jobID");
            role = extras.getString("role");
        }
    }

    @Override
    protected void onResume() {
        System.out.println("Resumed");
        super.onResume();
        startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            setupCamera();
        }
        else {
            getPermission();
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (requestCode == CAMERA_REQUEST_PERMISSION) {
            setupCamera();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture =
                ProcessCameraProvider.getInstance(this);

        int lensFacing = CameraSelector.LENS_FACING_BACK;
        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();

        cameraProviderListenableFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderListenableFuture.get();
                bindAllCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));

        System.out.println("Camera set up");
    }

    private void bindAllCameraUseCases() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            bindPreviewUseCase();
            bindAnalysisUseCase();

            System.out.println("Camera use cases bound");
        }
    }

    private void bindPreviewUseCase() {
        if (cameraProvider == null) {
            return;
        }

        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase);
        }

        Preview.Builder builder = new Preview.Builder();
        builder.setTargetRotation(getRotation());

        previewUseCase = builder.build();
        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());

        try {
            cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return;
        }

        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }

        Executor cameraExecutor = Executors.newSingleThreadExecutor();

        ImageAnalysis.Builder builder = new ImageAnalysis.Builder();
        builder.setTargetRotation(getRotation());

        analysisUseCase = builder.build();
        analysisUseCase.setAnalyzer(cameraExecutor, this::analyze);

        try {
            cameraProvider.bindToLifecycle(this, cameraSelector, analysisUseCase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getRotation() throws NullPointerException {
        return previewView.getDisplay().getRotation();
    }


    @SuppressLint("UnsafeOptInUsageError")
    private void analyze(@NonNull ImageProxy image) {
        if (image.getImage() == null) {
            return;
        }

        InputImage inputImage = InputImage.fromMediaImage(
                image.getImage(),
                image.getImageInfo().getRotationDegrees()
        );

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient(options);

        barcodeScanner.process(inputImage)
                .addOnSuccessListener(this::onSuccessListener)
                .addOnFailureListener(e -> Log.e(TAG, "Barcode process failure", e))
                .addOnCompleteListener(task -> image.close());
    }

    private void onSuccessListener(List<Barcode> barcodes) {
        if (barcodes.size() < 1) {
            return;
        }

        Toast.makeText(this, barcodes.get(0).getDisplayValue(), Toast.LENGTH_SHORT).show();

        success = false;

        if (!barcodes.get(0).getDisplayValue().equals(parcelID)) {
            return;
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::verifyBarcodeValue);
        if (!executorService.isTerminated()) {
            executorService.shutdown();
            try {
                if (executorService.awaitTermination(20, TimeUnit.SECONDS)) {
                    Toast.makeText(this, "Barcode scanning successful", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Barcode scanning unsuccessful", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (success) {
            Intent i = new Intent(PictureBarcodeActivity.this, JobsActivity.class);
            i.putExtra("displayName", displayName);
            i.putExtra("username", username);
            i.putExtra("password", password);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            this.setResult(100);
            this.finish();
        }
    }

    private void verifyBarcodeValue() {
        try {
            StringBuilder content = new StringBuilder();
            // Create connection to web api servlet via post
            URL openUrl = new URL(Common.connectionStringJobsUpdate);
            HttpURLConnection connection = (HttpURLConnection) openUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(1000000);
            connection.setReadTimeout(5000000);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            System.out.println("starting to send data.");

            // Send login credentials to servlet
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            String params = "username=" + username + "&password=" + password +
                    "&jobID=" + jobID + "&newStatus=2" + "&role=" + role + "&parcelID=" + parcelID;
            out.writeBytes(params);
            out.flush();
            out.close();

            System.out.println("data sent");
            System.out.println("starting to receive data");

            // Receive success state from login servlet
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            System.out.println("data received");

            // Close connection to servlet
            connection.disconnect();

            System.out.println("connection stopped");

            // Generate result based on servlet response content
            String[] result = content.toString().split(";");

            if (result[0].equals("success")) {
                System.out.println("success!");
                success = true;
                return;
            } else if (result[0].equals("failure")) {
                System.out.println("failure");
                success = false;
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        success = false;
    }
}