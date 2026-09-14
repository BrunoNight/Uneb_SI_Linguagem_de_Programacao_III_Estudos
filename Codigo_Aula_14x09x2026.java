public class ControleAcesso {
    
    public static void main(String[] args) throws InterruptedException {
        Estadio estadio = new Estadio(200);
        Catraca[] catracas = new Catraca[4];
        
        for(int i = 0; i < catracas.length; i++) {
            catracas[i] = new Catraca("Catraca-" + (i + 1), estadio);
            catracas[i].start();
        }
        
        for(Catraca c: catracas) {
            c.join();
        }
        
        estadio.relatorio();
    }
}


import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Estadio {
    private final int capacidade;
    // Isso vai no lugar de synchronized
    private final AtomicInteger publicoAtual = new AtomicInteger(0);
    // Coleção concorrente
    private final Map<String, Integer> entradasPorCatraca = new ConcurrentHashMap<>();
    // Coleção sincronizada: log
    private final List<String> log = Collections.synchronizedList(new ArrayList<>());
    
    public Estadio(int c) {
        this.capacidade = c;
    }
    
    public boolean liberarEntrada(String catraca) {
        int atual;
        do {
            atual = publicoAtual.get();
            if(atual >= capacidade) {
                return false;
            }
        } while(!publicoAtual.compareAndSet(atual, atual + 1));
        
        int numero = atual + 1;
        entradasPorCatraca.merge(catraca, 1, Integer::sum);
        log.add(numero + ";" + catraca);
        System.out.printf("%s liberou a entrada no %d | Vagas restantes: %d%n",
        catraca, numero, capacidade - numero);
        return true;
    }
    
    public void relatorio() {
        System.out.println("\n --- Relatório ---");
        System.out.println("Público total: " + publicoAtual.get());
        System.out.println("Registros no log: " + log.size());
        entradasPorCatraca.forEach((k, v) -> System.out.println(k + "; " + v));
        
        synchronized(log) {
            System.out.println("Primeiro registro: " + log.get(0));
            System.out.println("Último registro: " + log.get(log.size() - 1));
        }
    }
}


public class Catraca extends Thread {
    private final Estadio estadio;
    
    public Catraca(String nome, Estadio e) {
        super(nome);
        this.estadio = e;
    }
    
    @Override
    public void run() {
        while(Estadio.liberarEntrada(getName())) {
            try {
                Thread.sleep(5);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

