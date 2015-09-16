package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Collection;

public class Preferencias implements Serializable {


    private boolean selected;
    @Expose
    private Integer nroIntPref;
    @Expose
    private String nome;
    @Expose
    private String status;
    @Expose
    private Integer valor;
    @Expose
    private Collection<Ponto> pontoCollection;

    public Preferencias(String nome, int nroIntPref) {
        super();
        this.nome = nome;
        this.selected = false;
        this.nroIntPref = nroIntPref;
    }

    public Preferencias() {
    }

    public Preferencias(Integer nroIntPref) {
        this.nroIntPref = nroIntPref;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getNroIntPref() {
        return nroIntPref;
    }

    public void setNroIntPref(Integer nroIntPref) {
        this.nroIntPref = nroIntPref;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Collection<Ponto> getPontoCollection() {
        return pontoCollection;
    }

    public void setPontoCollection(Collection<Ponto> pontoCollection) {
        this.pontoCollection = pontoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroIntPref != null ? nroIntPref.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Preferencias)) {
            return false;
        }
        Preferencias other = (Preferencias) object;
        if ((this.nroIntPref == null && other.nroIntPref != null) || (this.nroIntPref != null && !this.nroIntPref.equals(other.nroIntPref))) {
            return false;
        }
        return true;
    }
}