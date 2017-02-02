/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pg;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marceloz
 */
@Entity
@Table(name = "ocorrencia", catalog = "metroplan", schema = "saac")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocorrencia.findAll", query = "SELECT o FROM Ocorrencia o"),
    @NamedQuery(name = "Ocorrencia.findByOcorrenciaId", query = "SELECT o FROM Ocorrencia o WHERE o.ocorrenciaId = :ocorrenciaId"),
    @NamedQuery(name = "Ocorrencia.findByVeiculoPlaca", query = "SELECT o FROM Ocorrencia o WHERE o.veiculoPlaca = :veiculoPlaca"),
    @NamedQuery(name = "Ocorrencia.findByVeiculoPrefixo", query = "SELECT o FROM Ocorrencia o WHERE o.veiculoPrefixo = :veiculoPrefixo"),
    @NamedQuery(name = "Ocorrencia.findByLinhaCodigo", query = "SELECT o FROM Ocorrencia o WHERE o.linhaCodigo = :linhaCodigo"),
    @NamedQuery(name = "Ocorrencia.findByDataOcorrencia", query = "SELECT o FROM Ocorrencia o WHERE o.dataOcorrencia = :dataOcorrencia"),
    @NamedQuery(name = "Ocorrencia.findByNomeReclamante", query = "SELECT o FROM Ocorrencia o WHERE o.nomeReclamante = :nomeReclamante"),
    @NamedQuery(name = "Ocorrencia.findByTelefoneReclamante", query = "SELECT o FROM Ocorrencia o WHERE o.telefoneReclamante = :telefoneReclamante"),
    @NamedQuery(name = "Ocorrencia.findByDataAtendimento", query = "SELECT o FROM Ocorrencia o WHERE o.dataAtendimento = :dataAtendimento"),
    @NamedQuery(name = "Ocorrencia.findByDescricao", query = "SELECT o FROM Ocorrencia o WHERE o.descricao = :descricao"),
    @NamedQuery(name = "Ocorrencia.findByAtendente", query = "SELECT o FROM Ocorrencia o WHERE o.atendente = :atendente"),
    @NamedQuery(name = "Ocorrencia.findByEmpresaCodigo", query = "SELECT o FROM Ocorrencia o WHERE o.empresaCodigo = :empresaCodigo"),
    @NamedQuery(name = "Ocorrencia.findByEmailReclamante", query = "SELECT o FROM Ocorrencia o WHERE o.emailReclamante = :emailReclamante"),
    @NamedQuery(name = "Ocorrencia.findByDataFechamento", query = "SELECT o FROM Ocorrencia o WHERE o.dataFechamento = :dataFechamento"),
    @NamedQuery(name = "Ocorrencia.findByBairroIdReclamante", query = "SELECT o FROM Ocorrencia o WHERE o.bairroIdReclamante = :bairroIdReclamante"),
    @NamedQuery(name = "Ocorrencia.findByLocalidadeIdReclamante", query = "SELECT o FROM Ocorrencia o WHERE o.localidadeIdReclamante = :localidadeIdReclamante"),
    @NamedQuery(name = "Ocorrencia.findByBairroIdOcorrencia", query = "SELECT o FROM Ocorrencia o WHERE o.bairroIdOcorrencia = :bairroIdOcorrencia"),
    @NamedQuery(name = "Ocorrencia.findByLocalidadeIdOcorrencia", query = "SELECT o FROM Ocorrencia o WHERE o.localidadeIdOcorrencia = :localidadeIdOcorrencia")})
