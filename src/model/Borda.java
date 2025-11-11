package src.model;

import java.io.Serializable;
import java.util.Objects;

public class Borda implements Serializable, Comparable<Borda> {
    private static final long serialVersionUID = 1L;

    private String nome;
    private double precoAdicional;
    private String descricao;

    public Borda(String nome, double precoAdicional) {
        this.nome = nome;
        this.precoAdicional = precoAdicional;
        this.descricao = "";
    }

    public Borda(String nome, double precoAdicional, String descricao) {
        this.nome = nome;
        this.precoAdicional = precoAdicional;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPrecoAdicional() { return precoAdicional; }
    public void setPrecoAdicional(double precoAdicional) { this.precoAdicional = precoAdicional; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        if (descricao != null && !descricao.isEmpty()) {
            return nome + " (" + descricao + ") - MT " + String.format("%.2f", precoAdicional);
        }
        return nome + " - MT " + String.format("%.2f", precoAdicional);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Borda)) return false;
        Borda borda = (Borda) o;
        return Objects.equals(nome, borda.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public int compareTo(Borda outra) {
        return Double.compare(this.precoAdicional, outra.precoAdicional);
    }
}
