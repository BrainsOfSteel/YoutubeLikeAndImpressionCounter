package com.YoutubeLikeCounter.YoutubeLike;

import com.YoutubeLikeCounter.YoutubeLike.request.CountViewRequest;
import com.YoutubeLikeCounter.YoutubeLike.request.CounterRequest;
import com.google.gson.Gson;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    private Gson gson = new Gson();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message){
        LOGGER.info(String.format("Message sent -> %s", message));
        kafkaTemplate.send(AppConstants.TOPIC_NAME, message);
    }

    public void sendMessageLikeCounter(CounterRequest request){
        LOGGER.info("Counter request : "+ request);
        kafkaTemplate.send(AppConstants.YOUTUBE_TOPIC_NAME, gson.toJson(request));
    }

    public void sendUniqueIpMessage(CountViewRequest request){
        LOGGER.info("Count view request : "+request);
        kafkaTemplate.send(AppConstants.YOUTUBE_COUNT_TOPIC, gson.toJson(request));
    }

    public void sendUniqueIpMessageFromGet(String uniqueIp){
        LOGGER.info("Count view request : "+uniqueIp);
        CountViewRequest request = new CountViewRequest();
        request.setUserIp(uniqueIp);
        kafkaTemplate.send(AppConstants.YOUTUBE_COUNT_TOPIC, gson.toJson(request));
    }
}
