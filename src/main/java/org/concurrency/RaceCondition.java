package org.concurrency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Counter {
    private ThreadLocal<Integer> threadLocal = new ThreadLocal<>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public void incAndGet() {
        int current = threadLocal.get();
        threadLocal.set(current + 1);
    }

    public int getCount() {
        return threadLocal.get();
    }
}

public class RaceCondition {
    public static void main(String[] args) {
        Counter counter = new Counter();
        Thread thread = new Thread(() -> runnable(counter));
        Thread thread1 = new Thread(() -> runnable(counter));
        thread.start();
        thread1.start();

        Map<String, String> map = new ConcurrentHashMap<>();
        Thread thread3 = new Thread(() -> runnable2(map));
        Thread thread4 = new Thread(() -> runnable2(map));
        thread3.start();
        thread4.start();
    }

    private static void runnable(Counter counter) {
        for (int i = 0; i < 1_000_000; i++) {
            counter.incAndGet();
        }
        System.out.println(counter.getCount());
    }

    private static void runnable2(Map<String, String> sharedMap) {
        for (int i = 0; i < 1_000_000; i++) {
            if (sharedMap.containsKey("key")) {
                String val = sharedMap.remove("key");
                if (val == null)
                    System.out.println("Iteration " + i + " : Value for 'key' was null");
            } else {
                sharedMap.put("key", "value");
            }
        }
    }
}
