// === === === === === === === === === === === === === === === === === === //

// Vender tickets em estádio

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Estadio {
    private final AtomicInteger qtTickets;
    
    public Estadio(int qtT) {
        this.qtTickets = new AtomicInteger(qtT);
    }
    
    public int getQtTickets() {
        return this.qtTickets.get();
    }
    
    public int decrementarTickets(int qt) {
        int atual;
        
        // Validação prévia para evitar loop infinito se o estoque for menor que o pedido
        atual = getQtTickets();
        if (atual <= 0) {
            System.out.println("Tickets esgotados!");
            return -1;
        } else if (atual < qt) {
            System.out.println("Quantidade de tickets insuficiente ao pedido!");
            return atual;
        }
        
        do {
            atual = getQtTickets();
            
            if (atual < qt) {
                System.out.println("Quantidade insuficiente no momento da venda!");
                return atual;
            }
            
        } while (!this.qtTickets.compareAndSet(atual, atual - qt));
        
        return atual - qt;
    }
    
    public static void main(String[] args) {
        Estadio est = new Estadio(100);
        MeuCallable call = new MeuCallable(est);
        ExecutorService servicoExecucao = Executors.newSingleThreadExecutor();
        
        try {
            Future<String> resultado = servicoExecucao.submit(call);
            System.out.println(resultado.get());
            servicoExecucao.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception e) { // Captura InterruptedException e ExecutionException do .get()
            System.out.println("Serviço interrompido.");
            servicoExecucao.shutdownNow();
        } finally {
            servicoExecucao.shutdown();
            System.out.println("Finalizado serviço de venda de tickets!");
        }
    }
}

import java.util.concurrent.Callable;
import java.util.Random;

public class MeuCallable implements Callable<String> {
    private final Estadio estad;
    
    public MeuCallable(Estadio e) {
        this.estad = e;
    }
    
    @Override
    public String call() throws Exception {
        String nome = Thread.currentThread().getName();
        int qtVendida = new Random().nextInt(10) + 1;
        int restantes = estad.decrementarTickets(qtVendida);
        
        if (restantes == -1 || restantes == qtVendida && estad.getQtTickets() < qtVendida) {
            return "Não há mais tickets suficientes disponíveis!";
        }
        
        return nome + " vendeu " + qtVendida + " tickets! Faltam: " + estad.getQtTickets();
    }
}
