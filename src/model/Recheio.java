package src.model;

import java.io.Serializable;
import java.util.Objects;

public class Recheio implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private double precoAdicional;

    public Recheio(String nome, double precoAdicional) {
        this.nome = nome;
        this.precoAdicional = precoAdicional;
    }

    public String getNome() { return nome; }
    public double getPrecoAdicional() { return precoAdicional; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recheio)) return false;
        Recheio recheio = (Recheio) o;
        return Objects.equals(nome, recheio.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return nome + " (+" + precoAdicional + " MT)";
    }
}
