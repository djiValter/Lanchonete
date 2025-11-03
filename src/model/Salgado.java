package src.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Salgado extends ItemCardapio implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tipo;      // e.g., "Frito" / "Assado"
    private String massa;
    private List<String> recheios;
    private double preco;

    public Salgado(double preco) {
        super("Salgado", preco);
        this.preco = preco;
    }

    public Salgado(String tipo, String massa, List<String> recheios, double preco) {
        super(tipo, preco);
        this.tipo = tipo;
        this.massa = massa;
        this.recheios = recheios;
        this.preco = preco;
    }

    @Override
    public double calcularPrecoTotal() {
        return preco;
    }

    public String getTipo() { return tipo; }
    public String getMassa() { return massa; }
    public List<String> getRecheios() { return recheios; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salgado)) return false;
        Salgado salgado = (Salgado) o;
        return Objects.equals(tipo, salgado.tipo) &&
                Objects.equals(massa, salgado.massa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, massa);
    }

    @Override
    public String toString() {
        return "Salgado{" +
                "tipo='" + tipo + '\'' +
                ", massa='" + massa + '\'' +
                ", preco=" + preco +
                '}';
    }
}
