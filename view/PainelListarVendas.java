package view;

import controller.LanchoneteController;
import model.Venda;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PainelListarVendas extends JPanel {
    private JTextArea areaLista;
    private JTextField txtFiltro;
    private LanchoneteController controller;

    public PainelListarVendas(LanchoneteController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Painel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        panelFiltros.add(new JLabel("Filtrar:"));
        txtFiltro = new JTextField(20);
        panelFiltros.add(txtFiltro);

        JButton btnFiltrar = new JButton("Aplicar Filtro");
        panelFiltros.add(btnFiltrar);

        areaLista = new JTextArea();
        areaLista.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaLista);

        add(panelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnFiltrar.addActionListener(e -> filtrarVendas());
        listarTodasVendas();
    }

    private void listarTodasVendas() {
        List<Venda> vendas = controller.listarVendas();
        exibirVendas(vendas);
    }

    private void filtrarVendas() {
        String filtro = txtFiltro.getText().toLowerCase();
        List<Venda> vendasFiltradas = controller.listarVendas().stream()
                .filter(v -> v.toString().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
        exibirVendas(vendasFiltradas);
    }

    private void exibirVendas(List<Venda> vendas) {
        StringBuilder sb = new StringBuilder();
        if (vendas.isEmpty()) {
            sb.append("Nenhuma venda encontrada.\n");
        } else {
            for (Venda v : vendas) {
                sb.append(v.toString()).append("\n\n");
            }
        }
        areaLista.setText(sb.toString());
    }
}