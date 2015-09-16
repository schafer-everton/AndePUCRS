package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * @author ScHaFeR
 */
public class Location implements Serializable {
    @Expose
    private Integer nroIntLocalizacao;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;

    public Location() {
    }

    public Location( Double latitude, Double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(Integer nroIntLocalizacao) {
        this.nroIntLocalizacao = nroIntLocalizacao;
    }

    public Integer getNroIntLocalizacao() {
        return nroIntLocalizacao;
    }

    public void setNroIntLocalizacao(Integer nroIntLocalizacao) {
        this.nroIntLocalizacao = nroIntLocalizacao;
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


    @Override
    public String toString() {
        return "Location{" +
                "nroIntLocalizacao=" + nroIntLocalizacao +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        if (getNroIntLocalizacao() != null ? !getNroIntLocalizacao().equals(location.getNroIntLocalizacao()) : location.getNroIntLocalizacao() != null)
            return false;
        if (getLatitude() != null ? !getLatitude().equals(location.getLatitude()) : location.getLatitude() != null)
            return false;
        return !(getLongitude() != null ? !getLongitude().equals(location.getLongitude()) : location.getLongitude() != null);

    }

    @Override
    public int hashCode() {
        int result = getNroIntLocalizacao() != null ? getNroIntLocalizacao().hashCode() : 0;
        result = 31 * result + (getLatitude() != null ? getLatitude().hashCode() : 0);
        result = 31 * result + (getLongitude() != null ? getLongitude().hashCode() : 0);
        return result;
    }
}
