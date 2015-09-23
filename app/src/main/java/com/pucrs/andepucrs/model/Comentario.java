package com.pucrs.andepucrs.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.annotations.Expose;

import java.io.Serializable;


public class Comentario implements Serializable {
    @Expose
    private Integer nroIntComentario;
    @Expose
    private Double latitudeOrigem;
    @Expose
    private Double longitudeOrigem;
    @Expose
    private Double latitudeDestino;
    @Expose
    private Double longitudeDestino;
    @Expose
    private String comentario;
    @Expose
    private Usuario nroIntUsuario;
    @Expose
    private int valor;

    public Comentario() {
    }

    public Comentario(Double latitudeOrigem, Double longitudeOrigem, Double latitudeDestino, Double longitudeDestino, String comentario,int valor, Usuario nroIntUsuario) {
        this.latitudeOrigem = latitudeOrigem;
        this.longitudeOrigem = longitudeOrigem;
        this.latitudeDestino = latitudeDestino;
        this.longitudeDestino = longitudeDestino;
        this.comentario = comentario;
        this.nroIntUsuario = nroIntUsuario;
        this.valor = valor;
    }

    public Comentario(Integer nroIntComentario) {
        this.nroIntComentario = nroIntComentario;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Integer getNroIntComentario() {
        return nroIntComentario;
    }

    public void setNroIntComentario(Integer nroIntComentario) {
        this.nroIntComentario = nroIntComentario;
    }

    public Double getLatitudeOrigem() {
        return latitudeOrigem;
    }

    public void setLatitudeOrigem(Double latitudeOrigem) {
        this.latitudeOrigem = latitudeOrigem;
    }

    public Double getLongitudeOrigem() {
        return longitudeOrigem;
    }

    public void setLongitudeOrigem(Double longitudeOrigem) {
        this.longitudeOrigem = longitudeOrigem;
    }

    public Double getLatitudeDestino() {
        return latitudeDestino;
    }

    public void setLatitudeDestino(Double latitudeDestino) {
        this.latitudeDestino = latitudeDestino;
    }

    public Double getLongitudeDestino() {
        return longitudeDestino;
    }

    public void setLongitudeDestino(Double longitudeDestino) {
        this.longitudeDestino = longitudeDestino;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getNroIntUsuario() {
        return nroIntUsuario;
    }

    public void setNroIntUsuario(Usuario nroIntUsuario) {
        this.nroIntUsuario = nroIntUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroIntComentario != null ? nroIntComentario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comentario)) {
            return false;
        }
        Comentario other = (Comentario) object;
        if ((this.nroIntComentario == null && other.nroIntComentario != null) || (this.nroIntComentario != null && !this.nroIntComentario.equals(other.nroIntComentario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "nroIntComentario=" + nroIntComentario +
                ", latitudeOrigem=" + latitudeOrigem +
                ", longitudeOrigem=" + longitudeOrigem +
                ", latitudeDestino=" + latitudeDestino +
                ", longitudeDestino=" + longitudeDestino +
                ", comentario='" + comentario + '\'' +
                ", nroIntUsuario=" + nroIntUsuario +
                '}';
    }
}
