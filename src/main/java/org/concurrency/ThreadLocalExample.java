package org.concurrency;

import java.util.concurrent.TimeUnit;

public class ThreadLocalExample {
    public static void main(String[] args) {
//        ThreadLocal<Integer> threadLocal = new ThreadLocal<>() {
//            @Override
//            protected Integer initialValue() {
//                return 1;
//            }
//        };
//
//        Thread thread = new Thread(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("Thread :- " + threadLocal.get());
//        });
//        thread.start();
//
//        Thread thread2 = new Thread(() -> {
//            threadLocal.set(2);
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("Thread :- " + threadLocal.get());
//        });
//        thread2.start();

        ThreadLocal<String> value = new ThreadLocal<>();
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

        Thread parentThread = new Thread(() -> {
            value.set("parent");
            inheritableThreadLocal.set("inherited parent");

            System.out.println(value.get());
            Thread childThread = new Thread(() -> {
                System.out.println(value.get());
                System.out.println(inheritableThreadLocal.get());
            });
            childThread.start();
        });
    parentThread.start();
    }
}
