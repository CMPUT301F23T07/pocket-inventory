package com.example.pocketinventory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel for managing and providing data related to item selection, specifically number of items selected.
 *
 * The `SelectionViewModel` class is responsible for maintaining a LiveData object
 * that holds a text value, which is used to represent the count of selected items.
 * It provides methods to set and retrieve this text value.
 */
public class SelectionViewModel extends ViewModel {

    // MutableLiveData to hold the text data
    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    /**
     * Set the text value in the ViewModel.
     *
     * @param item The new text value to be set in the ViewModel.
     */
    public void setText(String item) {
        mutableLiveData.setValue(item);
    }

    /**
     * Get the LiveData that holds the text value.
     *
     * @return The LiveData object containing the text value.
     */
    public MutableLiveData<String> getText() {
        return mutableLiveData;
    }
}
