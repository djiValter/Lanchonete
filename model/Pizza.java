package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pizza extends ItemCardapio implements Serializable {
    private TipoPizza tipo;
    private List<Recheio> recheios;
    private Borda borda;
    private List<Molho> molhos;

    // Construtor atualizado para trabalhar com objetos
    public Pizza(TipoPizza tipo, List<Molho> molhos) {
        super(tipo.getNome(), 0);
        this.tipo = tipo;
        this.recheios = new ArrayList<>();
        this.borda = null;
        this.molhos = new ArrayList<>(molhos);
        calcularPreco();
    }

    // Construtor completo
    public Pizza(TipoPizza tipo, List<Molho> molhos, Borda borda, List<Recheio> recheios) {
        super(tipo.getNome(), 0);
        this.tipo = tipo;
        this.molhos = new ArrayList<>(molhos);
        this.borda = borda;
        this.recheios = new ArrayList<>(recheios);
        calcularPreco();
    }

    public void adicionarMolho(Molho molho) {
        if (!molhos.contains(molho)) {
            molhos.add(molho);
            calcularPreco();
        }
    }

    public void removerMolho(Molho molho) {
        if (molhos.contains(molho)) {
            molhos.remove(molho);
            calcularPreco();
        }
    }

    public void setBorda(Borda borda) {
        this.borda = borda;
        calcularPreco();
    }

    public void adicionarRecheio(Recheio recheio) {
        if (!recheios.contains(recheio)) {
            recheios.add(recheio);
            calcularPreco();
        }
    }

    public void calcularPreco() {
        double precoTotal = tipo.getPrecoBase();

        // Adiciona preço dos molhos
        for (Molho molho : molhos) {
            precoTotal += molho.getPrecoAdicional();
        }

        // Adiciona preço da borda
        if (borda != null) {
            precoTotal += borda.getPrecoAdicional();
        }

        // Adiciona preço dos recheios
        for (Recheio recheio : recheios) {
            precoTotal += recheio.getPrecoAdicional();
        }

        setPreco(precoTotal);
    }

    // Método para calcular preço total (usado nos métodos de ordenação)
    public double calcularPrecoTotal() {
        return getPreco();
    }

    public TipoPizza getTipo() { return tipo; }
    public Borda getBorda() { return borda; }
    public List<Recheio> getRecheios() { return new ArrayList<>(recheios); }
    public List<Molho> getMolhos() { return new ArrayList<>(molhos); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pizza ").append(tipo.getNome())
                .append(" | Preço Base: MT ").append(String.format("%.2f", tipo.getPrecoBase()));

        if (!molhos.isEmpty()) {
            sb.append(" | Molhos: ");
            for (int i = 0; i < molhos.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(molhos.get(i).getNome());
            }
        }

        if (borda != null) {
            sb.append(" | Borda: ").append(borda.getNome());
        }

        if (!recheios.isEmpty()) {
            sb.append(" | Recheios: ");
            for (int i = 0; i < recheios.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(recheios.get(i).getNome());
            }
        }

        sb.append(" | Preço Total: MT ").append(String.format("%.2f", getPreco()));
        return sb.toString();
    }
}