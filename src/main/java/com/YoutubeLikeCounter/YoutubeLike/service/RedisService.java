package com.YoutubeLikeCounter.YoutubeLike.service;

import com.YoutubeLikeCounter.YoutubeLike.Utils.Util;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public byte[] getValueDumpFromKey(String key){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.getOperations().dump(key);
    }

    public void addToHyperLogLog(String key, String... values) {
        HyperLogLogOperations<String, String> hyperLogLogOps = redisTemplate.opsForHyperLogLog();
        hyperLogLogOps.add(key, values);
        redisTemplate.expire(key, Util.MINUTES_IN_DAY, TimeUnit.MINUTES);
    }

    public long getHyperLogLogCountForKeys(String ... key) {
        HyperLogLogOperations<String, String> hyperLogLogOps = redisTemplate.opsForHyperLogLog();
        return hyperLogLogOps.size(key);
    }

    public void setKeyValueHyperloglog(String testKey, byte[] value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.getOperations().restore(testKey, value, 5, TimeUnit.MINUTES, true);
    }

    private static String staticVar = "static variable";

    public static void main(String[] args) throws Exception {
        DoStuff stuff = () -> System.out.println("from work1");
        stuff.work1();
        int i = 0;
        RaiseToPower raiseToPower = (int x, int y) -> {
            staticVar = "new var";
            return (int) Math.pow(x, y);
        };
        System.out.println("power raised = "+ raiseToPower.raiseToN(2,10));
        System.out.println("staticVar="+staticVar);

        RedisService redisService = new RedisService(null);
        redisService.   getWorking();

    }

    public void getWorking() throws Exception {
        compute(() -> "done");
    }

    void compute(Runnable r) {
        System.out.println("Runnable invoked");
        r.run();
    }

    <T> void compute(Callable<T> c) throws Exception {
        System.out.println("Callable invoked");
        c.call();
    }
    interface DoStuff{
        void work1();
        default void work2(){};
    }


    interface RaiseToPower{
        int raiseToN(int x, int n);
    }
}
