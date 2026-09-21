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

// Registro de alunos em uma turma (por nome e nota) //

public class Main {
    public static void main(String[] args) {
        Turma turma = new Turma();
        MeuRunnable rn = new MeuRunnable(turma);
        int qtThreads = 5;
        
        Thread[] t = new Thread[qtThreads];
        
        for(int i = 0; i < qtThreads; i++) {
            t[i] = new Thread(rn, "Cadastrador" + i);
            t[i].start();
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Turma {
    private Map <String, Double> turma;
    
    public Turma() {
        this.turma = new ConcurrentHashMap<>();
    }
    
    public void addAluno(String nome, Double nota) {
        this.turma.put(nome, nota);
        System.out.println("Aluno " + nome + " adicionado com sucesso!");
        System.out.println("Nota: " + nota);
    }
}

import java.util.concurrent.atomic.AtomicInteger;

public class MeuRunnable implements Runnable {
    private Turma turma;
    public AtomicInteger n = new AtomicInteger(0);
    
    
    public MeuRunnable(Turma t) {
        this.turma = t;
    }
    
    
    @Override
    public void run() {
        int numAtual = n.getAndIncrement();
        turma.addAluno("Bruno" + numAtual, 2.6 * numAtual);
    }
}

// === === === === === === === === === === === === === === === === === === //
INCOMPLETO
// Registro de pacientes em uma fila //

public class Upa {
    public static void main(String[] args) {
        Fila fila = new Fila();
        Servico rn = new Servico(fila);
        int qtThreads = 15;
        
        Thread[] t = new Thread[qtThreads];
        
        for(int i = 0; i < qtThreads; i++) {
            t[i] = new Thread(rn, "Cadastrador" + i);
            t[i].start();
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Fila final: " + fila.getFila());
    }
}

public class Paciente {
    private String nome;
    private int idade;
    
    public Paciente(String n, int i) {
        this.nome = n;
        this.idade = i;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public int getIdade() {
        return this.idade;
    }
}

import java.util.concurrent.atomic.AtomicInteger;

public class Servico implements Runnable {
    private Fila fila;
    public AtomicInteger n = new AtomicInteger(0);
    
    public Servico(Fila f) {
        this.fila = f;
    }
    
    @Override
    public void run() {
        int numAtual = n.getAndIncrement();
        fila.addPac("AnaP0" + numAtual, 6 * numAtual);
    }
}

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Fila {
    private BlockingQueue <Paciente> filaPac;
    
    public Fila() {
        this.filaPac = new LinkedBlockingQueue<>();
    }
    
    public BlockingQueue <Paciente> getFila() {
        return this.filaPac;
    }
    
    public void addPac(String nome, int idade) {
        try {
            Paciente nvPac = new Paciente(nome, idade);
            filaPac.put(nvPac);
            System.out.println("Paciente " + nome + " adicionado à fila!"
            + " Idade de " + idade + " anos!");
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

