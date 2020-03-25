package com.example.fran.ipau.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Problematica2 implements Parcelable,Comparable<Problematica2> {

    private int idProblematica2;
    private String descripcion;
    private List<Problematica3> subProblematicas3;

    public Problematica2(int idProblematica2, String descripcion, List<Problematica3> subProblematicas3) {
        this.idProblematica2 = idProblematica2;
        this.descripcion = descripcion;
        this.subProblematicas3 = subProblematicas3;
    }

    public Problematica2(){}

    protected Problematica2(Parcel in) {
        idProblematica2 = in.readInt();
        descripcion = in.readString();
        subProblematicas3 = in.createTypedArrayList(Problematica3.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idProblematica2);
        dest.writeString(descripcion);
        dest.writeTypedList(subProblematicas3);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Problematica2> CREATOR = new Creator<Problematica2>() {
        @Override
        public Problematica2 createFromParcel(Parcel in) {
            return new Problematica2(in);
        }

        @Override
        public Problematica2[] newArray(int size) {
            return new Problematica2[size];
        }
    };

    public int getIdProblematica2() {
        return idProblematica2;
    }

    public void setIdProblematica2(int idProblematica2) {
        this.idProblematica2 = idProblematica2;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Problematica3> getSubProblematicas3() {
        return subProblematicas3;
    }

    public void setSubProblematicas3(List<Problematica3> subProblematicas3) {
        this.subProblematicas3 = subProblematicas3;
    }

    @Override
    public int compareTo(Problematica2 o) {
        if(this.idProblematica2 == o.getIdProblematica2() && this.descripcion.equals(o.getDescripcion())){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj){
        Problematica2 o = (Problematica2) obj;
        if(this.idProblematica2 == o.getIdProblematica2() && this.descripcion.equals(o.getDescripcion())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return this.descripcion;
    }
}
