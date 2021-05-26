package com.code.running;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author chenhaowei
 * @date 2021.05.20
 */
public class ProducerDemo1 {

    public static void main(String[] args) {

        List container = new ArrayList();


            Thread thread1 = new Thread(new Producer(container));
            thread1.setName("生产者线程_" );


            Thread thread2 = new Thread(new Consumer(container));
            thread2.setName("...消费者线程_" );

            thread1.start();
            thread2.start();

    }
}


class Producer implements Runnable {

    private List container;

    public Producer(List container) {
        this.container = container;
    }

    public void run() {
        while (true) {
            Random random = new Random();

            synchronized (container) {
                while (container.size() > 0) {
                    try {
                        Thread.sleep(100);
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                container.add(random.nextInt(100));
                System.out.println(Thread.currentThread().getName() + "  " + container.get(0));


                container.notifyAll();
            }
        }
    }
}

class Consumer implements Runnable {
    private List container;

    public Consumer(List container) {
        this.container = container;
    }

    public void run() {

        while (true) {
            synchronized (container) {
                while (container.size() < 1) {
                    try {
                        Thread.sleep(100);
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(Thread.currentThread().getName() + " " + container.remove(0));

                container.notifyAll();
            }
        }
    }
}

