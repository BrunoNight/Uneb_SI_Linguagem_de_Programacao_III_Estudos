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
