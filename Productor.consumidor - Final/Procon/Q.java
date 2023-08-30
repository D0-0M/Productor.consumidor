package Procon;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Q {
    private List<Integer> numbers = new ArrayList<>();

    private static Semaphore semCon = new Semaphore(0);
    private static Semaphore semProd = new Semaphore(1);
    private static Semaphore semConMutex = new Semaphore(1);

    private int nextConsumer = 1;
    private int bufferSize = new Random().nextInt(21) + 10; // Random buffer size between 10 and 30

    public void get(int consumerId) {
        try {
            while (consumerId != nextConsumer) {
                Thread.sleep(100); // Wait until it's the next consumer in order
            }
            semCon.acquire();
            semConMutex.acquire();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    
        int number = numbers.remove(0);
        System.out.println(Thread.currentThread().getName() + " consumed number: " + number);
    
        nextConsumer++; // Update the next consumer in order
    
        if (nextConsumer > 8) {
            nextConsumer = 1; // Reset to 1 if it exceeds the maximum number of consumer threads
        }
    
        semConMutex.release();
        semProd.release();
    
        if (isEmpty()) {
            synchronized (this) {
                notifyAll(); // Notify all waiting producers that the buffer is not empty
            }
        }
    }

    public void put() {
        try {
            semProd.acquire();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }

        Random random = new Random();
        int number = random.nextInt(101);
        numbers.add(number);

        System.out.println("Producer produced number: " + number);

        if (isFull()) {
            semCon.release(numbers.size());
        } else {
            semProd.release();
        }

        if (isFull()) {
            synchronized (this) {
                try {
                    wait(); // Wait until the buffer is not full
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isFull() {
        return numbers.size() >= bufferSize;
    }

    public boolean isEmpty() {
        return numbers.isEmpty();
    }
}