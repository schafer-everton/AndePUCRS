package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Preference implements Serializable {

    @Expose
    private String nome;
    @Expose
    private int nroIntPref;
    @Expose
    private String status;
    @Expose
    private int valor;
    private boolean selected;

    public Preference(String nome, int nroIntPref) {
        super();
        this.nome = nome;
        this.selected = false;
        this.nroIntPref = nroIntPref;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Preference{" +
                "nome='" + nome + '\'' +
                ", nroIntPref=" + nroIntPref +
                ", status='" + status + '\'' +
                ", valor=" + valor +
                ", selected=" + selected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Preference)) return false;

        Preference that = (Preference) o;

        if (getNroIntPref() != that.getNroIntPref()) return false;
        if (getValor() != that.getValor()) return false;
        if (isSelected() != that.isSelected()) return false;
        if (getNome() != null ? !getNome().equals(that.getNome()) : that.getNome() != null)
            return false;
        return !(getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null);

    }

    @Override
    public int hashCode() {
        int result = getNome() != null ? getNome().hashCode() : 0;
        result = 31 * result + getNroIntPref();
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + getValor();
        result = 31 * result + (isSelected() ? 1 : 0);
        return result;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNroIntPref() {
        return nroIntPref;
    }

    public void setNroIntPref(int nroIntPref) {
        this.nroIntPref = nroIntPref;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}