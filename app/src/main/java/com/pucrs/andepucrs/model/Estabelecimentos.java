package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * @author ScHaFeR
 */

public class Estabelecimentos implements Serializable {
    @Expose
    private Integer nroIntEstabelecimento;
    @Expose
    private String nome;
    @Expose
    private String descricao;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;
    @Expose
    private TipoEstabelecimento nroIntTipoEstabelecimento;

    public Estabelecimentos() {
    }

    public Estabelecimentos(Integer nroIntEstabelecimento) {
        this.nroIntEstabelecimento = nroIntEstabelecimento;
    }

    public Integer getNroIntEstabelecimento() {
        return nroIntEstabelecimento;
    }

    public void setNroIntEstabelecimento(Integer nroIntEstabelecimento) {
        this.nroIntEstabelecimento = nroIntEstabelecimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public TipoEstabelecimento getNroIntTipoEstabelecimento() {
        return nroIntTipoEstabelecimento;
    }

    public void setNroIntTipoEstabelecimento(TipoEstabelecimento nroIntTipoEstabelecimento) {
        this.nroIntTipoEstabelecimento = nroIntTipoEstabelecimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroIntEstabelecimento != null ? nroIntEstabelecimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estabelecimentos)) {
            return false;
        }
        Estabelecimentos other = (Estabelecimentos) object;
        if ((this.nroIntEstabelecimento == null && other.nroIntEstabelecimento != null) || (this.nroIntEstabelecimento != null && !this.nroIntEstabelecimento.equals(other.nroIntEstabelecimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Estabelecimentos{" +
                "nroIntEstabelecimento=" + nroIntEstabelecimento +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nroIntTipoEstabelecimento=" + nroIntTipoEstabelecimento +
                '}';
    }
}
