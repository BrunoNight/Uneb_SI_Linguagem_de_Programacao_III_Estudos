import java.util.List;
import java.util.ArrayList;

public class Aluno {
  private String nome, mat;
  private int id;

  public Aluno(String n, String m) {
    this.nome = n;
    this.mat = m;
  }

  public String getNome() {
    return this.nome;
  }

  public String getMat() {
    return this.mat;
  }

  public int getId() {
    return this.id;
  }

  public void setNome(String nm) {
    this.nome = nm;
  }

  public void setMat(String mt) {
    this.mat = mt;
  }

  public void setId(int novoID) {
    this.id = novoID;
  }
}

public class GerenciarAlunos {
  private List <Aluno> alunos = new ArrayList<>();
  private int id;

  public GerenciarAlunos() {
    this.id = 0;
  }

  public int getId() {
    return this.id;
  }
  
  public void cadastrarAluno(Aluno novoAluno) {
    for(Aluno a: this.alunos) {
      if(a.getMat().equals(novoAluno.getMat())) {
        System.out.println("Matrícula digitada já existe!");
        return;
      }
    }

    novoAluno.setId(this.id);
    this.id++;
    this.alunos.add(novoAluno);
    System.out.println("Aluno cadastrado com sucesso!");
    return;
  }

  public void listarPorId(int idBusca) {
    for(Aluno a: this.alunos) {
      if(a.getId() == idBusca) {
        System.out.println(a.getId() + " - " + a.getNome());
        return;
      }
    }
    System.out.println("Aluno não encontrado pelo ID!");
    return;
  }

  public void removerPorNome(String nomeRemov) {
    boolean removido = this.alunos.removeIf(a -> a.getNome().equalsIgnoreCase(nomeRemov));

    if(removido) {
      System.out.println("Aluno removido!");
    } else {
      System.out.println("Aluno não encontrado!");
    }

    return;
  }
}
