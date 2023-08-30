package Procon;

public class Producer implements Runnable {
    private Q q;

    public Producer(Q q) {
        this.q = q;
    }

    public void run() {
        while (true) {
            // wait until the list is empty
            while (!q.isFull()) {
                q.put();
            }

            synchronized (q) {
                try {
                    while (!q.isEmpty()) {
                        q.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}