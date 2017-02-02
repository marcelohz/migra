/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pg.Departamento;
import pg.Ocorrencia;
import pg.OcorrenciaAssunto;
import pg.OcorrenciaCanal;
import pg.OcorrenciaPrioridade;
import pg.OcorrenciaSituacao;
import pg.OcorrenciaTipo;

/**
 *
 * @author marceloz
 */
public class Migra {

    /**
     * @param args the command line arguments
     */
    static Connection oraCon = null;
    static Connection pgCon = null;
    static EntityManager oraEm = null;
    static EntityManager pgEm = null;
    static EntityManagerFactory oraEmf = null;
    static EntityManagerFactory pgEmf = null;

    public static void main(String[] args) throws SQLException {

        conecta();
        oraEm.getTransaction().begin();

        /*deleta();
         estados();
         tipoLog();
         localidades();
         bairros();
         logradouros();*/ //last run 28-09-2015 fim expediente
        pgEm.getTransaction().begin();
        pgEm.createNativeQuery("delete from saac.andamento; delete from saac.ocorrencia;").executeUpdate();

        pgEm.getTransaction().commit();
        canais();
        departamentos();
        prioridades();
        situacoes();
        tipos();
        pgEm.getTransaction().begin();
        pgEm.createNativeQuery("delete from saac.ocorrencia_assunto");
        pgEm.getTransaction().commit();
        pgEm.getTransaction().begin();
        ocorrencias();
        //pgEm.createNativeQuery("delete from saac.andamento;").executeUpdate(); //wtf?
        andamentos();
        pgEm.getTransaction().commit();

        oraEm.getTransaction().commit();
    }

    private static void canais() throws SQLException {
        pgEm.getTransaction().begin();
        pgCon.createStatement().execute("delete from saac.ocorrencia_canal;");
        ResultSet rs = oraCon.createStatement().executeQuery("select DS_CANAL from PRD_CORP.SAC_CANAIS group by DS_CANAL");
        while (rs.next()) {
            pgCon.createStatement().execute("insert into saac.ocorrencia_canal (nome) values ('" + rs.getString(1) + "');");
        }
        pgEm.getTransaction().commit();
    }

    private static void tipos() throws SQLException {
        pgEm.getTransaction().begin();
        pgCon.createStatement().execute("delete from saac.ocorrencia_tipo;");
        ResultSet rs = oraCon.createStatement().executeQuery("select DS_TIPO_OCORRENCIA from PRD_CORP.SAC_OCORRENCIAS_TIPOS group by DS_TIPO_OCORRENCIA");
        while (rs.next()) {
            pgCon.createStatement().execute("insert into saac.ocorrencia_tipo (nome) values ('" + rs.getString(1) + "');");
        }
        pgEm.getTransaction().commit();
    }

    private static void prioridades() throws SQLException {
        pgEm.getTransaction().begin();
        pgCon.createStatement().execute("delete from saac.ocorrencia_prioridade;");
        ResultSet rs = oraCon.createStatement().executeQuery("select DS_PRIORIDADE from PRD_CORP.SAC_PRIORIDADES group by DS_PRIORIDADE");
        while (rs.next()) {
            pgCon.createStatement().execute("insert into saac.ocorrencia_prioridade (nome) values ('" + rs.getString(1) + "');");
        }
        pgEm.getTransaction().commit();
    }

    private static void situacoes() throws SQLException {
        pgEm.getTransaction().begin();
        pgCon.createStatement().execute("delete from saac.ocorrencia_situacao;");
        ResultSet rs = oraCon.createStatement().executeQuery("select DS_SITUACAO from PRD_CORP.SAC_SITUACOES group by DS_SITUACAO");
        while (rs.next()) {
            pgCon.createStatement().execute("insert into saac.ocorrencia_situacao (nome) values ('" + rs.getString(1) + "');");
        }
        pgEm.getTransaction().commit();
    }

    private static void departamentos() throws SQLException {
        pgEm.getTransaction().begin();
        pgCon.createStatement().execute("delete from saac.departamento;");
        ResultSet rs = oraCon.createStatement().executeQuery("select DS_DEPARTAMENTO from PRD_CORP.COR_DEPARTAMENTOS group by DS_DEPARTAMENTO");
//        String depa = oraPegaValor("COR_DEPARTAMENTOS", "DS_DEPARTAMENTO", "ID_DEPARTAMENTO", rs.getInt("ID_DEPARTAMENTO"))
        while (rs.next()) {
            if (rs.getString(1) != null) {
                pgCon.createStatement().execute("insert into saac.departamento (nome) values ('" + rs.getString(1) + "');");
            }
        }
        pgEm.getTransaction().commit();
    }

