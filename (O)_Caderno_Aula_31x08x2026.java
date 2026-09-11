// === === === === === === === === === === === === === === === === === === //

// -> Atendimento de alunos com coleções para concorrência (Classe Turma)

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Turma {
    private static List<Aluno> turma = new CopyOnWriteArrayList<>();
    private static int qtThreads = 5;
    private static int qtAlunos = 10;
    
    public List<Aluno> getTurma() {
        return this.turma;
    }
    
    public int getQtAlunos() {
        return this.qtAlunos;
    }
    
    public static void main(String[] args) {
        Aluno[] a = new Aluno[qtAlunos];
        for(int i = 0; i < qtAlunos; i++) {
            String nome = "Aluno " + (i+1);
            String curso = "Matemática";
            int matricula = 2026000 + (i+2*i*2);
            int qtDisciplinas = 0;
            
            a[i] = new Aluno(nome, curso, matricula, qtDisciplinas);
            turma.add(a[i]);
        }
        
        MeuRunnable rn = new MeuRunnable(turma);
        Thread[] t = new Thread[qtThreads];
        
        for(int i = 0; i < qtThreads; i++) {
            t[i] = new Thread(rn, "Atendente " + (i+1));
            t[i].start();
        }
    }
}

// -> Atendimento de alunos com coleções para concorrência (Classe Aluno)

import java.util.concurrent.atomic.AtomicInteger;

public class Aluno {
    private String nome, curso;
    private int matricula;
    private AtomicInteger qtDisciplinas;
    
    public Aluno(String n, String c, int m, int q) {
        this.nome = n;
        this.curso = c;
        this.matricula = m;
        this.qtDisciplinas = new AtomicInteger(0);
    }
    
    public String getNome() { return this.nome; }
    public String getCurso() { return this.curso; }
    public int getMatricula() { return this.matricula; }
    public int getQtDisciplinas() { return this.qtDisciplinas.get(); }
    
    public int acrescentarDisc() { return this.qtDisciplinas.incrementAndGet(); }
}

// -> Atendimento de alunos com coleções para concorrência (Classe MeuRunnable)

import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

public class MeuRunnable implements Runnable {
    private final List<Aluno> turma;
    private static final AtomicInteger proxAluno = new AtomicInteger(0);
    private static final Object divid = new Object();

    public MeuRunnable(List<Aluno> l) {
        this.turma = l;
    }

    @Override
    public void run() {
        String nome = Thread.currentThread().getName();
        
        if(turma == null) {
            System.out.println("=========================");
            System.out.println("Turma vazia!");
            System.out.println("=========================");
            return;
        }
        
        while(true) {
            int indAtendimento = proxAluno.getAndIncrement();
            
            if(indAtendimento >= turma.size()) {
                break;
            }
            
            Aluno aluno = turma.get(indAtendimento);
            aluno.acrescentarDisc();
            
            System.out.println("O " + nome + " atendeu o aluno " + aluno.getNome()
            + " e acrescentou 1 disciplina!" + " Total = " + aluno.getQtDisciplinas()
            + " disciplinas!");
        }
    }
}

// === === === === === === === === === === === === === === === === === === //

// -> Colocar livros na livraria

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class Livraria {
    private static Map<String, Livro> catalogo = new ConcurrentHashMap<>();
    private static int qtFornecedores = 5;

    public Livraria(Map<String, Livro> c, int qtF) {
        this.catalogo = c;
        this.qtFornecedores = qtF;
    }

    public Map<String, Livro> getCatalogo() {
        return this.catalogo;
    }

    public int getFornecedores() {
        return this.qtFornecedores;
    }

    public static void main(String[] args) {
        Localizar_Acrescentar rn = new Localizar_Acrescentar(catalogo);
        Thread[] t = new Thread[qtFornecedores];

        for(int i = 0; i < qtFornecedores; i++) {
            t[i] = new Thread(rn, "Fornecedor " + (1 + i * 4 * i))
            t[i].start();
        }
    }
}

import java.util.concurrent.AtomicInteger;

public class Livro {
    private String nome, genero;
    private float preco;
    private static AtomicInteger numLivro = new AtomicInteger(0);

    public Livro(String n, String g, float p) {
        this.nome = n;
        this.genero = g;
        this.preco = p;
    }

    public String getNome() {
        return this.nome;
    }

    public String getGenero() {
        return this.genero;
    }

    public float getPreco() {
        return this.preco;
    }

    public void setPreco(float nvP) {
        this.preco = nvP;
    }
}

public class Localizar_Acrescentar implements Runnable {
    private static final Map<String, Livro> catalog;

    public Localizar_Acrescentar(Map<String, Livro> c) {
        this.catalog = c;
    }

    @Override
    public void run() {
        String nomeFornecedor = Thread.currentThread().getName();
        
        public int idUnico = Livraria.numLivro.getAndIncrement();
            
        String nomeLivro = "Roberto " + idUnico;
        String generoLivro = "Fantasia " + (idUnico * 16 - 2 * 3);
        float precoLivro = idUnico + 19 * 6 + 12

        Livro novoLivro(nomeLivro, generoLivro, precoLivro);

        String chave = nomeLivro + " - " + generoLivro;
        
        Livro livroExiste = catalog.novoLivro.putIfAbsent(nomeLivro, novoLivro);
        
        if(livroExiste == null) {
            System.out.println("Livro já existe!");
        } else {
            System.out.println(nomeLivro + " acrescentado ao catálogo!");
        }
    }
}
