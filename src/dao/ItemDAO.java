package src.dao;

import src.model.*;

import java.io.*;
import java.util.*;

/**
 * DAO para gerenciar itens do cardápio (Pizzas e Salgados).
 * Cria dados padrão na primeira execução e salva em arquivo .dat.
 * Agora também recarrega listas auxiliares (molhos, bordas, recheios etc.)
 * mesmo quando o arquivo já existe.
 */
public class ItemDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final File file = new File("data/items.dat").getAbsoluteFile();
    private List<ItemCardapio> itens;

    // =====================
    // Listas internas de dados padrão
    // =====================
    private final List<TipoPizza> tiposPizza = new ArrayList<>();
    private final List<Molho> molhos = new ArrayList<>();
    private final List<Borda> bordas = new ArrayList<>();
    private final List<Recheio> recheios = new ArrayList<>();

    private final List<String> tiposSalgado = new ArrayList<>();
    private final List<String> massas = new ArrayList<>();
    private final List<String> recheiosSalgado = new ArrayList<>();
    private final Map<String, Double> precoTiposSalgado = new HashMap<>();
    private final Map<String, Double> precoTiposMassa = new HashMap<>();
    private final Map<String, Double> precoRecheios = new HashMap<>();

    // =====================
    // Construtor
    // =====================
    public ItemDAO() {
        itens = new ArrayList<>();

        System.out.println("[ItemDAO] Caminho do arquivo: " + file.getAbsolutePath());
        load();

        if (itens.isEmpty()) {
            System.out.println("[ItemDAO] Nenhum item encontrado, gerando dados padrão...");
            inicializarDadosPadrao();
            gerarItensPadrao();
            save();
        } else {
            System.out.println("[ItemDAO] Dados carregados: " + itens.size() + " itens.");
        }
    }

    // =====================
    // Carregar e salvar arquivo
    // =====================
    private void load() {
        if (!file.exists()) {
            System.out.println("[ItemDAO] Arquivo não existe. Iniciando com lista vazia.");
            itens = new ArrayList<>();
            inicializarDadosPadrao();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object o = ois.readObject();
            if (o instanceof List) {
                @SuppressWarnings("unchecked")
                List<ItemCardapio> loaded = (List<ItemCardapio>) o;
                itens = loaded;
                System.out.println("[ItemDAO] Itens carregados com sucesso: " + itens.size());

                // 🔹 Garante que as listas auxiliares (molhos, bordas, etc.) sejam recarregadas
                inicializarDadosPadrao();

            } else {
                System.out.println("[ItemDAO] Arquivo inválido. Iniciando lista vazia.");
                itens = new ArrayList<>();
                inicializarDadosPadrao();
            }
        } catch (Exception ex) {
            System.out.println("[ItemDAO] Erro ao carregar dados: " + ex.getMessage());
            ex.printStackTrace();
            itens = new ArrayList<>();
            inicializarDadosPadrao();
        }
    }

    public void save() {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(itens);
            System.out.println("[ItemDAO] Dados salvos com sucesso: " + itens.size() + " itens.");
        } catch (IOException ex) {
            System.out.println("[ItemDAO] Erro ao salvar dados: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // =====================
    // CRUD
    // =====================
    public void salvar(ItemCardapio item) {
        if (item == null) return;
        itens.add(item);
        save();
    }

    public List<ItemCardapio> listarTodos() {
        return new ArrayList<>(itens);
    }

    // =====================
    // Pizzas
    // =====================
    public List<Pizza> listarTodasPizzas() {
        List<Pizza> pizzas = new ArrayList<>();
        for (ItemCardapio item : itens)
            if (item instanceof Pizza) pizzas.add((Pizza) item);
        return pizzas;
    }

    public List<Pizza> listarPizzasPorPrecoSelectionSort() {
        List<Pizza> pizzas = listarTodasPizzas();
        int n = pizzas.size();
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (pizzas.get(j).calcularPrecoTotal() < pizzas.get(min).calcularPrecoTotal()) {
                    min = j;
                }
            }
            Collections.swap(pizzas, i, min);
        }
        return pizzas;
    }

    public List<Pizza> listarPizzasPorPrecoBubbleSort() {
        List<Pizza> pizzas = listarTodasPizzas();
        int n = pizzas.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (pizzas.get(j).calcularPrecoTotal() > pizzas.get(j + 1).calcularPrecoTotal()) {
                    Collections.swap(pizzas, j, j + 1);
                }
            }
        }
        return pizzas;
    }

    // =====================
    // Salgados
    // =====================
    public List<Salgado> listarTodosSalgados() {
        List<Salgado> salgados = new ArrayList<>();
        for (ItemCardapio item : itens)
            if (item instanceof Salgado) salgados.add((Salgado) item);
        return salgados;
    }

    public List<Salgado> listarSalgadinhosPorPreco() {
        List<Salgado> salgados = listarTodosSalgados();
        salgados.sort(Comparator.comparingDouble(Salgado::calcularPrecoTotal));
        return salgados;
    }

    // =====================
    // Inicializar dados padrão
    // =====================
    private void inicializarDadosPadrao() {
        tiposPizza.clear();
        molhos.clear();
        bordas.clear();
        recheios.clear();
        tiposSalgado.clear();
        massas.clear();
        recheiosSalgado.clear();
        precoTiposSalgado.clear();
        precoTiposMassa.clear();
        precoRecheios.clear();

        // ===== PIZZAS =====
        tiposPizza.add(new TipoPizza("Margherita", 25.00, "Molho de tomate, mussarela, manjericão", "Fina"));
        tiposPizza.add(new TipoPizza("Pepperoni", 30.00, "Molho de tomate, mussarela, pepperoni", "Fina"));
        tiposPizza.add(new TipoPizza("Quatro Queijos", 32.00, "Molho de tomate, mussarela, parmesão, gorgonzola, provolone", "Fina"));
        tiposPizza.add(new TipoPizza("Calabresa", 28.00, "Molho de tomate, mussarela, calabresa, cebola", "Fina"));
        tiposPizza.add(new TipoPizza("Portuguesa", 35.00, "Molho de tomate, mussarela, presunto, ovo, cebola, azeitona", "Fina"));

        molhos.add(new Molho("Molho de Tomate Tradicional", 0.00));
        molhos.add(new Molho("Molho Branco", 3.00));
        molhos.add(new Molho("Molho Picante", 2.50));
        molhos.add(new Molho("Molho Especial da Casa", 4.00));

        bordas.add(new Borda("Catupiry", 3.00));
        bordas.add(new Borda("Cheddar", 3.50));
        bordas.add(new Borda("Chocolate", 4.00));
        bordas.add(new Borda("Tradicional", 0.00));

        recheios.add(new Recheio("Calabresa", 2.00));
        recheios.add(new Recheio("Frango", 2.50));
        recheios.add(new Recheio("Bacon", 3.00));
        recheios.add(new Recheio("Milho", 1.50));
        recheios.add(new Recheio("Azeitona", 1.50));
        recheios.add(new Recheio("Mussarela", 2.00));
        recheios.add(new Recheio("Provolone", 2.50));

        // ===== SALGADOS =====
        tiposSalgado.add("Frito");
        tiposSalgado.add("Assado");

        massas.add("Massa com Leite");
        massas.add("Com caldo de galinha");
        massas.add("Com farinha de trigo e manteiga");

        recheiosSalgado.add("Carne Moída");
        recheiosSalgado.add("Queijo");
        recheiosSalgado.add("Presunto e Queijo");
        recheiosSalgado.add("Calabresa com Queijo");

        precoTiposSalgado.put("Frito", 5.00);
        precoTiposSalgado.put("Assado", 6.00);

        precoTiposMassa.put("Massa com Leite", 0.00);
        precoTiposMassa.put("Com caldo de galinha", 1.50);
        precoTiposMassa.put("Com farinha de trigo e manteiga", 2.00);

        precoRecheios.put("Carne Moída", 2.00);
        precoRecheios.put("Queijo", 1.50);
        precoRecheios.put("Presunto e Queijo", 3.00);
        precoRecheios.put("Calabresa com Queijo", 3.50);
    }

    private void gerarItensPadrao() {
        List<ItemCardapio> novos = new ArrayList<>();

        // PIZZAS PADRÃO
        for (TipoPizza tipo : tiposPizza) {
            Pizza pizza = new Pizza(tipo, new ArrayList<>());
            if (!itens.contains(pizza)) novos.add(pizza);
        }

        // SALGADOS PADRÃO
        for (String tipo : tiposSalgado) {
            for (String recheio : recheiosSalgado) {
                String nome = tipo + " de " + recheio;
                double preco = precoTiposSalgado.get(tipo) + precoRecheios.get(recheio);
                List<String> listaRecheios = Collections.singletonList(recheio);
                Salgado salgado = new Salgado(tipo, nome, listaRecheios, preco);
                if (!itens.contains(salgado)) novos.add(salgado);
            }
        }

        itens.addAll(novos);
    }

    // =====================
    // Métodos de acesso
    // =====================
    public List<TipoPizza> listarTiposPizza() { return new ArrayList<>(tiposPizza); }
    public List<Molho> listarMolhos() { return new ArrayList<>(molhos); }
    public List<Borda> listarBordas() { return new ArrayList<>(bordas); }
    public List<Recheio> listarRecheios() { return new ArrayList<>(recheios); }

    public Map<String, Double> getPrecoTiposSalgado() { return new HashMap<>(precoTiposSalgado); }
    public Map<String, Double> getPrecoTiposMassa() { return new HashMap<>(precoTiposMassa); }
    public Map<String, Double> getPrecoRecheios() { return new HashMap<>(precoRecheios); }

    // =====================
    // Debug
    // =====================
    public void debugStatus() {
        System.out.println("=== DEBUG ItemDAO ===");
        System.out.println("Arquivo: " + file.getAbsolutePath());
        System.out.println("Existe: " + file.exists());
        System.out.println("Tamanho: " + (file.exists() ? file.length() + " bytes" : "N/A"));
        System.out.println("Itens em memória: " + itens.size());
        System.out.println("Pizzas: " + listarTodasPizzas().size());
        System.out.println("Salgados: " + listarTodosSalgados().size());
        System.out.println("Primeiros 3 itens:");
        for (int i = 0; i < Math.min(3, itens.size()); i++) {
            ItemCardapio item = itens.get(i);
            System.out.println("  " + (i + 1) + ". " + item.getClass().getSimpleName()
                    + " - " + item.getNome() + " - ID: " + item.getId());
        }
    }
}
