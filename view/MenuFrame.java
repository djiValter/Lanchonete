// MenuFrame.java
package view;

import controller.LanchoneteController;
import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {
    private JPanel sidebar, contentPanel;
    private LanchoneteController controller = new LanchoneteController();

    public MenuFrame() {
        setTitle("🍕 Quase Três Lanches - Sistema de Vendas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Sidebar ===
        sidebar = new JPanel(new GridLayout(8, 1, 5, 5));
        sidebar.setBackground(new Color(30, 30, 30));
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnRegistrarVendas = criarBotao("💰 Registrar Vendas");
        JButton btnListarPizzas = criarBotao("🍕 Listar Pizzas");
        JButton btnListarSalgados = criarBotao("🥟 Listar Salgados");
        JButton btnListarVendas = criarBotao("📋 Listar Vendas");
        JButton btnSair = criarBotao("🚪 Sair");

        sidebar.add(btnRegistrarVendas);
        sidebar.add(btnListarPizzas);
        sidebar.add(btnListarSalgados);
        sidebar.add(btnListarVendas);
        sidebar.add(new JLabel()); // espaço
        sidebar.add(btnSair);

        add(sidebar, BorderLayout.WEST);

        // === Painel Central ===
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        // Ações dos botões
        btnRegistrarVendas.addActionListener(e -> mostrarPainel(new PainelRegistrarVendas(controller, this)));
        btnListarPizzas.addActionListener(e -> mostrarPainel(new PainelListarPizzas(controller)));
        btnListarSalgados.addActionListener(e -> mostrarPainel(new PainelListarSalgados(controller)));
        btnListarVendas.addActionListener(e -> mostrarPainel(new PainelListarVendas(controller)));
        btnSair.addActionListener(e -> System.exit(0));

        // Iniciar com tela de registrar vendas
        mostrarPainel(new PainelRegistrarVendas(controller, this));
    }

    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 140, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return btn;
    }

    public void mostrarPainel(JComponent panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}