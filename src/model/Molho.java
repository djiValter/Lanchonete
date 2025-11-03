package src.model;

import java.io.Serializable;
import java.util.Objects;

public class Molho implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private double precoAdicional;

    public Molho(String nome, double precoAdicional) {
        this.nome = nome;
        this.precoAdicional = precoAdicional;
    }

    public String getNome() { return nome; }
    public double getPrecoAdicional() { return precoAdicional; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Molho)) return false;
        Molho molho = (Molho) o;
        return Objects.equals(nome, molho.nome);
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
