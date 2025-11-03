package src.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pizza extends ItemCardapio implements Serializable {
    private static final long serialVersionUID = 1L;

    private TipoPizza tipo;
    private Borda borda;
    private List<Molho> molhos;
    private List<Recheio> recheiosExtras;

    public Pizza(TipoPizza tipo, List<Molho> molhos) {
        super(tipo.getNome(), tipo.getPrecoBase());
        this.tipo = tipo;
        this.molhos = molhos != null ? new ArrayList<>(molhos) : new ArrayList<>();
        this.recheiosExtras = new ArrayList<>();
        this.borda = null;
    }

    public void setBorda(Borda borda) {
        this.borda = borda;
    }

    public void adicionarRecheio(Recheio r) {
        if (r != null) this.recheiosExtras.add(r);
    }

    @Override
    public double calcularPrecoTotal() {
        double total = tipo.getPrecoBase();
        if (borda != null) total += borda.getPrecoAdicional();
        for (Molho m : molhos) total += m.getPrecoAdicional();
        for (Recheio r : recheiosExtras) total += r.getPrecoAdicional();
        return total;
    }

    public TipoPizza getTipo() { return tipo; }
    public Borda getBorda() { return borda; }
    public List<Molho> getMolhos() { return molhos; }
    public List<Recheio> getRecheiosExtras() { return recheiosExtras; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pizza)) return false;
        Pizza pizza = (Pizza) o;
        return Objects.equals(tipo.getNome(), pizza.tipo.getNome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo.getNome());
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "tipo=" + tipo.getNome() +
                ", precoTotal=" + calcularPrecoTotal() +
                '}';
    }
}
