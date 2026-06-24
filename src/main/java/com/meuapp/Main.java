package com.meuapp;

import com.meuapp.dao.ClienteDAO;
import com.meuapp.gui.TelaPrincipal;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // Cria a tabela automaticamente caso ela ainda não exista no banco.
        // Comente a linha abaixo se preferir gerenciar a tabela manualmente
        // (veja o arquivo schema.sql para criar a tabela na mão).
        new ClienteDAO().criarTabelaSeNaoExistir();

        // Inicia a interface gráfica na thread correta do Swing (Event Dispatch Thread).
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
