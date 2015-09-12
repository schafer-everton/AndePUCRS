package com.pucrs.andepucrs.model;

import java.io.Serializable;

/**
 * @author ScHaFeR
 */
public class EstablishmentType implements Serializable {
    private Integer nroIntTipoEstabelecimento;
    private String descricao;

    public EstablishmentType() {
    }

    public EstablishmentType(Integer nroIntTipoEstabelecimento) {
        this.nroIntTipoEstabelecimento = nroIntTipoEstabelecimento;
    }

    public Integer getNroIntTipoEstabelecimento() {
        return nroIntTipoEstabelecimento;
    }

    public void setNroIntTipoEstabelecimento(Integer nroIntTipoEstabelecimento) {
        this.nroIntTipoEstabelecimento = nroIntTipoEstabelecimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "EstablishmentType{" +
                "nroIntTipoEstabelecimento=" + nroIntTipoEstabelecimento +
                ", descricao='" + descricao + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstablishmentType)) return false;

        EstablishmentType that = (EstablishmentType) o;

        if (getNroIntTipoEstabelecimento() != null ? !getNroIntTipoEstabelecimento().equals(that.getNroIntTipoEstabelecimento()) : that.getNroIntTipoEstabelecimento() != null)
            return false;
        return !(getDescricao() != null ? !getDescricao().equals(that.getDescricao()) : that.getDescricao() != null);

    }

    @Override
    public int hashCode() {
        int result = getNroIntTipoEstabelecimento() != null ? getNroIntTipoEstabelecimento().hashCode() : 0;
        result = 31 * result + (getDescricao() != null ? getDescricao().hashCode() : 0);
        return result;
    }
}
