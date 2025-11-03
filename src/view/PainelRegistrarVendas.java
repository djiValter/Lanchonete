package src.view;

import src.controller.LanchoneteController;

import javax.swing.*;
import java.awt.*;

/**
 * Painel principal de registro de vendas, com abas para Pizza e Salgado.
 */
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
        panelVendaPizza = new PainelVendaPizza(controller);
        panelVendaSalgado = new PainelVendaSalgado(controller);

        // Adicionar abas
        abas.addTab("🍕 Vender Pizza", panelVendaPizza);
        abas.addTab("🥟 Vender Salgado", panelVendaSalgado);

        add(abas, BorderLayout.CENTER);
    }
}
