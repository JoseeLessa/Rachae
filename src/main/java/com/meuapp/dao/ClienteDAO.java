package com.meuapp.dao;

import com.meuapp.db.ConexaoBD;
import com.meuapp.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável por todas as operações de CRUD
 * (Create, Read, Update, Delete) na tabela de clientes, usando JDBC puro
 * com PreparedStatement (evita SQL Injection).
 */
public class ClienteDAO {

    // ===========================================================================
    // ALTERE AQUI: nome da tabela, caso seja diferente no seu banco de dados.
    // ===========================================================================
    private static final String TABELA = "clientes";

    /**
     * Cria a tabela automaticamente, caso ainda não exista.
     * Útil para o primeiro uso. Se preferir criar a tabela manualmente,
     * use o arquivo schema.sql incluso no projeto e pode remover/comentar
     * a chamada deste método no Main.java.
     */
    public void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA + " (" +
                "id SERIAL PRIMARY KEY," +
                "nome VARCHAR(150) NOT NULL," +
                "email VARCHAR(150)," +
                "telefone VARCHAR(30)" +
                ")";
        try (Connection con = ConexaoBD.conectar();
             Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage(), e);
        }
    }

    // ---------------------------- CREATE ----------------------------
    public void inserir(Cliente c) {
        String sql = "INSERT INTO " + TABELA + " (nome, email, telefone) VALUES (?, ?, ?)";
        try (Connection con = ConexaoBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefone());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        }
    }

    // ------------------------- READ (todos) --------------------------
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nome, email, telefone FROM " + TABELA + " ORDER BY id";
        try (Connection con = ConexaoBD.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    // ------------------- READ (consulta por nome) ---------------------
    /** Busca clientes cujo nome contenha o termo informado (case-insensitive). */
    public List<Cliente> buscarPorNome(String nome) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nome, email, telefone FROM " + TABELA +
                     " WHERE nome ILIKE ? ORDER BY id";
        try (Connection con = ConexaoBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente: " + e.getMessage(), e);
        }
        return lista;
    }

    // ---------------------------- UPDATE ----------------------------
    public void atualizar(Cliente c) {
        String sql = "UPDATE " + TABELA + " SET nome = ?, email = ?, telefone = ? WHERE id = ?";
        try (Connection con = ConexaoBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefone());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    // ---------------------------- DELETE ----------------------------
    public void excluir(int id) {
        String sql = "DELETE FROM " + TABELA + " WHERE id = ?";
        try (Connection con = ConexaoBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage(), e);
        }
    }

    /** Converte uma linha do ResultSet em um objeto Cliente. */
    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("telefone")
        );
    }
}
