package com.meuapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por abrir conexões JDBC com o PostgreSQL hospedado no Aiven.
 *
 * Cada método do DAO abre sua própria conexão (try-with-resources) e a fecha
 * logo depois de usar — isso evita "vazar" conexões e bate com o limite de
 * conexões simultâneas que os planos do Aiven costumam ter.
 */
public class ConexaoBD {

    // ===================================================================
    // ALTERE AQUI: dados de conexão do seu serviço PostgreSQL no Aiven.
    // Você encontra esses valores no painel do Aiven, na aba "Overview"
    // do seu serviço PostgreSQL (Host, Port, User, Password, Database name).
    // ===================================================================
    private static final String HOST     = "altere"; // ex: pg-xxxxxxx-projeto.aivencloud.com
    private static final String PORT     = "altere";                  // porta numérica fornecida pelo Aiven
    private static final String DATABASE = "altere";               // nome do banco (geralmente "defaultdb")
    private static final String USUARIO  = "avnadmin";                // usuário padrão criado pelo Aiven
    private static final String SENHA    = "suasenha";          // senha do serviço (não deixe em produção em texto puro!)

    // O Aiven exige conexão criptografada (SSL/TLS).
    // "require" funciona na maioria dos casos sem precisar baixar certificado.
    //
    // Se quiser validação completa do certificado (mais seguro):
    //   1. Baixe o certificado CA no painel do Aiven (arquivo ca.pem).
    //   2. Troque SSL_MODE para "verify-full".
    //   3. Adicione "&sslrootcert=/caminho/para/ca.pem" na URL abaixo.
    private static final String SSL_MODE = "require";

    private static final String URL = String.format(
            "jdbc:postgresql://%s:%s/%s?sslmode=%s",
            HOST, PORT, DATABASE, SSL_MODE
    );

    // Bloco estático: garante que o driver JDBC do PostgreSQL seja registrado
    // assim que esta classe for carregada pela primeira vez.
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC do PostgreSQL não encontrado no classpath.", e);
        }
    }

    /**
     * Abre e retorna uma nova conexão com o banco.
     * Use sempre dentro de um try-with-resources para garantir que será fechada.
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    /**
     * Método utilitário só para testar rapidamente se a conexão está funcionando.
     * Pode chamar isso a partir de um main() de teste antes de abrir a tela gráfica.
     */
    public static void testarConexao() {
        try (Connection con = conectar()) {
            System.out.println("Conexão com o PostgreSQL (Aiven) realizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Falha ao conectar no banco: " + e.getMessage());
        }
    }
}
