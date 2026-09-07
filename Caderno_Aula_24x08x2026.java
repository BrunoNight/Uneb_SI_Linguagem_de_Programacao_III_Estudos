// === === === === === === === === === === === === === === === === === === //

// -> Classe para instanciar threads por lambda no main e pelo Runnable diretamente

public class Thread_Rev {
    public static void main(String[] args) {
        // T1 é instanciada recebendo um comportamento Runnable via Lambda
        Thread t1 = new Thread(() -> {
            System.out.println("Teste: " + Thread.currentThread().getName());
        });
        // start() efetivamente cria o fluxo paralelo. 
        // Se usássemos .run(), rodaria sequencialmente na main.
        // t1.start();
        t1.run();
        
        MeuRunnable rn = new MeuRunnable();
        // T2 é instanciada recebendo o Runnable
        Thread t2 = new Thread(rn);
        // t2.start();
        t2.run();
        
        // Nesse caso start() há uma condição de corrida, assim,
        // quem terminar sua execução primeiro, é impressa primeiro
        // visto o fluxo paralelo do start()
        
        // Se for run(), a thread t1 nesse caso é sempre impressa
        // primeira visto a execução sequencial do método no main,
        // e o nome da thread será da thread main se não definido
        // um nome
    }
}

// -> Classe Runnable

public class MeuRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Imprimindo isso diretamente do Runnable: " 
        + Thread.currentThread().getName());
    }
}

// === === === === === === === === === === === === === === === === === === //

// -> Classe para execução de saques em conta bancária de forma sincronizada

public class ContaBancaria {
    private int saldo = 1000;
    
    public int getSaldo() {
        return this.saldo;
    }
    
    public synchronized void sacar(int valor) {
        String nomeThread = Thread.currentThread().getName();
        
        if(saldo < valor) {
            System.out.println("Saldo insuficiente " + nomeThread + "!");
            return;
        } else if(saldo == 0) {
            System.out.println("Saldo zerado " + nomeThread + "!");
            return;
        }
        
        this.saldo -= valor;
        System.out.println("Saque de " + valor + " feito com sucesso!");
    }
    
    public static void main(String[] args) {
        ContaBancaria conta = new ContaBancaria();
        
        MeuRunnable rn = new MeuRunnable(conta);
        
        Thread t1 = new Thread(rn);
        Thread t2 = new Thread(rn);
        t1.start();
        t2.start();
    }
}

// -> Classe MeuRunnable para cada thread executar o saque na conta bancária

import java.util.concurrent.ThreadLocalRandom;

public class MeuRunnable implements Runnable {
    private ContaBancaria conta;
    
    public MeuRunnable(ContaBancaria c) {
        this.conta = c;
    }
    
    @Override
    public void run() {
        int valorAleatorio = ThreadLocalRandom.current().nextInt(50, 1000);
        conta.sacar(valorAleatorio);
    }
}


