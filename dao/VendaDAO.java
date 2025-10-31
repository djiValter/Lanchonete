package dao;

import model.Venda;
import java.io.*;
import java.util.*;

public class VendaDAO {
    private static final String FILE = "vendas.dat";

    @SuppressWarnings("unchecked")
    public List<Venda> ler() {
        File f = new File(FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Venda>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void gravar(List<Venda> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Venda v) {
        List<Venda> vendas = ler();
        vendas.add(v);
        gravar(vendas);
    }

    public List<Venda> listarTodas() {
        return ler();
    }
}