package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Salgado extends ItemCardapio implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tipo;
    private String massa;
    private List<String> recheios;

    // Construtor antigo mantido para compatibilidade
    public Salgado(double preco) {
        super("Salgado", preco);
        this.tipo = "Frito";
        this.massa = "Tradicional";
        this.recheios = new ArrayList<>();
    }

    // NOVO CONSTRUTOR: Aceita todos os detalhes do salgado
    public Salgado(String tipo, String massa, List<String> recheios, double preco) {
        super("Salgado Personalizado", preco);
        this.tipo = tipo;
        this.massa = massa;
        this.recheios = new ArrayList<>(recheios);
    }

    // Getters
    public String getTipo() { return tipo; }
    public String getMassa() { return massa; }
    public List<String> getRecheios() { return new ArrayList<>(recheios); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Salgado ").append(tipo)
                .append(" | Massa: ").append(massa)
                .append(" | Recheios: ");

        if (recheios.isEmpty()) {
            sb.append("Nenhum");
        } else {
            for (int i = 0; i < recheios.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(recheios.get(i));
            }
        }

        sb.append(" | Preço: MT ").append(String.format("%.2f", getPreco()));
        return sb.toString();
    }
}