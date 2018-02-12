package com.example.ice.screw;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ice on 17/6/10.
 */
public class Station implements Parcelable {
    private String no;
    private String name;
    private String address;
    private double longitude;
    private double latitude;

    public Station() {

    }

    public Station(String no, String name, String address, double longitude, double latitude) {
        this.no = no;
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Station(Parcel in) {
        no = in.readString();
        name = in.readString();
        address = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(no);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    public static final Parcelable.Creator<Station> CREATOR = new Creator<Station>() {

        @Override
        public Station createFromParcel(Parcel source) {
            Station station = new Station();
            station.no = source.readString();
            station.name = source.readString();
            station.address = source.readString();
            station.longitude = source.readDouble();
            station.latitude = source.readDouble();
            return station;
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
