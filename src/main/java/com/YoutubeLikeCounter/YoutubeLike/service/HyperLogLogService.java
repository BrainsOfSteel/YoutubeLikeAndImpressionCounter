package com.YoutubeLikeCounter.YoutubeLike.service;

import com.YoutubeLikeCounter.YoutubeLike.Utils.Util;
import com.YoutubeLikeCounter.YoutubeLike.model.HistoricalHyperloglog;
import com.YoutubeLikeCounter.YoutubeLike.repository.HistoricalHyperlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HyperLogLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HyperLogLogService.class);

    private final RedisService redisService;

    public HyperLogLogService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    private HistoricalHyperlogRepository historicalHyperlogRepository;

    public void processElements(String key, List<String> elements) {
        // Add elements to the HyperLogLog
        redisService.addToHyperLogLog(key, elements.toArray(new String[0]));
        // Retrieve approximate distinct count
        long distinctCount = redisService.getHyperLogLogCountForKeys(key);
        System.out.println("Approximate distinct count: " + distinctCount);
    }

    public byte[] getValueDumpFromKey(String key){
        return redisService.getValueDumpFromKey(key);
    }

    public Long getApproximateCount(String ... keys){
        return redisService.getHyperLogLogCountForKeys(keys);
    }

    public Long getApproximateCountInTimeRange(Long startTime, Long endTime){
        if(startTime == null || endTime == null){
            return 0L;
        }
        if(startTime > endTime){
            return 0L;
        }

        long endMinutes = endTime / Util.MILLIS_TO_MINUTES;
        long startMinutes = startTime / Util.MILLIS_TO_MINUTES;
        List<HistoricalHyperloglog> persistentHyperlogslogs = new ArrayList<>();
        if(endMinutes - startMinutes > Util.MINUTES_IN_DAY){
            persistentHyperlogslogs = historicalHyperlogRepository.
                    findByMinutesElapsedBetween(startMinutes, endMinutes - Util.MINUTES_IN_DAY);
            if(persistentHyperlogslogs == null){
                persistentHyperlogslogs = new ArrayList<>();
            }
            startMinutes = endMinutes - Util.MINUTES_IN_DAY + 1;
        }
        dumpHistoricalHyperLogLog(persistentHyperlogslogs);

        LOGGER.info(String.format("Minutes to count %s", (endMinutes - startMinutes)));
        String [] keys = new String[(int) (endMinutes - startMinutes+1) + persistentHyperlogslogs.size()];
        int ind = 0;
        for(long minute = startMinutes; minute <= endMinutes; minute++){
            keys[(int) (minute - startMinutes)] = Util.REDIS_KEY+minute;
            ind++;
        }
        for(HistoricalHyperloglog historicalHyperloglog : persistentHyperlogslogs){
            keys[ind++] = historicalHyperloglog.getRedisKey();
        }
        return getApproximateCount(keys);
    }

    private void dumpHistoricalHyperLogLog(List<HistoricalHyperloglog> persistentHyperlogslogs) {
        if(persistentHyperlogslogs == null || persistentHyperlogslogs.size() == 0){
            return;
        }

        for(HistoricalHyperloglog historicalHyperloglog : persistentHyperlogslogs){
            redisService.setKeyValueHyperloglog(historicalHyperloglog.getRedisKey(), historicalHyperloglog.getValue());
        }
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        for(int i =0;i<100;i++){
            ResponseEntity<String> response
                    = restTemplate.getForEntity("http://localhost:8080" + "/api/v1/kafka/publishIpCountMessageGet?uniqueIp="+ UUID.randomUUID()
                    , String.class);
            System.out.println("inventory = "+ response.getBody());
        }
    }
}
