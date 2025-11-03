package src.model;

import java.io.Serializable;
import java.util.Objects;

public class TipoPizza implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private double precoBase;
    private String descricao;
    private String estiloMassa;

    public TipoPizza(String nome, double precoBase, String descricao, String estiloMassa) {
        this.nome = nome;
        this.precoBase = precoBase;
        this.descricao = descricao;
        this.estiloMassa = estiloMassa;
    }

    public String getNome() { return nome; }
    public double getPrecoBase() { return precoBase; }
    public String getDescricao() { return descricao; }
    public String getEstiloMassa() { return estiloMassa; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoPizza)) return false;
        TipoPizza that = (TipoPizza) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return nome + " (" + estiloMassa + ") - " + precoBase + " MT";
    }
}
