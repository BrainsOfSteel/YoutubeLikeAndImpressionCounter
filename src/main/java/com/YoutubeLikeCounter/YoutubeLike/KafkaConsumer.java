package com.YoutubeLikeCounter.YoutubeLike;


import com.YoutubeLikeCounter.YoutubeLike.Utils.Util;
import com.YoutubeLikeCounter.YoutubeLike.model.HistoricalHyperloglog;
import com.YoutubeLikeCounter.YoutubeLike.service.HyperLogLogService;
import com.YoutubeLikeCounter.YoutubeLike.repository.HistoricalHyperlogRepository;
import com.YoutubeLikeCounter.YoutubeLike.request.CountViewRequest;
import com.YoutubeLikeCounter.YoutubeLike.request.CounterRequest;
import com.YoutubeLikeCounter.YoutubeLike.service.YoutubeService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private Gson gson = new Gson();

    @Autowired
    private YoutubeService youtubeService;

    @Autowired
    private HyperLogLogService redisService;

    @Autowired
    private HistoricalHyperlogRepository historicalHyperlogRepository;

    @KafkaListener(topics = AppConstants.TOPIC_NAME,
            groupId = AppConstants.GROUP_ID)
    public void consume(String message){
        LOGGER.info(String.format("Message received -> %s", message));
    }


    @KafkaListener(topics = AppConstants.YOUTUBE_TOPIC_NAME, containerFactory = "kafkaListenerContainerFactory")
    public void consumeLikeMessage(List<String> messages){
        LOGGER.info(String.format("Message received from youtube topic -> %s", messages));
        Map<String, Integer> videoNameVsCount = new HashMap<>();
        for(String msg : messages){
            CounterRequest request = gson.fromJson(msg, CounterRequest.class);
            videoNameVsCount.computeIfAbsent(request.getVideoName(), k->0);
            videoNameVsCount.put(request.getVideoName(), videoNameVsCount.get(request.getVideoName())+1);
        }

        LOGGER.info(String.format("batch size -> %s", videoNameVsCount));
        for(Map.Entry<String, Integer> entry : videoNameVsCount.entrySet()){
            youtubeService.updateLikeCounter(entry.getKey(), entry.getValue());
        }
    }

    @KafkaListener(topics = AppConstants.YOUTUBE_COUNT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void consumeIpCountMessage(List<String> messages){
        LOGGER.info(String.format("Message received from youtube ip count topic -> %s", messages));
        Map<String, Integer> ipVsCount = new HashMap<>();
        for(String msg : messages){
            CountViewRequest request = gson.fromJson(msg, CountViewRequest.class);
            ipVsCount.computeIfAbsent(request.getUserIp(), k-> 0);
            ipVsCount.put(request.getUserIp(), ipVsCount.get(request.getUserIp())+1);
        }
        LOGGER.info(String.format("batch size ip count-> %s", ipVsCount));
        String database = String.valueOf(System.currentTimeMillis() / Util.MILLIS_TO_MINUTES);
        try{
            HistoricalHyperloglog historicalHyperloglog = historicalHyperlogRepository.findByRedisKey(Util.REDIS_KEY+database);
            if(historicalHyperloglog == null){
                historicalHyperloglog = new HistoricalHyperloglog();
                historicalHyperloglog.setRedisKey(Util.REDIS_KEY+database);
                historicalHyperloglog.setMinutesElapsed(Long.parseLong(database));
                historicalHyperlogRepository.save(historicalHyperloglog);
            }

        }catch (Exception e){
            LOGGER.error("Unable to add new key -> "+(Util.REDIS_KEY+database));
            e.printStackTrace();
        }
        redisService.processElements(Util.REDIS_KEY+database, new ArrayList<>(ipVsCount.keySet()));
    }
}