package src.view;

import src.controller.LanchoneteController;
import src.dao.BinarySearchTree;
import src.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PainelVendaSalgado extends JPanel {

    private JComboBox<String> cbTipoSalgado;
    private JComboBox<String> cbTipoMassa;
    private JList<String> listRecheios;
    private JTextField txtQuantidade;
    private JTextArea areaVendas;
    private JLabel lblPrecoBase;
    private JLabel lblPrecoAdicionais;
    private JLabel lblPrecoTotal;
    private JButton btnVender;

    private LanchoneteController controller;

    private Map<String, Double> precoTiposSalgado;
    private Map<String, Double> precoTiposMassa;
    private Map<String, Double> precoRecheios;

    private List<String> tiposSalgadoKeys;
    private List<String> tiposMassaKeys;
    private List<String> recheiosKeys;

    // ===== Construtor =====
    public PainelVendaSalgado(LanchoneteController controller) {
        this.controller = controller;

        // Busca dados diretamente do DAO
        this.precoTiposSalgado = controller.getItemDAO().getPrecoTiposSalgado();
        this.precoTiposMassa = controller.getItemDAO().getPrecoTiposMassa();
        this.precoRecheios = controller.getItemDAO().getPrecoRecheios();

        tiposSalgadoKeys = new ArrayList<>(precoTiposSalgado.keySet());
        tiposMassaKeys = new ArrayList<>(precoTiposMassa.keySet());
        recheiosKeys = new ArrayList<>(precoRecheios.keySet());

        initComponents();
        carregarDados(); // << aqui!
        listarVendasRecentes();
    }

    // ===== Inicialização dos componentes =====
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Tipo de Salgado
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        form.add(new JLabel("Tipo de Salgado:"), gbc);

        String[] tiposSalgadoDisplay = precoTiposSalgado.keySet().stream()
                .map(k -> k + " - MZN " + String.format("%.2f", precoTiposSalgado.get(k)))
                .toArray(String[]::new);

        cbTipoSalgado = new JComboBox<>(tiposSalgadoDisplay);
        cbTipoSalgado.addActionListener(e -> atualizarPrecos());
        gbc.gridx = 1; gbc.gridy = row++; gbc.gridwidth = 2; gbc.weightx = 1.0;
        form.add(cbTipoSalgado, gbc);

        // Tipo de Massa
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        form.add(new JLabel("Massa:"), gbc);

        String[] tiposMassaDisplay = precoTiposMassa.keySet().stream()
                .map(k -> k + " - MZN " + String.format("%.2f", precoTiposMassa.get(k)))
                .toArray(String[]::new);

        cbTipoMassa = new JComboBox<>(tiposMassaDisplay);
        cbTipoMassa.addActionListener(e -> atualizarPrecos());
        gbc.gridx = 1; gbc.gridy = row++; gbc.gridwidth = 2;
        form.add(cbTipoMassa, gbc);

        // Recheios
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3;
        JLabel lblRecheios = new JLabel("🥓 Recheios (Selecione um ou mais):");
        lblRecheios.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRecheios.setForeground(new Color(70, 130, 180));
        form.add(lblRecheios, gbc);

        DefaultListModel<String> recheiosModel = new DefaultListModel<>();
        for (String key : precoRecheios.keySet()) {
            String display = key + " - MZN " + String.format("%.2f", precoRecheios.get(key));
            recheiosModel.addElement(display);
        }
        listRecheios = new JList<>(recheiosModel);
        listRecheios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listRecheios.addListSelectionListener(e -> atualizarPrecos());
        listRecheios.setVisibleRowCount(4);
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.4;
        form.add(new JScrollPane(listRecheios), gbc);

        // Quantidade
        gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        form.add(new JLabel("Quantidade:"), gbc);

        txtQuantidade = new JTextField("1");
        txtQuantidade.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { atualizarPrecos(); }
        });
        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 2;
        form.add(txtQuantidade, gbc);

        // Preços
        row++;
        lblPrecoBase = new JLabel("MZN 0.00");
        lblPrecoAdicionais = new JLabel("MZN 0.00");
        lblPrecoTotal = new JLabel("MZN 0.00");
        lblPrecoTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrecoTotal.setForeground(new Color(0, 100, 0));

        gbc.gridx = 0; gbc.gridy = ++row; form.add(new JLabel("Preço Base:"), gbc);
        gbc.gridx = 1; form.add(lblPrecoBase, gbc);
        gbc.gridx = 0; gbc.gridy = ++row; form.add(new JLabel("Adicionais:"), gbc);
        gbc.gridx = 1; form.add(lblPrecoAdicionais, gbc);
        gbc.gridx = 0; gbc.gridy = ++row; form.add(new JLabel("Preço Total:"), gbc);
        gbc.gridx = 1; form.add(lblPrecoTotal, gbc);

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotoes.setOpaque(false);
        btnVender = new JButton("Registrar Venda");
        btnVender.addActionListener(e -> registrarVenda());
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        panelBotoes.add(btnVender);
        panelBotoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 3;
        form.add(panelBotoes, gbc);

        // Área de vendas
        areaVendas = new JTextArea();
        areaVendas.setEditable(false);
        areaVendas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaVendas.setBackground(new Color(250, 250, 250));
        JScrollPane scrollVendas = new JScrollPane(areaVendas);
        scrollVendas.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        mainPanel.add(form);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(scrollVendas);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void carregarDados() {
        // ===== TIPOS DE SALGADO =====
        List<String> tiposSalgado = new ArrayList<>(precoTiposSalgado.keySet());
        tiposSalgado.sort((a, b) -> Double.compare(precoTiposSalgado.get(a), precoTiposSalgado.get(b)));

        DefaultComboBoxModel<String> modelSalgado = new DefaultComboBoxModel<>();
        for (String tipo : tiposSalgado) {
            modelSalgado.addElement(tipo + " - MZN " + String.format("%.2f", precoTiposSalgado.get(tipo)));
        }
        cbTipoSalgado.setModel(modelSalgado);
        tiposSalgadoKeys = tiposSalgado;

        // ===== MASSAS =====
        List<String> massas = new ArrayList<>(precoTiposMassa.keySet());
        massas.sort((a, b) -> Double.compare(precoTiposMassa.get(a), precoTiposMassa.get(b)));

        DefaultComboBoxModel<String> modelMassa = new DefaultComboBoxModel<>();
        for (String massa : massas) {
            modelMassa.addElement(massa + " - MZN " + String.format("%.2f", precoTiposMassa.get(massa)));
        }
        cbTipoMassa.setModel(modelMassa);
        tiposMassaKeys = massas;

        // ===== RECHEIOS =====
        List<String> recheios = new ArrayList<>(precoRecheios.keySet());
        recheios.sort((a, b) -> Double.compare(precoRecheios.get(a), precoRecheios.get(b)));

        DefaultListModel<String> modelRecheios = new DefaultListModel<>();
        for (String recheio : recheios) {
            modelRecheios.addElement(recheio + " - MZN " + String.format("%.2f", precoRecheios.get(recheio)));
        }
        listRecheios.setModel(modelRecheios);
        recheiosKeys = recheios;
    }



    // ===== Atualização de preços =====
    private void atualizarPrecos() {
        try {
            int qtd = Integer.parseInt(txtQuantidade.getText());

            String tipoSalgadoKey = tiposSalgadoKeys.get(cbTipoSalgado.getSelectedIndex());
            String massaKey = tiposMassaKeys.get(cbTipoMassa.getSelectedIndex());

            double base = precoTiposSalgado.get(tipoSalgadoKey) + precoTiposMassa.get(massaKey);

            double adicionais = 0;
            for (int idx : listRecheios.getSelectedIndices()) {
                String key = recheiosKeys.get(idx);
                adicionais += precoRecheios.get(key);
            }

            lblPrecoBase.setText(String.format("MZN %.2f", base));
            lblPrecoAdicionais.setText(String.format("MZN %.2f", adicionais));
            lblPrecoTotal.setText(String.format("MZN %.2f", (base + adicionais) * qtd));

        } catch (Exception e) {
            lblPrecoBase.setText("MZN 0.00");
            lblPrecoAdicionais.setText("MZN 0.00");
            lblPrecoTotal.setText("MZN 0.00");
        }
    }

    // ===== Registrar venda =====
    private void registrarVenda() {
        try {
            int qtd = Integer.parseInt(txtQuantidade.getText());

            String tipoSalgadoKey = tiposSalgadoKeys.get(cbTipoSalgado.getSelectedIndex());
            String massaKey = tiposMassaKeys.get(cbTipoMassa.getSelectedIndex());

            List<String> recheiosSelecionados = new ArrayList<>();
            double adicionais = 0;
            for (int idx : listRecheios.getSelectedIndices()) {
                String key = recheiosKeys.get(idx);
                recheiosSelecionados.add(key);
                adicionais += precoRecheios.get(key); // soma os adicionais corretamente
            }

            double precoBase = precoTiposSalgado.get(tipoSalgadoKey) + precoTiposMassa.get(massaKey);

            Salgado salgado = new Salgado(
                    tipoSalgadoKey,
                    tipoSalgadoKey + " com " + String.join(", ", recheiosSelecionados),
                    recheiosSelecionados,
                    precoBase + adicionais // aqui incluímos os adicionais
            );

            controller.registrarVenda(salgado, qtd);
            JOptionPane.showMessageDialog(this, "Venda registrada!");
            listarVendasRecentes();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda: " + e.getMessage());
        }
    }


    // ===== Listar vendas =====
    private void listarVendasRecentes() {
        areaVendas.setText("");
        List<Venda> vendas = controller.listarVendas().stream()
                .filter(v -> v.getItem() instanceof Salgado)
                .toList();
        if (vendas.isEmpty()) areaVendas.setText("Nenhuma venda registrada ainda.");
        else vendas.forEach(v -> areaVendas.append(v + "\n"));
    }

    // ===== Limpar campos =====
    private void limparCampos() {
        txtQuantidade.setText("1");
        if (cbTipoSalgado.getItemCount() > 0) cbTipoSalgado.setSelectedIndex(0);
        if (cbTipoMassa.getItemCount() > 0) cbTipoMassa.setSelectedIndex(0);
        listRecheios.clearSelection();
        atualizarPrecos();
    }
}
