package com.YoutubeLikeCounter.YoutubeLike.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestProducer {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private List<Integer> list = new ArrayList<>();
    private int UPPER_LIMIT=5;
    private int LO_LIMIT = 0;
    private Random random = new Random();

    public void produce() throws InterruptedException {
        while(true) {
            lock.lock();
            try {
                if(list.size() >= UPPER_LIMIT){
                    condition.await();
                }
                else{
                    int num = random.nextInt();
                    list.add(num);
                    System.out.println("Produced..." + num);
                    Thread.sleep(1000);
                    condition.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }
    public void consume() throws InterruptedException{
        while(true) {
            lock.lock();
            try {
                if(list.size() <= LO_LIMIT){
                    condition.await();
                }
                else{
                    int num = list.remove(list.size()-1);
                    System.out.println("Consumer..." + num);
                    Thread.sleep(100);
                    condition.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        TestProducer producer = new TestProducer();
        Thread t1 = new Thread(() -> {
            try {
                producer.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                producer.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
    }
}
