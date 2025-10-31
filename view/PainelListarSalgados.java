package view;

import controller.LanchoneteController;
import model.Salgado;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelListarSalgados extends JPanel {
    private JTextArea areaLista;
    private LanchoneteController controller;
    private JComboBox<String> cbOrdenacao;
    private JLabel lblEstatisticas;
    private JTextField txtFiltro;

    public PainelListarSalgados(LanchoneteController controller) {
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
                BorderFactory.createTitledBorder("Salgados Cadastrados"),
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
        atualizarListaSalgados();
    }

    private JPanel criarPanelControles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Label e ComboBox para ordenação
        panel.add(new JLabel("Ordenar por:"));

        String[] opcoesOrdenacao = {
                "Preço (Crescente)",
                "Preço (Decrescente)",
                "Tipo (A-Z)",
                "Tipo (Z-A)"
        };

        cbOrdenacao = new JComboBox<>(opcoesOrdenacao);
        cbOrdenacao.addActionListener(e -> atualizarListaSalgados());
        panel.add(cbOrdenacao);

        // Campo de filtro
        panel.add(new JLabel("Filtrar:"));
        txtFiltro = new JTextField(15);
        txtFiltro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarSalgados(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarSalgados(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarSalgados(); }
        });
        panel.add(txtFiltro);

        // Botão para atualizar
        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> atualizarListaSalgados());
        panel.add(btnAtualizar);

        // Botão para limpar
        JButton btnLimpar = new JButton("Limpar Filtros");
        btnLimpar.addActionListener(e -> {
            txtFiltro.setText("");
            cbOrdenacao.setSelectedIndex(0);
            atualizarListaSalgados();
        });
        panel.add(btnLimpar);

        return panel;
    }

    private void atualizarListaSalgados() {
        List<Salgado> salgados = controller.listarSalgadosOrdenados();
        aplicarFiltroEOrdenacao(salgados);
    }

    private void aplicarFiltroEOrdenacao(List<Salgado> salgados) {
        String filtro = txtFiltro.getText().toLowerCase();
        int ordenacao = cbOrdenacao.getSelectedIndex();

        // Aplicar filtro
        java.util.List<Salgado> salgadosFiltrados = new java.util.ArrayList<>();
        for (Salgado s : salgados) {
            if (filtro.isEmpty() ||
                    s.getTipo().toLowerCase().contains(filtro) ||
                    s.getMassa().toLowerCase().contains(filtro) ||
                    s.getRecheios().toString().toLowerCase().contains(filtro) ||
                    String.valueOf(s.getPreco()).contains(filtro)) {
                salgadosFiltrados.add(s);
            }
        }

        // Aplicar ordenação
        switch (ordenacao) {
            case 0: // Preço Crescente
                salgadosFiltrados.sort((s1, s2) -> Double.compare(s1.getPreco(), s2.getPreco()));
                break;
            case 1: // Preço Decrescente
                salgadosFiltrados.sort((s1, s2) -> Double.compare(s2.getPreco(), s1.getPreco()));
                break;
            case 2: // Tipo A-Z
                salgadosFiltrados.sort((s1, s2) -> s1.getTipo().compareToIgnoreCase(s2.getTipo()));
                break;
            case 3: // Tipo Z-A
                salgadosFiltrados.sort((s1, s2) -> s2.getTipo().compareToIgnoreCase(s1.getTipo()));
                break;
        }

        exibirSalgados(salgadosFiltrados);
    }

    private void filtrarSalgados() {
        List<Salgado> salgados = controller.listarSalgadosOrdenados();
        aplicarFiltroEOrdenacao(salgados);
    }

    private void exibirSalgados(List<Salgado> salgados) {
        StringBuilder sb = new StringBuilder();

        if (salgados.isEmpty()) {
            if (!txtFiltro.getText().isEmpty()) {
                sb.append("Nenhum salgado encontrado com o filtro: '").append(txtFiltro.getText()).append("'\n\n");
            } else {
                sb.append("Nenhum salgado cadastrado.\n\n");
            }
            sb.append("Para cadastrar salgados, vá para a tela 'Registrar Vendas' -> 'Salgado'.");
        } else {
            sb.append("=== SALGADOS CADASTRADOS ===\n");
            if (!txtFiltro.getText().isEmpty()) {
                sb.append("Filtro aplicado: '").append(txtFiltro.getText()).append("'\n");
            }
            sb.append("Total de salgados: ").append(salgados.size()).append("\n\n");

            for (int i = 0; i < salgados.size(); i++) {
                Salgado s = salgados.get(i);
                sb.append("Salgado #").append(i + 1).append("\n");
                sb.append("• Tipo: ").append(s.getTipo()).append("\n");
                sb.append("• Massa: ").append(s.getMassa()).append("\n");

                // Recheios
                if (!s.getRecheios().isEmpty()) {
                    sb.append("• Recheios: ");
                    List<String> recheios = s.getRecheios();
                    for (int j = 0; j < recheios.size(); j++) {
                        if (j > 0) sb.append(", ");
                        sb.append(recheios.get(j));
                    }
                    sb.append("\n");
                }

                sb.append("• PREÇO: MZN ").append(String.format("%.2f", s.getPreco())).append("\n");
                sb.append("----------------------------------------\n\n");
            }
        }

        areaLista.setText(sb.toString());
        atualizarEstatisticas(salgados);
    }

    private void atualizarEstatisticas(List<Salgado> salgados) {
        if (salgados.isEmpty()) {
            lblEstatisticas.setText("Nenhum salgado cadastrado");
            return;
        }

        double precoMaisCaro = 0;
        double precoMaisBarato = Double.MAX_VALUE;
        double somaPrecos = 0;

        // Contadores por tipo
        int countFrito = 0;
        int countAssado = 0;

        for (Salgado s : salgados) {
            double preco = s.getPreco();
            somaPrecos += preco;

            if (preco > precoMaisCaro) {
                precoMaisCaro = preco;
            }
            if (preco < precoMaisBarato) {
                precoMaisBarato = preco;
            }

            // Contar por tipo
            if (s.getTipo().equalsIgnoreCase("Frito")) {
                countFrito++;
            } else if (s.getTipo().equalsIgnoreCase("Assado")) {
                countAssado++;
            }
        }

        double precoMedio = somaPrecos / salgados.size();

        String estatisticas = String.format(
                "Estatísticas: %d salgados (Fritos: %d | Assados: %d) | Mais caro: MZN %.2f | Mais barato: MZN %.2f | Média: MZN %.2f",
                salgados.size(), countFrito, countAssado, precoMaisCaro, precoMaisBarato, precoMedio
        );

        lblEstatisticas.setText(estatisticas);
    }
}