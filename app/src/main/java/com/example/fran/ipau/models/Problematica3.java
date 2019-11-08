package com.example.fran.ipau.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Problematica3 implements Parcelable, Comparable<Problematica3> {

    private int idProblematica3;
    private String descripcion;

    public Problematica3(int idProblematica3, String descripcion) {
        this.idProblematica3 = idProblematica3;
        this.descripcion = descripcion;
    }

    public Problematica3(){}

    protected Problematica3(Parcel in) {
        idProblematica3 = in.readInt();
        descripcion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idProblematica3);
        dest.writeString(descripcion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Problematica3> CREATOR = new Creator<Problematica3>() {
        @Override
        public Problematica3 createFromParcel(Parcel in) {
            return new Problematica3(in);
        }

        @Override
        public Problematica3[] newArray(int size) {
            return new Problematica3[size];
        }
    };

    public int getIdProblematica3() {
        return idProblematica3;
    }

    public void setIdProblematica3(int idProblematica3) {
        this.idProblematica3 = idProblematica3;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString(){
        return this.descripcion;
    }


    @Override
    public int compareTo(Problematica3 o) {
        if(this.idProblematica3 == o.getIdProblematica3() && this.descripcion.equals(o.getDescripcion()))
            return 1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object obj){
        Problematica3 o = (Problematica3) obj;
        if(this.idProblematica3 == o.getIdProblematica3() && this.descripcion.equals(o.getDescripcion())){
            return true;
        }else{
            return false;
        }
    }
}
