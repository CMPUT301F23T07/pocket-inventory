package com.example.pocketinventory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

/**
 * This class is the activity that allows the user to add a new item to the inventory.
 */
public class ItemAddActivity extends AppCompatActivity {

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

        TextInputLayout makeInput = findViewById(R.id.make_text);
        TextInputLayout modelInput = findViewById(R.id.model_text);
        TextInputLayout serialInput = findViewById(R.id.serial_number_text);
        TextInputLayout estimatedValueInput = findViewById(R.id.estimated_value_text);
        TextInputLayout dateOfPurchaseInput = findViewById(R.id.date_of_purchase_text);
        TextInputLayout descriptionInput = findViewById(R.id.description_text);
        TextInputLayout commentInput = findViewById(R.id.comment_text);

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
                String make = makeInput.getEditText().getText().toString();
                String model = modelInput.getEditText().getText().toString();
                String serialNumber = serialInput.getEditText().getText().toString();
                String estimatedValue = estimatedValueInput.getEditText().getText().toString();
                String dateOfPurchase = dateOfPurchaseInput.getEditText().getText().toString();
                String description = descriptionInput.getEditText().getText().toString();
                String comment = commentInput.getEditText().getText().toString();

                // if any field other than comment is empty, display a toast dialog and return
                if (make.isEmpty() || model.isEmpty() || serialNumber.isEmpty() || estimatedValue.isEmpty() || dateOfPurchase.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Item item = new Item(parseDate(dateOfPurchase), make, model, description, Double.parseDouble(estimatedValue), comment, serialNumber);
                Intent intent = new Intent();
                intent.putExtra("item", item);
                setResult(RESULT_OK, intent);
                finish();
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
}