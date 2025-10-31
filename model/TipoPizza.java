package model;

import java.io.Serializable;

public class TipoPizza implements Serializable {
    private String nome;
    private double precoBase;
    private String recheio;
    private String borda;

    public TipoPizza(String nome, double precoBase, String recheio, String borda) {
        this.nome = nome;
        this.precoBase = precoBase;
        this.recheio = recheio;
        this.borda = borda;
    }

    public String getNome() { return nome; }
    public double getPrecoBase() { return precoBase; }
    public String getRecheio() { return recheio; }
    public String getBorda() { return borda; }

    @Override
    public String toString() {
        return nome + " (MT " + String.format("%.2f", precoBase) + ")";
    }
}