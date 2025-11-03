package src.dao;

import src.model.Venda;
import java.io.*;
import java.util.List;

public class VendaDAO {
    private final File file = new File("data/vendas.dat");
    private BinarySearchTree<Venda> arvore;

    public VendaDAO() {
        load();
    }

    private void load() {
        if (!file.exists()) {
            arvore = new BinarySearchTree<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object o = ois.readObject();
            if (o instanceof BinarySearchTree) {
                // unchecked cast but ok
                @SuppressWarnings("unchecked")
                BinarySearchTree<Venda> loaded = (BinarySearchTree<Venda>) o;
                arvore = loaded;
            } else {
                arvore = new BinarySearchTree<>();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            arvore = new BinarySearchTree<>();
        }
    }

    private void save() {
        // garante pasta
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(arvore);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void salvar(Venda venda) {
        if (venda == null) return;
        arvore.insert(venda);
        save();
    }

    public java.util.List<Venda> listarTodas() {
        return arvore.inOrder();
    }
}
