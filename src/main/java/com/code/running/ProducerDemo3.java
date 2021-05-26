package com.code.running;

/**
 * @author chenhaowei
 * @date 2021.05.26
 */

public class ProducerDemo3 {
    public static void main(String[] args) {

        Buffer buffer = new Buffer();

        new Thread(new Producer3(buffer)).start();
        new Thread(new Consumer3(buffer)).start();
    }
}

class Buffer {

    private int count = 0;
    private int size = 10;

    public synchronized void put() {
        while (count == size) {
            try {
                System.out.println("[put thread] " + Thread.currentThread().getName() + " is waiting");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;

            System.out.println("[put thread] " + Thread.currentThread().getName() + " add 1, count is: " + count);
            this.notifyAll();
        }
    }

    public synchronized void take() {
        while (count == 0) {
            try {
                System.out.println("[take thread] " + Thread.currentThread().getName() + " is waiting");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count--;

            System.out.println("[take thread] " + Thread.currentThread().getName() + " remove 1, count is: " + count);
            this.notifyAll();
        }
    }
}


class Producer3 implements Runnable {

    private Buffer buffer;

    public Producer3(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            Thread.sleep(1000);
            buffer.put();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer3 implements Runnable {

    private Buffer buffer;

    public Consumer3(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            Thread.sleep(1000);
            buffer.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
