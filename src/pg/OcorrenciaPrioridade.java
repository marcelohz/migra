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
@Table(name = "ocorrencia_prioridade", catalog = "metroplan", schema = "saac")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OcorrenciaPrioridade.findAll", query = "SELECT o FROM OcorrenciaPrioridade o"),
    @NamedQuery(name = "OcorrenciaPrioridade.findByNome", query = "SELECT o FROM OcorrenciaPrioridade o WHERE o.nome = :nome")})
public class OcorrenciaPrioridade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nome", nullable = false, length = 2147483647)
    private String nome;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ocorrenciaPrioridadeNome")
    private Collection<Ocorrencia> ocorrenciaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ocorrenciaPrioridadeNome")
    private Collection<Andamento> andamentoCollection;

    public OcorrenciaPrioridade() {
    }

    public OcorrenciaPrioridade(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlTransient
    public Collection<Ocorrencia> getOcorrenciaCollection() {
        return ocorrenciaCollection;
    }

    public void setOcorrenciaCollection(Collection<Ocorrencia> ocorrenciaCollection) {
        this.ocorrenciaCollection = ocorrenciaCollection;
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
        if (!(object instanceof OcorrenciaPrioridade)) {
            return false;
        }
        OcorrenciaPrioridade other = (OcorrenciaPrioridade) object;
        if ((this.nome == null && other.nome != null) || (this.nome != null && !this.nome.equals(other.nome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pg.OcorrenciaPrioridade[ nome=" + nome + " ]";
    }
    
}
