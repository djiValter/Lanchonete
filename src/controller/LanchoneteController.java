package src.controller;

import src.dao.ItemDAO;
import src.dao.VendaDAO;
import src.model.*;

import java.util.List;

/**
 * Camada de controle do sistema de Lanchonete.
 * Versão corrigida com inicialização segura e tratamento de erros.
 */
public class LanchoneteController {

    private static volatile ItemDAO itemDAO;
    private static volatile VendaDAO vendaDAO;

    // Bloco de inicialização estática para garantir única instância
    static {
        initializeDAOs();
    }

    public LanchoneteController() {
        // Garante que os DAOs estão inicializados
        if (itemDAO == null || vendaDAO == null) {
            initializeDAOs();
        }
    }

    private static synchronized void initializeDAOs() {
        if (itemDAO == null) {
            itemDAO = new ItemDAO();
        }
        if (vendaDAO == null) {
            vendaDAO = new VendaDAO();
        }
    }

    // =====================
    // GETTERS SEGUROS
    // =====================
    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public VendaDAO getVendaDAO() {
        return vendaDAO;
    }

    // =====================
    // MÉTODOS DE ITENS - CORRIGIDOS
    // =====================

    public void salvarItem(ItemCardapio item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        try {
            itemDAO.salvar(item);
            // FORÇA salvamento adicional para garantir
            itemDAO.save();
            System.out.println("Item salvo e persistido: " + item.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao salvar item: " + e.getMessage());
            throw new RuntimeException("Falha ao salvar item", e);
        }
    }

    // ADICIONE método para verificar dados em tempo real
    public void verificarDados() {
        System.out.println("=== VERIFICAÇÃO DE DADOS ===");
        System.out.println("Itens totais: " + listarTodosItens().size());
        System.out.println("Pizzas: " + getPizzas().size());
        System.out.println("Salgados: " + getSalgados().size());
        System.out.println("Vendas: " + listarVendas().size());

        itemDAO.debugStatus();
    }

    public List<ItemCardapio> listarTodosItens() {
        try {
            return itemDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar itens: " + e.getMessage());
            return List.of(); // Retorna lista vazia em caso de erro
        }
    }

    // =====================
    // MÉTODOS ESPECÍFICOS - COM FALLBACK
    // =====================

    public List<Salgado> listarSalgadosPorPreco() {
        try {
            // Tenta usar o método específico se existir
            return itemDAO.listarSalgadinhosPorPreco();
        } catch (Exception e) {
            // Fallback: ordena manualmente
            System.err.println("Método específico não disponível, usando fallback: " + e.getMessage());
            List<Salgado> salgados = itemDAO.listarTodosSalgados();
            salgados.sort((s1, s2) -> Double.compare(s1.calcularPrecoTotal(), s2.calcularPrecoTotal()));
            return salgados;
        }
    }

    public List<Pizza> getPizzas() {
        try {
            return itemDAO.listarTodasPizzas();
        } catch (Exception e) {
            System.err.println("Erro ao carregar pizzas: " + e.getMessage());
            return List.of();
        }
    }

    public List<Salgado> getSalgados() {
        try {
            return itemDAO.listarTodosSalgados();
        } catch (Exception e) {
            System.err.println("Erro ao carregar salgados: " + e.getMessage());
            return List.of();
        }
    }

    // =====================
    // MÉTODOS DE VENDA - CORRIGIDOS
    // =====================

    public void venderPizza(Pizza pizza, int quantidade) {
        if (pizza == null || quantidade <= 0) {
            throw new IllegalArgumentException("Pizza ou quantidade inválidos!");
        }
        try {
            registrarVenda(pizza, quantidade);
            System.out.println("Venda registrada: " + quantidade + "x " + pizza.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao registrar venda de pizza: " + e.getMessage());
            throw new RuntimeException("Falha ao registrar venda", e);
        }
    }

    public void venderSalgado(Salgado salgado, int quantidade) {
        if (salgado == null || quantidade <= 0) {
            throw new IllegalArgumentException("Salgado ou quantidade inválidos!");
        }
        try {
            registrarVenda(salgado, quantidade);
            System.out.println("Venda registrada: " + quantidade + "x " + salgado.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao registrar venda de salgado: " + e.getMessage());
            throw new RuntimeException("Falha ao registrar venda", e);
        }
    }

    public void registrarVenda(ItemCardapio item, int quantidade) {
        if (item == null || quantidade <= 0) {
            throw new IllegalArgumentException("Item ou quantidade inválidos!");
        }
        try {
            Venda venda = new Venda(item, quantidade);
            vendaDAO.salvar(venda);
            System.out.println("Venda registrada: " + quantidade + "x " + item.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao registrar venda: " + e.getMessage());
            throw new RuntimeException("Falha ao registrar venda", e);
        }
    }

    public List<Venda> listarVendas() {
        try {
            return vendaDAO.listarTodas();
        } catch (Exception e) {
            System.err.println("Erro ao listar vendas: " + e.getMessage());
            return List.of();
        }
    }

    // =====================
    // UTILITÁRIOS - CORRIGIDOS
    // =====================

    public double calcularTotalGeralVendas() {
        try {
            double total = 0.0;
            for (Venda venda : listarVendas()) {
                total += venda.getTotal();
            }
            return total;
        } catch (Exception e) {
            System.err.println("Erro ao calcular total de vendas: " + e.getMessage());
            return 0.0;
        }
    }

    public int contarVendas() {
        try {
            return listarVendas().size();
        } catch (Exception e) {
            System.err.println("Erro ao contar vendas: " + e.getMessage());
            return 0;
        }
    }

    // =====================
    // MÉTODOS DE CATALOGO - COM FALLBACK
    // =====================

    public List<Molho> getMolhos() {
        try {
            return itemDAO.listarMolhos();
        } catch (Exception e) {
            System.err.println("Erro ao carregar molhos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Borda> getBordas() {
        try {
            return itemDAO.listarBordas();
        } catch (Exception e) {
            System.err.println("Erro ao carregar bordas: " + e.getMessage());
            return List.of();
        }
    }

    public List<Recheio> getRecheios() {
        try {
            return itemDAO.listarRecheios();
        } catch (Exception e) {
            System.err.println("Erro ao carregar recheios: " + e.getMessage());
            return List.of();
        }
    }

    public List<TipoPizza> getTiposPizza() {
        try {
            return itemDAO.listarTiposPizza();
        } catch (Exception e) {
            System.err.println("Erro ao carregar tipos de pizza: " + e.getMessage());
            return List.of();
        }
    }

    // =====================
    // MÉTODO PARA VERIFICAR INTEGRIDADE
    // =====================

    public void verificarIntegridadeDados() {
        try {
            int totalItens = listarTodosItens().size();
            int totalPizzas = getPizzas().size();
            int totalSalgados = getSalgados().size();
            int totalVendas = contarVendas();

            System.out.println("=== INTEGRIDADE DOS DADOS ===");
            System.out.println("Total de itens: " + totalItens);
            System.out.println("Total de pizzas: " + totalPizzas);
            System.out.println("Total de salgados: " + totalSalgados);
            System.out.println("Total de vendas: " + totalVendas);
            System.out.println("Total geral de vendas: R$ " + calcularTotalGeralVendas());
        } catch (Exception e) {
            System.err.println("Erro ao verificar integridade: " + e.getMessage());
        }
    }
}