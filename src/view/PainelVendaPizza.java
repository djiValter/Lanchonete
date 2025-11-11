package src.view;

import src.controller.LanchoneteController;
import src.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

/**
 * Painel de venda de pizzas.
 */
public class PainelVendaPizza extends JPanel {
    private JComboBox<TipoPizza> cbTipoPizza;
    private JList<Molho> listMolhos;
    private JList<Borda> listBordas;
    private JList<Recheio> listRecheios;
    private JTextField txtQuantidade;
    private JTextArea areaVendas;
    private JLabel lblPrecoBase, lblPrecoAdicionais, lblPrecoTotal;
    private JButton btnVender;
    private LanchoneteController controller;

    public PainelVendaPizza(LanchoneteController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Painel principal que conterá todo o conteúdo
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Tipo de Pizza
        mainPanel.add(criarLabelSecao("🍕 Tipo de Pizza"));
        cbTipoPizza = new JComboBox<>();
        cbTipoPizza.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(cbTipoPizza);
        mainPanel.add(Box.createVerticalStrut(10));

        // Molhos
        mainPanel.add(criarLabelSecao("🍅 Molhos (Selecione um ou mais)"));
        listMolhos = new JList<>();
        listMolhos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollMolhos = new JScrollPane(listMolhos);
        scrollMolhos.setPreferredSize(new Dimension(0, 100));
        scrollMolhos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        mainPanel.add(scrollMolhos);
        mainPanel.add(Box.createVerticalStrut(10));

        // Bordas
        mainPanel.add(criarLabelSecao("🧀 Bordas (Selecione uma)"));
        listBordas = new JList<>();
        listBordas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollBordas = new JScrollPane(listBordas);
        scrollBordas.setPreferredSize(new Dimension(0, 100));
        scrollBordas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        mainPanel.add(scrollBordas);
        mainPanel.add(Box.createVerticalStrut(10));

        // Recheios
        mainPanel.add(criarLabelSecao("🥓 Recheios (Selecione um ou mais)"));
        listRecheios = new JList<>();
        listRecheios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollRecheios = new JScrollPane(listRecheios);
        scrollRecheios.setPreferredSize(new Dimension(0, 120));
        scrollRecheios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(scrollRecheios);
        mainPanel.add(Box.createVerticalStrut(10));

        // Quantidade e preços
        JPanel panelPreco = new JPanel(new GridLayout(4, 2, 5, 5));
        panelPreco.setBackground(Color.WHITE);
        panelPreco.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        panelPreco.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField("1");
        panelPreco.add(txtQuantidade);

        panelPreco.add(new JLabel("Preço Base:"));
        lblPrecoBase = new JLabel("R$ 0.00");
        panelPreco.add(lblPrecoBase);

        panelPreco.add(new JLabel("Adicionais:"));
        lblPrecoAdicionais = new JLabel("R$ 0.00");
        panelPreco.add(lblPrecoAdicionais);

        panelPreco.add(new JLabel("Preço Total:"));
        lblPrecoTotal = new JLabel("R$ 0.00");
        lblPrecoTotal.setForeground(new Color(0, 128, 0));
        lblPrecoTotal.setFont(new Font("Arial", Font.BOLD, 14));
        panelPreco.add(lblPrecoTotal);

        mainPanel.add(panelPreco);
        mainPanel.add(Box.createVerticalStrut(10));

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotoes.setBackground(Color.WHITE);
        panelBotoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnVender = criarBotao("Registrar Venda");
        JButton btnLimpar = criarBotao("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        panelBotoes.add(btnVender);
        panelBotoes.add(btnLimpar);
        mainPanel.add(panelBotoes);
        mainPanel.add(Box.createVerticalStrut(10));

        // Área de histórico
        areaVendas = new JTextArea();
        areaVendas.setEditable(false);
        areaVendas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollVendas = new JScrollPane(areaVendas);
        scrollVendas.setBorder(BorderFactory.createTitledBorder("📜 Vendas de Pizza"));
        scrollVendas.setPreferredSize(new Dimension(0, 200));
        scrollVendas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        mainPanel.add(scrollVendas);

        // SOLUÇÃO: Envolver todo o mainPanel em um JScrollPane
        JScrollPane scrollPrincipal = new JScrollPane(mainPanel);
        scrollPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        scrollPrincipal.setBorder(null);

        // Adicionar o scroll ao painel principal
        add(scrollPrincipal, BorderLayout.CENTER);

        // Carregar dados do DAO
        carregarDados();

        // Adiciona listeners para atualizar preço automaticamente
        adicionarListeners();

        listarVendasRecentes();
    }

    private void carregarDados() {
        // Tipos de Pizza
        DefaultComboBoxModel<TipoPizza> modelTipoPizza = new DefaultComboBoxModel<>();
        for (TipoPizza t : controller.getTiposPizza()) modelTipoPizza.addElement(t);
        cbTipoPizza.setModel(modelTipoPizza);

        // Molhos
        DefaultListModel<Molho> modelMolhos = new DefaultListModel<>();
        for (Molho m : controller.getMolhos()) modelMolhos.addElement(m);
        listMolhos.setModel(modelMolhos);

        // Bordas
        DefaultListModel<Borda> modelBordas = new DefaultListModel<>();
        for (Borda b : controller.getBordas()) modelBordas.addElement(b);
        listBordas.setModel(modelBordas);

        // Recheios
        DefaultListModel<Recheio> modelRecheios = new DefaultListModel<>();
        for (Recheio r : controller.getRecheios()) modelRecheios.addElement(r);
        listRecheios.setModel(modelRecheios);
    }

    private void adicionarListeners() {
        // Atualiza preço ao mudar quantidade
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
        };
        txtQuantidade.getDocument().addDocumentListener(docListener);

        // Atualiza preço ao mudar tipo de pizza
        cbTipoPizza.addActionListener(e -> atualizarPrecos());

        // Atualiza preço ao mudar seleção de molhos
        listMolhos.addListSelectionListener(e -> atualizarPrecos());

        // Atualiza preço ao mudar borda
        listBordas.addListSelectionListener(e -> atualizarPrecos());

        // Atualiza preço ao mudar recheios
        listRecheios.addListSelectionListener(e -> atualizarPrecos());

        // Botão vender
        btnVender.addActionListener(e -> registrarVenda());
    }