    private static void andamentos() throws SQLException {
        ResultSet rs = oraCon.createStatement().executeQuery("select * from PRD_CORP.SAC_ANDAMENTOS");
        while (rs.next()) {

            pg.Andamento a = new pg.Andamento();
            Integer prot = Integer.parseInt(oraPegaValor("SAC_OCORRENCIAS", "NR_PROTOCOLO", "ID_OCORRENCIA", rs.getInt("ID_OCORRENCIA")));
            if (prot == null) {
                System.out.println("wtf");
            }
            //a.setOcorrenciaId(new Ocorrencia(prot));
            a.setOcorrenciaId(pgEm.find(Ocorrencia.class, prot));
            a.setData(rs.getTimestamp("DT_ANDAMENTO"));
            String depa = oraPegaValor("COR_DEPARTAMENTOS", "DS_DEPARTAMENTO", "ID_DEPARTAMENTO", rs.getInt("ID_DEPARTAMENTO"));
            if (depa != null) {
                Departamento d = new Departamento(depa);
                a.setDepartamentoNome(d);
            }

            String codemp = oraPegaValor("COR_EMPRESAS", "CD_EMPRESA_MTR", "ID_EMPRESA", rs.getInt("ID_EMPRESA"));
            if (codemp != null && codemp.equals("MTR1")) {
                a.setEmpresaCodigo(null);
            } else {
                a.setEmpresaCodigo(codemp);
            }
            a.setNotas(rs.getString("DS_NOTAS"));
            a.setResposta(rs.getString("DS_RESPOSTA"));
            String prio = oraPegaValor("SAC_PRIORIDADES", "DS_PRIORIDADE", "ID_PRIORIDADE", rs.getInt("ID_PRIORIDADE"));
            String situ = oraPegaValor("SAC_SITUACOES", "DS_SITUACAO", "ID_SITUACAO", rs.getInt("ID_SITUACAO"));
            OcorrenciaPrioridade op = new OcorrenciaPrioridade(prio);
            OcorrenciaSituacao os = new OcorrenciaSituacao(situ);
            //pgEm.persist(os);
            //pgEm.persist(op);
            a.setOcorrenciaPrioridadeNome(op);
            a.setOcorrenciaSituacaoNome(os);
            pgEm.persist(a);
        }
    }

