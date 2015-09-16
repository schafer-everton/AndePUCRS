package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author ScHaFeR
 */
public class TipoUsuario implements Serializable {
    @Expose
    private Integer nroIntTipoUsuario;
    @Expose
    private String descricao;
    @Expose
    private Collection<Usuario> usuarioCollection;

    public TipoUsuario() {
        nroIntTipoUsuario = 2;
        descricao = "2-regular-user";
    }

    public TipoUsuario(Integer nroIntTipoUsuario) {
        this.nroIntTipoUsuario = nroIntTipoUsuario;
    }

    public Integer getNroIntTipoUsuario() {
        return nroIntTipoUsuario;
    }

    public void setNroIntTipoUsuario(Integer nroIntTipoUsuario) {
        this.nroIntTipoUsuario = nroIntTipoUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroIntTipoUsuario != null ? nroIntTipoUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoUsuario)) {
            return false;
        }
        TipoUsuario other = (TipoUsuario) object;
        if ((this.nroIntTipoUsuario == null && other.nroIntTipoUsuario != null) || (this.nroIntTipoUsuario != null && !this.nroIntTipoUsuario.equals(other.nroIntTipoUsuario))) {
            return false;
        }
        return true;
    }
}
