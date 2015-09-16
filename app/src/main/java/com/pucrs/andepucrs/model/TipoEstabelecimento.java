package com.pucrs.andepucrs.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author ScHaFeR
 */
public class TipoEstabelecimento implements Serializable {
    @Expose
    private Integer nroIntTipoEstabelecimento;
    @Expose
    private String descricao;
    @Expose
    private Collection<Estabelecimentos> estabelecimentosCollection;

    public TipoEstabelecimento() {
    }

    public TipoEstabelecimento(Integer nroIntTipoEstabelecimento) {
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

    public Collection<Estabelecimentos> getEstabelecimentosCollection() {
        return estabelecimentosCollection;
    }

    public void setEstabelecimentosCollection(Collection<Estabelecimentos> estabelecimentosCollection) {
        this.estabelecimentosCollection = estabelecimentosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroIntTipoEstabelecimento != null ? nroIntTipoEstabelecimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEstabelecimento)) {
            return false;
        }
        TipoEstabelecimento other = (TipoEstabelecimento) object;
        if ((this.nroIntTipoEstabelecimento == null && other.nroIntTipoEstabelecimento != null) || (this.nroIntTipoEstabelecimento != null && !this.nroIntTipoEstabelecimento.equals(other.nroIntTipoEstabelecimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoEstabelecimento{" +
                "nroIntTipoEstabelecimento=" + nroIntTipoEstabelecimento +
                ", descricao='" + descricao + '\'' +
                '}';
    }


}
