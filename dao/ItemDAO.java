package dao;

import model.*;
import java.io.*;
import java.util.*;

public class ItemDAO {
    private static final String FILE = "itens.dat";

    @SuppressWarnings("unchecked")
    private List<ItemCardapio> ler() {
        File f = new File(FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<ItemCardapio>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void gravar(List<ItemCardapio> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvar(ItemCardapio item) {
        List<ItemCardapio> lista = ler();
        lista.add(item);
        gravar(lista);
    }

    public List<ItemCardapio> listarTodos() {
        return ler();
    }

    // Método com Selection Sort para objetos Pizza completos
    public List<Pizza> listarPizzasPorPrecoSelectionSort() {
        List<Pizza> pizzas = new ArrayList<>();
        for (ItemCardapio i : ler()) {
            if (i instanceof Pizza) {
                pizzas.add((Pizza) i);
            }
        }

        selectionSortPorPreco(pizzas);
        return pizzas;
    }

    // Método com Bubble Sort para objetos Pizza completos
    public List<Pizza> listarPizzasPorPrecoBubbleSort() {
        List<Pizza> pizzas = new ArrayList<>();
        for (ItemCardapio i : ler()) {
            if (i instanceof Pizza) {
                pizzas.add((Pizza) i);
            }
        }

        bubbleSortPorPreco(pizzas);
        return pizzas;
    }

    // Método para obter apenas os nomes das pizzas ordenados por preço (Selection Sort)
    public List<String> listarNomesPizzasPorPrecoSelectionSort() {
        List<Pizza> pizzas = listarPizzasPorPrecoSelectionSort();
        List<String> nomes = new ArrayList<>();

        for (Pizza pizza : pizzas) {
            nomes.add(pizza.getNome() + " - R$ " + String.format("%.2f", pizza.getPreco()));
        }
        return nomes;
    }

    // Método para obter apenas os nomes das pizzas ordenados por preço (Bubble Sort)
    public List<String> listarNomesPizzasPorPrecoBubbleSort() {
        List<Pizza> pizzas = listarPizzasPorPrecoBubbleSort();
        List<String> nomes = new ArrayList<>();

        for (Pizza pizza : pizzas) {
            nomes.add(pizza.getNome() + " - R$ " + String.format("%.2f", pizza.getPreco()));
        }
        return nomes;
    }

    // Algoritmo Selection Sort
    private void selectionSortPorPreco(List<Pizza> pizzas) {
        int n = pizzas.size();

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (pizzas.get(j).getPreco() < pizzas.get(minIndex).getPreco()) {
                    minIndex = j;
                }
            }

            Pizza temp = pizzas.get(minIndex);
            pizzas.set(minIndex, pizzas.get(i));
            pizzas.set(i, temp);
        }
    }

    // Algoritmo Bubble Sort
    private void bubbleSortPorPreco(List<Pizza> pizzas) {
        int n = pizzas.size();
        boolean trocou;

        for (int i = 0; i < n - 1; i++) {
            trocou = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (pizzas.get(j).getPreco() > pizzas.get(j + 1).getPreco()) {
                    Pizza temp = pizzas.get(j);
                    pizzas.set(j, pizzas.get(j + 1));
                    pizzas.set(j + 1, temp);
                    trocou = true;
                }
            }
            if (!trocou) break;
        }
    }

    // Método original mantido para compatibilidade
    public List<Pizza> listarPizzasPorPreco() {
        return listarPizzasPorPrecoSelectionSort();
    }

    public List<Salgado> listarSalgadinhosPorPreco() {
        List<Salgado> salg = new ArrayList<>();
        for (ItemCardapio i : ler()) if (i instanceof Salgado) salg.add((Salgado)i);
        salg.sort(Comparator.comparingDouble(ItemCardapio::getPreco));
        return salg;
    }
}