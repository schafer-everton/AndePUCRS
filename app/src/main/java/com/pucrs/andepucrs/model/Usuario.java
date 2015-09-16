package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by ScHaFeR on 25/08/2015.
 */
public class Usuario implements Serializable {

    @Expose
    private Integer nroIntUsuario;
    @Expose
    private String nome;
    @Expose
    private String email;
    @Expose
    private String hashSenha;
    @Expose
    private String sessionHash;
    @Expose
    private TipoUsuario nroIntTipoUsuario;
    @Expose
    private Collection<PontoUsuario> pontoUsuarioCollection;

    public Usuario(String nome, String email, String hashSenha) {
        this.nome = nome;
        this.email = email;
        this.hashSenha = hashSenha;
        TipoUsuario u = new TipoUsuario();
        u.setNroIntTipoUsuario(2);
        u.setDescricao("regular-user");
        this.nroIntTipoUsuario = u;

    }

    public Usuario() {
    }

    public Usuario(Integer nroIntUsuario) {
        this.nroIntUsuario = nroIntUsuario;
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

    public TipoUsuario getNroIntTipoUsuario() {
        return nroIntTipoUsuario;
    }

    public void setNroIntTipoUsuario(TipoUsuario nroIntTipoUsuario) {
        this.nroIntTipoUsuario = nroIntTipoUsuario;
    }

    public Collection<PontoUsuario> getPontoUsuarioCollection() {
        return pontoUsuarioCollection;
    }

    public void setPontoUsuarioCollection(Collection<PontoUsuario> pontoUsuarioCollection) {
        this.pontoUsuarioCollection = pontoUsuarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroIntUsuario != null ? nroIntUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.nroIntUsuario == null && other.nroIntUsuario != null) || (this.nroIntUsuario != null && !this.nroIntUsuario.equals(other.nroIntUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nroIntUsuario=" + nroIntUsuario +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", hashSenha='" + hashSenha + '\'' +
                ", sessionHash='" + sessionHash + '\'' +
                ", nroIntTipoUsuario=" + nroIntTipoUsuario +
                '}';
    }
}
