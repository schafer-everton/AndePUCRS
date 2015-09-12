package com.pucrs.andepucrs.model;

import java.io.Serializable;

/**
 * @author ScHaFeR
 */
public class UserPoint implements Serializable {
    protected UserPointPK userPointPK;
    private String comentario;
    private User user;
    private Ponto ponto;

    public UserPoint() {
    }

    public UserPoint(UserPointPK userPointPK) {
        this.userPointPK = userPointPK;
    }

    public UserPoint(int nroIntPonto, int nroIntUsuario) {
        this.userPointPK = new UserPointPK(nroIntPonto, nroIntUsuario);
    }

    public UserPointPK getUserPointPK() {
        return userPointPK;
    }

    public void setUserPointPK(UserPointPK userPointPK) {
        this.userPointPK = userPointPK;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPoint)) return false;

        UserPoint that = (UserPoint) o;

        if (getUserPointPK() != null ? !getUserPointPK().equals(that.getUserPointPK()) : that.getUserPointPK() != null)
            return false;
        if (getComentario() != null ? !getComentario().equals(that.getComentario()) : that.getComentario() != null)
            return false;
        if (getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null)
            return false;
        return !(getPonto() != null ? !getPonto().equals(that.getPonto()) : that.getPonto() != null);

    }

    @Override
    public int hashCode() {
        int result = getUserPointPK() != null ? getUserPointPK().hashCode() : 0;
        result = 31 * result + (getComentario() != null ? getComentario().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        result = 31 * result + (getPonto() != null ? getPonto().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserPoint{" +
                "userPointPK=" + userPointPK +
                ", comentario='" + comentario + '\'' +
                ", user=" + user +
                ", ponto=" + ponto +
                '}';
    }
}
