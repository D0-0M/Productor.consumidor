package Procon;


public class Consumer implements Runnable {
    private Q q;
    private int consumerId;
    private Producer producer;

    public Consumer(Q q, int consumerId, Producer producer) {
        this.q = q;
        this.consumerId = consumerId;
        this.producer = producer;
    }

    public void run() {
        while (true) {
            // consumer get items
            while (!q.isEmpty()) {
                q.get(consumerId);
            }

            // wait for the producer to fill the list
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (q.isEmpty()) {
                synchronized (q) {
                    q.notify();
                }
            }
        }
    }
}