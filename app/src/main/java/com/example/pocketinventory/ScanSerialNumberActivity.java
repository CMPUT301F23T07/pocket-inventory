package com.example.pocketinventory;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is the scan serial number activity of the app. It gives the user access to the camera to take
 * photos of serial number and save the serial number in the item details
 */
public class ScanSerialNumberActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_serial_number);

        Intent intent = getIntent();
        Item item = intent.getParcelableExtra("item");

        Button scanSerialNumberButton = findViewById(R.id.scan_serial_number_button);
        ActionMode.Callback callback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.scan_serial_number_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        };

        // Call the ActionMode
        ((AppCompatActivity) this).startActionMode(callback);
    }
}
