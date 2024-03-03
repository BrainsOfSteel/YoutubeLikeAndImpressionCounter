package com.YoutubeLikeCounter.YoutubeLike.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class TempRunnable implements Runnable{

    @Override
    public void run() {
        while(true){
            try {
                System.out.println("From thread = "+Thread.currentThread().getName());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Received interrupted exception");
                break;
            }
        }
    }
}

class TempCallable implements Callable<String> {
    int id;

    public TempCallable(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        System.out.println("callable = "+id);
        return "Id: "+id;
    }
}

public class ThreadPool {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<String>> futures = new ArrayList<>();
        for(int i=0;i<12;i++){
            futures.add(executorService.submit(new TempCallable(i)));
        }

        for(Future<String> f : futures){
            System.out.println(f.get());
        }
        executorService.shutdownNow();
    }
}
