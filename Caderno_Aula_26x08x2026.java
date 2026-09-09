// === === === === === === === === === === === === === === === === === === //

// -> Threads em vetor e sincronização por objeto lock em parte específica do código (Classe Sincr_Obj)

public class Sincr_Obj {

    // Objeto para ser o monitor para quem executará o código restrito
    private Object lock = new Object();
    private int n = 0;

    // Pegar o Lock sem alterar ele
    public Object getLock() {
        return this.lock;
    }

    // Pega o número atual sem mexer na fonte
    public int getN() {
        return this.n;
    }

    // Método público para incrementar
    public void incrementar() {
        this.n++;
    }

    public static void main(String[] args) {

        // Declara objeto para ser referenciado no MeuRunnable
        Sincr_Obj obj = new Sincr_Obj();
        
        // Declara MeuRunnable com parâmetro do objeto lock para ser usado no método restrito
        MeuRunnable rn = new MeuRunnable(obj);
        
        // Declara vetor de threads sem precisar declarar cada uma individualmente
        Thread[] t = new Thread[5];

        // Criação das threads
        for(int i = 0; i < 5; i++) {
            t[i] = new Thread(rn);
            t[i].start();
        }

    }
}

// -> Threads em vetor e sincronização por objeto lock em parte específica do código (Classe MeuRunnable)

public class MeuRunnable implements Runnable {

    // Declara variável da classe Sincr_Obj para que essa classe possa ter
    // acesso às variáveis e métodos presentes lá para serem executadas aqui
    private Sincr_Obj objeto;

    // Construtor do MeuRunnable
    public MeuRunnable(Sincr_Obj o) {
        this.objeto = o;
    }

    @Override
    // Método "run" aberto
    public void run() {

        // Parte aberta
        System.out.println("=================================");
        System.out.println("Isso está fora da parte restrita!");

        // Parte restrita (uma thread por vez)
        synchronized(objeto.getLock()) {
            System.out.print("Atualizado valor de " + objeto.getN());
            objeto.incrementar();
            System.out.println(" para " + objeto.getN());
        }

        // Parte aberta
        System.out.println("Isso está fora da parte restrita!");
        System.out.println("=================================");

    }
}

// === === === === === === === === === === === === === === === === === === //

// -> Classes para venda de ingressos por Caixa (Classe Caixas)

public class Caixas {
    private int ingressos = 200;
    private int tamanho = this.ingressos;
    
    public int getIngressos() {
        return this.ingressos;
    }
    
    public int getTamanho() {
        return this.tamanho;
    }
    
    public synchronized int decrementar() {
        return this.ingressos--;
    }
    
    public static void main(String[] args) {
        Caixas cxs = new Caixas();
        int qtCaixas = 5;
        int cotaPorCaixa = cxs.getTamanho() / qtCaixas;
        
        Bilheteria rn = new Bilheteria(cxs, cotaPorCaixa);
        
        Thread[] caixa = new Thread[5];
        
        for(int i = 0; i < 5; i++) {
            caixa[i] = new Thread(rn);
            caixa[i].start();
        }
    }
}

// -> Classes para venda de ingressos por Caixa (Classe Bilheteria)

public class Bilheteria implements Runnable {
    private Caixas cxas;
    private int cota;
    
    public Bilheteria(Caixas c, int ct) {
        this.cxas = c;
        this.cota = ct;
    }
    
    @Override
    public void run() {
        int contador = 0;
        
        while(contador < cota) {
            
            synchronized(cxas) {
                if(cxas.getIngressos() <= 0) {
                    System.out.println("==================================================");
                    System.out.println("Sem ingressos!");
                    System.out.println("==================================================");
                    break;
                }
        
                String nome = Thread.currentThread().getName();
                cxas.decrementar();
                System.out.println("==================================================");
                System.out.println("O caixa " + nome + " vendeu 1 ingresso! " + "Faltam " 
                + cxas.getIngressos() + " ingressos!");
                System.out.println("==================================================");
            }
            
            contador++;
        }
    }
}

// === === === === === === === === === === === === === === === === === === //

// -> Classes para venda de ingressos por Caixa com Atomicidade (Classe Caixas)

import java.util.concurrent.atomic.AtomicInteger;

public class Atomicidade {
    private AtomicInteger ingressos = new AtomicInteger(100);
    private int qt = 5;
    private int cota = ingressos.get() / qt;
    
    public Atomicidade() {}
    
    public Atomicidade(AtomicInteger i, int q, int c) {
        this.ingressos = i;
        this.qt = q;
        this.cota = c;
    }
    
    public AtomicInteger getIng() { return this.ingressos; }
    
    public int getQt() { return this.qt; }
    
    public int getCota() { return this.cota; }
    
    public static void main(String[] args) {
        Atomicidade at = new Atomicidade();
        MeuRunnable rn = new MeuRunnable(at, at.getCota());
        Thread[] t = new Thread[at.getQt()];
        
        for(int i = 0; i < at.getQt(); i++) {
            t[i] = new Thread(rn);
            t[i].start();
        }
    }
}

// -> Classes para venda de ingressos por Caixa com Atomicidade (Classe MeuRunnable)

public class MeuRunnable implements Runnable {
    private Atomicidade atomic;
    private int cota;
    
    public MeuRunnable(Atomicidade a, int c) {
        this.atomic = a;
        this.cota = c;
    }
    
    @Override
    public void run() {
        for(int i = 0; i < cota; i++) {
            int atual;
        
            // Tenta decrementar de forma atômica e segura
            do {
                atual = atomic.getIng().get();
                
                if (atual <= 0) {
                    System.out.println("==================================================");
                    System.out.println("Não há ingressos disponíveis!");
                    System.out.println("==================================================");
                    return; // Encerra a thread se acabaram os ingressos
                }
                
            // O compareAndSet garante que se o valor mudou por outra thread, ele tenta de novo
            } while(!atomic.getIng().compareAndSet(atual, atual - 1));
        
            String nome = Thread.currentThread().getName();
            int ingRestantes = atual - 1; // O valor após a nossa venda bem-sucedida
        
            System.out.println("O caixa " + nome + " vendeu 1 ingresso! Falta(m) " + ingRestantes + " ingressos!");
        }
    }
}
