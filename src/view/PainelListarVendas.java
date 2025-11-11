package src.view;

import src.controller.LanchoneteController;
import src.model.Venda;
import src.model.Pizza;
import src.model.Salgado;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * PainelListarVendas exibe todas as vendas registradas, permitindo filtrar por categoria.
 * Os dados são carregados do VendaDAO através do LanchoneteController.
 */
public class PainelListarVendas extends JPanel {

    private final LanchoneteController controller;
    private final JTable tabela;
    private final JComboBox<String> filtroCategoria;
    private final DefaultTableModel modeloTabela;

    public PainelListarVendas(LanchoneteController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Título ===
        JLabel titulo = new JLabel("📋 Histórico de Vendas", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // === Painel de filtros ===
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        painelFiltros.setBackground(Color.WHITE);

        filtroCategoria = new JComboBox<>(new String[]{"Todas", "Pizzas", "Salgados"});
        filtroCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filtroCategoria.addActionListener(e -> atualizarTabela());

        JButton btnAtualizar = new JButton("🔄 Recarregar");
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtualizar.setBackground(new Color(255, 140, 0));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.addActionListener(e -> atualizarTabela());

        painelFiltros.add(new JLabel("Filtrar por categoria:"));
        painelFiltros.add(filtroCategoria);
        painelFiltros.add(btnAtualizar);

        add(painelFiltros, BorderLayout.SOUTH);

        // === Tabela ===
        String[] colunas = {"Item", "Categoria", "Quantidade", "Preço Unitário (MT)", "Total (MT)", "Data/Hora"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabela somente leitura
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(new Color(245, 245, 245));
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Vendas Registradas",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        add(scrollTabela, BorderLayout.CENTER);

        // === Carrega dados iniciais ===
        atualizarTabela();
    }

    /**
     * Atualiza a tabela de vendas conforme o filtro selecionado.
     */
    private void atualizarTabela() {
        modeloTabela.setRowCount(0); // limpa a tabela

        List<Venda> vendas = controller.listarVendas();
        String categoriaSelecionada = (String) filtroCategoria.getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Venda v : vendas) {
            if (v.getItem() == null) continue;

            String categoria = obterCategoria(v);
            if (!categoriaSelecionada.equals("Todas") && !categoria.equals(categoriaSelecionada)) {
                continue;
            }

            String nomeItem = v.getItem().getNome();
            int quantidade = v.getQuantidade();
            double total = v.getTotal();
            String dataHora = (v.getDataHora() != null)
                    ? sdf.format(v.getDataHora())
                    : "-";

            modeloTabela.addRow(new Object[]{
                    nomeItem,
                    categoria,
                    quantidade,
                    String.format("%.2f", total),
                    dataHora
            });
        }

        // Caso não haja resultados
        if (modeloTabela.getRowCount() == 0) {
            modeloTabela.addRow(new Object[]{"-", "-", "-", "-", "-", "Nenhuma venda encontrada"});
        }
    }

    /**
     * Retorna a categoria da venda com base no tipo do item.
     */
    private String obterCategoria(Venda venda) {
        if (venda.getItem() instanceof Pizza) return "Pizzas";
        if (venda.getItem() instanceof Salgado) return "Salgados";
        return "Outros";
    }
}
