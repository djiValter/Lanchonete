package view;

import controller.LanchoneteController;
import model.Salgado;

import javax.swing.*;
import java.awt.*;

public class PainelSalgados extends JPanel {
    private JTextArea areaLista;
    private LanchoneteController controller;
    private JButton btnSalvar;
    private final double PRECO_FIXO_SALGADO = 5.00; // Preço fixo unitário

    public PainelSalgados(LanchoneteController controller, JFrame menuFrame) {
        this.controller = controller;
        double precoFixo = controller.getPrecoFixoSalgado();
        setLayout(new BorderLayout(10, 10));

        // Painel do formulário
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Cadastrar Salgadinho"));

        // Mostra o preço fixo em um label
        JLabel lblPrecoFixo = new JLabel("Preço Fixo: MT " + String.format("%.2f", PRECO_FIXO_SALGADO));
        lblPrecoFixo.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecoFixo.setForeground(Color.BLUE);

        btnSalvar = new JButton("Adicionar Salgado");
        btnSalvar.addActionListener(e -> salvar());

        form.add(new JLabel("Tipo: Frito"));
        form.add(lblPrecoFixo);
        form.add(btnSalvar);

        JButton btnListar = new JButton("Listar Salgadinhos (Preço ↑)");
        btnListar.addActionListener(e -> listar());
        form.add(btnListar);

        areaLista = new JTextArea();
        areaLista.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaLista);
        scroll.setBorder(BorderFactory.createTitledBorder("Salgadinhos Cadastrados"));

        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Listar automaticamente ao abrir o painel
        listar();
    }

    private void salvar() {
        try {
            controller.cadastrarSalgado(); // Agora não passamos parâmetro
            double precoFixo = controller.getPrecoFixoSalgado();
            JOptionPane.showMessageDialog(this, "Salgadinho adicionado com sucesso!\nPreço: MT " +
                    String.format("%.2f", precoFixo));
            listar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar salgado: " + e.getMessage());
        }
    }


    private void listar() {
        areaLista.setText("");
        for (Salgado s : controller.listarSalgadosOrdenados()) {
            areaLista.append(s.toString() + "\n");
        }
    }
}