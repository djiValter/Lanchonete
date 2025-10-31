package model;

import java.io.Serializable;

public class Borda implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private double precoAdicional;

    public Borda(String nome, double precoAdicional) {
        this.nome = nome;
        this.precoAdicional = precoAdicional;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoAdicional() {
        return precoAdicional;
    }

    @Override
    public String toString() {
        return nome + " (MZN " + String.format("%.2f", precoAdicional) + ")";
    }
}