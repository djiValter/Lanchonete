package view;

import controller.LanchoneteController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PainelRegistrarVendas extends JPanel {
    private JTabbedPane abas;
    private PainelVendaPizza panelVendaPizza;
    private PainelVendaSalgado panelVendaSalgado;
    private LanchoneteController controller;
    private MenuFrame menuFrame;

    public PainelRegistrarVendas(LanchoneteController controller, MenuFrame menuFrame) {
        this.controller = controller;
        this.menuFrame = menuFrame;
        setLayout(new BorderLayout());

        abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Criar os painéis de venda para cada produto
        panelVendaPizza = new PainelVendaPizza(controller, menuFrame);
        panelVendaSalgado = new PainelVendaSalgado(controller, menuFrame);

        // Adicionar abas com ícones e tooltips
        abas.addTab("🍕 Vender Pizza", panelVendaPizza);
        abas.addTab("🥟 Vender Salgado", panelVendaSalgado);

        add(abas, BorderLayout.CENTER);
    }
}

// Painel específico para venda de pizzas (mantido igual)
class PainelVendaPizza extends JPanel {
    private JComboBox<TipoPizza> cbTipoPizza;
    private JList<Molho> listMolhos;
    private JList<Borda> listBordas;
    private JList<Recheio> listRecheios;
    private JTextField txtQuantidade;
    private JTextArea areaVendas;
    private JLabel lblPrecoBase;
    private JLabel lblPrecoAdicionais;
    private JLabel lblPrecoTotal;
    private LanchoneteController controller;
    private JButton btnVender;

