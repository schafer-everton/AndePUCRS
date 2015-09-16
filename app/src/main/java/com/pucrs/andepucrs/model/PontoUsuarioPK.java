package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;



public class PontoUsuarioPK implements Serializable {
    @Expose
    private int nroIntPonto;
    @Expose
    private int nroIntUsuario;

    public PontoUsuarioPK() {
    }

    public PontoUsuarioPK(int nroIntPonto, int nroIntUsuario) {
        this.nroIntPonto = nroIntPonto;
        this.nroIntUsuario = nroIntUsuario;
    }

    public int getNroIntPonto() {
        return nroIntPonto;
    }

    public void setNroIntPonto(int nroIntPonto) {
        this.nroIntPonto = nroIntPonto;
    }

    public int getNroIntUsuario() {
        return nroIntUsuario;
    }

    public void setNroIntUsuario(int nroIntUsuario) {
        this.nroIntUsuario = nroIntUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) nroIntPonto;
        hash += (int) nroIntUsuario;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PontoUsuarioPK)) {
            return false;
        }
        PontoUsuarioPK other = (PontoUsuarioPK) object;
        if (this.nroIntPonto != other.nroIntPonto) {
            return false;
        }
        if (this.nroIntUsuario != other.nroIntUsuario) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        return "PontoUsuarioPK{" +
                "nroIntPonto=" + nroIntPonto +
                ", nroIntUsuario=" + nroIntUsuario +
                '}';
    }
}