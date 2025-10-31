package view;

import controller.LanchoneteController;
import model.TipoPizza;
import model.BinarySearchTree;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PainelListarPizzas extends JPanel {
    private JTextArea areaLista;
    private LanchoneteController controller;
    private JComboBox<String> cbFiltro;
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
                BorderFactory.createTitledBorder("Tipos de Pizza Cadastrados - Ordenação com BST"),
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

        // Label e ComboBox para filtro
        panel.add(new JLabel("Ordenar com BST:"));

        String[] opcoesFiltro = {
                "Todos (ordem original)",
                "BST - Menor preço primeiro (in-order)",
                "BST - Maior preço primeiro (reverse)"
        };

        cbFiltro = new JComboBox<>(opcoesFiltro);
        cbFiltro.addActionListener(e -> atualizarListaPizzas());
        panel.add(cbFiltro);

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
        try {
            List<TipoPizza> tiposPizza = controller.getCardapioPizzas();

            if (tiposPizza == null || tiposPizza.isEmpty()) {
                areaLista.setText("Nenhum tipo de pizza cadastrado.\n\n"
                        + "Para cadastrar tipos de pizza, verifique o método inicializarTiposPizzas no controller.");
                lblEstatisticas.setText("Nenhum tipo de pizza cadastrado");
                return;
            }

            String filtroSelecionado = (String) cbFiltro.getSelectedItem();
            List<TipoPizza> pizzasOrdenadas = aplicarOrdenacaoBST(tiposPizza, filtroSelecionado);
            exibirTiposPizza(pizzasOrdenadas, filtroSelecionado);

        } catch (Exception e) {
            areaLista.setText("Erro ao carregar pizzas: " + e.getMessage() + "\n\n"
                    + "Certifique-se de que o método getCardapioPizzas() existe no controller.");
            lblEstatisticas.setText("Erro ao carregar dados");
        }
    }

    private List<TipoPizza> aplicarOrdenacaoBST(List<TipoPizza> tiposPizza, String filtro) {
        // Usar wrapper para garantir a ordenação por preço
        BinarySearchTree<PizzaPrecoWrapper> bst = new BinarySearchTree<>();

        // Inserir todos os elementos na BST usando wrapper
        for (TipoPizza pizza : tiposPizza) {
            bst.insert(new PizzaPrecoWrapper(pizza));
        }

        // Aplicar a ordenação baseada na seleção
        switch (filtro) {
            case "BST - Menor preço primeiro (in-order)":
                List<PizzaPrecoWrapper> wrappersCrescente = bst.inOrder();
                return unwrapList(wrappersCrescente);

            case "BST - Maior preço primeiro (reverse)":
                List<PizzaPrecoWrapper> wrappersCrescente2 = bst.inOrder();
                Collections.reverse(wrappersCrescente2);
                return unwrapList(wrappersCrescente2);

            case "Todos (ordem original)":
            default:
                return new ArrayList<>(tiposPizza); // Ordem original
        }
    }

    // Classe wrapper para fazer TipoPizza comparável
    private static class PizzaPrecoWrapper implements Comparable<PizzaPrecoWrapper> {
        private TipoPizza pizza;

        public PizzaPrecoWrapper(TipoPizza pizza) {
            this.pizza = pizza;
        }

        public TipoPizza getPizza() {
            return pizza;
        }

        @Override
        public int compareTo(PizzaPrecoWrapper outro) {
            return Double.compare(this.pizza.getPrecoBase(), outro.pizza.getPrecoBase());
        }
    }

    private List<TipoPizza> unwrapList(List<PizzaPrecoWrapper> wrappers) {
        List<TipoPizza> result = new ArrayList<>();
        for (PizzaPrecoWrapper wrapper : wrappers) {
            result.add(wrapper.getPizza());
        }
        return result;
    }

    private void exibirTiposPizza(List<TipoPizza> tiposPizza, String filtro) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== TIPOS DE PIZZA CADASTRADOS ===\n");
        sb.append("Método de ordenação: ").append(filtro).append("\n");
        sb.append("Total de tipos: ").append(tiposPizza.size()).append("\n");
        sb.append("Estrutura: Árvore Binária de Pesquisa (BST)\n\n");

        for (int i = 0; i < tiposPizza.size(); i++) {
            TipoPizza tipo = tiposPizza.get(i);
            sb.append("Tipo #").append(i + 1).append("\n");
            sb.append("• Nome: ").append(tipo.getNome()).append("\n");
            sb.append("• Preço Base: MT ").append(String.format("%.2f", tipo.getPrecoBase())).append("\n");

            // Usando os métodos corretos da classe TipoPizza
            if (tipo.getRecheio() != null && !tipo.getRecheio().isEmpty()) {
                sb.append("• Recheio: ").append(tipo.getRecheio()).append("\n");
            }

            if (tipo.getBorda() != null && !tipo.getBorda().isEmpty()) {
                sb.append("• Borda: ").append(tipo.getBorda()).append("\n");
            }

            sb.append("----------------------------------------\n\n");
        }

        areaLista.setText(sb.toString());
        atualizarEstatisticas(tiposPizza);
    }

    private void atualizarEstatisticas(List<TipoPizza> tiposPizza) {
        if (tiposPizza.isEmpty()) {
            lblEstatisticas.setText("Nenhum tipo de pizza cadastrado");
            return;
        }

        double precoMaisCaro = 0;
        double precoMaisBarato = Double.MAX_VALUE;
        double somaPrecos = 0;

        for (TipoPizza tipo : tiposPizza) {
            double preco = tipo.getPrecoBase();
            somaPrecos += preco;

            if (preco > precoMaisCaro) {
                precoMaisCaro = preco;
            }
            if (preco < precoMaisBarato) {
                precoMaisBarato = preco;
            }
        }

        double precoMedio = somaPrecos / tiposPizza.size();

        String estatisticas = String.format(
                "Estatísticas: %d tipos | Mais cara: MT %.2f | Mais barata: MT %.2f | Média: MT %.2f",
                tiposPizza.size(), precoMaisCaro, precoMaisBarato, precoMedio
        );

        lblEstatisticas.setText(estatisticas);
    }
}