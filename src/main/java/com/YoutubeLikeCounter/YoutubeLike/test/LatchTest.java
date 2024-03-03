package com.YoutubeLikeCounter.YoutubeLike.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class LatchRunner implements Runnable{
    private CountDownLatch latch;
    private int id;

    public LatchRunner(CountDownLatch latch, int id) {
        this.latch = latch;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Executed task = "+id);
        latch.countDown();
    }
}
public class LatchTest {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for(int i=0;i<5;i++){
            executorService.submit(new LatchRunner(latch, i));
        }
        try {
            latch.await();
            System.out.println("Completed execution....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
