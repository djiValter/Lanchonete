package view;

import controller.LanchoneteController;
import model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class PainelPizzas extends JPanel {
    private JComboBox<String> cbPizzasOrdenadas;
    private JList<Molho> listMolhos;
    private JList<Borda> listBordas;
    private JList<Recheio> listRecheios;
    private JTextArea areaLista;
    private LanchoneteController controller;
    private MenuFrame menuFrame;
    private JLabel lblPreco;
    private JButton btnSalvar;

    public PainelPizzas(LanchoneteController controller, MenuFrame menuFrame) {
        this.controller = controller;
        this.menuFrame = menuFrame;
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Adicionar Pizzas"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        cbPizzasOrdenadas = new JComboBox<>();

        // Listas para Molhos, Bordas e Recheios
        listMolhos = new JList<>(controller.getMolhos().toArray(new Molho[0]));
        listMolhos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listMolhos.setVisibleRowCount(3);

        listBordas = new JList<>(controller.getBordas().toArray(new Borda[0]));
        listBordas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listBordas.setVisibleRowCount(3);

        listRecheios = new JList<>(controller.getRecheios().toArray(new Recheio[0]));
        listRecheios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listRecheios.setVisibleRowCount(3);

        JScrollPane molhosScroll = new JScrollPane(listMolhos);
        molhosScroll.setPreferredSize(new Dimension(200, 60));

        JScrollPane bordasScroll = new JScrollPane(listBordas);
        bordasScroll.setPreferredSize(new Dimension(200, 60));

        JScrollPane recheiosScroll = new JScrollPane(listRecheios);
        recheiosScroll.setPreferredSize(new Dimension(200, 60));

        lblPreco = new JLabel("Preço: MT 0.00");
        lblPreco.setFont(new Font("Arial", Font.BOLD, 12));
        lblPreco.setForeground(new Color(0, 100, 0));

        // Linha 1: Pizzas Disponíveis (agora é a primeira linha)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        form.add(new JLabel("Pizzas Disponíveis:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        form.add(cbPizzasOrdenadas, gbc);

        // Linha 2: Molhos
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        form.add(new JLabel("Molhos:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        form.add(molhosScroll, gbc);

        // Linha 3: Bordas
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        form.add(new JLabel("Borda:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        form.add(bordasScroll, gbc);

        // Linha 4: Recheios
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        form.add(new JLabel("Recheios:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        form.add(recheiosScroll, gbc);

        // Linha 5: Preço
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        form.add(new JLabel("Preço Total:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        form.add(lblPreco, gbc);

        // Linha 6: Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnSalvar = new JButton("Adicionar Pizza");
        btnSalvar.setPreferredSize(new Dimension(140, 30));
        JButton btnListar = new JButton("Atualizar Lista");
        btnListar.setPreferredSize(new Dimension(120, 30));
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setPreferredSize(new Dimension(80, 30));

        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnListar);
        panelBotoes.add(btnLimpar);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(panelBotoes, gbc);

        // Área de texto para listagem
        areaLista = new JTextArea();
        areaLista.setEditable(false);
        areaLista.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLista.setBackground(new Color(248, 248, 248));
        JScrollPane scroll = new JScrollPane(areaLista);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Pizzas Adicionadas"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Configurar listeners
        cbPizzasOrdenadas.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    atualizarPrecoTotal();
                }
            }
        });

        // Listeners para as listas de seleção
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                atualizarPrecoTotal();
                validarCampos();
            }
        };

        listMolhos.addListSelectionListener(selectionListener);
        listBordas.addListSelectionListener(selectionListener);
        listRecheios.addListSelectionListener(selectionListener);

        // Listeners para os botões
        btnSalvar.addActionListener(e -> salvarPizza());
        btnListar.addActionListener(e -> {
            atualizarComboBoxPizzas();
            listarPizzas();
        });
        btnLimpar.addActionListener(e -> limparCampos());

        // Inicialização
        atualizarComboBoxPizzas();
        atualizarPrecoTotal();
        validarCampos();
        listarPizzas();
    }

    private void atualizarComboBoxPizzas() {
        cbPizzasOrdenadas.removeAllItems();

        // Usa Selection Sort como padrão (ou pode usar qualquer outro)
        List<String> nomesPizzas = controller.listarNomesPizzasOrdenadasSelectionSort();

        for (String nome : nomesPizzas) {
            cbPizzasOrdenadas.addItem(nome);
        }

        if (!nomesPizzas.isEmpty()) {
            cbPizzasOrdenadas.setSelectedIndex(0);
        }
    }

    private void atualizarPrecoTotal() {
        String pizzaSelecionada = (String) cbPizzasOrdenadas.getSelectedItem();
        if (pizzaSelecionada != null) {
            try {
                String[] partes = pizzaSelecionada.split(" - MT ");
                if (partes.length > 1) {
                    double precoBase = Double.parseDouble(partes[1].replace(",", "."));
                    List<Molho> molhosSelecionados = listMolhos.getSelectedValuesList();
                    Borda bordaSelecionada = listBordas.getSelectedValue();
                    List<Recheio> recheiosSelecionados = listRecheios.getSelectedValuesList();

                    double precoTotal = precoBase;

                    // Adicionar preço dos molhos
                    for (Molho molho : molhosSelecionados) {
                        precoTotal += molho.getPrecoAdicional();
                    }

                    // Adicionar preço da borda (se selecionada)
                    if (bordaSelecionada != null) {
                        precoTotal += bordaSelecionada.getPrecoAdicional();
                    }

                    // Adicionar preço dos recheios
                    for (Recheio recheio : recheiosSelecionados) {
                        precoTotal += recheio.getPrecoAdicional();
                    }

                    lblPreco.setText(String.format("Preço: MT %.2f", precoTotal));
                }
            } catch (NumberFormatException e) {
                lblPreco.setText("Preço: MT 0.00");
            }
        }
    }

    private void validarCampos() {
        // Apenas a pizza selecionada é obrigatória
        // Molhos, bordas e recheios são opcionais
        boolean pizzaSelecionada = cbPizzasOrdenadas.getSelectedItem() != null;
        boolean camposValidos = pizzaSelecionada;

        btnSalvar.setEnabled(camposValidos);
        if (camposValidos) {
            btnSalvar.setBackground(new Color(70, 130, 180));
            btnSalvar.setForeground(Color.WHITE);
        } else {
            btnSalvar.setBackground(null);
            btnSalvar.setForeground(null);
        }
    }

    private void salvarPizza() {
        try {
            String pizzaSelecionada = (String) cbPizzasOrdenadas.getSelectedItem();
            List<Molho> molhosSelecionados = listMolhos.getSelectedValuesList();
            Borda bordaSelecionada = listBordas.getSelectedValue();
            List<Recheio> recheiosSelecionados = listRecheios.getSelectedValuesList();

            // Apenas a pizza é obrigatória, os outros componentes são opcionais
            if (pizzaSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma pizza!");
                return;
            }

            // Extrai o nome da pizza (remove o preço)
            String nomePizza = pizzaSelecionada.split(" - MT ")[0];

            // Encontra o TipoPizza correspondente
            TipoPizza tipoSelecionado = null;
            for (TipoPizza tipo : controller.getTiposPizza()) {
                if (tipo.getNome().equals(nomePizza)) {
                    tipoSelecionado = tipo;
                    break;
                }
            }

            if (tipoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Tipo de pizza não encontrado!");
                return;
            }

            // Cadastra a pizza com todos os componentes (podem ser nulos ou vazios)
            controller.cadastrarPizza(tipoSelecionado, molhosSelecionados, bordaSelecionada, recheiosSelecionados);

            double precoTotal = controller.calcularPrecoPizza(tipoSelecionado, molhosSelecionados,
                    bordaSelecionada, recheiosSelecionados);

            JOptionPane.showMessageDialog(this,
                    "Pizza " + nomePizza + " adicionada com sucesso!\n" +
                            "Preço: MT " + String.format("%.2f", precoTotal),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            limparCampos();
            atualizarComboBoxPizzas();
            listarPizzas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar pizza: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarPizzas() {
        areaLista.setText("");

        // Usa Selection Sort como padrão para listar as pizzas cadastradas
        List<Pizza> pizzas = controller.listarPizzasOrdenadasSelectionSort();

        if (pizzas.isEmpty()) {
            areaLista.setText("Nenhuma pizza cadastrada ainda.\n" +
                    "Selecione uma pizza acima e clique em 'Adicionar Pizza'.\n" +
                    "Molhos, bordas e recheios são opcionais.");
            areaLista.setForeground(Color.GRAY);
        } else {
            areaLista.setForeground(Color.BLACK);
            for (Pizza p : pizzas) {
                areaLista.append("• " + p.toString() + "\n\n");
            }
        }
    }

    private void limparCampos() {
        listMolhos.clearSelection();
        listBordas.clearSelection();
        listRecheios.clearSelection();
        atualizarPrecoTotal();
        validarCampos();
    }
}