package com.code.running;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenhaowei
 * @date 2021.05.27
 */
public class ProducerDemo4 {

    public static void main(String[] args) {

        Buffer4 buffer4 = new Buffer4();

        new Thread(new Producer4(buffer4)).start();
        new Thread(new Consumer4(buffer4)).start();

    }
}

class Buffer4 {
    private List<String> queue;

    private int size;


    private final Lock lock = new ReentrantLock();

    // 读线程条件
    private final Condition notEmpty = lock.newCondition();

    // 写线程条件
    private final Condition notFull = lock.newCondition();

    public Buffer4() {
        this(10);
    }

    public Buffer4(int size) {
        this.size = size;
        queue = new ArrayList<String>();
    }

    public void put() throws InterruptedException {
        lock.lock();

        try {
          while(true) {
              if (queue.size() == size) {
                  System.out.println("生产者已满" + Thread.currentThread().getName() + "等待中");
                  Thread.sleep(100);
                  notFull.await();
              }

              queue.add("xxx");

              System.out.println("生产者生成元素" + Thread.currentThread().getName() + "   大小： " + queue.size());

              notEmpty.signal();
          }
        } finally {
            lock.unlock();
        }
    }

    public void take() throws InterruptedException {
        lock.lock();

        try {
           while(true) {
               if (queue.size() == 0) {
                   System.out.println("消费者没有可消费数据" + Thread.currentThread().getName() + "等待中");
                   Thread.sleep(100);
                   notEmpty.await();
               }
               System.out.println("消费者队列大小   " + queue.size());
               queue.remove(queue.size() - 1);

               System.out.println("消费者消费元素" + Thread.currentThread().getName() + "   大小： " + queue.size());

               notFull.signal();
           }
        } finally {
            lock.unlock();
        }
    }
}

class Producer4 implements Runnable {

    private Buffer4 buffer;

    public Producer4(Buffer4 buffer) {
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

class Consumer4 implements Runnable {

    private Buffer4 buffer;

    public Consumer4(Buffer4 buffer) {
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


