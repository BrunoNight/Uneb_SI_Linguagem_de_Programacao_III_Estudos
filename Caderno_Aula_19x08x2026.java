// === === === === === === === === === === === === === === === === === === //

// -> Classe para básica obtenção de thread atual, no caso a 'main'

public class Thread_Basic { // Classe para básica obtenção de thread atual, no caso a 'main'
    public static void main(String[] args) {
        // Criando variável thread 't', que é a thread do local,
        // no caso com nome 'main', definido naturalmente pelo java
        Thread t = Thread.currentThread();
        // Imprime nome da thread 't' ao pegar o mesmo com o getName();
        System.out.println(t.getName());
    }
}

// === === === === === === === === === === === === === === === === === === //

// -> Classe para impressão dos nomes da thread atual e de uma thread criada do zero

// Classe para impressão dos nomes da thread atual
// e de uma thread criada do zero
public class Thread_Basic_2 { 
    public static void main(String[] args) {
        // Variável thread que obtém a Thread atual 'main'
        Thread tMain = Thread.currentThread();
        
        // Variável thread para criar uma nova Thread, sendo 'Thread-0'
        // ao não ser atribuído um nome à ela
        Thread tCriada1 = new Thread();
        
        // Variável thread para criar uma nova Thread, sendo 'Thread_Bala'
        // ao atribuir um nome à ela
        Thread tCriada2 = new Thread("Thread_Bala");
        
        // Impressão dos nomes das threads
        System.out.println("Thread atual: " + tMain.getName()
        + "\n" + "Thread Criada 1: " + tCriada1.getName() + "\n" 
        + "Thread Criada 2: " + tCriada2.getName()
        );
    }
}
