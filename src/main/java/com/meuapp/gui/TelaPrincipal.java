package com.meuapp.gui;

import com.meuapp.dao.PlanoDAO;
import com.meuapp.model.Plano;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Tela principal da aplicação: formulário de cadastro + tabela de
 * visualização/consulta + botões de ação (Inserir, Atualizar, Excluir,
 * Buscar, Listar Todos).
 */
public class TelaPrincipal extends JFrame {

    private final PlanoDAO dao = new PlanoDAO();

    private JTextField campoId;
    private JTextField campoName;
    private JTextField campoScreens;
    private JTextField campoValue;
    private JTextField campoPayDue;
    private JTextField campoBusca;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        painel.setBorder(BorderFactory.createTitledBorder("Dados do Plano"));

        campoId = new JTextField();
        campoId.setEditable(false); // o ID é gerado pelo banco (SERIAL)

        campoId  = new JTextField();
        campoName = new JTextField();
        campoScreens = new JTextField();
        campoValue = new JTextField();
        campoPayDue = new JTextField();
        /*
        campoCreationDate = new JTextField();
        campoUpdatedAt = new JTextField();
         */
        campoBusca = new JTextField();

        campoBusca = new JTextField();

        painel.add(new JLabel("ID:"));
        painel.add(campoId);
        painel.add(new JLabel("Name:"));
        painel.add(campoName);
        painel.add(new JLabel("Screens:"));
        painel.add(campoScreens);
        painel.add(new JLabel("Value:"));
        painel.add(campoValue);
        painel.add(new JLabel("Pay Due:"));
        painel.add(campoPayDue);
        
        /*
        painel.add(new JLabel("Creation Date:"));
        painel.add(campoCreationDate);
        painel.add(new JLabel("Updated At:"));
        painel.add(campoUpdatedAt);
        */
        painel.add(new JLabel("Buscar por nome:"));
        painel.add(campoBusca);

        add(painel, BorderLayout.NORTH);
    }

    private void montarPainelTabela() {
        modeloTabela = new DefaultTableModel(
        new Object[]{"ID", "Name", "Screens", "Value", "Pay Due", "Creation Date", "Updated At"}, 0) {

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
        Plano p = new Plano(
        campoName.getText(),
        Integer.parseInt(campoScreens.getText()),
        Double.parseDouble(campoValue.getText().replace(",", ".")),
        LocalDate.parse(campoPayDue.getText(), FORMATO_DATA)
        );  
        try {
            dao.inserir(p);
            JOptionPane.showMessageDialog(this, "Plano inserido com sucesso!");
            limparFormulario();
            carregarTodos();
        } catch (RuntimeException ex) {
            mostrarErro(ex);
        }
    }

    private void atualizar() {
        if (campoId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um plano na tabela antes de atualizar.");
            return;
        }
        if (!validarCampos()) return;
        Plano p = new Plano(Integer.parseInt(campoId.getText()), campoName.getText(),
            Integer.parseInt(campoScreens.getText()),
            Double.parseDouble(campoValue.getText().replace(",", ".")),
            LocalDate.parse(campoPayDue.getText(), FORMATO_DATA),
            LocalDate.now(),  // Data atual mas DAO.atualizar não muda.
            LocalDate.now()); // atualiza agora.
        try {
            dao.atualizar(p);
            JOptionPane.showMessageDialog(this, "Plano atualizado com sucesso!");
            limparFormulario();
            carregarTodos();
        } catch (RuntimeException ex) {
            mostrarErro(ex);
        }
    }

    private void excluir() {
        if (campoId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um plano na tabela antes de excluir.");
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Confirma a exclusão?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            dao.excluir(Integer.parseInt(campoId.getText()));
            JOptionPane.showMessageDialog(this, "Plano excluído com sucesso!");
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

    private void preencherTabela(List<Plano> planos) {
        modeloTabela.setRowCount(0);
        for (Plano p: planos) {
            modeloTabela.addRow(new Object[]{p.getId(), p.getName(), p.getScreens(), p.getValue(), p.getPayDue().format(FORMATO_DATA), p.getCreationDate().format(FORMATO_DATA), p.getUpdateDate().format(FORMATO_DATA)});
        }
    }

    private void preencherFormularioComSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        campoId.setText(modeloTabela.getValueAt(linha, 0).toString());
        campoName.setText(valorOuVazio(modeloTabela.getValueAt(linha, 1)));
        campoScreens.setText(valorOuVazio(modeloTabela.getValueAt(linha, 2)));
        campoValue.setText(valorOuVazio(modeloTabela.getValueAt(linha, 3)));
        campoPayDue.setText(valorOuVazio(modeloTabela.getValueAt(linha, 4)));
    }

    private String valorOuVazio(Object valor) {
        return valor == null ? "" : valor.toString();
    }

    private void limparFormulario() {
        campoId.setText("");
        campoName.setText("");
        campoScreens.setText("");
        campoValue.setText("");
        campoPayDue.setText("");
        tabela.clearSelection();
    }

    private boolean validarCampos() {
        if (campoName.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório.");
            return false;
        }
        try {
            Integer.parseInt(campoScreens.getText());
            Double.parseDouble(campoValue.getText().replace(",", "."));
            LocalDate.parse(campoPayDue.getText(), FORMATO_DATA);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Telas e Valor devem ser números válidos.");
            return false;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy.");
            return false;
        }
        return true;
    }

    private void mostrarErro(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
