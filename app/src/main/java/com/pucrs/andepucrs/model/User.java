package com.pucrs.andepucrs.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by ScHaFeR on 25/08/2015.
 */


public class User implements Serializable {
    private Integer nroIntUsuario;
    private String nome;
    private String email;
    private String hashSenha;
    private String sessionHash;
    private UserType nroIntUserType;
    private Collection<UserPoint> userPointCollection;

    public User(String nome, String email, String hashSenha) {
        this.nome = nome;
        this.email = email;
        this.hashSenha = hashSenha;
        UserType u = new UserType();
        u.setNroIntTipoUsuario(2);
        u.setDescricao("regular-user");
        this.nroIntUserType = u;

    }

    public User(Integer nroIntUsuario) {
        this.nroIntUsuario = nroIntUsuario;
    }

    public UserType getNroIntUserType() {
        return nroIntUserType;
    }

    public void setNroIntUserType(UserType nroIntUserType) {
        this.nroIntUserType = nroIntUserType;
    }

    public Integer getNroIntUsuario() {
        return nroIntUsuario;
    }

    public void setNroIntUsuario(Integer nroIntUsuario) {
        this.nroIntUsuario = nroIntUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashSenha() {
        return hashSenha;
    }

    public void setHashSenha(String hashSenha) {
        this.hashSenha = hashSenha;
    }

    public String getSessionHash() {
        return sessionHash;
    }

    public void setSessionHash(String sessionHash) {
        this.sessionHash = sessionHash;
    }


    public Collection<UserPoint> getUserPointCollection() {
        return userPointCollection;
    }

    public void setUserPointCollection(Collection<UserPoint> userPointCollection) {
        this.userPointCollection = userPointCollection;
    }

    @Override
    public String toString() {
        return "User{" +
                "nroIntUsuario=" + nroIntUsuario +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", hashSenha='" + hashSenha + '\'' +
                ", sessionHash='" + sessionHash + '\'' +
                ", nroIntUserType=" + nroIntUserType +
                '}';
    }
}
