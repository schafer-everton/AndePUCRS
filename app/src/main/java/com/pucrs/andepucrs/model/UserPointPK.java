package com.pucrs.andepucrs.model;

import java.io.Serializable;

/**
 * @author ScHaFeR
 */
public class UserPointPK implements Serializable {
    private int nroIntPonto;
    private int nroIntUsuario;

    public UserPointPK() {
    }

    public UserPointPK(int nroIntPonto, int nroIntUsuario) {
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
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof UserPointPK)) return false;

        UserPointPK that = (UserPointPK) o;

        if (getNroIntPonto() != that.getNroIntPonto()) return false;
        return getNroIntUsuario() == that.getNroIntUsuario();

    }

    @Override
    public int hashCode() {
        int result = getNroIntPonto();
        result = 31 * result + getNroIntUsuario();
        return result;
    }

    @Override
    public String toString() {
        return "UserPointPK{" +
                "nroIntPonto=" + nroIntPonto +
                ", nroIntUsuario=" + nroIntUsuario +
                '}';
    }
}
