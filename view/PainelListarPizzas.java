package view;

import controller.LanchoneteController;
import model.Pizza;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelListarPizzas extends JPanel {
    private JTextArea areaLista;
    private LanchoneteController controller;
    private JComboBox<String> cbOrdenacao;
    private JLabel lblEstatisticas;

    public PainelListarPizzas(LanchoneteController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));

        // Painel de controles no topo
        JPanel panelControles = criarPanelControles();

        // Área de listagem
        areaLista = new JTextArea();
        areaLista.setEditable(false);
        areaLista.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLista.setBackground(new Color(248, 248, 248));

        JScrollPane scroll = new JScrollPane(areaLista);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Pizzas Cadastradas"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Painel de estatísticas
        JPanel panelEstatisticas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEstatisticas = new JLabel();
        lblEstatisticas.setFont(new Font("Arial", Font.BOLD, 12));
        lblEstatisticas.setForeground(new Color(0, 100, 0));
        panelEstatisticas.add(lblEstatisticas);

        add(panelControles, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelEstatisticas, BorderLayout.SOUTH);

        // Carregar dados iniciais
        atualizarListaPizzas();
    }

    private JPanel criarPanelControles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Label e ComboBox para ordenação
        panel.add(new JLabel("Ordenar por:"));

        String[] opcoesOrdenacao = {
                "Selection Sort (Preço Crescente)",
                "Bubble Sort (Preço Crescente)",
                "Selection Sort (Lista Interna)",
                "Bubble Sort (Lista Interna)"
        };

        cbOrdenacao = new JComboBox<>(opcoesOrdenacao);
        cbOrdenacao.addActionListener(e -> atualizarListaPizzas());
        panel.add(cbOrdenacao);

        // Botão para atualizar
        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> atualizarListaPizzas());
        panel.add(btnAtualizar);

        // Botão para limpar
        JButton btnLimpar = new JButton("Limpar Tela");
        btnLimpar.addActionListener(e -> areaLista.setText(""));
        panel.add(btnLimpar);

        return panel;
    }

    private void atualizarListaPizzas() {
        List<Pizza> pizzas;
        String metodoOrdenacao = "";

        int selectedIndex = cbOrdenacao.getSelectedIndex();
        switch (selectedIndex) {
            case 0:
                pizzas = controller.listarPizzasOrdenadasSelectionSort();
                metodoOrdenacao = "Selection Sort (DAO)";
                break;
            case 1:
                pizzas = controller.listarPizzasOrdenadasBubbleSort();
                metodoOrdenacao = "Bubble Sort (DAO)";
                break;
            case 2:
                pizzas = controller.ordenarPizzasSelectionSort();
                metodoOrdenacao = "Selection Sort (Lista Interna)";
                break;
            case 3:
                pizzas = controller.ordenarPizzasBubbleSort();
                metodoOrdenacao = "Bubble Sort (Lista Interna)";
                break;
            default:
                pizzas = controller.listarPizzasOrdenadasSelectionSort();
                metodoOrdenacao = "Selection Sort (DAO)";
        }

        exibirPizzas(pizzas, metodoOrdenacao);
    }

    private void exibirPizzas(List<Pizza> pizzas, String metodoOrdenacao) {
        StringBuilder sb = new StringBuilder();

        if (pizzas.isEmpty()) {
            sb.append("Nenhuma pizza cadastrada.\n\n");
            sb.append("Para cadastrar pizzas, vá para a tela 'Registrar Vendas' -> 'Pizza'.");
        } else {
            sb.append("=== PIZZAS CADASTRADAS ===\n");
            sb.append("Método de ordenação: ").append(metodoOrdenacao).append("\n");
            sb.append("Total de pizzas: ").append(pizzas.size()).append("\n\n");

            for (int i = 0; i < pizzas.size(); i++) {
                Pizza p = pizzas.get(i);
                sb.append("Pizza #").append(i + 1).append("\n");
                sb.append("• Tipo: ").append(p.getTipo().getNome()).append("\n");
                sb.append("• Preço Base: R$ ").append(String.format("%.2f", p.getTipo().getPrecoBase())).append("\n");

                // Molhos
                if (!p.getMolhos().isEmpty()) {
                    sb.append("• Molhos: ");
                    for (int j = 0; j < p.getMolhos().size(); j++) {
                        if (j > 0) sb.append(", ");
                        sb.append(p.getMolhos().get(j).getNome());
                    }
                    sb.append("\n");
                }

                // Borda
                if (p.getBorda() != null) {
                    sb.append("• Borda: ").append(p.getBorda().getNome()).append("\n");
                }

                // Recheios
                if (!p.getRecheios().isEmpty()) {
                    sb.append("• Recheios: ");
                    for (int j = 0; j < p.getRecheios().size(); j++) {
                        if (j > 0) sb.append(", ");
                        sb.append(p.getRecheios().get(j).getNome());
                    }
                    sb.append("\n");
                }

                sb.append("• PREÇO TOTAL: R$ ").append(String.format("%.2f", p.getPreco())).append("\n");
                sb.append("----------------------------------------\n\n");
            }
        }

        areaLista.setText(sb.toString());
        atualizarEstatisticas(pizzas);
    }

    private void atualizarEstatisticas(List<Pizza> pizzas) {
        if (pizzas.isEmpty()) {
            lblEstatisticas.setText("Nenhuma pizza cadastrada");
            return;
        }

        double precoMaisCaro = 0;
        double precoMaisBarato = Double.MAX_VALUE;
        double somaPrecos = 0;

        for (Pizza p : pizzas) {
            double preco = p.getPreco();
            somaPrecos += preco;

            if (preco > precoMaisCaro) {
                precoMaisCaro = preco;
            }
            if (preco < precoMaisBarato) {
                precoMaisBarato = preco;
            }
        }

        double precoMedio = somaPrecos / pizzas.size();

        String estatisticas = String.format(
                "Estatísticas: %d pizzas | Mais cara: R$ %.2f | Mais barata: R$ %.2f | Média: R$ %.2f",
                pizzas.size(), precoMaisCaro, precoMaisBarato, precoMedio
        );

        lblEstatisticas.setText(estatisticas);
    }
}