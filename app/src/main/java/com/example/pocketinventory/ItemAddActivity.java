package com.example.pocketinventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import android.net.Uri;

/**
 * This class is the activity that allows the user to add a new item to the inventory.
 */
public class ItemAddActivity extends AppCompatActivity {

    private boolean isEditing = false;
    private ItemDB itemDB = ItemDB.getInstance();
    private Uri fileUri;
    private ArrayList<String> imageUrls = new ArrayList<>();


    /**
     * This method is called when the activity is created. It sets up the buttons and text fields
     * and reads in the data from the text fields when the add button is clicked.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        Button cancelButton = findViewById(R.id.cancel_button);
        Button addButton = findViewById(R.id.add_button);
        Button deleteButton = findViewById(R.id.delete_button);
        View deleteButtonSpacer = findViewById(R.id.delete_button_spacer);
        // Delete button is only visible if the user is editing an item
        deleteButton.setVisibility(View.GONE);
        deleteButtonSpacer.setVisibility(View.GONE);


        TextInputLayout makeInput = findViewById(R.id.make_text);
        TextInputLayout modelInput = findViewById(R.id.model_text);
        TextInputLayout serialInput = findViewById(R.id.serial_number_text);
        TextInputLayout estimatedValueInput = findViewById(R.id.estimated_value_text);
        TextInputLayout dateOfPurchaseInput = findViewById(R.id.date_of_purchase_text);
        TextInputLayout descriptionInput = findViewById(R.id.description_text);
        TextInputLayout commentInput = findViewById(R.id.comment_text);
        TextInputLayout tagsInput = findViewById(R.id.tag_input);

        RecyclerView recyclerView = findViewById(R.id.carousel_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Button uploadButton = findViewById(R.id.upload_image_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check camera permissions
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 0);
                } else {
                    // start camera activity and grab the image taken
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, 100);
                }
            }
        });



        // Check if an item was passed in from the previous activity
        Intent intent = getIntent();
        Item item = intent.getParcelableExtra("item");
        if (item != null) {
            isEditing = true;
            addButton.setText("Save");
            deleteButton.setVisibility(View.VISIBLE);
            deleteButtonSpacer.setVisibility(View.VISIBLE);

            // If an item was passed in, set the text fields to the item's values
            makeInput.getEditText().setText(item.getMake());
            modelInput.getEditText().setText(item.getModel());
            serialInput.getEditText().setText(item.getSerialNumber());
            estimatedValueInput.getEditText().setText(String.valueOf(item.getValue()));
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            dateOfPurchaseInput.getEditText().setText(dateFormat.format(item.getDate()));
            descriptionInput.getEditText().setText(item.getDescription());
            commentInput.getEditText().setText(item.getComment());

            // Retrieving the tags String from the item
            ArrayList<String> tagsList = item.getTags();
            StringBuilder tagsStringBuilder = new StringBuilder();

            // Iterates through the list of tags and combines them into a String devided by comma (,)
            for (int i = 0; i < tagsList.size(); i++){
                tagsStringBuilder.append(tagsList.get(i));
                if (i < tagsList.size() - 1){
                    tagsStringBuilder.append(", ");
                }
            }

            // Get the string from the StringBuilder
            String tagsString = tagsStringBuilder.toString();
            // Set the String of tags (comma seperating them) to the EditText associated with the tags
            tagsInput.getEditText().setText(tagsString);

            imageUrls = item.getImageUrls();
        }

        // set the images
        CarouselAdapter adapter = new CarouselAdapter(imageUrls);
        recyclerView.setAdapter(adapter);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // debug print item
                Log.d("ItemAddActivity", "onClick: " + item.toString());
                itemDB.deleteItem(item);
                setResult(RESULT_OK);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // Read in all of the text fields (some may be empty) and create an item object
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> tags;
                // Read in all of the text fields
                String make = makeInput.getEditText().getText().toString();
                String model = modelInput.getEditText().getText().toString();
                String serialNumber = serialInput.getEditText().getText().toString();
                String estimatedValue = estimatedValueInput.getEditText().getText().toString();
                String dateOfPurchase = dateOfPurchaseInput.getEditText().getText().toString();
                String description = descriptionInput.getEditText().getText().toString();
                String comment = commentInput.getEditText().getText().toString();
                String tagsString = tagsInput.getEditText().getText().toString().trim();
                // Create a new ArrayList to store tags.
                tags = new ArrayList<>();

                // Check if the String derived from the EditText is not empty.
                if (!tagsString.isEmpty()) {

                    // Split the String derived from the EditText into an array using commas as separators.
                    String[] tagArray = tagsString.split(",");

                    // Iterate through the elements in the 'tagArray'.
                    for (String tag : tagArray) {
                        // Check if the tag is not empty
                        if (!tag.trim().isEmpty()) {
                            // Add the non-empty tag to the 'tags' ArrayList
                            tags.add(tag.trim());
                        }
                    }
                }


                // if any field other than comment or serial number is empty, display a toast dialog and return
                if (make.isEmpty() || model.isEmpty() || estimatedValue.isEmpty() || dateOfPurchase.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If the user is editing an item, update the item in the database
                if (isEditing) {
                    item.setMake(make);
                    item.setModel(model);
                    item.setSerialNumber(serialNumber);
                    item.setValue(Double.parseDouble(estimatedValue));
                    item.setDate(parseDate(dateOfPurchase));
                    item.setDescription(description);
                    item.setComment(comment);
                    for (String tag:tags){
                        item.addTags(tag);
                    }
                    // log the id
                    Log.d("ItemAddActivity", "onClick: " + item.getId());
                    itemDB.updateItem(item);
                    setResult(RESULT_OK);
                    finish();
                } else { // Otherwise, create a new item and add it to the database

                    Item item = new Item(parseDate(dateOfPurchase), make, model, description, Double.parseDouble(estimatedValue), comment, serialNumber, tags);
                    // wait for the item to be added to the database
                    itemDB.addItem(item, new AddItemCallback() {
                        @Override
                        public void onItemAdded(Item item) {
                            setResult(RESULT_OK);
                            finish();
                        }
                        @Override
                        public void onError(Exception e) {
                            setResult(RESULT_CANCELED);
                            Toast.makeText(getApplicationContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        estimatedValueInput.getEditText().addTextChangedListener(new TextWatcher() {
            boolean isEditing = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            /**
             * This method is called when the text is changed. It ensures that the estimated value
             * is formatted correctly to two decimal places.
             * @param s
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;
                String value = s.toString();
                if (value.contains(".")) {
                    String[] parts = value.split("\\.");
                    if (parts.length > 1) {
                        String decimal = parts[1];
                        if (decimal.length() > 2) {
                            decimal = decimal.substring(0, 2);
                            value = parts[0] + "." + decimal;
                            estimatedValueInput.getEditText().setText(value);
                            estimatedValueInput.getEditText().setSelection(value.length());
                        }
                    }
                }
                isEditing = false;
            }
        });
    }

    /**
     * This method is called when the user clicks on the date of purchase text field. It opens a
     * date picker dialog.
     * @param v
     */
    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                // Do something with the selected date
                String selectedDate = (selectedMonth + 1) + "/" + selectedDayOfMonth + "/" + selectedYear;
                TextInputLayout dateOfPurchaseInput = findViewById(R.id.date_of_purchase_text);
                dateOfPurchaseInput.getEditText().setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // Prevents the user from selecting a future date
        datePickerDialog.show();
    }

    /**
     * This method parses a string to a date object
     * @param date
     * @return Date Object
     */
    private Date parseDate(String date) {
        String[] dateList = date.split("/");
        int month = Integer.parseInt(dateList[0]);
        int day = Integer.parseInt(dateList[1]);
        int year = Integer.parseInt(dateList[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    private Uri getOutputMediaFileUri() {
        // if build version is greater than or equal to 24, use file provider
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return androidx.core.content.FileProvider.getUriForFile(this, "com.example.pocketinventory.fileprovider", getOutputMediaFile());
        } else {
            return Uri.fromFile(getOutputMediaFile());
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PocketInventory");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Upload", "Failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            uploadImageToFirestore(fileUri);
        }
    }

    private void uploadImageToFirestore(Uri fileUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("images/" + fileUri.getLastPathSegment());

        imageRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Upload", "Image uploaded to Firestore successfully");
                        // Upload the image URL to Firestore and add it to the item
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrls.add(uri.toString());
                                Log.d("Upload", "Image URL retrieved successfully");
                                RecyclerView recyclerView = findViewById(R.id.carousel_recycler_view);
                                CarouselAdapter adapter = new CarouselAdapter(imageUrls);
                                recyclerView.setAdapter(adapter);
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Upload", "Failed to upload image to Firestore");
                    }
                });
    }
}