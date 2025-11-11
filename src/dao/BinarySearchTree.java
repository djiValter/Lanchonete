package src.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação genérica de uma Árvore Binária de Pesquisa (BST).
 * Permite inserir, buscar e listar elementos em ordem crescente.
 * @param <T> Tipo de dado que implementa Comparable<T>.
 */
public class BinarySearchTree<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Classe interna que representa um nó da árvore.
     */
    private class Node implements Serializable {
        T value;
        Node left, right;

        Node(T value) {
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    /**
     * Insere um novo valor na árvore.
     * @param value valor a ser inserido.
     */
    public void insert(T value) {
        if (value == null) return;
        root = insertRec(root, value);
    }

    /**
     * Método recursivo auxiliar para inserção.
     * Evita duplicados: se o valor já existir, não insere novamente.
     */
    private Node insertRec(Node node, T value) {
        if (node == null) {
            size++;
            return new Node(value);
        }

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insertRec(node.left, value);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, value);
        }
        // se cmp == 0, o valor já existe — não insere duplicado
        return node;
    }

    /**
     * Busca um valor na árvore.
     * @param value valor a ser buscado.
     * @return o valor encontrado, ou null se não existir.
     */
    public T search(T value) {
        return searchRec(root, value);
    }

    private T searchRec(Node node, T value) {
        if (node == null || value == null) return null;

        int cmp = value.compareTo(node.value);
        if (cmp == 0)
            return node.value;
        else if (cmp < 0)
            return searchRec(node.left, value);
        else
            return searchRec(node.right, value);
    }

    /**
     * Retorna os elementos da árvore em ordem crescente (in-order traversal).
     * @return lista ordenada dos elementos.
     */
    public List<T> inOrder() {
        List<T> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }

    private void inOrderRec(Node node, List<T> list) {
        if (node == null) return;
        inOrderRec(node.left, list);
        list.add(node.value);
        inOrderRec(node.right, list);
    }

    /**
     * Retorna o número de elementos na árvore.
     */
    public int size() {
        return size;
    }

    /**
     * Remove todos os elementos da árvore.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Verifica se a árvore está vazia.
     */
    public boolean isEmpty() {
        return size == 0;
    }
}
