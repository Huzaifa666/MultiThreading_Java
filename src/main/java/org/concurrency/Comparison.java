package org.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class SynchronizedCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}

class AtomicCounter {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}

public class Comparison {
    private static final int THREAD_COUNT = 100;

    public static void main(String[] args) {
        ExecutorService executorSync = Executors.newFixedThreadPool(THREAD_COUNT);
        SynchronizedCounter counter = new SynchronizedCounter();
        long startSync = System.nanoTime();
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorSync.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
        }

        executorSync.shutdown();
        try {
            executorSync.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endSync = System.nanoTime();

        ExecutorService executorAtomic = Executors.newFixedThreadPool(THREAD_COUNT);
        long startAtomic = System.nanoTime();
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorAtomic.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
        }
        try {
            executorAtomic.shutdown();
            executorAtomic.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endAtomic = System.nanoTime();
        System.out.println("Synchronized Count : " + counter.getCount());
        System.out.println("Atomic Count : " + counter.getCount());
        System.out.println("Synchronized Time Taken : " + (endSync - startSync) / 1_000_000);
        System.out.println("Atomic Time Taken : " + (endAtomic - startAtomic) / 1_000_000);

    }
}
