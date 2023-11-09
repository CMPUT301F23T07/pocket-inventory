package com.example.pocketinventory;

public interface AddItemCallback {
    void onItemAdded(Item item);
    void onError(Exception e);
}
