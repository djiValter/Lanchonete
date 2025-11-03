package src.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;

    private class Node implements Serializable {
        T value;
        Node left, right;
        Node(T value) { this.value = value; }
    }

    private Node root;
    private int size = 0;

    public void insert(T value) {
        if (value == null) return;
        root = insertRec(root, value);
    }

    private Node insertRec(Node node, T value) {
        if (node == null) {
            size++;
            return new Node(value);
        }
        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insertRec(node.left, value);
        } else {
            node.right = insertRec(node.right, value);
        }
        return node;
    }

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

    public int size() { return size; }

    public void clear() {
        root = null;
        size = 0;
    }
}