    public PainelVendaPizza(LanchoneteController controller, MenuFrame menuFrame) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));

        // Painel principal com rolagem
        JPanel mainPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);

        // Painel do formulário
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Vender Pizza - Personalize sua Pizza"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Tipo de Pizza
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        form.add(new JLabel("Tipo de Pizza:"), gbc);

        cbTipoPizza = new JComboBox<>();
        carregarTiposPizza();
        cbTipoPizza.addActionListener(e -> atualizarPrecos());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        form.add(cbTipoPizza, gbc);

        // Separador - Molhos
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), gbc);

        // Título Molhos
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JLabel lblTituloMolhos = new JLabel("🍅 Molhos (Selecione um ou mais):");
        lblTituloMolhos.setFont(new Font("Arial", Font.BOLD, 12));
        lblTituloMolhos.setForeground(new Color(70, 130, 180));
        form.add(lblTituloMolhos, gbc);

        // Lista de Molhos
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;

        DefaultListModel<Molho> molhosModel = new DefaultListModel<>();
        for (Molho molho : controller.getMolhos()) {
            molhosModel.addElement(molho);
        }
        listMolhos = new JList<>(molhosModel);
        listMolhos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listMolhos.setVisibleRowCount(2);
        listMolhos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Molho) {
                    Molho molho = (Molho) value;
                    label.setText(molho.getNome() + " (+R$ " + String.format("%.2f", molho.getPrecoAdicional()) + ")");
                }
                return label;
            }
        });
        listMolhos.addListSelectionListener(e -> atualizarPrecos());

        JScrollPane molhosScroll = new JScrollPane(listMolhos);
        molhosScroll.setPreferredSize(new Dimension(0, 70));
        form.add(molhosScroll, gbc);

        // Separador - Bordas
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), gbc);

        // Título Bordas
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        JLabel lblTituloBordas = new JLabel("🧀 Bordas (Selecione uma):");
        lblTituloBordas.setFont(new Font("Arial", Font.BOLD, 12));
        lblTituloBordas.setForeground(new Color(70, 130, 180));
        form.add(lblTituloBordas, gbc);

        // Lista de Bordas
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;

        DefaultListModel<Borda> bordasModel = new DefaultListModel<>();
        for (Borda borda : controller.getBordas()) {
            bordasModel.addElement(borda);
        }
        listBordas = new JList<>(bordasModel);
        listBordas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listBordas.setVisibleRowCount(2);
        listBordas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Borda) {
                    Borda borda = (Borda) value;
                    label.setText(borda.getNome() + " (+R$ " + String.format("%.2f", borda.getPrecoAdicional()) + ")");
                }
                return label;
            }
        });
        listBordas.addListSelectionListener(e -> atualizarPrecos());

        JScrollPane bordasScroll = new JScrollPane(listBordas);
        bordasScroll.setPreferredSize(new Dimension(0, 70));
        form.add(bordasScroll, gbc);

        // Separador - Recheios
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), gbc);

        // Título Recheios
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        JLabel lblTituloRecheios = new JLabel("🥓 Recheios (Selecione um ou mais):");
        lblTituloRecheios.setFont(new Font("Arial", Font.BOLD, 12));
        lblTituloRecheios.setForeground(new Color(70, 130, 180));
        form.add(lblTituloRecheios, gbc);

        // Lista de Recheios
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;

        DefaultListModel<Recheio> recheiosModel = new DefaultListModel<>();
        for (Recheio recheio : controller.getRecheios()) {
            recheiosModel.addElement(recheio);
        }
        listRecheios = new JList<>(recheiosModel);
        listRecheios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listRecheios.setVisibleRowCount(3);
        listRecheios.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Recheio) {
                    Recheio recheio = (Recheio) value;
                    label.setText(recheio.getNome() + " (+R$ " + String.format("%.2f", recheio.getPrecoAdicional()) + ")");
                }
                return label;
            }
        });
        listRecheios.addListSelectionListener(e -> atualizarPrecos());

        JScrollPane recheiosScroll = new JScrollPane(listRecheios);
        recheiosScroll.setPreferredSize(new Dimension(0, 90));
        form.add(recheiosScroll, gbc);

        // Separador - Quantidade e Preços
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), gbc);

        // Quantidade
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JLabel("Quantidade:"), gbc);

        txtQuantidade = new JTextField("1");
        txtQuantidade.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
        });
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        form.add(txtQuantidade, gbc);

        // Preço Base
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 1;
        form.add(new JLabel("Preço Base:"), gbc);

        lblPrecoBase = new JLabel("R$ 0.00");
        lblPrecoBase.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        form.add(lblPrecoBase, gbc);

        // Preço Adicionais
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        form.add(new JLabel("Adicionais:"), gbc);

        lblPrecoAdicionais = new JLabel("R$ 0.00");
        lblPrecoAdicionais.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        form.add(lblPrecoAdicionais, gbc);

        // Preço Total
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.gridwidth = 1;
        form.add(new JLabel("Preço Total:"), gbc);

        lblPrecoTotal = new JLabel("R$ 0.00");
        lblPrecoTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecoTotal.setForeground(new Color(0, 100, 0));
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.gridwidth = 2;
        form.add(lblPrecoTotal, gbc);

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnVender = new JButton("Registrar Venda");
        btnVender.setPreferredSize(new Dimension(140, 30));
        btnVender.addActionListener(e -> registrarVenda());

        JButton btnLimpar = new JButton("Limpar Tudo");
        btnLimpar.setPreferredSize(new Dimension(100, 30));
        btnLimpar.addActionListener(e -> limparCampos());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 30));
        btnAtualizar.addActionListener(e -> carregarTiposPizza());

        panelBotoes.add(btnVender);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnAtualizar);

        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(panelBotoes, gbc);

        // Área de histórico de vendas
        areaVendas = new JTextArea();
        areaVendas.setEditable(false);
        areaVendas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaVendas.setBackground(new Color(248, 248, 248));
        JScrollPane scrollVendas = new JScrollPane(areaVendas);
        scrollVendas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Vendas de Pizza Registradas"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        mainPanel.add(form, BorderLayout.NORTH);
        mainPanel.add(scrollVendas, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);

        // Carregar dados iniciais
        atualizarPrecos();
        listarVendasRecentes();
    }

    private void carregarTiposPizza() {
        cbTipoPizza.removeAllItems();
        List<TipoPizza> tiposPizza = controller.getTiposPizza();

        for (TipoPizza tipo : tiposPizza) {
            cbTipoPizza.addItem(tipo);
        }

        if (cbTipoPizza.getItemCount() > 0) {
            cbTipoPizza.setSelectedIndex(0);
        }
    }

    private void atualizarPrecos() {
        try {
            TipoPizza tipoPizza = (TipoPizza) cbTipoPizza.getSelectedItem();
            if (tipoPizza != null) {
                double precoBase = tipoPizza.getPrecoBase();
                double precoAdicionais = 0.0;

                // Calcular preço dos adicionais
                // Molhos selecionados
                List<Molho> molhosSelecionados = listMolhos.getSelectedValuesList();
                for (Molho molho : molhosSelecionados) {
                    precoAdicionais += molho.getPrecoAdicional();
                }

                // Borda selecionada
                Borda bordaSelecionada = listBordas.getSelectedValue();
                if (bordaSelecionada != null) {
                    precoAdicionais += bordaSelecionada.getPrecoAdicional();
                }

                // Recheios selecionados
                List<Recheio> recheiosSelecionados = listRecheios.getSelectedValuesList();
                for (Recheio recheio : recheiosSelecionados) {
                    precoAdicionais += recheio.getPrecoAdicional();
                }

                int quantidade = Integer.parseInt(txtQuantidade.getText());
                double precoUnitario = precoBase + precoAdicionais;
                double precoTotal = precoUnitario * quantidade;

                lblPrecoBase.setText(String.format("R$ %.2f", precoBase));
                lblPrecoAdicionais.setText(String.format("R$ %.2f", precoAdicionais));
                lblPrecoTotal.setText(String.format("R$ %.2f", precoTotal));
            } else {
                lblPrecoBase.setText("R$ 0.00");
                lblPrecoAdicionais.setText("R$ 0.00");
                lblPrecoTotal.setText("R$ 0.00");
            }
        } catch (NumberFormatException e) {
            lblPrecoBase.setText("R$ 0.00");
            lblPrecoAdicionais.setText("R$ 0.00");
            lblPrecoTotal.setText("R$ 0.00");
        }
    }

    private void registrarVenda() {
        try {
            TipoPizza tipoPizza = (TipoPizza) cbTipoPizza.getSelectedItem();
            int quantidade = Integer.parseInt(txtQuantidade.getText());

            if (tipoPizza == null) {
                JOptionPane.showMessageDialog(this, "Selecione um tipo de pizza!");
                return;
            }
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero!");
                return;
            }

            // Criar a pizza com os adicionais selecionados
            List<Molho> molhosSelecionados = listMolhos.getSelectedValuesList();
            Borda bordaSelecionada = listBordas.getSelectedValue();
            List<Recheio> recheiosSelecionados = listRecheios.getSelectedValuesList();

            // Criar a pizza personalizada
            Pizza pizzaPersonalizada = new Pizza(tipoPizza, molhosSelecionados, bordaSelecionada, recheiosSelecionados);

            // Registrar a venda
            controller.registrarVenda(pizzaPersonalizada, quantidade);

            double total = pizzaPersonalizada.getPreco() * quantidade;

            // Montar descrição detalhada
            StringBuilder descricao = new StringBuilder();
            descricao.append("🍕 Venda registrada com sucesso!\n\n")
                    .append("Pizza: ").append(tipoPizza.getNome()).append("\n")
                    .append("Quantidade: ").append(quantidade).append("\n");

            if (!molhosSelecionados.isEmpty()) {
                descricao.append("Molhos: ");
                for (int i = 0; i < molhosSelecionados.size(); i++) {
                    if (i > 0) descricao.append(", ");
                    descricao.append(molhosSelecionados.get(i).getNome());
                }
                descricao.append("\n");
            }

            if (bordaSelecionada != null) {
                descricao.append("Borda: ").append(bordaSelecionada.getNome()).append("\n");
            }

            if (!recheiosSelecionados.isEmpty()) {
                descricao.append("Recheios: ");
                for (int i = 0; i < recheiosSelecionados.size(); i++) {
                    if (i > 0) descricao.append(", ");
                    descricao.append(recheiosSelecionados.get(i).getNome());
                }
                descricao.append("\n");
            }

            descricao.append("\n💰 Total: R$ ").append(String.format("%.2f", total));

            JOptionPane.showMessageDialog(this, descricao.toString(),
                    "Venda Registrada", JOptionPane.INFORMATION_MESSAGE);

            listarVendasRecentes();
            limparCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida! Digite um número válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarVendasRecentes() {
        areaVendas.setText("");
        List<Venda> vendas = controller.listarVendas();

        // Filtrar apenas vendas de pizza
        List<Venda> vendasPizza = vendas.stream()
                .filter(v -> v.getItem() instanceof Pizza)
                .toList();

        if (vendasPizza.isEmpty()) {
            areaVendas.setText("Nenhuma venda de pizza registrada ainda.\n\n" +
                    "Para registrar uma venda:\n" +
                    "1. Selecione o tipo de pizza\n" +
                    "2. Escolha os molhos, bordas e recheios desejados\n" +
                    "3. Informe a quantidade\n" +
                    "4. Clique em 'Registrar Venda'");
            areaVendas.setForeground(Color.GRAY);
        } else {
            areaVendas.setForeground(Color.BLACK);
            areaVendas.append("=== VENDAS DE PIZZA ===\n\n");

            for (Venda venda : vendasPizza) {
                areaVendas.append("• " + venda.toString() + "\n\n");
            }
        }
    }

    private void limparCampos() {
        txtQuantidade.setText("1");
        if (cbTipoPizza.getItemCount() > 0) {
            cbTipoPizza.setSelectedIndex(0);
        }
        listMolhos.clearSelection();
        listBordas.clearSelection();
        listRecheios.clearSelection();
        atualizarPrecos();
    }
}

