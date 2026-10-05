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

// === === === === === === === === === === === === === === === === === === //

// Análise de retirada de dinheiro

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Conta {
    private final AtomicInteger saldo;
    private final String dono;
    
    public Conta(int s, String d) {
        this.saldo = new AtomicInteger(s);
        this.dono = d;
    }
    
    public int getSaldo() {
        return this.saldo.get();
    }
    
    public String getDono() {
        return this.dono;
    }
    
    public int decrementar(int valor) {
        int atual;
        
        do {
            atual = getSaldo();
            
            if(atual <= 0) {
                System.out.println("Saldo insuficiente!");
                return -1;
            } else if(atual < valor) {
                System.out.println("Saldo menor que o valor solicitado para sacar!");
                return -1;
            }
        } while(!this.saldo.compareAndSet(atual, atual - valor));
        
        return atual - valor;
    }
    
    public static void main(String args[]) {
        Conta c = new Conta(10000, "Bruno");
        BCallable call = new BCallable(c);
        ExecutorService e = Executors.newFixedThreadPool(2);
        
        try {
            for(int i = 0; i < 10; i++) {
                Future<String> promessa = e.submit(call);
                
                System.out.println("A tarefa terminou? " + promessa.isDone());
            
                String resultado = promessa.get(1, TimeUnit.SECONDS);
                System.out.println("Resultado: " + resultado);
            
                System.out.println("Tarefa terminou após o get? " + promessa.isDone());
            }
        } catch(TimeoutException t) {
            System.out.println("Erro: " + t);
        } catch(Exception ex) {
            System.out.println("Erro: " + ex);
        } finally {
            if(e != null) {
                e.shutdown();
            }
        }
    }
}

import java.util.concurrent.Callable;
import java.util.Random;

public class BCallable implements Callable<String> {
    private final Conta conta;
    
    public BCallable(Conta c) {
        this.conta = c;
    }
    
    @Override
    public String call() throws Exception {
        String nome = Thread.currentThread().getName();
        int valorRetirada = new Random().nextInt(40);
        
        conta.decrementar(valorRetirada);
        
        Thread.sleep(500);
        
        return nome + " retirou da conta de " + conta.getDono() + " " + valorRetirada + " reais! "
        + "Faltam " + conta.getSaldo() + " reais!";
    }
}
