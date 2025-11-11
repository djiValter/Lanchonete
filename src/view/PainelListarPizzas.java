package src.view;

import src.controller.LanchoneteController;
import src.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelListarPizzas extends JPanel {

    private final LanchoneteController controller;
    private final JComboBox<TipoPizza> comboTiposPizza;
    private final JComboBox<Molho> comboMolhos;
    private final JComboBox<Borda> comboBordas;
    private final JComboBox<Recheio> comboRecheios;
    private final JTextArea areaResumo;

    public PainelListarPizzas(LanchoneteController controller) {
        this.controller = new LanchoneteController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("🍕 Listar Pizzas"));
        setBackground(Color.WHITE);

        // Painel de seleção (topo)
        JPanel painelFiltros = new JPanel(new GridLayout(4, 2, 10, 10));
        painelFiltros.setBackground(Color.WHITE);

        comboTiposPizza = new JComboBox<>();
        comboMolhos = new JComboBox<>();
        comboBordas = new JComboBox<>();
        comboRecheios = new JComboBox<>();

        painelFiltros.add(new JLabel("Tipo de Pizza:"));
        painelFiltros.add(comboTiposPizza);
        painelFiltros.add(new JLabel("Molho:"));
        painelFiltros.add(comboMolhos);
        painelFiltros.add(new JLabel("Borda:"));
        painelFiltros.add(comboBordas);
        painelFiltros.add(new JLabel("Recheio:"));
        painelFiltros.add(comboRecheios);

        // Área de texto para exibir o cardápio
        areaResumo = new JTextArea(12, 40);
        areaResumo.setEditable(false);
        areaResumo.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaResumo);

        // Botão para atualizar os dados
        JButton btnAtualizar = new JButton("🔄 Atualizar Dados");
        btnAtualizar.addActionListener(e -> carregarDados());

        add(painelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        comboTiposPizza.removeAllItems();
        comboMolhos.removeAllItems();
        comboBordas.removeAllItems();
        comboRecheios.removeAllItems();

        // ===== Carrega filtros =====
        List<TipoPizza> tipos = controller.getTiposPizza();
        List<Molho> molhos = controller.getMolhos();
        List<Borda> bordas = controller.getBordas();
        List<Recheio> recheios = controller.getRecheios();

        // Ordena pelo preço crescente
        tipos.sort((p1, p2) -> Double.compare(p1.getPrecoBase(), p2.getPrecoBase()));
        molhos.sort((m1, m2) -> Double.compare(m1.getPrecoAdicional(), m2.getPrecoAdicional()));
        bordas.sort((b1, b2) -> Double.compare(b1.getPrecoAdicional(), b2.getPrecoAdicional()));
        recheios.sort((r1, r2) -> Double.compare(r1.getPrecoAdicional(), r2.getPrecoAdicional()));

        // Adiciona aos combos
        tipos.forEach(comboTiposPizza::addItem);
        molhos.forEach(comboMolhos::addItem);
        bordas.forEach(comboBordas::addItem);
        recheios.forEach(comboRecheios::addItem);

        // Mostra o cardápio atual de pizzas em ordem crescente de preço
        StringBuilder sb = new StringBuilder("=== CARDÁPIO DE PIZZAS ===\n\n");
        List<Pizza> pizzas = controller.getPizzas();

        if (pizzas.isEmpty()) {
            sb.append("Nenhuma pizza encontrada.\n");
        } else {
            // Ordena pelo preço total
            pizzas.sort((p1, p2) -> Double.compare(p1.calcularPrecoTotal(), p2.calcularPrecoTotal()));

            for (Pizza p : pizzas) {
                sb.append(p.getNome())
                        .append(" - R$ ")
                        .append(String.format("%.2f", p.calcularPrecoTotal()))
                        .append("\n");
            }
        }

        areaResumo.setText(sb.toString());
    }


}