// Painel específico para venda de salgados - CORRIGIDO
class PainelVendaSalgado extends JPanel {
    private JComboBox<String> cbTipoSalgado;
    private JComboBox<String> cbTipoMassa;
    private JList<String> listRecheios;
    private JTextField txtQuantidade;
    private JTextArea areaVendas;
    private JLabel lblPrecoBase;
    private JLabel lblPrecoAdicionais;
    private JLabel lblPrecoTotal;
    private LanchoneteController controller;
    private JButton btnVender;

    // Preços para cada opção
    private Map<String, Double> precoTiposSalgado;
    private Map<String, Double> precoTiposMassa;
    private Map<String, Double> precoRecheios;

    // Listas para armazenar as chaves reais
    private java.util.List<String> tiposSalgadoKeys;
    private java.util.List<String> tiposMassaKeys;
    private java.util.List<String> recheiosKeys;

    public PainelVendaSalgado(LanchoneteController controller, MenuFrame menuFrame) {
        this.controller = controller;
        inicializarPrecos();
        setLayout(new BorderLayout(10, 10));

        // Painel principal com rolagem
        JPanel mainPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);

        // Painel do formulário
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Vender Salgado - Personalize seu Salgado"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar listas de chaves
        tiposSalgadoKeys = new ArrayList<>();
        tiposMassaKeys = new ArrayList<>();
        recheiosKeys = new ArrayList<>();

