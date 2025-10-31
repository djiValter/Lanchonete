package controller;

import dao.*;
import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LanchoneteController {

    private final ItemDAO itemDAO = new ItemDAO();
    private final VendaDAO vendaDAO = new VendaDAO();
    private final double PRECO_FIXO_SALGADO = 5.00;

    // Tipos de pizza pré-cadastrados
    private final List<TipoPizza> tiposPizza = new ArrayList<>();
    private final List<Molho> molhos = new ArrayList<>();
    private final List<Borda> bordas = new ArrayList<>();
    private final List<Recheio> recheios = new ArrayList<>();
    private final List<Pizza> pizzas = new ArrayList<>();

    // Árvore Binária de Pesquisa para vendas
    private BinarySearchTree<Venda> arvoreVendas = new BinarySearchTree<>();

    public double getPrecoFixoSalgado() {
        return PRECO_FIXO_SALGADO;
    }

    public LanchoneteController() {
        inicializarTiposPizza();
        inicializarMolhos();
        inicializarBordas();
        inicializarRecheios();
        carregarVendasNaArvore();
    }

    private void carregarVendasNaArvore() {
        // Carrega as vendas do arquivo e insere na árvore
        List<Venda> vendas = vendaDAO.listarTodas();
        for (Venda venda : vendas) {
            arvoreVendas.insert(venda);
        }
    }

    private void inicializarTiposPizza() {
        tiposPizza.add(new TipoPizza("Margherita", 25.00, "Molho de tomate, mussarela, manjericão", "Fina"));
        tiposPizza.add(new TipoPizza("Pepperoni", 30.00, "Molho de tomate, mussarela, pepperoni", "Fina"));
        tiposPizza.add(new TipoPizza("Quatro Queijos", 32.00, "Molho de tomate, mussarela, parmesão, gorgonzola, provolone", "Fina"));
        tiposPizza.add(new TipoPizza("Calabresa", 28.00, "Molho de tomate, mussarela, calabresa, cebola", "Fina"));
        tiposPizza.add(new TipoPizza("Portuguesa", 35.00, "Molho de tomate, mussarela, presunto, ovo, cebola, azeitona", "Fina"));
    }

    private void inicializarMolhos() {
        molhos.add(new Molho("Molho de Tomate Tradicional", 0.00));
        molhos.add(new Molho("Molho Branco", 3.00));
        molhos.add(new Molho("Molho Picante", 2.50));
        molhos.add(new Molho("Molho Especial da Casa", 4.00));
    }

    private void inicializarBordas() {
        bordas.add(new Borda("Catupiry", 3.00));
        bordas.add(new Borda("Cheddar", 3.50));
        bordas.add(new Borda("Chocolate", 4.00));
        bordas.add(new Borda("Tradicional", 0.00));
    }

    private void inicializarRecheios() {
        recheios.add(new Recheio("Calabresa", 2.00));
        recheios.add(new Recheio("Frango", 2.50));
        recheios.add(new Recheio("Bacon", 3.00));
        recheios.add(new Recheio("Milho", 1.50));
        recheios.add(new Recheio("Azeitona", 1.50));
        recheios.add(new Recheio("Mussarela", 2.00));
        recheios.add(new Recheio("Provolone", 2.50));
    }

    public List<TipoPizza> getTiposPizza() {
        return new ArrayList<>(tiposPizza);
    }

    public List<Molho> getMolhos() {
        return new ArrayList<>(molhos);
    }

    public List<Borda> getBordas() {
        return new ArrayList<>(bordas);
    }

    public List<Recheio> getRecheios() {
        return new ArrayList<>(recheios);
    }

    // =========================
    // MÉTODOS PARA O COMBOBOX DE PIZZAS
    // =========================

    public List<String> listarNomesPizzasOrdenadasBubbleSort() {
        List<TipoPizza> tiposOrdenados = bubbleSortTiposPizza(new ArrayList<>(tiposPizza));
        return formatarNomesPizzas(tiposOrdenados);
    }

    public List<String> listarNomesPizzasOrdenadasSelectionSort() {
        List<TipoPizza> tiposOrdenados = selectionSortTiposPizza(new ArrayList<>(tiposPizza));
        return formatarNomesPizzas(tiposOrdenados);
    }

    private List<TipoPizza> bubbleSortTiposPizza(List<TipoPizza> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (lista.get(j).getPrecoBase() > lista.get(j + 1).getPrecoBase()) {
                    // Troca
                    TipoPizza temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
        return lista;
    }

    private List<TipoPizza> selectionSortTiposPizza(List<TipoPizza> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (lista.get(j).getPrecoBase() < lista.get(minIndex).getPrecoBase()) {
                    minIndex = j;
                }
            }
            // Troca
            TipoPizza temp = lista.get(minIndex);
            lista.set(minIndex, lista.get(i));
            lista.set(i, temp);
        }
        return lista;
    }

    private List<String> formatarNomesPizzas(List<TipoPizza> tiposPizza) {
        List<String> nomesFormatados = new ArrayList<>();
        for (TipoPizza tipo : tiposPizza) {
            nomesFormatados.add(tipo.getNome() + " - MT " + String.format("%.2f", tipo.getPrecoBase()));
        }
        return nomesFormatados;
    }

    // =========================
    // CADASTRO DE ITENS
    // =========================
    public void adicionarItem(ItemCardapio item) {
        itemDAO.salvar(item);
    }

    // Método original mantido para compatibilidade
    public void cadastrarPizza(TipoPizza tipo, List<Molho> molhosSelecionados) {
        Pizza pizza = new Pizza(tipo, molhosSelecionados);
        pizzas.add(pizza);
        itemDAO.salvar(pizza);
    }

    // NOVO MÉTODO: Cadastra pizza com borda e recheios
    public void cadastrarPizza(TipoPizza tipo, List<Molho> molhosSelecionados, Borda borda, List<Recheio> recheiosSelecionados) {
        Pizza pizza = new Pizza(tipo, molhosSelecionados);
        if (borda != null) {
            pizza.setBorda(borda);
        }
        if (recheiosSelecionados != null && !recheiosSelecionados.isEmpty()) {
            for (Recheio recheio : recheiosSelecionados) {
                pizza.adicionarRecheio(recheio);
            }
        }
        pizzas.add(pizza);
        itemDAO.salvar(pizza);
    }

    // Método antigo mantido para compatibilidade
    public void cadastrarSalgado() {
        Salgado salgado = new Salgado(PRECO_FIXO_SALGADO);
        itemDAO.salvar(salgado);
    }

    // NOVO MÉTODO: Cadastra salgado com detalhes e preço calculado
    public void cadastrarSalgado(String tipo, String massa, List<String> recheios, double preco) {
        Salgado salgado = new Salgado(tipo, massa, recheios, preco);
        itemDAO.salvar(salgado);
    }

    // =========================
    // LISTAGENS ORDENADAS - DIFERENTES ALGORITMOS
    // =========================

    // Para objetos Pizza completos
    public List<Pizza> listarPizzasOrdenadas() {
        return itemDAO.listarPizzasPorPrecoSelectionSort();
    }

    public List<Pizza> listarPizzasOrdenadasBubbleSort() {
        return itemDAO.listarPizzasPorPrecoBubbleSort();
    }

    public List<Pizza> listarPizzasOrdenadasSelectionSort() {
        return itemDAO.listarPizzasPorPrecoSelectionSort();
    }

    // Para nomes formatados das pizzas (usar no ComboBox) - MÉTODOS JÁ IMPLEMENTADOS ACIMA

    public List<Salgado> listarSalgadosOrdenados() {
        return itemDAO.listarSalgadinhosPorPreco();
    }

    // =========================
    // MÉTODOS PARA COMPATIBILIDADE
    // =========================
    public List<Pizza> listarPizzas() {
        return listarPizzasOrdenadas();
    }

    public List<Salgado> listarSalgados() {
        return listarSalgadosOrdenados();
    }

    public List<ItemCardapio> listarTodosItens() {
        return itemDAO.listarTodos();
    }

    // =========================
    // VENDAS COM ÁRVORE BINÁRIA DE PESQUISA
    // =========================
    public void registrarVenda(ItemCardapio item, int quantidade) {
        Venda venda = new Venda(item, quantidade);
        vendaDAO.salvar(venda);
        arvoreVendas.insert(venda);
    }

    public List<Venda> listarVendas() {
        return arvoreVendas.inOrder();
    }

    public List<Venda> pesquisarVendas(String termo) {
        List<Venda> todasVendas = listarVendas();
        return todasVendas.stream()
                .filter(venda -> venda.toString().toLowerCase().contains(termo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Venda> filtrarVendasPorItem(String nomeItem) {
        List<Venda> todasVendas = listarVendas();
        return todasVendas.stream()
                .filter(venda -> venda.getItem().getNome().toLowerCase().contains(nomeItem.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Venda> filtrarVendasPorQuantidadeMinima(int quantidadeMinima) {
        List<Venda> todasVendas = listarVendas();
        return todasVendas.stream()
                .filter(venda -> venda.getQuantidade() >= quantidadeMinima)
                .collect(Collectors.toList());
    }

    public List<Venda> filtrarVendasPorValorMinimo(double valorMinimo) {
        List<Venda> todasVendas = listarVendas();
        return todasVendas.stream()
                .filter(venda -> venda.getTotal() >= valorMinimo)
                .collect(Collectors.toList());
    }

    // =========================
    // CÁLCULO DE PREÇO EM TEMPO REAL
    // =========================

    // Método original mantido para compatibilidade
    public double calcularPrecoPizza(TipoPizza tipo, List<Molho> molhosSelecionados) {
        if (tipo == null) return 0.0;

        double precoTotal = tipo.getPrecoBase();
        for (Molho molho : molhosSelecionados) {
            precoTotal += molho.getPrecoAdicional();
        }
        return precoTotal;
    }

    // NOVO MÉTODO: Calcula preço incluindo borda e recheios
    public double calcularPrecoPizza(TipoPizza tipo, List<Molho> molhosSelecionados, Borda borda, List<Recheio> recheiosSelecionados) {
        if (tipo == null) return 0.0;

        double precoTotal = tipo.getPrecoBase();

        // Adicionar preço dos molhos
        for (Molho molho : molhosSelecionados) {
            precoTotal += molho.getPrecoAdicional();
        }

        // Adicionar preço da borda (se selecionada)
        if (borda != null) {
            precoTotal += borda.getPrecoAdicional();
        }

        // Adicionar preço dos recheios
        for (Recheio recheio : recheiosSelecionados) {
            precoTotal += recheio.getPrecoAdicional();
        }

        return precoTotal;
    }

    // =========================
    // MÉTODOS AUXILIARES PARA PIZZAS CADASTRADAS
    // =========================

    public List<Pizza> getPizzasCadastradas() {
        return new ArrayList<>(pizzas);
    }

    public void limparPizzasCadastradas() {
        pizzas.clear();
    }

    // =========================
    // MÉTODOS DE ORDENAÇÃO PARA A LISTA INTERNA
    // =========================

    public List<Pizza> ordenarPizzasBubbleSort() {
        List<Pizza> listaOrdenada = new ArrayList<>(pizzas);
        int n = listaOrdenada.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (listaOrdenada.get(j).calcularPrecoTotal() > listaOrdenada.get(j + 1).calcularPrecoTotal()) {
                    // Troca
                    Pizza temp = listaOrdenada.get(j);
                    listaOrdenada.set(j, listaOrdenada.get(j + 1));
                    listaOrdenada.set(j + 1, temp);
                }
            }
        }
        return listaOrdenada;
    }

    public List<Pizza> ordenarPizzasSelectionSort() {
        List<Pizza> listaOrdenada = new ArrayList<>(pizzas);
        int n = listaOrdenada.size();

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (listaOrdenada.get(j).calcularPrecoTotal() < listaOrdenada.get(minIndex).calcularPrecoTotal()) {
                    minIndex = j;
                }
            }
            // Troca
            Pizza temp = listaOrdenada.get(minIndex);
            listaOrdenada.set(minIndex, listaOrdenada.get(i));
            listaOrdenada.set(i, temp);
        }
        return listaOrdenada;
    }

    // =========================
    // MÉTODOS DA ÁRVORE BINÁRIA
    // =========================

    public int getQuantidadeVendas() {
        return arvoreVendas.size();
    }

    public boolean existeVenda(int id) {
        // Para verificar se existe uma venda com ID específico
        Venda vendaTeste = new Venda(null, 0);
        // Note: Esta é uma implementação simplificada
        // Em uma implementação real, precisaríamos de um método de busca por ID na BST
        List<Venda> todasVendas = listarVendas();
        return todasVendas.stream().anyMatch(v -> v.getId() == id);
    }

    public void limparArvoreVendas() {
        arvoreVendas = new BinarySearchTree<>();
        // Note: Isso apenas limpa a árvore em memória, não o arquivo
    }
}