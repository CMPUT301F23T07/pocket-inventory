package com.example.pocketinventory;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * This class manages the Firestore database for the items.
 * It manages all CRUD operations for the items.
 */
public class ItemDB {
    private static ItemDB instance; // Static instance of the class
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // Private constructor to prevent instantiation from other classes
    private ItemDB() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    // Static method to access the singleton instance
    public static ItemDB getInstance() {
        if (instance == null) {
            instance = new ItemDB();
        }
        return instance;
    }

    /**
     * This method adds an item to the database.
     * @param item The item to be added to the database.
     */
    public void addItem(Item item) {
        //String userId = auth.getCurrentUser().getUid();
        //item.setUserId(userId);

        // add item and set id
        db.collection("items").add(item).addOnSuccessListener(documentReference -> {
            item.setId(documentReference.getId());
            updateItem(item);
        });
    }

    public void updateItem(Item item) {
        db.collection("items").document(item.getId()).set(item);
    }

    public void deleteItem(Item item) {
        db.collection("items").document(item.getId()).delete();
    }

    public void getAllItems(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("items").get().addOnCompleteListener(listener);
    }



}
