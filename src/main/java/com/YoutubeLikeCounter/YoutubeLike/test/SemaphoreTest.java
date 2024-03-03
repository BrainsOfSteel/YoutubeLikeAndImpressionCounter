package com.YoutubeLikeCounter.YoutubeLike.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

enum Downloader{
    INSTANCE;

    private Semaphore semaphore = new Semaphore(4);

    public void downloadData() throws InterruptedException {
        try{
            semaphore.acquire();
            System.out.println("Downloading data....");
            Thread.sleep(4000);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            semaphore.release();
        }
    }

}

public class SemaphoreTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        for(int i =0;i<12;i++){
            service.execute(() -> {
                try {
                    Downloader.INSTANCE.downloadData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
