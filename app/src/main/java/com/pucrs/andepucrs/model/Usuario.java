package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

/**
 * Created by ScHaFeR on 25/08/2015.
 */
public class Usuario {
    @Expose
    private String email;
    @Expose
    private String hashSenha;
    @Expose
    private String nome;
    @Expose
    private int nroIntTipoUsuario;
    @Expose
    private int nroIntUsuario;

    public Usuario(String nome,String email, String hashSenha) {
        this.email = email;
        this.hashSenha = hashSenha;
        this.nome = nome;
        this.nroIntTipoUsuario = 2;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The hashSenha
     */
    public String getHashSenha() {
        return hashSenha;
    }

    /**
     * @param hashSenha The hashSenha
     */
    public void setHashSenha(String hashSenha) {
        this.hashSenha = hashSenha;
    }

    /**
     * @return The nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome The nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return The nroIntTipoUsuario
     */
    public int getNroIntTipoUsuario() {
        return nroIntTipoUsuario;
    }

    /**
     * @param nroIntTipoUsuario The nroIntTipoUsuario
     */
    public void setNroIntTipoUsuario(int nroIntTipoUsuario) {
        this.nroIntTipoUsuario = nroIntTipoUsuario;
    }

    /**
     * @return The nroIntUsuario
     */
    public int getNroIntUsuario() {
        return nroIntUsuario;
    }

    /**
     * @param nroIntUsuario The nroIntUsuario
     */
    public void setNroIntUsuario(int nroIntUsuario) {
        this.nroIntUsuario = nroIntUsuario;
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + hashSenha + '\'' +
                '}';
    }
}
