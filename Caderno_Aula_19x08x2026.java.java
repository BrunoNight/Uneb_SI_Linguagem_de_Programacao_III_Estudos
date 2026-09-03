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

