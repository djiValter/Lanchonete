package src.view;

import src.controller.LanchoneteController;

import javax.swing.*;
import java.awt.*;

/**
 * MenuFrame é a janela principal do sistema de vendas da lanchonete.
 * Possui uma sidebar com botões e um painel central que troca de acordo com a seleção.
 */
public class MenuFrame extends JFrame {

    private JPanel sidebar, contentPanel;
    private final LanchoneteController controller;

    public MenuFrame() {
        this.controller = new LanchoneteController();

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
        sidebar.add(new JLabel()); // espaço vazio
        sidebar.add(btnSair);

        add(sidebar, BorderLayout.WEST);

        // === Painel Central ===
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        // === Ações dos botões ===
        btnRegistrarVendas.addActionListener(e -> mostrarPainel(new PainelRegistrarVendas(controller, this)));
        btnListarPizzas.addActionListener(e -> mostrarPainel(new PainelListarPizzas(controller)));
        btnListarSalgados.addActionListener(e -> mostrarPainel(new PainelListarSalgados(controller)));
        btnListarVendas.addActionListener(e -> mostrarPainel(new PainelListarVendas(controller)));
        btnSair.addActionListener(e -> System.exit(0));

        // === Iniciar com tela de registrar vendas ===
        mostrarPainel(new PainelRegistrarVendas(controller, this));
    }

    /**
     * Cria um botão estilizado para a sidebar.
     */
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 140, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return btn;
    }

    /**
     * Troca o painel central pelo fornecido.
     */
    public void mostrarPainel(JComponent panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Método main para testar a janela.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuFrame frame = new MenuFrame();
            frame.setVisible(true);
        });
    }
}
