import java.util.List;

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
        for(int i = 0; i < this.qtAlunos; i++) {
            String nome = "Aluno " + (i+1);
            String curso = "Matemática";
            int matricula = 2026000 + (i+2*i*2);
            int qtDisciplinas = 0;
            
            a[i] = new Aluno(nome, curso, matricula, qtDisciplinas);
            turma.add(a[i]);
        }
        
        MeuRunnable rn = new MeuRunnable(turma);
        Thread[] t = new Thread[qtThreads];
        
        for(int i = 0; i < this.qtThreads; i++) {
            t[i] = new Thread(rn, "Atendente " + (i+1));
            t[i].start();
        }
    }
}



import java.util.concurrent.atomic.AtomicInteger;

public class Aluno {
    private String nome, curso;
    private int matricula;
    private AtomicInteger qtDisciplinas;
    
    public Aluno(String n, String c, int m, int q) {
        this.nome = n;
        this.curso = c;
        this.matricula = m;
        this.qtDisciplinas = = new AtomicInteger(0);
    }
    
    public String getNome() { return this.nome; }
    public String getCurso() { return this.curso; }
    public int getMatricula() { return this.matricula; }
    public int getQtDisciplinas() { return this.qtDisciplinas.get(); }
    
    public int acrescentarDisc() { return this.qtDisciplinas.incrementAndGet(); }
    
}




import java.util.concurrent.atomic.AtomicInteger;

public class MeuRunnable implements Runnable {
    private final List<Aluno> turma;
    private static final AtomicInteger proxAluno = new AtomicInteger(0);

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
        }
        
        
        
        
        
        
        
        
        
        while(alunosAtender >= 0 && atendeu == 0) {
            int alunosAtender = turma.getQtAlunos();
            int atendeu = 0;
            
            
            
            for(int i = 0; i < alunosAtender; i++) {
                System.out.println("=========================");
                System.out.println("O " + nome + "atendeu o " + turma[i].getNome())
                + ", adicionado 1 disciplina!"
            }
        }
    }
    
    
}

