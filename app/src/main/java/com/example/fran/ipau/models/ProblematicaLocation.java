package com.example.fran.ipau.models;

public class ProblematicaLocation {

    private Integer idProblematicaLocation;
    private double latitud;
    private double longitud;
    private String descripcion;
    private Problematica3 problematica3;

    public ProblematicaLocation(){}

    public Integer getIdProblematicaLocation() {
        return idProblematicaLocation;
    }

    public void setIdProblematicaLocation(Integer idProblematicaLocation) {
        this.idProblematicaLocation = idProblematicaLocation;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Problematica3 getProblematica3() {
        return problematica3;
    }

    public void setProblematica3(Problematica3 problematica3) {
        this.problematica3 = problematica3;
    }
}
