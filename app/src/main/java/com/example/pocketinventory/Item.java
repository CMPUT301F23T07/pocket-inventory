package com.example.pocketinventory;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String date;
    private String make;
    private String model;
    private String description;
    private String serialNumber;

    private double value;
    private String comment;


    public Item(String date, String make, String model, String description, double value, String comment, String serialNumber) {

        this.make = make;
        this.model = model;
        this.date = date;
        this.description = description;
        this.serialNumber = serialNumber;
        this.value = value;
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public double getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    // Parcelable implementation for passing item objects between activities
    //Citation: https://medium.com/techmacademy/how-to-implement-and-use-a-parcelable-class-in-android-part-1-28cca73fc2d1
    protected Item(Parcel in) {
        make = in.readString();
        model = in.readString();
        date = in.readString();
        description = in.readString();
        serialNumber = in.readString();
        value = in.readDouble();
        comment = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(make);
        dest.writeString(model);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(serialNumber);
        dest.writeDouble(value);
        dest.writeString(comment);
    }

    // Generate a string representation of an item
    public String toString() {
        return "Make: " + make + ", Model: "+model+", value: $" + value + ", Date Purchased: "+date +
                ", Description: "+description+", Comment: "+comment;
    }
}