    private JLabel criarLabelSecao(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(70, 130, 180));
        label.setBorder(new EmptyBorder(5, 0, 5, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(new Color(70, 130, 180));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        return botao;
    }

    private void atualizarPrecos() {
        try {
            TipoPizza tipo = (TipoPizza) cbTipoPizza.getSelectedItem();
            if (tipo == null) return;
            double base = tipo.getPrecoBase();
            double adicionais = 0;

            for (Molho m : listMolhos.getSelectedValuesList()) adicionais += m.getPrecoAdicional();
            Borda b = listBordas.getSelectedValue();
            if (b != null) adicionais += b.getPrecoAdicional();
            for (Recheio r : listRecheios.getSelectedValuesList()) adicionais += r.getPrecoAdicional();

            int qtd = 1;
            try { qtd = Integer.parseInt(txtQuantidade.getText()); } catch (NumberFormatException ignored) {}

            lblPrecoBase.setText(String.format("R$ %.2f", base));
            lblPrecoAdicionais.setText(String.format("R$ %.2f", adicionais));
            lblPrecoTotal.setText(String.format("R$ %.2f", (base + adicionais) * qtd));
        } catch (Exception e) {
            lblPrecoBase.setText("R$ 0.00");
            lblPrecoAdicionais.setText("R$ 0.00");
            lblPrecoTotal.setText("R$ 0.00");
        }
    }

    private void registrarVenda() {
        try {
            TipoPizza tipo = (TipoPizza) cbTipoPizza.getSelectedItem();
            if (tipo == null) return;
            int qtd = 1;
            try { qtd = Integer.parseInt(txtQuantidade.getText()); } catch (NumberFormatException ignored) {}

            Pizza pizza = new Pizza(tipo, listMolhos.getSelectedValuesList());
            pizza.setBorda(listBordas.getSelectedValue());
            for (Recheio r : listRecheios.getSelectedValuesList()) pizza.adicionarRecheio(r);

            controller.registrarVenda(pizza, qtd);
            JOptionPane.showMessageDialog(this, "Venda registrada!");
            listarVendasRecentes();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda: " + e.getMessage());
        }
    }

    private void listarVendasRecentes() {
        areaVendas.setText("");
        List<Venda> vendas = controller.listarVendas().stream()
                .filter(v -> v.getItem() instanceof Pizza).toList();
        if (vendas.isEmpty()) areaVendas.setText("Nenhuma venda registrada ainda.");
        else vendas.forEach(v -> areaVendas.append(v + "\n"));
    }

    private void limparCampos() {
        txtQuantidade.setText("1");
        if (cbTipoPizza.getItemCount() > 0) cbTipoPizza.setSelectedIndex(0);
        listMolhos.clearSelection();
        listBordas.clearSelection();
        listRecheios.clearSelection();
        atualizarPrecos();
    }
}