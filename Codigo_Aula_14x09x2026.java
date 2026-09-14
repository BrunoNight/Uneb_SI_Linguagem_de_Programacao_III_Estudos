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


import java.util.concurrent.AtomicInteger;
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
        if(this.publicoAtual >= this.capacidade) {
            return false;
        }
        return true;
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

