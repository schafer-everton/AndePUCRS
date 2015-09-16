package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * @author ScHaFeR
 */
public class PontoUsuario implements Serializable {
    @Expose
    protected PontoUsuarioPK pontoUsuarioPK;
    @Expose
    private String comentario;
    @Expose
    private Usuario usuario;
    @Expose
    private Ponto ponto;

    public PontoUsuario() {
    }

    public PontoUsuario(PontoUsuarioPK pontoUsuarioPK) {
        this.pontoUsuarioPK = pontoUsuarioPK;
    }

    public PontoUsuario(int nroIntPonto, int nroIntUsuario) {
        this.pontoUsuarioPK = new PontoUsuarioPK(nroIntPonto, nroIntUsuario);
    }

    public PontoUsuarioPK getPontoUsuarioPK() {
        return pontoUsuarioPK;
    }

    public void setPontoUsuarioPK(PontoUsuarioPK pontoUsuarioPK) {
        this.pontoUsuarioPK = pontoUsuarioPK;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pontoUsuarioPK != null ? pontoUsuarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PontoUsuario)) {
            return false;
        }
        PontoUsuario other = (PontoUsuario) object;
        if ((this.pontoUsuarioPK == null && other.pontoUsuarioPK != null) || (this.pontoUsuarioPK != null && !this.pontoUsuarioPK.equals(other.pontoUsuarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PontoUsuario{" +
                "pontoUsuarioPK=" + pontoUsuarioPK +
                ", comentario='" + comentario + '\'' +
                ", usuario=" + usuario +
                ", ponto=" + ponto +
                '}';
    }
}
