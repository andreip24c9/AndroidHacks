package com.andrei.host.domain.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Andrei on 24/03/2018.
 */
public class Car implements Parcelable {

    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("km")
    @Expose
    private String km;
    @SerializedName("picture")
    @Expose
    private String pictureUrl;

    public Car() {
    }

    public Car(String make, String model, String price, String year, String km, String pictureUrl) {
        this.make = make;
        this.model = model;
        this.price = price;
        this.year = year;
        this.km = km;
        this.pictureUrl = pictureUrl;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    private Car(Parcel in) {
        make = in.readString();
        model = in.readString();
        price = in.readString();
        year = in.readString();
        km = in.readString();
        pictureUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(make);
        dest.writeString(model);
        dest.writeString(price);
        dest.writeString(year);
        dest.writeString(km);
        dest.writeString(pictureUrl);
    }

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}
