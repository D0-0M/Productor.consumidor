package Procon;

public class PC {
    public static void main(String args[]) {
        // creating buffer queue
        Q q = new Q();

        // starting producer thread
        Producer producer = new Producer(q);
        new Thread(producer, "Producer").start();

        // starting consumer threads
        for (int i = 0; i < 8; i++) {
            new Thread(new Consumer(q, i + 1, producer), "Consumer " + (i + 1)).start();
        }
    }
}