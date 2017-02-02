/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pg;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marceloz
 */
@Entity
@Table(name = "ocorrencia_situacao", catalog = "metroplan", schema = "saac")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OcorrenciaSituacao.findAll", query = "SELECT o FROM OcorrenciaSituacao o"),
    @NamedQuery(name = "OcorrenciaSituacao.findByNome", query = "SELECT o FROM OcorrenciaSituacao o WHERE o.nome = :nome")})
public class OcorrenciaSituacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nome", nullable = false, length = 2147483647)
    private String nome;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "ocorrenciaSituacaoNome")
    private Collection<Andamento> andamentoCollection;

    public OcorrenciaSituacao() {
    }

    public OcorrenciaSituacao(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlTransient
    public Collection<Andamento> getAndamentoCollection() {
        return andamentoCollection;
    }

    public void setAndamentoCollection(Collection<Andamento> andamentoCollection) {
        this.andamentoCollection = andamentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nome != null ? nome.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OcorrenciaSituacao)) {
            return false;
        }
        OcorrenciaSituacao other = (OcorrenciaSituacao) object;
        if ((this.nome == null && other.nome != null) || (this.nome != null && !this.nome.equals(other.nome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pg.OcorrenciaSituacao[ nome=" + nome + " ]";
    }
    
}
