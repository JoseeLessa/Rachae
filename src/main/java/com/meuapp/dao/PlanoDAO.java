package com.meuapp.dao;

import com.meuapp.db.ConexaoBD;
import com.meuapp.model.Plano;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável por todas as operações de CRUD
 * (Create, Read, Update, Delete) na tabela de clientes, usando JDBC puro
 * com PreparedStatement (evita SQL Injection).
 */
public class PlanoDAO {

    // ===========================================================================
    // ALTERE AQUI: nome da tabela, caso seja diferente no seu banco de dados.
    // ===========================================================================
    private static final String TABELA = "Planos";

    /**
     * Cria a tabela automaticamente, caso ainda não exista.
     * Útil para o primeiro uso. Se preferir criar a tabela manualmente,
     * use o arquivo schema.sql incluso no projeto e pode remover/comentar
     * a chamada deste método no Main.java.
     */ 
    public void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA + " (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(150) NOT NULL," +
                "screens INTEGER," +
                "value DECIMAL(10, 2)," +
                "paymentDue DATE," +
                "creationDate DATE," +
                "updatedAt DATE" +
                ")";
        try (Connection con = ConexaoBD.conectar();
             Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage(), e);
        }
    }

    // ---------------------------- CREATE ----------------------------
    public void inserir(Plano p) {
        String sql = "INSERT INTO " + TABELA + " (name, screens, value, paymentDue, creationDate, updatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getScreens());
            ps.setDouble(3, p.getValue());
            ps.setDate(4, Date.valueOf(p.getPayDue()));
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.setDate(6, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        }
    }

    // ------------------------- READ (todos) --------------------------
    public List<Plano> listarTodos() {
        List<Plano> planos = new ArrayList<>();
        String sql = "SELECT id, name, screens, value, paymentDue, creationDate, updatedAt FROM " + TABELA + " ORDER BY id";
        try (Connection con = ConexaoBD.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                planos.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Planos: " + e.getMessage(), e);
        }
        return planos;
    }

    // ------------------- READ (consulta por nome) ---------------------
    /** Busca clientes cujo nome contenha o termo informado (case-insensitive). */
    public List<Plano> buscarPorNome(String name) {
        List<Plano> planos = new ArrayList<>();
        String sql = "SELECT id, name, screens, value, paymentDue, creationDate, updatedAt FROM " + TABELA +
                     " WHERE name ILIKE ? ORDER BY id";
        try (Connection con = ConexaoBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    planos.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Plano: " + e.getMessage(), e);
        }
        return planos;
    }

    // ---------------------------- UPDATE ----------------------------
    public void atualizar(Plano p) {
        String sql = "UPDATE " + TABELA + " SET name = ?, screens = ?, value = ?, paymentDue = ?, updatedAt = ? WHERE id = ?";
        try (Connection con = ConexaoBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getName());
            ps.setInt(2, p.getScreens());
            ps.setDouble(3, p.getValue());
            ps.setDate(4, Date.valueOf(p.getPayDue()));
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar plano: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro ao excluir plano: " + e.getMessage(), e);
        }
    }

    /** Converte uma linha do ResultSet em um objeto Plano. */
    private Plano mapear(ResultSet rs) throws SQLException {
        return new Plano(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("screens"),
                rs.getDouble("value"),
                rs.getDate("paymentDue").toLocalDate(),
                rs.getDate("creationDate").toLocalDate(),
                rs.getDate("updatedAt").toLocalDate()
        );
    }
}