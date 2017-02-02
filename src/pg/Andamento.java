/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pg;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marceloz
 */
@Entity
@Table(name = "andamento", catalog = "metroplan", schema = "saac")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Andamento.findAll", query = "SELECT a FROM Andamento a"),
    @NamedQuery(name = "Andamento.findById", query = "SELECT a FROM Andamento a WHERE a.id = :id"),
    @NamedQuery(name = "Andamento.findByData", query = "SELECT a FROM Andamento a WHERE a.data = :data"),
    @NamedQuery(name = "Andamento.findByEmpresaCodigo", query = "SELECT a FROM Andamento a WHERE a.empresaCodigo = :empresaCodigo"),
    @NamedQuery(name = "Andamento.findByResposta", query = "SELECT a FROM Andamento a WHERE a.resposta = :resposta"),
    @NamedQuery(name = "Andamento.findByNotas", query = "SELECT a FROM Andamento a WHERE a.notas = :notas")})
public class Andamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name = "empresa_codigo", length = 2147483647)
    private String empresaCodigo;
    @Column(name = "resposta", length = 2147483647)
    private String resposta;
    @Column(name = "notas", length = 2147483647)
    private String notas;
    @JoinColumn(name = "ocorrencia_situacao_nome", referencedColumnName = "nome", nullable = false)
    @ManyToOne(optional = false)
    private OcorrenciaSituacao ocorrenciaSituacaoNome;
    @JoinColumn(name = "ocorrencia_prioridade_nome", referencedColumnName = "nome", nullable = false)
    @ManyToOne(optional = false)
    private OcorrenciaPrioridade ocorrenciaPrioridadeNome;
    @JoinColumn(name = "ocorrencia_id", referencedColumnName = "ocorrencia_id")
    @ManyToOne
    private Ocorrencia ocorrenciaId;
    @JoinColumn(name = "departamento_nome", referencedColumnName = "nome")
    @ManyToOne
    private Departamento departamentoNome;

    public Andamento() {
    }

    public Andamento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getEmpresaCodigo() {
        return empresaCodigo;
    }

    public void setEmpresaCodigo(String empresaCodigo) {
        this.empresaCodigo = empresaCodigo;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public OcorrenciaSituacao getOcorrenciaSituacaoNome() {
        return ocorrenciaSituacaoNome;
    }

    public void setOcorrenciaSituacaoNome(OcorrenciaSituacao ocorrenciaSituacaoNome) {
        this.ocorrenciaSituacaoNome = ocorrenciaSituacaoNome;
    }

    public OcorrenciaPrioridade getOcorrenciaPrioridadeNome() {
        return ocorrenciaPrioridadeNome;
    }

    public void setOcorrenciaPrioridadeNome(OcorrenciaPrioridade ocorrenciaPrioridadeNome) {
        this.ocorrenciaPrioridadeNome = ocorrenciaPrioridadeNome;
    }

    public Ocorrencia getOcorrenciaId() {
        return ocorrenciaId;
    }

    public void setOcorrenciaId(Ocorrencia ocorrenciaId) {
        this.ocorrenciaId = ocorrenciaId;
    }

    public Departamento getDepartamentoNome() {
        return departamentoNome;
    }

    public void setDepartamentoNome(Departamento departamentoNome) {
        this.departamentoNome = departamentoNome;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Andamento)) {
            return false;
        }
        Andamento other = (Andamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pg.Andamento[ id=" + id + " ]";
    }
    
}
