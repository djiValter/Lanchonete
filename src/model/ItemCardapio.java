package src.model;

import java.io.Serializable;

public abstract class ItemCardapio implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contador = 1;

    private int id;
    private String nome;
    private double precoBase;

    public ItemCardapio(String nome, double precoBase) {
        this.id = contador++;
        this.nome = nome;
        this.precoBase = precoBase;
    }

    public abstract double calcularPrecoTotal();

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPrecoBase() { return precoBase; }

    @Override
    public String toString() {
        return nome + " (R$ " + calcularPrecoTotal() + ")";
    }
}
