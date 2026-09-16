// === === === === === === === === === === === === === === === === === === //

// Registro de livros em uma livraria por bibliotecários //

// -> Classe Main

public class Main {
    
    public static void main(String[] args) {
        int qtThreads = 5;
        
        System.out.println("Hello and welcome!");
        
        Livraria livraria = new Livraria();
        MeuRunnable rn = new MeuRunnable(livraria);
        
        Thread[] t = new Thread[qtThreads];
        
        for(int i = 0; i < qtThreads; i++) {
            t[i] = new Thread(rn, "Bibliotecário-" + (i+1));
            t[i].start();
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// -> Classe Livro

public class Livro {
    private String nome, autor;
    private int numPaginas;
    
    public Livro(String n, String a, int nP) {
        this.nome = n;
        this.autor = a;
        this.numPaginas = nP;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public String getAutor() {
        return this.autor;
    }
    
    public int getNumPaginas() {
        return this.numPaginas;
    }
}

// -> Classe Livraria

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Livraria {
    private final List<Livro> livraria;
    
    public Livraria() {
        this.livraria = new CopyOnWriteArrayList<>();
    }
    
    public void adicionarLivro(Livro liv) {
        livraria.add(liv);
    }
}

// -> Classe MeuRunnable

public class MeuRunnable implements Runnable {
    private Livraria livraria;
    private final Object lock = new Object();
    private int regAtual = 0;
    
    public MeuRunnable(Livraria l) {
        this.livraria = l;
    }
    
    @Override
    public void run() {
        String nomeThread = Thread.currentThread().getName();
        
        synchronized(lock) {
            String nomeLivro = "Livro-" + (regAtual * 3);
            String autorLivro = "Bruno-" + (regAtual + 1);
            int numPagLivro = 90 + (regAtual * 4);
                
            Livro novoLivro = new Livro(nomeLivro, autorLivro, numPagLivro);
                
            livraria.adicionarLivro(novoLivro);
            System.out.println("O " + nomeThread + " adicionou o livro "
            + nomeLivro + " !");
            regAtual++;
        }
    }
}

// === === === === === === === === === === === === === === === === === === //


