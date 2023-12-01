package com.example.pocketinventory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

/**
 * This class is the scan serial number activity of the app. It gives the user access to the camera to take
 * photos of serial number and save the serial number in the item details
 */
public class ScanSerialNumberActivity extends AppCompatActivity {

    // UI elements
    private Button scanSerialNumberButton;
    private EditText serialNumberEditText;
    private TextView recognizedTextView;
    private Button confirmButton;
    private Button retakeButton;
    private ShapeableImageView serialNumberImage;
    private String recognizedText;

    // URI for the captured serial number image
    private Uri serialNumberUri = null;

    // Text recognizer for extracting text from image
    private TextRecognizer textRecognizer;

    /**
     * Called when the activity is starting
     *
     * Citation: Used the resource: https://www.youtube.com/watch?v=1wewsm0Av98 to code many parts of this class
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_serial_number);

        // Retrieve item details from the intent
        Intent intent = getIntent();
        Item itemSerialNumber = intent.getParcelableExtra("item");

        // Initialize UI elements
        scanSerialNumberButton = findViewById(R.id.scan_serial_number_button);
        serialNumberEditText = findViewById(R.id.serial_number_edit_text);
        recognizedTextView = findViewById(R.id.recognize_text_view);
        serialNumberImage = findViewById(R.id.serial_number_image);
        confirmButton = findViewById(R.id.confirm_serial_number_button);
        retakeButton = findViewById(R.id.retake_serial_number_button);

        // Initialize TextRecognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Handle the click on the "Scan Serial No." Button
        scanSerialNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If access to camera is granted, start the camera
                if (checkCameraPermission()) {
                    pickImageCamera();
                } else { // If access to camera is not granted, ask for the permission to access the camera
                    requestCameraPermission();
                }
            }
        });

        // Handle the click on the "Confirm" Button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set serial number in the item details
                itemSerialNumber.setSerialNumber(serialNumberEditText.getText().toString());

                // Reset UI elements
                confirmButton.setVisibility(View.GONE);
                recognizedTextView.setVisibility(View.GONE);
                retakeButton.setVisibility(View.GONE);

                scanSerialNumberButton.setVisibility(View.VISIBLE);

                // Prepare resultIntent and finish the activity
                Intent resultIntent = new Intent();
                // Send the serialNumber (String) back to the ItemAddActivity
                resultIntent.putExtra("serialNumber", serialNumberEditText.getText().toString());
                // This activity is successful in generating the right result (serial number)
                setResult(RESULT_OK, resultIntent);
                // Terminate this activity to go back to the ItemAddActivity
                finish();
            }
        });

        // Handle the click on the "Retake" Button
        retakeButton.setOnClickListener(new View.OnClickListener() {
            // Check for camera permissions and start the camera intent again
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    pickImageCamera();
                } else {
                    requestCameraPermission();
                }
            }
        });

        // Have a menu displayed with a "Scan Serial Number" Title and back
        // button which takes the user back to the previously accessed activity
        ActionMode.Callback callback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the ActionMode
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.scan_serial_number_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Set the title for the ActionMode
                mode.setTitle("Scan Serial Number");
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Finish the activity when ActionMode is destroyed (i.e. when back button is clicked in the menu)
                finish();
            }
        };

        // Start the ActionMode (Menu)
        (this).startActionMode(callback);
    }
    /**
     * Recognizes text from the captured image using ML Kit's Text Recognition.
     */
    private void recognizeTextFromImage() {
        // Method to recognize text from the captured image
        try {
            // Get the image that needs text recognition
            InputImage inputImage = InputImage.fromFilePath(this, serialNumberUri);

            // Start the text recognition process with the image that needs text recognition
            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    // If the text recognition was successful, i.e., some text is detected
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            // Set recognized text in the serialNumberEditText
                            recognizedText = text.getText();
                            serialNumberEditText.setText(recognizedText);
                        }
                    })
                    // If the text recognition was unsuccessful, i.e., there is trouble recognizing text
                    // when the text is present (Can happen when the image with the text is blurry and unclear)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Toast a message regarding the recognizing text failure
                            Toast.makeText(ScanSerialNumberActivity.this, "Failure recognizing text due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            // Toast a message regarding image preparation failure
            Toast.makeText(ScanSerialNumberActivity.this, "Failed preparing image due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Launches the camera and captures an image.
     */
    private void pickImageCamera() {
        // Method to launch the camera and capture an image

        // Set values of the camera intent
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Click an Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Scan Serial Number");

        // Insert the image file (serialNumberUri) and get its URI
        serialNumberUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Start the camera intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, serialNumberUri);
        cameraActivityResultLauncher.launch(intent);
    }
    /**
     * Handles the result of the camera intent.
     */
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                // ActivityResultLauncher to handle camera intent result
                @Override
                public void onActivityResult(ActivityResult result) {
                    // If image is captured successfully
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Set the image on the app screen
                        serialNumberImage.setImageURI(serialNumberUri);

                        // Make the EditText with the recognized serial number and recognizedTextView visible
                        recognizedTextView.setVisibility(View.VISIBLE);
                        serialNumberEditText.setVisibility(View.VISIBLE);

                        // Make the "Scan Serial No." Button gone
                        scanSerialNumberButton.setVisibility(View.GONE);

                        // Make buttons, retake and confirm, visible
                        confirmButton.setVisibility(View.VISIBLE);
                        retakeButton.setVisibility(View.VISIBLE);

                        // Recognize text from the captured image
                        recognizeTextFromImage();
                    } else {
                        // Image capture canceled

                        // Toast a message telling image capturing is cancelled
                        Toast.makeText(ScanSerialNumberActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

                        // End the camera activity
                        finish();
                    }
                }
            }
    );
    /**
     * Checks if camera permission is granted.
     *
     * @return True if allowed, false if not allowed.
     */
    private Boolean checkCameraPermission() {

        Boolean cameraPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return cameraPermissionResult;
    }

    /**
     * Requests camera permission.
     */
    private void requestCameraPermission() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 0);
    }

}