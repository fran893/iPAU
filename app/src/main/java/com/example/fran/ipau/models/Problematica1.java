package com.example.fran.ipau.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Problematica1 implements Parcelable {

    private int idProblematica1;
    private String descripcion;
    private List<Problematica2> subProblematicas2;
    private String rutaImg;

    public Problematica1(int idProblematica1, String descripcion, List<Problematica2> subProblematicas2, String rutaImg) {
        this.idProblematica1 = idProblematica1;
        this.descripcion = descripcion;
        this.subProblematicas2 = subProblematicas2;
        this.rutaImg = rutaImg;
    }

    public Problematica1(){}

    protected Problematica1(Parcel in) {
        idProblematica1 = in.readInt();
        descripcion = in.readString();
        subProblematicas2 = in.createTypedArrayList(Problematica2.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idProblematica1);
        dest.writeString(descripcion);
        dest.writeTypedList(subProblematicas2);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Problematica1> CREATOR = new Creator<Problematica1>() {
        @Override
        public Problematica1 createFromParcel(Parcel in) {
            return new Problematica1(in);
        }

        @Override
        public Problematica1[] newArray(int size) {
            return new Problematica1[size];
        }
    };

    public int getIdProblematica1() {
        return idProblematica1;
    }

    public void setIdProblematica1(int idProblematica1) {
        this.idProblematica1 = idProblematica1;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Problematica2> getSubProblematicas2() {
        return subProblematicas2;
    }

    public String getRutaImg() {
        return rutaImg;
    }

    public void setRutaImg(String rutaImg) {
        this.rutaImg = rutaImg;
    }

    public void setSubProblematicas2(List<Problematica2> subProblematicas2) {
        this.subProblematicas2 = subProblematicas2;
    }
}
