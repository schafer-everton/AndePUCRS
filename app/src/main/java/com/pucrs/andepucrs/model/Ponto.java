/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pucrs.andepucrs.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author ScHaFeR
 */
public class Ponto implements Serializable {
    private Integer nroIntPonto;
    private String descricao;
    private String status;
    private Double latitude;
    private Double longitude;
    private Preference nroIntPref;
    private Collection<UserPoint> pontoUsuarioCollection;

    public Ponto(String descricao, String status, Double latitude, Double longitude, Preference nroIntPref) {
        this.descricao = descricao;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nroIntPref = nroIntPref;
    }

    public Ponto() {
    }


    public Ponto(Integer nroIntPonto) {
        this.nroIntPonto = nroIntPonto;
    }

    @Override
    public String toString() {
        return "Ponto{" +
                "nroIntPonto=" + nroIntPonto +
                ", descricao='" + descricao + '\'' +
                ", status='" + status + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nroIntPref=" + nroIntPref +
                ", pontoUsuarioCollection=" + pontoUsuarioCollection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ponto)) return false;

        Ponto ponto = (Ponto) o;

        if (getNroIntPonto() != null ? !getNroIntPonto().equals(ponto.getNroIntPonto()) : ponto.getNroIntPonto() != null)
            return false;
        if (getDescricao() != null ? !getDescricao().equals(ponto.getDescricao()) : ponto.getDescricao() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(ponto.getStatus()) : ponto.getStatus() != null)
            return false;
        if (getLatitude() != null ? !getLatitude().equals(ponto.getLatitude()) : ponto.getLatitude() != null)
            return false;
        if (getLongitude() != null ? !getLongitude().equals(ponto.getLongitude()) : ponto.getLongitude() != null)
            return false;
        if (getNroIntPref() != null ? !getNroIntPref().equals(ponto.getNroIntPref()) : ponto.getNroIntPref() != null)
            return false;
        return !(getPontoUsuarioCollection() != null ? !getPontoUsuarioCollection().equals(ponto.getPontoUsuarioCollection()) : ponto.getPontoUsuarioCollection() != null);

    }

    @Override
    public int hashCode() {
        int result = getNroIntPonto() != null ? getNroIntPonto().hashCode() : 0;
        result = 31 * result + (getDescricao() != null ? getDescricao().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getLatitude() != null ? getLatitude().hashCode() : 0);
        result = 31 * result + (getLongitude() != null ? getLongitude().hashCode() : 0);
        result = 31 * result + (getNroIntPref() != null ? getNroIntPref().hashCode() : 0);
        result = 31 * result + (getPontoUsuarioCollection() != null ? getPontoUsuarioCollection().hashCode() : 0);
        return result;
    }

    public Integer getNroIntPonto() {
        return nroIntPonto;
    }

    public void setNroIntPonto(Integer nroIntPonto) {
        this.nroIntPonto = nroIntPonto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Preference getNroIntPref() {
        return nroIntPref;
    }

    public void setNroIntPref(Preference nroIntPref) {
        this.nroIntPref = nroIntPref;
    }

    public Collection<UserPoint> getPontoUsuarioCollection() {
        return pontoUsuarioCollection;
    }

    public void setPontoUsuarioCollection(Collection<UserPoint> pontoUsuarioCollection) {
        this.pontoUsuarioCollection = pontoUsuarioCollection;
    }
}
