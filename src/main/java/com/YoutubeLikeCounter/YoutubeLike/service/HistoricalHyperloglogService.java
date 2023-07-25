package com.YoutubeLikeCounter.YoutubeLike.service;

import com.YoutubeLikeCounter.YoutubeLike.model.HistoricalHyperloglog;
import com.YoutubeLikeCounter.YoutubeLike.repository.HistoricalHyperlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HistoricalHyperloglogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalHyperloglogService.class);

    @Autowired
    private HistoricalHyperlogRepository repository;

    @Autowired
    private HyperLogLogService redisService;

    @Scheduled(fixedDelay = 30000)
    public void dumpHistoricalDataFromRedis(){
        LOGGER.info("Starting Hyperloglog dump process for redis");
        for(int i=1000; i>=0; i--) {
            HistoricalHyperloglog historicalHyperloglog = repository.findOldestNonPopulatedEntry();
            if(historicalHyperloglog == null){
                break;
            }
            historicalHyperloglog.setValue(redisService.getValueDumpFromKey(historicalHyperloglog.getRedisKey()));
            repository.save(historicalHyperloglog);
        }
    }
}
