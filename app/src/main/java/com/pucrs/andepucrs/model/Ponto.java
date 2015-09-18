
package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author ScHaFeR
 */
public class Ponto implements Serializable {
    @Expose
    private Integer nroIntPonto;
    @Expose
    private String descricao;
    @Expose
    private String status;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;
    @Expose
    private Preferencias nroIntPref;
    @Expose
    private Collection<PontoUsuario> pontoUsuarioCollection;

    public Ponto(String descricao, String status, Double latitude, Double longitude, Preferencias nroIntPref) {
        this.descricao = descricao;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nroIntPref = nroIntPref;
    }

    public Ponto() {
    }

    public Ponto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.descricao = "Waypoints";
        this.status = "ok";
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

    public Preferencias getNroIntPref() {
        return nroIntPref;
    }

    public void setNroIntPref(Preferencias nroIntPref) {
        this.nroIntPref = nroIntPref;
    }

    public Collection<PontoUsuario> getPontoUsuarioCollection() {
        return pontoUsuarioCollection;
    }

    public void setPontoUsuarioCollection(Collection<PontoUsuario> pontoUsuarioCollection) {
        this.pontoUsuarioCollection = pontoUsuarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroIntPonto != null ? nroIntPonto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ponto)) {
            return false;
        }
        Ponto other = (Ponto) object;
        if ((this.nroIntPonto == null && other.nroIntPonto != null) || (this.nroIntPonto != null && !this.nroIntPonto.equals(other.nroIntPonto))) {
            return false;
        }
        return true;
    }
}
