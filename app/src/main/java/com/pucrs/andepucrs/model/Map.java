package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ScHaFeR on 12/10/2015.
 */
public class Map implements Serializable {
    @Expose
    private Integer nroIntMapa;
    @Expose
    private Integer x;
    @Expose
    private Integer y;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;
    @Expose
    private Integer valor;

    public Map() {
    }

    public Map(Integer nroIntMapa) {
        this.nroIntMapa = nroIntMapa;
    }

    public Integer getNroIntMapa() {
        return nroIntMapa;
    }

    public void setNroIntMapa(Integer nroIntMapa) {
        this.nroIntMapa = nroIntMapa;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroIntMapa != null ? nroIntMapa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Map)) {
            return false;
        }
        Map other = (Map) object;
        if ((this.nroIntMapa == null && other.nroIntMapa != null) || (this.nroIntMapa != null && !this.nroIntMapa.equals(other.nroIntMapa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Map{" +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", valor=" + valor +
                '}';
    }
}