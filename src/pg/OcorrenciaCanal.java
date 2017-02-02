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
@Table(name = "ocorrencia_canal", catalog = "metroplan", schema = "saac")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OcorrenciaCanal.findAll", query = "SELECT o FROM OcorrenciaCanal o"),
    @NamedQuery(name = "OcorrenciaCanal.findByNome", query = "SELECT o FROM OcorrenciaCanal o WHERE o.nome = :nome")})
public class OcorrenciaCanal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nome", nullable = false, length = 2147483647)
    private String nome;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "ocorrenciaCanalNome")
    private Collection<Ocorrencia> ocorrenciaCollection;

    public OcorrenciaCanal() {
    }

    public OcorrenciaCanal(String nome) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nome != null ? nome.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OcorrenciaCanal)) {
            return false;
        }
        OcorrenciaCanal other = (OcorrenciaCanal) object;
        if ((this.nome == null && other.nome != null) || (this.nome != null && !this.nome.equals(other.nome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pg.OcorrenciaCanal[ nome=" + nome + " ]";
    }
    
}
