package javapro;

import java.util.ArrayList;
import java.util.List;

public class ProducerConsumerLab {

    static class SharedBuffer {
        private final List<Integer> buffer = new ArrayList<>();
        private final int capacity;

        public SharedBuffer(int capacity) {
            this.capacity = capacity;
            System.out.println("Buffer created with capacity: " + capacity);
        }

        public synchronized void produce(int value) throws InterruptedException {
            while (buffer.size() >= capacity) {
                System.out.println("[Producer] Buffer FULL - waiting...");
                wait();
            }
            buffer.add(value);
            System.out.println("[Producer] Produced: " + value + " | Buffer: " + buffer);
            notifyAll();
        }

        public synchronized int consume() throws InterruptedException {
            while (buffer.isEmpty()) {
                System.out.println("[Consumer] Buffer EMPTY - waiting...");
                wait();
            }
            int val = buffer.remove(0);
            System.out.println("[Consumer] Consumed: " + val + " | Buffer: " + buffer);
            notifyAll();
            return val;
        }
    }

    static class Producer implements Runnable {
        private final SharedBuffer buffer;

        public Producer(SharedBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.produce(i);
                }
                System.out.println("[Producer] finished producing 10 items");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {
        private final SharedBuffer buffer;

        public Consumer(SharedBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.consume();
                }
                System.out.println("[Consumer] finished consuming 10 items");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SharedBuffer buffer = new SharedBuffer(5);

        Thread producer = new Thread(new Producer(buffer));
        Thread consumer = new Thread(new Consumer(buffer));

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("All threads completed successfully!");
    }
}
