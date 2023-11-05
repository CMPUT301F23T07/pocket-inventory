package com.example.pocketinventory;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents an item in the inventory. It contains information about the item such as
 * the date it was purchased, the make and model, the estimated value, and a comment.
 * It also implements Parcelable so that it can be passed between activities.
 */
public class Item implements Parcelable {
    private String make;
    private String model;
    private String description;
    private String serialNumber;
    private Date date;
    private double value;
    private String comment;


    /**
     * Constructor for an item object
     * @param date
     * @param make
     * @param model
     * @param description
     * @param value
     * @param comment
     * @param serialNumber
     */
    public Item(Date date, String make, String model, String description, double value, String comment, String serialNumber) {

        this.make = make;
        this.model = model;
        this.description = description;
        this.serialNumber = serialNumber;
        this.value = value;
        this.comment = comment;
        this.date = date;
    }

    /**
     * Constructor for an item object that takes in a parcel
     * Citation: https://medium.com/techmacademy/how-to-implement-and-use-a-parcelable-class-in-android-part-1-28cca73fc2d1
     * @param in
     */
    protected Item(Parcel in) {
        make = in.readString();
        model = in.readString();
        date = (Date) in.readValue(Date.class.getClassLoader());
        description = in.readString();
        serialNumber = in.readString();
        value = in.readDouble();
        comment = in.readString();
    }

    /**
     * Getter for the date
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the date
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Getter for the value
     * @return value
     */
    public double getValue() {
        return value;
    }

    /**
     * Setter for the value
     * @param value
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Getter for the comment
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Setter for the comment
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Getter for the make
     * @return make
     */
    public String getMake() {
        return make;
    }

    /**
     * Setter for the make
     * @param make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Getter for the model
     * @return model
     */
    public String getModel() {
        return model;
    }

    /**
     * Setter for the model
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Getter for the serial number
     * @return serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Setter for the serial number
     * @param serialNumber
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Creator for the parcelable
     */
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

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(make);
        dest.writeString(model);
        dest.writeValue(date);
        dest.writeString(description);
        dest.writeString(serialNumber);
        dest.writeDouble(value);
        dest.writeString(comment);
    }

    /**
     * Returns a string representation of the item
     * @return string representation of the item
     */
    public String toString() {
        return "Make: " + make + ", Model: "+model+", value: $" + value + ", Date Purchased: "+date +
                ", Description: "+description+", Comment: "+comment;
    }
}
