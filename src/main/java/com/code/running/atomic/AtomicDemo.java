package com.code.running.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo implements Runnable {

    private AtomicInteger data = new AtomicInteger(0);
    private Integer intNum = 0;

    public static void main(final String[] args) {
        AtomicDemo atomicDemo = new AtomicDemo();
        for (int i = 0; i < 2000; i++) {
            new Thread(atomicDemo, "线程" + i).start();
        }
    }

    public void run() {
        int curr = data.incrementAndGet();
        intNum++;
        System.out.println(curr + ": " + intNum);
    }
}
