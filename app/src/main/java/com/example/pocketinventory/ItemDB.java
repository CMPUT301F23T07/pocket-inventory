package com.example.pocketinventory;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


/**
 * This class manages the Firestore database for the items.
 * It manages all CRUD operations for the items.
 * It is a singleton class.
 * Citations:
 * https://firebase.google.com/docs/android/setup#java
 * https://firebase.google.com/docs/firestore/quickstart#java_1
 */
public class ItemDB {
    private static ItemDB instance; // Static instance of the class
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    /**
     * This method is the constructor for the ItemDB class.
     */
    private ItemDB() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    /**
     * This method gets the instance of the ItemDB class.
     * @return The instance of the ItemDB class.
     */
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
    public void addItem(Item item, AddItemCallback callback) {
        db.collection("items").add(item).addOnSuccessListener(documentReference -> {
            item.setId(documentReference.getId());
            updateItem(item);
            callback.onItemAdded(item);
        }).addOnFailureListener(e -> {
            callback.onError(e);
        });
    }

    /**
     * This method updates an item in the database.
     * @param item The item to be updated in the database.
     */
    public void updateItem(Item item) {
        db.collection("items").document(item.getId()).set(item);
    }

    /**
     * This method deletes an item from the database.
     * @param item The item to be deleted from the database.
     */






    public void deleteItem(Item item) {
        try {
            db.collection("items").document(item.getId()).delete();
        } catch (Exception e) {
            System.out.println("Error deleting item");
        }
    }

    /**
     @@ -79,6 +83,16 @@ public void getAllItems(OnCompleteListener<QuerySnapshot> listener) {
     db.collection("items").whereEqualTo("userId", userId).get().addOnCompleteListener(listener);
     }


     /**
      * Remove all items for the current user from the database.
     */


    /**
     * This method gets all items from the database that are owned by the current user.
     * @param listener The listener to be called when the query is complete.
     */
    public void getAllItems(OnCompleteListener<QuerySnapshot> listener) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("items").whereEqualTo("userId", userId).get().addOnCompleteListener(listener);
    }

    /**
     * Remove all items for the current user from the database.
     */
    public void deleteAllItems() {
        getAllItems(task -> {
            List<Item> items = task.getResult().toObjects(Item.class);
            for (Item item : items) {
                deleteItem(item);
            }
        });
    }

}
