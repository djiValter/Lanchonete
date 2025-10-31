package view;

import javax.swing.*;
import java.awt.*;

public class PainelInicio extends JPanel {

    private Image backgroundImage;

    public PainelInicio(String caminhoImagem) {
        // Carrega a imagem do arquivo
        backgroundImage = new ImageIcon(getClass().getResource("/img/Background.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Desenha a imagem esticada para preencher todo o painel
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
