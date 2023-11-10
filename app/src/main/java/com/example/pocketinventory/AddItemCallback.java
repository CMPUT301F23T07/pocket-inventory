package com.example.pocketinventory;

/**
 * A callback interface to handle the result of adding an item.
 */
public interface AddItemCallback {

    /**
     * Called when an item is successfully added.
     *
     * @param item The item that has been added.
     */
    void onItemAdded(Item item);

    /**
     * Called when an error occurs during the item addition process.
     *
     * @param e The exception that occurred.
     */
    void onError(Exception e);
}
