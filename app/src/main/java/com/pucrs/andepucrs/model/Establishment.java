package com.pucrs.andepucrs.model;

import java.io.Serializable;

/**
 * @author ScHaFeR
 */

public class Establishment implements Serializable {
    private Integer nroIntEstabelecimento;
    private String nome;
    private String descricao;
    private Double latitude;
    private Double longitude;
    private EstablishmentType nroIntTipoEstabelecimento;

    public Establishment() {
    }

    public Establishment(Integer nroIntEstabelecimento) {
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

    public EstablishmentType getNroIntTipoEstabelecimento() {
        return nroIntTipoEstabelecimento;
    }

    public void setNroIntTipoEstabelecimento(EstablishmentType nroIntTipoEstabelecimento) {
        this.nroIntTipoEstabelecimento = nroIntTipoEstabelecimento;
    }

    @Override
    public String toString() {
        return "Establishment{" +
                "nroIntEstabelecimento=" + nroIntEstabelecimento +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nroIntTipoEstabelecimento=" + nroIntTipoEstabelecimento +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Establishment)) return false;

        Establishment that = (Establishment) o;

        if (getNroIntEstabelecimento() != null ? !getNroIntEstabelecimento().equals(that.getNroIntEstabelecimento()) : that.getNroIntEstabelecimento() != null)
            return false;
        if (getNome() != null ? !getNome().equals(that.getNome()) : that.getNome() != null)
            return false;
        if (getDescricao() != null ? !getDescricao().equals(that.getDescricao()) : that.getDescricao() != null)
            return false;
        if (getLatitude() != null ? !getLatitude().equals(that.getLatitude()) : that.getLatitude() != null)
            return false;
        if (getLongitude() != null ? !getLongitude().equals(that.getLongitude()) : that.getLongitude() != null)
            return false;
        return !(getNroIntTipoEstabelecimento() != null ? !getNroIntTipoEstabelecimento().equals(that.getNroIntTipoEstabelecimento()) : that.getNroIntTipoEstabelecimento() != null);

    }

    @Override
    public int hashCode() {
        int result = getNroIntEstabelecimento() != null ? getNroIntEstabelecimento().hashCode() : 0;
        result = 31 * result + (getNome() != null ? getNome().hashCode() : 0);
        result = 31 * result + (getDescricao() != null ? getDescricao().hashCode() : 0);
        result = 31 * result + (getLatitude() != null ? getLatitude().hashCode() : 0);
        result = 31 * result + (getLongitude() != null ? getLongitude().hashCode() : 0);
        result = 31 * result + (getNroIntTipoEstabelecimento() != null ? getNroIntTipoEstabelecimento().hashCode() : 0);
        return result;
    }
}
