package com.example.pocketinventory;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

/**
 * This class is the scan serial number activity of the app. It gives the user access to the camera to take
 * photos of serial number and save the serial number in the item details
 */

public class ScanSerialNumberActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting
     *
     * Citation: Used the resource: https://www.youtube.com/watch?v=1wewsm0Av98 to code many parts of this class
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */

    private Button scanSerialNumberButton;
    private EditText serialNumberEditText;
    private Button recognizeTextButton;
    private Button confirmButton;
    private ShapeableImageView serialNumberImage;
    private String recognizedText;

    private Uri serialNumberUri = null;

    private String[] cameraPermissions;

    private TextRecognizer textRecognizer;

    private int CAMERA_REQUEST_CODE = 100;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_serial_number);

        Intent intent = getIntent();
        Item itemSerialNumber = intent.getParcelableExtra("item");

        scanSerialNumberButton = findViewById(R.id.scan_serial_number_button);
        serialNumberEditText = findViewById(R.id.serial_number_edit_text);
        recognizeTextButton = findViewById(R.id.recognize_text_button);
        serialNumberImage = findViewById(R.id.serial_number_image);
        confirmButton = findViewById(R.id.confirm_serial_number_button);

        // Initialize arrays of permission required for camera, gallery
        cameraPermissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


        // Initialize TextRecognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Handle the click on the "Scan Serial No." Button
        scanSerialNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()){
                    pickImageCamera();
                }
                else {
                    requestCameraPermission();
                }
            }
        });

        recognizeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (serialNumberUri == null){
                    Toast.makeText(ScanSerialNumberActivity.this, "Pick Image First", Toast.LENGTH_SHORT).show();
                }
                else {
                    recognizeTextFromImage();

                    confirmButton.setVisibility(View.VISIBLE);
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemSerialNumber.setSerialNumber(serialNumberEditText.getText().toString());
                            Toast.makeText(ScanSerialNumberActivity.this, itemSerialNumber.getSerialNumber(), Toast.LENGTH_SHORT).show();
                            confirmButton.setVisibility(View.GONE);
                            recognizeTextButton.setVisibility(View.GONE);


                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("serialNumber", serialNumberEditText.getText().toString());

                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                }
            }
        });

        // Have a menu displayed with a "Scan Serial Number" Title and back
        // button which takes the user back to the previously accessed activity
        ActionMode.Callback callback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.scan_serial_number_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("Scan Serial Number");
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                finish();
            }
        };

        // Call the ActionMode
        (this).startActionMode(callback);

    }


    private void recognizeTextFromImage() {

        try {
            InputImage inputImage = InputImage.fromFilePath(this, serialNumberUri);

            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            recognizedText = text.getText();
                            serialNumberEditText.setText(recognizedText);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ScanSerialNumberActivity.this, "Failure recognizing text due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(ScanSerialNumberActivity.this, "Failed preparing image due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void pickImageCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Click an Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Scan Serial Number");

        serialNumberUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, serialNumberUri);
        cameraActivityResultLauncher.launch(intent);

        recognizeTextButton.setVisibility(View.VISIBLE);
        serialNumberEditText.setVisibility(View.VISIBLE);
        scanSerialNumberButton.setVisibility(View.GONE);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // If taken by the camera, get the image
                    if (result.getResultCode() == Activity.RESULT_OK){
                        serialNumberImage.setImageURI(serialNumberUri);
                    }
                    else{
                        // cancelled
                        Toast.makeText(ScanSerialNumberActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );



    private Boolean checkCameraPermission(){
        /*
           Check if camera & storage permissions are allowed or not
           Return true if allowed, false if not allowed
         */
        Boolean cameraPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        return cameraPermissionResult;
    }

    private void requestCameraPermission(){
        // Request camera permissions (for camera intent)
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    // Handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0){
            Boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (cameraAccepted){
                pickImageCamera();
            }
            else {
                Toast.makeText(this, "Camera & Storage permissions are required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
