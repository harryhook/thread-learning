package com.code.running;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DiscardPolicyDemo {


    public static void main(String[] args) throws InterruptedException {

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            tasks.add(() -> {
                System.out.println("callable " + finalI);
                Thread.sleep(500);
                return null;
            });
        }

        ExecutorService executor = new ThreadPoolExecutor(
                1,
                1,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1),
                new ThreadPoolExecutor.DiscardPolicy());
        Thread executorInvokerThread = new Thread(() -> {
            try {
                List<Future<Void>> list = executor.invokeAll(tasks);
                System.out.println("list size: " + list.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("invokeAll returned");
        });
        executorInvokerThread.start();
        Thread.sleep(100);
        System.out.println("shutdownnow start");
        List<Runnable> list = executor.shutdownNow();
        for (Runnable runnable : list) {
            if (runnable instanceof Future<?>) {
                ((Future<?>) runnable).cancel(true);
            }
        }
        System.out.println("shutdownnow returned, size:" + list.size());
    }
}
