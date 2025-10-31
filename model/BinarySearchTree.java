package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> implements Serializable {
    private Node<T> root;
    private int size;

    private static class Node<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        T data;
        Node<T> left, right;

        Node(T data) {
            this.data = data;
        }
    }

    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node<T> insertRec(Node<T> root, T data) {
        if (root == null) {
            size++;
            return new Node<>(data);
        }

        if (data.compareTo(root.data) < 0)
            root.left = insertRec(root.left, data);
        else if (data.compareTo(root.data) > 0)
            root.right = insertRec(root.right, data);

        return root;
    }

    public List<T> inOrder() {
        List<T> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }

    private void inOrderRec(Node<T> root, List<T> list) {
        if (root != null) {
            inOrderRec(root.left, list);
            list.add(root.data);
            inOrderRec(root.right, list);
        }
    }

    public boolean contains(T data) {
        return containsRec(root, data);
    }

    private boolean containsRec(Node<T> root, T data) {
        if (root == null) return false;

        int cmp = data.compareTo(root.data);
        if (cmp == 0) return true;
        else if (cmp < 0) return containsRec(root.left, data);
        else return containsRec(root.right, data);
    }

    public int size() { return size; }
    public boolean isEmpty() { return root == null; }

    public List<T> inOrderReversed() {
        List<T> list = new ArrayList<>();
        inOrderReversedRec(root, list);
        return list;
    }

    private void inOrderReversedRec(Node<T> root, List<T> list) {
        if (root != null) {
            inOrderReversedRec(root.right, list);
            list.add(root.data);
            inOrderReversedRec(root.left, list);
        }
    }
}