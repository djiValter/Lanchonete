package src.view;

import src.controller.LanchoneteController;
import src.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelListarSalgados extends JPanel {

    private final LanchoneteController controller;
    private final JComboBox<String> comboTiposSalgado;
    private final JComboBox<String> comboMassas;
    private final JComboBox<Recheio> comboRecheios;
    private final JTextArea areaResumo;

    public PainelListarSalgados(LanchoneteController controller) {
        this.controller = new LanchoneteController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("🥐 Listar Salgados"));
        setBackground(Color.WHITE);

        JPanel painelFiltros = new JPanel(new GridLayout(3, 2, 10, 10));
        painelFiltros.setBackground(Color.WHITE);

        comboTiposSalgado = new JComboBox<>();
        comboMassas = new JComboBox<>();
        comboRecheios = new JComboBox<>();

        painelFiltros.add(new JLabel("Tipo de Salgado:"));
        painelFiltros.add(comboTiposSalgado);
        painelFiltros.add(new JLabel("Massa:"));
        painelFiltros.add(comboMassas);
        painelFiltros.add(new JLabel("Recheio:"));
        painelFiltros.add(comboRecheios);

        areaResumo = new JTextArea(12, 40);
        areaResumo.setEditable(false);
        areaResumo.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaResumo);

        JButton btnAtualizar = new JButton("🔄 Atualizar Dados");
        btnAtualizar.addActionListener(e -> carregarDados());

        add(painelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        comboTiposSalgado.removeAllItems();
        comboMassas.removeAllItems();
        comboRecheios.removeAllItems();

        // Tipos de salgado podem ser obtidos a partir dos nomes dos salgados cadastrados
        List<Salgado> salgados = controller.getSalgados();
        List<Recheio> recheios = controller.getRecheios();

        // massas fixas (ou poderiam vir de um DAO futuro)
        String[] massasPadrao = {"Tradicional", "Integral", "Folhada", "Fina"};

        salgados.stream()
                .map(Salgado::getTipo)
                .distinct()
                .forEach(comboTiposSalgado::addItem);

        for (String massa : massasPadrao) {
            comboMassas.addItem(massa);
        }

        recheios.forEach(comboRecheios::addItem);

        StringBuilder sb = new StringBuilder("=== CARDÁPIO DE SALGADOS ===\n\n");

        if (salgados.isEmpty()) {
            sb.append("Nenhum salgado encontrado.\n");
        } else {
            for (Salgado s : salgados) {
                sb.append(s.getNome())
                        .append(" - R$ ")
                        .append(String.format("%.2f", s.calcularPrecoTotal()))
                        .append("\n");
            }
        }

        areaResumo.setText(sb.toString());
    }
}