public class Ocorrencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ocorrencia_id", nullable = false)
    private Integer ocorrenciaId;
    @Column(name = "veiculo_placa", length = 2147483647)
    private String veiculoPlaca;
    @Column(name = "veiculo_prefixo")
    private Integer veiculoPrefixo;
    @Column(name = "linha_codigo", length = 2147483647)
    private String linhaCodigo;
    @Column(name = "data_ocorrencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataOcorrencia;
    @Column(name = "nome_reclamante", length = 2147483647)
    private String nomeReclamante;
    @Column(name = "telefone_reclamante", length = 2147483647)
    private String telefoneReclamante;
    @Column(name = "data_atendimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtendimento;
    @Column(name = "descricao", length = 2147483647)
    private String descricao;
    @Column(name = "atendente", length = 2147483647)
    private String atendente;
    @Column(name = "empresa_codigo", length = 2147483647)
    private String empresaCodigo;
    @Column(name = "email_reclamante", length = 2147483647)
    private String emailReclamante;
    @Column(name = "data_fechamento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFechamento;
    @Column(name = "bairro_id_reclamante")
    private Integer bairroIdReclamante;
    @Column(name = "localidade_id_reclamante")
    private Integer localidadeIdReclamante;
    @Column(name = "bairro_id_ocorrencia")
    private Integer bairroIdOcorrencia;
    @Column(name = "localidade_id_ocorrencia")
    private Integer localidadeIdOcorrencia;
    @JoinColumn(name = "ocorrencia_tipo_nome", referencedColumnName = "nome")
    @ManyToOne
    private OcorrenciaTipo ocorrenciaTipoNome;
    @JoinColumn(name = "ocorrencia_prioridade_nome", referencedColumnName = "nome", nullable = false)
    @ManyToOne(optional = false)
    private OcorrenciaPrioridade ocorrenciaPrioridadeNome;
    @JoinColumn(name = "ocorrencia_canal_nome", referencedColumnName = "nome", nullable = false)
    @ManyToOne(optional = false)
    private OcorrenciaCanal ocorrenciaCanalNome;
    @JoinColumn(name = "ocorrencia_assunto_tipo_nome", referencedColumnName = "nome")
    @ManyToOne
    private OcorrenciaAssuntoTipo ocorrenciaAssuntoTipoNome;
    @JoinColumn(name = "ocorrencia_assunto_nome", referencedColumnName = "nome", nullable = false)
    @ManyToOne(optional = false)
    private OcorrenciaAssunto ocorrenciaAssuntoNome;
    @OneToMany(mappedBy = "ocorrenciaId")
    private Collection<Andamento> andamentoCollection;

    public Ocorrencia() {
    }

    public Ocorrencia(Integer ocorrenciaId) {
        this.ocorrenciaId = ocorrenciaId;
    }

    public Integer getOcorrenciaId() {
        return ocorrenciaId;
    }

    public void setOcorrenciaId(Integer ocorrenciaId) {
        this.ocorrenciaId = ocorrenciaId;
    }

    public String getVeiculoPlaca() {
        return veiculoPlaca;
    }

    public void setVeiculoPlaca(String veiculoPlaca) {
        this.veiculoPlaca = veiculoPlaca;
    }

    public Integer getVeiculoPrefixo() {
        return veiculoPrefixo;
    }

    public void setVeiculoPrefixo(Integer veiculoPrefixo) {
        this.veiculoPrefixo = veiculoPrefixo;
    }

    public String getLinhaCodigo() {
        return linhaCodigo;
    }

    public void setLinhaCodigo(String linhaCodigo) {
        this.linhaCodigo = linhaCodigo;
    }

    public Date getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(Date dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public String getNomeReclamante() {
        return nomeReclamante;
    }

    public void setNomeReclamante(String nomeReclamante) {
        this.nomeReclamante = nomeReclamante;
    }

    public String getTelefoneReclamante() {
        return telefoneReclamante;
    }

    public void setTelefoneReclamante(String telefoneReclamante) {
        this.telefoneReclamante = telefoneReclamante;
    }

    public Date getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(Date dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAtendente() {
        return atendente;
    }

    public void setAtendente(String atendente) {
        this.atendente = atendente;
    }

    public String getEmpresaCodigo() {
        return empresaCodigo;
    }

    public void setEmpresaCodigo(String empresaCodigo) {
        this.empresaCodigo = empresaCodigo;
    }

    public String getEmailReclamante() {
        return emailReclamante;
    }

    public void setEmailReclamante(String emailReclamante) {
        this.emailReclamante = emailReclamante;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Integer getBairroIdReclamante() {
        return bairroIdReclamante;
    }

    public void setBairroIdReclamante(Integer bairroIdReclamante) {
        this.bairroIdReclamante = bairroIdReclamante;
    }

    public Integer getLocalidadeIdReclamante() {
        return localidadeIdReclamante;
    }

    public void setLocalidadeIdReclamante(Integer localidadeIdReclamante) {
        this.localidadeIdReclamante = localidadeIdReclamante;
    }

    public Integer getBairroIdOcorrencia() {
        return bairroIdOcorrencia;
    }

    public void setBairroIdOcorrencia(Integer bairroIdOcorrencia) {
        this.bairroIdOcorrencia = bairroIdOcorrencia;
    }

    public Integer getLocalidadeIdOcorrencia() {
        return localidadeIdOcorrencia;
    }

    public void setLocalidadeIdOcorrencia(Integer localidadeIdOcorrencia) {
        this.localidadeIdOcorrencia = localidadeIdOcorrencia;
    }

    public OcorrenciaTipo getOcorrenciaTipoNome() {
        return ocorrenciaTipoNome;
    }

    public void setOcorrenciaTipoNome(OcorrenciaTipo ocorrenciaTipoNome) {
        this.ocorrenciaTipoNome = ocorrenciaTipoNome;
    }

    public OcorrenciaPrioridade getOcorrenciaPrioridadeNome() {
        return ocorrenciaPrioridadeNome;
    }

    public void setOcorrenciaPrioridadeNome(OcorrenciaPrioridade ocorrenciaPrioridadeNome) {
        this.ocorrenciaPrioridadeNome = ocorrenciaPrioridadeNome;
    }

    public OcorrenciaCanal getOcorrenciaCanalNome() {
        return ocorrenciaCanalNome;
    }

    public void setOcorrenciaCanalNome(OcorrenciaCanal ocorrenciaCanalNome) {
        this.ocorrenciaCanalNome = ocorrenciaCanalNome;
    }

    public OcorrenciaAssuntoTipo getOcorrenciaAssuntoTipoNome() {
        return ocorrenciaAssuntoTipoNome;
    }

    public void setOcorrenciaAssuntoTipoNome(OcorrenciaAssuntoTipo ocorrenciaAssuntoTipoNome) {
        this.ocorrenciaAssuntoTipoNome = ocorrenciaAssuntoTipoNome;
    }

    public OcorrenciaAssunto getOcorrenciaAssuntoNome() {
        return ocorrenciaAssuntoNome;
    }

    public void setOcorrenciaAssuntoNome(OcorrenciaAssunto ocorrenciaAssuntoNome) {
        this.ocorrenciaAssuntoNome = ocorrenciaAssuntoNome;
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
        hash += (ocorrenciaId != null ? ocorrenciaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocorrencia)) {
            return false;
        }
        Ocorrencia other = (Ocorrencia) object;
        if ((this.ocorrenciaId == null && other.ocorrenciaId != null) || (this.ocorrenciaId != null && !this.ocorrenciaId.equals(other.ocorrenciaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pg.Ocorrencia[ ocorrenciaId=" + ocorrenciaId + " ]";
    }
    
}