    private static void ocorrencias() throws SQLException {
        ResultSet rs = oraCon.createStatement().executeQuery("select * from PRD_CORP.SAC_OCORRENCIAS");
        //TODO: DS_RESPOSTA e DS_NOTAS SÓ NO ANDAMENTO?
//          ocorrencia_id serial NOT NULL,                NR_PROTOCOLO
//          ocorrencia_assunto_nome text NOT NULL,        ID_ASSUNTO
//          veiculo_placa text,                           ID_VEICULO
//          veiculo_prefixo integer, --                   ID_VEICULO
//          linha_codigo text,                            ID_LINHA
//          local_ocorrencia text,                        ID_LOCALIDADE
//          municipio_nome_ocorrencia text,               ID_LOCALIDADE
//          data_ocorrencia timestamp(0) without time     DT_OCORRENCIA
//          nome_reclamante text,                         ID_PESSOA
//          telefone_reclamante text,                     ID_PESSOA
//          data_atendimento timestamp(0                  DT_ABERTURA
//          descricao text,                               DS_RELATO
//          atendente text,                               ID_USUARIO_INC
//          empresa_codigo text,                          ID_EMPRESA
//          ocorrencia_canal_nome text NOT NULL,          ID_CANAL
//          ocorrencia_prioridade_nome text NOT NULL,     ID_PRIORIDADE
//          email_reclamante text,                        ID_PESSOA
//          municipio_nome_reclamante text,               ID_PESSOA
//          bairro_nome_reclamante text,                  ID_PESSOA
//          ocorrencia_assunto_tipo_nome text,            ID_TIPO_OCORRENCIA
//          data_fechamento timestamp(0) withou           DT_FECHAMENTO
        int oaNulos = 0;
        OcorrenciaAssunto ass = new OcorrenciaAssunto("INDEFINIDO");
        pgEm.persist(ass);
        while (rs.next()) {
//            pgEm.createNativeQuery("insert into saac.ocorrencia(nome, id) values (?, ?);")
//                    .setParameter(1, rs.getString("DS_TIPO"))
//                    .setParameter(2, rs.getInt("ID_TIPO"))
//                    .executeUpdate();
            pg.Ocorrencia o = new pg.Ocorrencia();
            o.setOcorrenciaId(rs.getInt("NR_PROTOCOLO"));

//APARENTEMENTE A IDEIA É SÓ REFERENCIAR O ASSUNTO POIS TIPO_ASSUNTO JÁ É AUTOMATICAMENTE REFERENCIADO. D'OH.
//            o.setOcorrenciaAssuntoTipoNome(new OcorrenciaAssuntoTipo(oraPegaValor("SAC_ASSUNTOS_TIPOS", "DS_ASSUNTO_TIPO", "ID_ASSUNTO_TIPO", rs.getInt("ID_ASSUNTO_TIPO"))));
            //o.setOcorrenciaAssuntoNome    (new OcorrenciaAssunto(    oraPegaValor("SAC_ASSUNTOS",       "DS_ASSUNTO",    "ID_ASSUNTO", rs.getInt("ID_ASSUNTO"))));
            String val = oraPegaValor("SAC_ASSUNTOS", "DS_ASSUNTO", "ID_ASSUNTO", rs.getInt("ID_ASSUNTO"));
            String tipo = oraPegaValor("SAC_OCORRENCIAS_TIPOS", "DS_TIPO_OCORRENCIA", "ID_TIPO_OCORRENCIA", rs.getInt("ID_TIPO_OCORRENCIA"));
            if (val == null) {
                val = oraPegaValor("SAC_ASSUNTOS", "DS_ASSUNTO", "ID_ASSUNTO", 83); //outros
                //System.out.println("+1 Outros");
            }
            OcorrenciaTipo ot = new OcorrenciaTipo(tipo);
//            pgEm.persist(ot);
            o.setOcorrenciaTipoNome(ot);
            //o.setOcorrenciaAssuntoNome(ass); //force this shit down
            OcorrenciaAssunto oa = pgEm.find(OcorrenciaAssunto.class, val);

            if (oa != null) {
                o.setOcorrenciaAssuntoNome(oa);
            } else {
                //System.out.println("oa nulo!!!!!!!");
                oaNulos++;
                o.setOcorrenciaAssuntoNome(ass);
                if (oraPegaValor("SAC_ASSUNTOS", "DS_ASSUNTO", "ID_ASSUNTO", rs.getInt("ID_ASSUNTO")) == null) {

                }
            }

            o.setVeiculoPlaca(oraPegaValor("FRT_VEICULOS", "NR_PLACA", "ID_VEICULO", rs.getInt("ID_VEICULO")));
            if (rs.getInt("ID_VEICULO") > 0) {
                String tmp = oraPegaValor("FRT_VEICULOS", "NR_PREFIXO", "ID_VEICULO", rs.getInt("ID_VEICULO"));
                if (tmp != null) {
                    o.setVeiculoPrefixo(Integer.parseInt(tmp));
                }
            }
            o.setLinhaCodigo(oraPegaValor("OFE_LINHAS", "CD_LINHA_MTR", "ID_LINHA", rs.getInt("ID_LINHA")));
            o.setLocalidadeIdOcorrencia(rs.getInt("ID_LOCALIDADE") > 0 ? rs.getInt("ID_LOCALIDADE") : null);
//            o.setBairroIdOcorrencia(rs.getInt("ID_BAIRRO") > 0 ? rs.getInt("ID_BAIRRO") : null);
            o.setDataOcorrencia(rs.getTimestamp("DT_OCORRENCIA"));
            o.setDataAtendimento(rs.getTimestamp("DT_ABERTURA"));
            o.setDescricao(rs.getString("DS_RELATO"));
            o.setAtendente(pegaUsuarioPG(rs.getString("ID_USUARIO_INC")));
            String codemp = oraPegaValor("COR_EMPRESAS", "CD_EMPRESA_MTR", "ID_EMPRESA", rs.getInt("ID_EMPRESA"));
            //if(codemp != null && pgPegaValor("geral.empresa_codigo", "codigo", "codigo", codemp) == null) {
            if (codemp != null && codemp.equals("MTR1")) {
                //System.out.println("**** codemp invalido... skipando!!!!!!!! : " + codemp);
                //continue;
                o.setEmpresaCodigo(null);
            } else {
                o.setEmpresaCodigo(codemp);
            }
            OcorrenciaCanal c = new OcorrenciaCanal(oraPegaValor("SAC_CANAIS", "DS_CANAL", "ID_CANAL", rs.getInt("ID_CANAL")));
//            pgEm.persist(c);
            o.setOcorrenciaCanalNome(c);
            OcorrenciaPrioridade p = new OcorrenciaPrioridade(oraPegaValor("SAC_PRIORIDADES", "DS_PRIORIDADE", "ID_PRIORIDADE", rs.getInt("ID_PRIORIDADE")));
            //pgEm.persist(p);
            o.setOcorrenciaPrioridadeNome(p);
            o.setDataFechamento(rs.getTimestamp("DT_FECHAMENTO"));

            if (rs.getInt("ID_PESSOA") > 0) {
                String tmp = oraPegaValor("SAC_PESSOAS", "ID_LOCALIDADE", "ID_PESSOA", rs.getInt("ID_PESSOA"));
                if (tmp != null) {
                    o.setLocalidadeIdReclamante(Integer.parseInt(tmp));
                }
                tmp = oraPegaValor("SAC_PESSOAS", "ID_BAIRRO", "ID_PESSOA", rs.getInt("ID_PESSOA"));
                if (tmp != null) {
                    o.setBairroIdReclamante(Integer.parseInt(tmp));
                }
                o.setNomeReclamante(oraPegaValor("SAC_PESSOAS", "NM_PESSOA", "ID_PESSOA", rs.getInt("ID_PESSOA")));
                o.setEmailReclamante(oraPegaValor("SAC_PESSOAS", "DS_EMAIL", "ID_PESSOA", rs.getInt("ID_PESSOA")));
                o.setTelefoneReclamante(oraPegaValor("SAC_PESSOAS", "NR_FONE", "ID_PESSOA", rs.getInt("ID_PESSOA")));
            }
            pgEm.persist(o);

        }
        System.out.println("total oaNulos = " + oaNulos);
    }

