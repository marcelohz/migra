/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pg;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "ocorrencia_assunto_tipo", catalog = "metroplan", schema = "saac")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OcorrenciaAssuntoTipo.findAll", query = "SELECT o FROM OcorrenciaAssuntoTipo o"),
    @NamedQuery(name = "OcorrenciaAssuntoTipo.findByNome", query = "SELECT o FROM OcorrenciaAssuntoTipo o WHERE o.nome = :nome")})
public class OcorrenciaAssuntoTipo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nome", nullable = false, length = 2147483647)
    private String nome;
    @OneToMany(mappedBy = "ocorrenciaAssuntoTipoNome")
    private Collection<OcorrenciaAssunto> ocorrenciaAssuntoCollection;
    @OneToMany(mappedBy = "ocorrenciaAssuntoTipoNome")
    private Collection<Ocorrencia> ocorrenciaCollection;

    public OcorrenciaAssuntoTipo() {
    }

    public OcorrenciaAssuntoTipo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlTransient
    public Collection<OcorrenciaAssunto> getOcorrenciaAssuntoCollection() {
        return ocorrenciaAssuntoCollection;
    }

    public void setOcorrenciaAssuntoCollection(Collection<OcorrenciaAssunto> ocorrenciaAssuntoCollection) {
        this.ocorrenciaAssuntoCollection = ocorrenciaAssuntoCollection;
    }

    @XmlTransient
    public Collection<Ocorrencia> getOcorrenciaCollection() {
        return ocorrenciaCollection;
    }

    public void setOcorrenciaCollection(Collection<Ocorrencia> ocorrenciaCollection) {
        this.ocorrenciaCollection = ocorrenciaCollection;
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
        if (!(object instanceof OcorrenciaAssuntoTipo)) {
            return false;
        }
        OcorrenciaAssuntoTipo other = (OcorrenciaAssuntoTipo) object;
        if ((this.nome == null && other.nome != null) || (this.nome != null && !this.nome.equals(other.nome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pg.OcorrenciaAssuntoTipo[ nome=" + nome + " ]";
    }
    
}
