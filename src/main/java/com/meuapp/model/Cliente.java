package com.meuapp.model;

/**
 * Representa um registro da tabela "clientes" no banco de dados.
 *
 * ===========================================================================
 * ALTERE AQUI: se a sua tabela tiver outro nome ou outras colunas, ajuste:
 *   - os atributos abaixo,
 *   - os construtores,
 *   - os getters/setters,
 * para refletirem exatamente as colunas da sua tabela.
 * ===========================================================================
 */
public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String telefone;

    public Cliente() {
    }

    /** Construtor completo (usado quando o registro já existe no banco, com ID). */
    public Cliente(int id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    /** Construtor sem ID (usado para criar um novo registro, antes de inserir). */
    public Cliente(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return nome;
    }
}
