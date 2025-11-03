package src.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;

public class Venda implements Serializable, Comparable<Venda> {
    private static final long serialVersionUID = 1L;

    private static final AtomicLong COUNTER = new AtomicLong(1);

    private long id;
    private ItemCardapio item;
    private int quantidade;
    private long timestamp; // epoch millis

    public Venda(ItemCardapio item, int quantidade) {
        this.id = COUNTER.getAndIncrement();
        this.item = item;
        this.quantidade = quantidade;
        this.timestamp = Instant.now().toEpochMilli();
    }

    // Getter para uso externo
    public long getId() { return id; }
    public ItemCardapio getItem() { return item; }
    public int getQuantidade() { return quantidade; }
    public long getTimestamp() { return timestamp; }

    public double getTotal() {
        return item != null ? item.calcularPrecoTotal() * quantidade : 0.0;
    }


    public Date getDataHora() {
        return new Date(timestamp);
    }

    @Override
    public String toString() {
        String nome = (item != null) ? item.getNome() : "desconhecido";
        return String.format("Venda[id=%d, item=%s, qtd=%d, total=MT %.2f]", id, nome, quantidade, getTotal());
    }

    // Ordenação por timestamp (mais antigo primeiro). Ajusta se quiseres ordenar por total.
    @Override
    public int compareTo(Venda o) {
        if (o == null) return 1;
        int cmp = Long.compare(this.timestamp, o.timestamp);
        if (cmp != 0) return cmp;
        return Long.compare(this.id, o.id);
    }
}