        // Tipo de Salgado
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        form.add(new JLabel("Tipo de Salgado:"), gbc);

        // Usar as chaves reais do mapa para criar as opções
        String[] tiposSalgadoDisplay = new String[precoTiposSalgado.size()];
        int index = 0;
        for (Map.Entry<String, Double> entry : precoTiposSalgado.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            tiposSalgadoDisplay[index] = key + " - MZN " + String.format("%.2f", value);
            tiposSalgadoKeys.add(key);
            index++;
        }

        cbTipoSalgado = new JComboBox<>(tiposSalgadoDisplay);
        cbTipoSalgado.addActionListener(e -> atualizarPrecos());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        form.add(cbTipoSalgado, gbc);

        // Separador - Massa
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), gbc);

        // Título Massa
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JLabel lblTituloMassa = new JLabel("🥟 Tipo de Massa:");
        lblTituloMassa.setFont(new Font("Arial", Font.BOLD, 12));
        lblTituloMassa.setForeground(new Color(70, 130, 180));
        form.add(lblTituloMassa, gbc);

        // Tipo de Massa
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        form.add(new JLabel("Massa:"), gbc);

        // Usar as chaves reais do mapa para criar as opções
        String[] tiposMassaDisplay = new String[precoTiposMassa.size()];
        index = 0;
        for (Map.Entry<String, Double> entry : precoTiposMassa.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            tiposMassaDisplay[index] = key + " - MZN " + String.format("%.2f", value);
            tiposMassaKeys.add(key);
            index++;
        }

        cbTipoMassa = new JComboBox<>(tiposMassaDisplay);
        cbTipoMassa.addActionListener(e -> atualizarPrecos());
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        form.add(cbTipoMassa, gbc);

        // Separador - Recheios
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), gbc);

        // Título Recheios
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        JLabel lblTituloRecheios = new JLabel("🥓 Recheios (Selecione um ou mais):");
        lblTituloRecheios.setFont(new Font("Arial", Font.BOLD, 12));
        lblTituloRecheios.setForeground(new Color(70, 130, 180));
        form.add(lblTituloRecheios, gbc);

        // Lista de Recheios
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        gbc.fill = GridBagConstraints.BOTH;

        // Usar as chaves reais do mapa para criar as opções
        String[] recheiosDisplay = new String[precoRecheios.size()];
        index = 0;
        for (Map.Entry<String, Double> entry : precoRecheios.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            recheiosDisplay[index] = key + " - MZN " + String.format("%.2f", value);
            recheiosKeys.add(key);
            index++;
        }

        DefaultListModel<String> recheiosModel = new DefaultListModel<>();
        for (String recheio : recheiosDisplay) {
            recheiosModel.addElement(recheio);
        }

        listRecheios = new JList<>(recheiosModel);
        listRecheios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listRecheios.setVisibleRowCount(4);
        listRecheios.addListSelectionListener(e -> atualizarPrecos());

        JScrollPane recheiosScroll = new JScrollPane(listRecheios);
        recheiosScroll.setPreferredSize(new Dimension(0, 100));
        form.add(recheiosScroll, gbc);

        // Separador - Quantidade e Preços
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), gbc);

        // Quantidade
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(new JLabel("Quantidade:"), gbc);

        txtQuantidade = new JTextField("1");
        txtQuantidade.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
        });
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        form.add(txtQuantidade, gbc);

        // Preço Base
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        form.add(new JLabel("Preço Base:"), gbc);

        lblPrecoBase = new JLabel("MZN 0.00");
        lblPrecoBase.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        form.add(lblPrecoBase, gbc);

        // Preço Adicionais
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        form.add(new JLabel("Adicionais:"), gbc);

        lblPrecoAdicionais = new JLabel("MZN 0.00");
        lblPrecoAdicionais.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        form.add(lblPrecoAdicionais, gbc);

        // Preço Total
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        form.add(new JLabel("Preço Total:"), gbc);

        lblPrecoTotal = new JLabel("MZN 0.00");
        lblPrecoTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecoTotal.setForeground(new Color(0, 100, 0));
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        form.add(lblPrecoTotal, gbc);

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnVender = new JButton("Registrar Venda");
        btnVender.setPreferredSize(new Dimension(140, 30));
        btnVender.addActionListener(e -> registrarVenda());

        JButton btnLimpar = new JButton("Limpar Tudo");
        btnLimpar.setPreferredSize(new Dimension(100, 30));
        btnLimpar.addActionListener(e -> limparCampos());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 30));
        btnAtualizar.addActionListener(e -> atualizarPrecos());

        panelBotoes.add(btnVender);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnAtualizar);

        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(panelBotoes, gbc);

        // Área de histórico de vendas
        areaVendas = new JTextArea();
        areaVendas.setEditable(false);
        areaVendas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaVendas.setBackground(new Color(248, 248, 248));
        JScrollPane scrollVendas = new JScrollPane(areaVendas);
        scrollVendas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Vendas de Salgado Registradas"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        mainPanel.add(form, BorderLayout.NORTH);
        mainPanel.add(scrollVendas, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);

        // Carregar dados iniciais
        atualizarPrecos();
        listarVendasRecentes();
    }

    private void inicializarPrecos() {
        // Preços para tipos de salgado
        precoTiposSalgado = new HashMap<>();
        precoTiposSalgado.put("Frito", 5.00);
        precoTiposSalgado.put("Assado", 6.00);

        // Preços para tipos de massa
        precoTiposMassa = new HashMap<>();
        precoTiposMassa.put("Massa com Leite", 0.00);
        precoTiposMassa.put("Com caldo de galinha", 1.50);
        precoTiposMassa.put("Com farinha de trigo e manteiga", 2.00);

        // Preços para recheios
        precoRecheios = new HashMap<>();
        precoRecheios.put("Carne Moída", 2.00);
        precoRecheios.put("Queijo", 1.50);
        precoRecheios.put("Presunto e Queijo", 3.00);
        precoRecheios.put("Calabresa com Queijo", 3.50);
    }

    private void atualizarPrecos() {
        try {
            double precoBase = 0.0;
            double precoAdicionais = 0.0;

            // Calcular preço base (tipo de salgado)
            int selectedSalgadoIndex = cbTipoSalgado.getSelectedIndex();
            if (selectedSalgadoIndex >= 0 && selectedSalgadoIndex < tiposSalgadoKeys.size()) {
                String tipoKey = tiposSalgadoKeys.get(selectedSalgadoIndex);
                Double preco = precoTiposSalgado.get(tipoKey);
                if (preco != null) {
                    precoBase = preco;
                }
            }

            // Calcular preço da massa
            int selectedMassaIndex = cbTipoMassa.getSelectedIndex();
            if (selectedMassaIndex >= 0 && selectedMassaIndex < tiposMassaKeys.size()) {
                String massaKey = tiposMassaKeys.get(selectedMassaIndex);
                Double preco = precoTiposMassa.get(massaKey);
                if (preco != null) {
                    precoAdicionais += preco;
                }
            }

            // Calcular preço dos recheios selecionados
            List<String> recheiosSelecionados = listRecheios.getSelectedValuesList();
            for (String recheioDisplay : recheiosSelecionados) {
                // Encontrar a chave correspondente
                for (int i = 0; i < recheiosKeys.size(); i++) {
                    if (recheioDisplay.startsWith(recheiosKeys.get(i))) {
                        String recheioKey = recheiosKeys.get(i);
                        Double preco = precoRecheios.get(recheioKey);
                        if (preco != null) {
                            precoAdicionais += preco;
                        }
                        break;
                    }
                }
            }

            int quantidade = 1;
            try {
                quantidade = Integer.parseInt(txtQuantidade.getText());
            } catch (NumberFormatException e) {
                // Mantém o valor padrão 1
            }

            double precoUnitario = precoBase + precoAdicionais;
            double precoTotal = precoUnitario * quantidade;

            lblPrecoBase.setText(String.format("MZN %.2f", precoBase));
            lblPrecoAdicionais.setText(String.format("MZN %.2f", precoAdicionais));
            lblPrecoTotal.setText(String.format("MZN %.2f", precoTotal));
        } catch (Exception e) {
            // Em caso de qualquer erro, define os preços como zero
            lblPrecoBase.setText("MZN 0.00");
            lblPrecoAdicionais.setText("MZN 0.00");
            lblPrecoTotal.setText("MZN 0.00");
        }
    }

    private void registrarVenda() {
        try {
            int selectedSalgadoIndex = cbTipoSalgado.getSelectedIndex();
            int selectedMassaIndex = cbTipoMassa.getSelectedIndex();
            List<String> recheiosSelecionados = listRecheios.getSelectedValuesList();

            if (selectedSalgadoIndex < 0 || selectedMassaIndex < 0 || recheiosSelecionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
                return;
            }

            int quantidade = 1;
            try {
                quantidade = Integer.parseInt(txtQuantidade.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida! Digite um número válido.");
                return;
            }

            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero!");
                return;
            }

            // Obter as chaves reais
            String tipoSalgado = tiposSalgadoKeys.get(selectedSalgadoIndex);
            String tipoMassa = tiposMassaKeys.get(selectedMassaIndex);

            // Extrair nomes dos recheios
            List<String> nomesRecheios = new ArrayList<>();
            for (String recheioDisplay : recheiosSelecionados) {
                for (String recheioKey : recheiosKeys) {
                    if (recheioDisplay.startsWith(recheioKey)) {
                        nomesRecheios.add(recheioKey);
                        break;
                    }
                }
            }

            // Calcular preço total
            double precoTotal = 0.0;
            precoTotal += precoTiposSalgado.get(tipoSalgado);
            precoTotal += precoTiposMassa.get(tipoMassa);
            for (String nomeRecheio : nomesRecheios) {
                precoTotal += precoRecheios.get(nomeRecheio);
            }

            // Cadastra o salgado com os detalhes e preço calculado
            controller.cadastrarSalgado(tipoSalgado, tipoMassa, nomesRecheios, precoTotal);

            // Buscar o salgado recém-cadastrado para registrar a venda
            List<Salgado> salgados = controller.listarSalgadosOrdenados();
            Salgado salgadoCadastrado = salgados.get(salgados.size() - 1); // Último salgado cadastrado

            // Registrar a venda
            controller.registrarVenda(salgadoCadastrado, quantidade);

            double totalVenda = precoTotal * quantidade;

            // Montar descrição detalhada
            StringBuilder descricao = new StringBuilder();
            descricao.append("🥟 Venda registrada com sucesso!\n\n")
                    .append("Salgado: ").append(tipoSalgado).append("\n")
                    .append("Massa: ").append(tipoMassa).append("\n")
                    .append("Quantidade: ").append(quantidade).append("\n");

            if (!nomesRecheios.isEmpty()) {
                descricao.append("Recheios: ");
                for (int i = 0; i < nomesRecheios.size(); i++) {
                    if (i > 0) descricao.append(", ");
                    descricao.append(nomesRecheios.get(i));
                }
                descricao.append("\n");
            }

            descricao.append("\n💰 Preço Unitário: MZN ").append(String.format("%.2f", precoTotal))
                    .append("\n💰 Total: MZN ").append(String.format("%.2f", totalVenda));

            JOptionPane.showMessageDialog(this, descricao.toString(),
                    "Venda Registrada", JOptionPane.INFORMATION_MESSAGE);

            listarVendasRecentes();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarVendasRecentes() {
        areaVendas.setText("");
        List<Venda> vendas = controller.listarVendas();

        // Filtrar apenas vendas de salgado
        List<Venda> vendasSalgado = vendas.stream()
                .filter(v -> v.getItem() instanceof Salgado)
                .toList();

        if (vendasSalgado.isEmpty()) {
            areaVendas.setText("Nenhuma venda de salgado registrada ainda.\n\n" +
                    "Para registrar uma venda:\n" +
                    "1. Selecione o tipo de salgado (Frito/Assado)\n" +
                    "2. Escolha o tipo de massa\n" +
                    "3. Selecione os recheios desejados\n" +
                    "4. Informe a quantidade\n" +
                    "5. Clique em 'Registrar Venda'");
            areaVendas.setForeground(Color.GRAY);
        } else {
            areaVendas.setForeground(Color.BLACK);
            areaVendas.append("=== VENDAS DE SALGADO ===\n\n");

            for (Venda venda : vendasSalgado) {
                areaVendas.append("• " + venda.toString() + "\n\n");
            }
        }
    }

    private void limparCampos() {
        txtQuantidade.setText("1");
        if (cbTipoSalgado.getItemCount() > 0) {
            cbTipoSalgado.setSelectedIndex(0);
        }
        if (cbTipoMassa.getItemCount() > 0) {
            cbTipoMassa.setSelectedIndex(0);
        }
        listRecheios.clearSelection();
        atualizarPrecos();
    }
}