    private static String pegaUsuarioPG(String id) throws SQLException {
        if (id.equals("103183115120313886")) {
            return "alexandra";
        }
        if (id.equals("103053409404523872")) {
            return "kyzi";
        }
        if (id.equals("15615217096185165")) {
            return "operador-saac";
        }
        if (id.equals("32991718688393594")) {
            return "leticia";
        }
        if (id.equals("44881312182725047")) {
            return "luana";
        }
        if (id.equals("32989323621376095")) {
            return "reginara";
        }
        if (id.equals("32990732410388051")) {
            return "gilvana";
        }
        if (id.equals("93858516954765449")) {
            return "marcelo";
        }
        if (id.equals("16680005256625274")) {
            return "atendimento";
        }
        if (id.equals("32987702712360615")) {
            return "jorge";
        }
        if (id.equals("113249604997933156")) {
            return "wilson";
        }
        //System.out.println("nao achou atendente na pegausuariopg(): " + id);
        return null;
    }

    private static void deleta() {
        pgEm.createNativeQuery("delete from dne.logradouro;").executeUpdate();
        pgEm.createNativeQuery("delete from dne.logradouro_tipo;").executeUpdate();
        pgEm.createNativeQuery("delete from dne.bairro;").executeUpdate();
        pgEm.createNativeQuery("delete from dne.localidade;").executeUpdate();
        pgEm.createNativeQuery("delete from dne.estado;").executeUpdate();
    }

