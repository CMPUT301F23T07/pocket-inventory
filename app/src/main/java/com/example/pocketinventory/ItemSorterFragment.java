package com.example.pocketinventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



/**
 * This class represents a dialog fragment for sorting items in the inventory
 */
public class ItemSorterFragment extends DialogFragment {

    private ArrayList<Item> dataList;

    private ItemAdapter adapter;

    private  TagAdapter tagAdapter;

    /**
     * Constructor for the dialog fragment
     * @param dataList  The list of items to be sorted
     * @param adapter   Adapter for the recycler view. Used to notify the adapter when the list is sorted
     */
    public ItemSorterFragment(ArrayList<Item> dataList, ItemAdapter adapter) {
        this.dataList = dataList;
        this.adapter = adapter;
    }

    //Buttons for sorting functionalities
    Button Sort_Date_Asc;
    Button Sort_Date_Des;
    Button Sort_Description_Asc;
    Button Sort_Description_Des;
    Button Sort_Make_Asc;
    Button Sort_Make_Des;
    Button Sort_Value_Asc;
    Button Sort_Value_Des;
    Button Sort_Tag_Asc;
    Button Sort_Tag_Des;

    /**
     * This method is called to create the dialog fragment, and returns the dialog after sorting
     * the list of items
     * @param   savedInstanceState  Saved instance state
     * @return  Dialog fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_sort_fragment, null);

        //Sort items by ascending order of date
        Sort_Date_Asc = view.findViewById(R.id.Date_btn_asc);
        Sort_Date_Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Item>() {

                    @Override
                    public int compare(Item item1, Item item2) {
                        return item1.getDate().compareTo(item2.getDate());
                    }
                });
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        //Sort items by descending order of date
        Sort_Date_Des = view.findViewById(R.id.Date_btn_des);
        Sort_Date_Des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        return item1.getDate().compareTo(item2.getDate());
                    }
                });
                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        //Sort items by ascending order of description(Case insensitive, meaning Uppercase/Lowercase doesn't matter)
        Sort_Description_Asc = view.findViewById(R.id.Description_btn_asc);
        Sort_Description_Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        String des1 = item1.getDescription().toLowerCase();
                        String des2 = item2.getDescription().toLowerCase();
                        return String.CASE_INSENSITIVE_ORDER.compare(des1, des2);
                    }
                });
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        //Sort items by descending order of description(Case insensitive, meaning Uppercase/Lowercase doesn't matter)
        Sort_Description_Des = view.findViewById(R.id.Description_btn_des);
        Sort_Description_Des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        String des1 = item1.getDescription().toLowerCase();
                        String des2 = item2.getDescription().toLowerCase();
                        return String.CASE_INSENSITIVE_ORDER.compare(des1, des2);
                    }
                });
                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        //Sort items by ascending order of make(Case insensitive, meaning Uppercase/Lowercase doesn't matter)
        Sort_Make_Asc = view.findViewById(R.id.Make_btn_asc);
        Sort_Make_Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        String des1 = item1.getMake().toLowerCase();
                        String des2 = item2.getMake().toLowerCase();
                        return String.CASE_INSENSITIVE_ORDER.compare(des1, des2);
                    }
                });
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        //Sort items by descending order of make(Case insensitive, meaning Uppercase/Lowercase doesn't matter)
        Sort_Make_Des = view.findViewById(R.id.Make_btn_des);
        Sort_Make_Des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        String des1 = item1.getMake().toLowerCase();
                        String des2 = item2.getMake().toLowerCase();
                        return String.CASE_INSENSITIVE_ORDER.compare(des1, des2);
                    }
                });
                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        //Sort items by ascending order of value
        Sort_Value_Asc = view.findViewById(R.id.Value_btn_asc);
        Sort_Value_Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        return Double.compare(item1.getValue(), item2.getValue());
                    }
                });
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        //Sort items by descending order of value
        Sort_Value_Des = view.findViewById(R.id.Value_btn_des);
        Sort_Value_Des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        return Double.compare(item1.getValue(), item2.getValue());
                    }
                });
                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        Sort_Tag_Asc = view.findViewById(R.id.Tag_btn_asc);

        Sort_Tag_Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sort tags of item alphabetically
                for (Item item : dataList){
                    if(item.getTags() != null){
                        Collections.sort(item.getTags());
                    }
                }

                //Sort items by ascending order of tags
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        if(item1.getTags() != null && item1.getTags().isEmpty() != true){
                            //Both item1 and item2 have tags, Sort items by ascending order of tag(Case insensitive, meaning Uppercase/Lowercase doesn't matter)
                            if(item2.getTags() != null && item2.getTags().isEmpty() != true){
                                String tag1 = item1.getTags().get(0).toLowerCase();
                                String tag2 = item2.getTags().get(0).toLowerCase();
                                return String.CASE_INSENSITIVE_ORDER.compare(tag1, tag2);
                            }
                            //Only item1 has tags
                            else{
                                return -1;
                            }
                        }
                        //Only item2 has tags
                        else if(item2.getTags() != null && item2.getTags().isEmpty() != true){
                            return 1;
                        }
                        // Both item1 and item2 don't have tags
                        else{
                            return 0;
                        }
                    }
                });
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });


        Sort_Tag_Des = view.findViewById(R.id.Tag_btn_des);

        Sort_Tag_Des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sort tags of item alphabetically
                for (Item item : dataList){
                    if(item.getTags() != null){
                        Collections.sort(item.getTags());
                    }
                }

                //Sort items by descending order of tags
                Collections.sort(dataList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        if(item1.getTags() != null && item1.getTags().isEmpty() != true){
                            //Both item1 and item2 have tags, Sort items by descending order of tag(Case insensitive, meaning Uppercase/Lowercase doesn't matter)
                            if(item2.getTags() != null && item2.getTags().isEmpty() != true){
                                String tag1 = item1.getTags().get(0).toLowerCase();
                                String tag2 = item2.getTags().get(0).toLowerCase();
                                return String.CASE_INSENSITIVE_ORDER.compare(tag2, tag1);
                            }
                            //Only item1 has tags
                            else{
                                return -1;
                            }
                        }
                        //Only item2 has tags
                        else if(item2.getTags() != null && item2.getTags().isEmpty() != true){
                            return 1;
                        }
                        // Both item1 and item2 don't have tags
                        else{
                            return 0;
                        }
                    }
                });

                adapter.notifyDataSetChanged();
                dismiss();
            }
        });




        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Sort Item")
                .setNegativeButton("Cancel", null)
                .create();
    }



}