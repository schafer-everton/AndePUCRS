package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author ScHaFeR
 */
public class UserType implements Serializable {
    @Expose
    private int nroIntTipoUsuario;
    @Expose
    private String descricao;
    @Expose
    private Collection<User> usuarioCollection;

    public UserType() {
        this.nroIntTipoUsuario = 2;
        this.descricao = "regular-user";
    }

    public UserType(int nroIntTipoUsuario) {
        this.nroIntTipoUsuario = nroIntTipoUsuario;
    }

    public Collection<User> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<User> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public int getNroIntTipoUsuario() {
        return nroIntTipoUsuario;
    }

    public void setNroIntTipoUsuario(int nroIntTipoUsuario) {
        this.nroIntTipoUsuario = nroIntTipoUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Id Tipo User=" + nroIntTipoUsuario + " Descição " + descricao;
    }
}
