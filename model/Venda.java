package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Venda implements Serializable, Comparable<Venda> {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private int id;
    private ItemCardapio item;
    private int quantidade;
    private LocalDateTime dataHora;

    public Venda(ItemCardapio item, int quantidade) {
        this.id = nextId++;
        this.item = item;
        this.quantidade = quantidade;
        this.dataHora = LocalDateTime.now();
    }

    public int getId() { return id; }
    public ItemCardapio getItem() { return item; }
    public int getQuantidade() { return quantidade; }
    public LocalDateTime getDataHora() { return dataHora; }

    public double getTotal() {
        return item.getPreco() * quantidade;
    }

    @Override
    public int compareTo(Venda outra) {
        return Integer.compare(this.id, outra.id);
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Venda #" + id + " - " + quantidade + "x " + item.getNome() +
                " | Total: R$ " + String.format("%.2f", getTotal()) +
                " | Data: " + dataHora.format(fmt);
    }
}