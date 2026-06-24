package com.meuapp.gui;

import com.meuapp.dao.ClienteDAO;
import com.meuapp.model.Cliente;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

/**
 * Tela principal da aplicação: formulário de cadastro + tabela de
 * visualização/consulta + botões de ação (Inserir, Atualizar, Excluir,
 * Buscar, Listar Todos).
 */
public class TelaPrincipal extends JFrame {

    private final ClienteDAO dao = new ClienteDAO();

    private JTextField campoId;
    private JTextField campoNome;
    private JTextField campoEmail;
    private JTextField campoTelefone;
    private JTextField campoBusca;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaPrincipal() {
        super("CRUD Clientes - PostgreSQL (Aiven)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        montarPainelFormulario();
        montarPainelTabela();
        montarPainelBotoes();

        carregarTodos();
    }

    // ------------------------- Montagem da UI -------------------------

    private void montarPainelFormulario() {
        JPanel painel = new JPanel(new GridLayout(2, 5, 8, 8));
        painel.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));

        campoId = new JTextField();
        campoId.setEditable(false); // o ID é gerado pelo banco (SERIAL)

        campoNome = new JTextField();
        campoEmail = new JTextField();
        campoTelefone = new JTextField();
        campoBusca = new JTextField();

        painel.add(new JLabel("ID:"));
        painel.add(campoId);
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("Buscar por nome:"));

        painel.add(new JLabel("Email:"));
        painel.add(campoEmail);
        painel.add(new JLabel("Telefone:"));
        painel.add(campoTelefone);
        painel.add(campoBusca);

        add(painel, BorderLayout.NORTH);
    }

    private void montarPainelTabela() {
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Email", "Telefone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // edição é feita pelo formulário, não direto na tabela
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.getSelectionModel().addListSelectionListener(e -> preencherFormularioComSelecao());

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void montarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout());

        JButton btnInserir = new JButton("Inserir");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnListarTodos = new JButton("Listar Todos");
        JButton btnBuscar = new JButton("Buscar");

        btnInserir.addActionListener(e -> inserir());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnListarTodos.addActionListener(e -> carregarTodos());
        btnBuscar.addActionListener(e -> buscar());

        painel.add(btnInserir);
        painel.add(btnAtualizar);
        painel.add(btnExcluir);
        painel.add(btnLimpar);
        painel.add(btnListarTodos);
        painel.add(btnBuscar);

        add(painel, BorderLayout.SOUTH);
    }

    // --------------------------- Ações CRUD ---------------------------

    private void inserir() {
        if (!validarCampos()) return;
        Cliente c = new Cliente(campoNome.getText(), campoEmail.getText(), campoTelefone.getText());
        try {
            dao.inserir(c);
            JOptionPane.showMessageDialog(this, "Cliente inserido com sucesso!");
            limparFormulario();
            carregarTodos();
        } catch (RuntimeException ex) {
            mostrarErro(ex);
        }
    }

    private void atualizar() {
        if (campoId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela antes de atualizar.");
            return;
        }
        if (!validarCampos()) return;
        Cliente c = new Cliente(Integer.parseInt(campoId.getText()), campoNome.getText(),
                campoEmail.getText(), campoTelefone.getText());
        try {
            dao.atualizar(c);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            limparFormulario();
            carregarTodos();
        } catch (RuntimeException ex) {
            mostrarErro(ex);
        }
    }

    private void excluir() {
        if (campoId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela antes de excluir.");
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Confirma a exclusão?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            dao.excluir(Integer.parseInt(campoId.getText()));
            JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
            limparFormulario();
            carregarTodos();
        } catch (RuntimeException ex) {
            mostrarErro(ex);
        }
    }

    private void carregarTodos() {
        try {
            preencherTabela(dao.listarTodos());
        } catch (RuntimeException ex) {
            mostrarErro(ex);
        }
    }

    private void buscar() {
        String termo = campoBusca.getText().trim();
        if (termo.isEmpty()) {
            carregarTodos();
            return;
        }
        try {
            preencherTabela(dao.buscarPorNome(termo));
        } catch (RuntimeException ex) {
            mostrarErro(ex);
        }
    }

    // ----------------------------- Auxiliares -----------------------------

    private void preencherTabela(List<Cliente> clientes) {
        modeloTabela.setRowCount(0);
        for (Cliente c : clientes) {
            modeloTabela.addRow(new Object[]{c.getId(), c.getNome(), c.getEmail(), c.getTelefone()});
        }
    }

    private void preencherFormularioComSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        campoId.setText(modeloTabela.getValueAt(linha, 0).toString());
        campoNome.setText(valorOuVazio(modeloTabela.getValueAt(linha, 1)));
        campoEmail.setText(valorOuVazio(modeloTabela.getValueAt(linha, 2)));
        campoTelefone.setText(valorOuVazio(modeloTabela.getValueAt(linha, 3)));
    }

    private String valorOuVazio(Object valor) {
        return valor == null ? "" : valor.toString();
    }

    private void limparFormulario() {
        campoId.setText("");
        campoNome.setText("");
        campoEmail.setText("");
        campoTelefone.setText("");
        tabela.clearSelection();
    }

    private boolean validarCampos() {
        if (campoNome.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório.");
            return false;
        }
        return true;
    }

    private void mostrarErro(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
