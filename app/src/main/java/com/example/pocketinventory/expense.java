package com.example.pocketinventory;

import android.os.Parcel;
import android.os.Parcelable;




import android.os.Parcel;
import android.os.Parcelable;

    public class expense implements Parcelable {
        private String date;
        private String make;
        private String model;
        private String description;

        private double value;
        private String comment;

        // Constructor to initialize an expense object


        public expense(String date, String make, String model, String description, double value, String comment) {

            this.make = make;
            this.model = model;
            this.date = date;
            this.description = description;

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

        // Parcelable implementation for passing expense objects between activities
        //Citation: https://medium.com/techmacademy/how-to-implement-and-use-a-parcelable-class-in-android-part-1-28cca73fc2d1
        protected expense(Parcel in) {


            date = in.readString();
            make = in.readString();
            value = in.readFloat();
            model = in.readString();
            description = in.readString();
            comment = in.readString();

        }

        public static final Creator<expense> CREATOR = new Creator<expense>() {
            @Override
            public expense createFromParcel(Parcel in) {
                return new expense(in);
            }

            @Override
            public expense[] newArray(int size) {
                return new expense[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(date);
            dest.writeDouble(value);
            dest.writeString(comment);
            dest.writeString(make);
            dest.writeString(model);
            dest.writeString(description);
        }

        // Generate a string representation of an expense
        public String toString() {
            return "Make: " + make + ", Model: "+model+", value: $" + value + ", Date Purchased: "+date +
                    ", Description: "+description+", Comment: "+comment;
        }
    }