    private static void conecta() {
        System.out.println("-------- Oracle JDBC Connection Testing ------");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            return;
        }
        //System.out.println("Oracle JDBC Driver Registered!");
        try {
            oraCon = DriverManager.getConnection(
                    "jdbc:oracle:thin:@10.244.36.235:1521:PRDDB", "sys as sysdba",
                    "master");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (oraCon != null) {
            System.out.println("Oracle in!");
        } else {
            System.out.println("Failed to make Oracle connection!");
            return;
        }
        oraEmf = Persistence.createEntityManagerFactory("MigraPU", getEMProperties());
        oraEm = oraEmf.createEntityManager();
        //CONECTADO AO ORACLE

        //COMECAR PG
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PG JDBC Driver?");
            e.printStackTrace();
            return;
        }
        //System.out.println("PG JDBC Driver Registered!");
        try {
            pgCon = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/metroplan", "postgres",
                    "master");
        } catch (SQLException e) {
            System.out.println("PG Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (pgCon != null) {
            System.out.println("PG in!");
        } else {
            System.out.println("Failed to make PG connection!");
            return;
        }
        pgEmf = Persistence.createEntityManagerFactory("pg", getEMProperties());
        pgEm = pgEmf.createEntityManager();
    }

    //migra tipos de logradouro
    private static void tipoLog() throws SQLException {
        ResultSet rs = oraCon.createStatement().executeQuery("select * from PRD_CORP.DNE_TIPOS");
        //pgEm.createNativeQuery("delete from dne.logradouro_tipo;").executeUpdate();
        while (rs.next()) {
            pgEm.createNativeQuery("insert into dne.logradouro_tipo(nome, id) values (?, ?);")
                    .setParameter(1, rs.getString("DS_TIPO"))
                    .setParameter(2, rs.getInt("ID_TIPO"))
                    .executeUpdate();
        }
    }

    //migra bairros
    private static void bairros() throws SQLException {
        ResultSet rs = oraCon.createStatement().executeQuery("select * from PRD_CORP.DNE_BAIRROS");
        //pgEm.createNativeQuery("delete from dne.logradouro_tipo;").executeUpdate();
        while (rs.next()) {
            /*if(rs.getInt("ID_LOCALIDADE") == -1) {
             continue;
             }*/
            pgEm.createNativeQuery("insert into dne.bairro(nome, id, dne_id, nome_abreviado, localidade_id, versao) values (?, ?, ?, ?, ?, ?);")
                    .setParameter(1, rs.getString("NM_BAIRRO"))
                    .setParameter(2, rs.getInt("ID_BAIRRO"))
                    .setParameter(3, rs.getInt("CD_BAIRRO"))
                    .setParameter(4, rs.getString("NM_ABREVIADO"))
                    .setParameter(5, rs.getInt("ID_LOCALIDADE"))
                    .setParameter(6, rs.getInt("ID_VERSAO"))
                    .executeUpdate();
        }
    }

    //migra estados
    private static void estados() throws SQLException {
        ResultSet rs = oraCon.createStatement().executeQuery("select * from PRD_CORP.DNE_ESTADOS");
        //pgEm.createNativeQuery("delete from dne.estado;").executeUpdate();
        while (rs.next()) {
            if (rs.getString("NM_OFICIAL").equals("Nao definido")) {
                continue;
            }
            //perceba versao hardcoded, 1
            pgEm.createNativeQuery("insert into dne.estado(nome, sigla, id, versao) values (?, ?, ?, 1);")
                    .setParameter(1, rs.getString("NM_OFICIAL"))
                    .setParameter(2, rs.getString("SG_ESTADO"))
                    .setParameter(3, rs.getInt("ID_ESTADO"))
                    .executeUpdate();
        }
    }

    //MIGRA LOCALIDADES
    private static void localidades() throws SQLException {
        ResultSet rs = oraCon.createStatement().executeQuery("select * from PRD_CORP.DNE_LOCALIDADES");
        //pgEm.createNativeQuery("delete from dne.localidade;").executeUpdate();
        boolean jahMenosUm = false;
        while (rs.next()) {
            if (rs.getInt("ID_LOCALIDADE") == -1) {
                if (jahMenosUm == true) {
                    continue;
                }
                jahMenosUm = true;
            }
            pgEm.createNativeQuery("insert into dne.localidade(id, nome, id_sub, nome_abreviado, cep, tipo_localidade, estado_sigla, dne_id) values (?, ?, ?, ?, ?, ?, ?, ?);")
                    .setParameter(1, rs.getInt("ID_LOCALIDADE"))
                    .setParameter(2, rs.getString("NM_LOCALIDADE"))
                    .setParameter(3, rs.getInt("NR_SUBOR_LOCAL_DNE"))
                    .setParameter(4, rs.getString("NM_ABREVIADO"))
                    .setParameter(5, rs.getString("NR_CEP"))
                    .setParameter(6, rs.getString("TP_LOCALIDADE"))
                    .setParameter(7, pgPegaValor("dne.estado", "sigla", "id", rs.getInt("ID_ESTADO")))
                    .setParameter(8, rs.getInt("CD_LOCALIDADE"))
                    .executeUpdate();
        }
    }

    //migra logradouros
    private static void logradouros() throws SQLException {
        ResultSet rs = oraCon.createStatement().executeQuery("select * from PRD_CORP.DNE_LOGRADOUROS");
        //pgEm.createNativeQuery("delete from dne.logradouro;").executeUpdate();
        while (rs.next()) {
//            id integer NOT NULL,
//            nome text,
//            nome_abreviado text,
//            complemento text,
//            localidade_id integer,
//            tipo_id integer,
//            bairro_id_inicial integer,
//            bairro_id_final integer,
//            cep text,
//            dne_id integer,
            pgEm.createNativeQuery("insert into dne.logradouro(id, nome, nome_abreviado, complemento, localidade_id, tipo_id, bairro_id_inicial, bairro_id_final, cep, dne_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
                    .setParameter(1, rs.getInt("ID_LOGRADOURO"))
                    .setParameter(2, rs.getString("NM_LOGRADOURO"))
                    .setParameter(3, rs.getString("NM_ABREVIADO"))
                    .setParameter(4, rs.getString("NM_COMPLEMENTO"))
                    .setParameter(5, rs.getInt("ID_LOCALIDADE"))
                    .setParameter(6, rs.getInt("ID_TIPO"))
                    .setParameter(7, rs.getInt("ID_BAIRRO_INICIAL"))
                    .setParameter(8, rs.getInt("ID_BAIRRO_FINAL"))
                    .setParameter(9, rs.getString("NR_CEP"))
                    .setParameter(10, rs.getInt("CD_LOGRADOURO"))
                    .executeUpdate();
        }
    }

    private static Map getEMProperties() {
        Map props = new HashMap();
        props.put("hibernate.connection.url", "jdbc:oracle:thin:@10.244.36.235:1521:PRDDB");
        props.put("hibernate.connection.username", "sys as sysdba");
        props.put("hibernate.connection.password", "master");
        return props;
    }

    private static String pgPegaValor(String tabela, String campoDesejado, String campoCompara, String valorCompara) throws SQLException {
        ResultSet rs = pgCon.createStatement().executeQuery("select " + campoDesejado + " from " + tabela + " where " + campoCompara + " = '" + valorCompara + "'");
        if (rs.next()) {
            return rs.getString(campoDesejado);
        } else {
            return null;
        }
    }

    private static String pgPegaValor(String tabela, String campoDesejado, String campoCompara, Integer valorCompara) throws SQLException {
        ResultSet rs = pgCon.createStatement().executeQuery("select " + campoDesejado + " from " + tabela + " where " + campoCompara + " = " + valorCompara);
        if (rs.next()) {
            return rs.getString(campoDesejado);
        } else {
            return null;
        }
    }

    private static class Campos {

        String tabela;
        String campoDesejado;
        String campoCompara;
        Integer valorCompara;
        String resultado;
    }

    private static ArrayList<Campos> campos = new ArrayList<Campos>();

    private static String oraPegaValor(String tabela, String campoDesejado, String campoCompara, Integer valorCompara) throws SQLException {
        for (Campos c : campos) {
            if (c.valorCompara.equals(valorCompara) && c.campoCompara.equals(campoCompara) && c.campoDesejado.equals(campoDesejado) && c.tabela.equals(tabela)) {
                return c.resultado;
            }
        }
        Campos c = new Campos();
        c.tabela = tabela;
        c.campoDesejado = campoDesejado;
        c.campoCompara = campoCompara;
        c.valorCompara = valorCompara;

        try (ResultSet rs = oraCon.createStatement().executeQuery("select " + campoDesejado + " from PRD_CORP." + tabela + " where " + campoCompara + " = " + valorCompara)) {
            if (rs.next()) {
                c.resultado = rs.getString(campoDesejado);
            } else {
                c.resultado = null;
            }
        }
        campos.add(c);
        return c.resultado;
    }

}
