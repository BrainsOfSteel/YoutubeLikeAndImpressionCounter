package com.YoutubeLikeCounter.YoutubeLike;

import com.YoutubeLikeCounter.YoutubeLike.Utils.Util;
import com.YoutubeLikeCounter.YoutubeLike.service.HistoricalHyperloglogService;
import com.YoutubeLikeCounter.YoutubeLike.service.HyperLogLogService;
import com.YoutubeLikeCounter.YoutubeLike.request.CountViewRequest;
import com.YoutubeLikeCounter.YoutubeLike.request.CounterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaProducerController {

    private KafkaProducer kafkaProducer;

    @Autowired
    private HyperLogLogService hyperLogLogService;

    @Autowired
    private HistoricalHyperloglogService historicalHyperloglogService;

    public KafkaProducerController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/publish")
    public ResponseEntity<String> publish(@RequestParam("message") String message){
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to kafka topic");
    }

    @GetMapping("/publishLikeMessage")
    public ResponseEntity<String> publishLikeMessage(@RequestBody CounterRequest request){
        kafkaProducer.sendMessageLikeCounter(request);
        return ResponseEntity.ok("Message sent to youtube kafka topic");
    }

    @GetMapping("/publishIpCountMessage")
    public ResponseEntity<String> publishIpCountMessage(@RequestBody CountViewRequest request){
        kafkaProducer.sendUniqueIpMessage(request);
        return ResponseEntity.ok("Message sent to youtube count kafka topic");
    }

    @GetMapping("/publishIpCountMessageGet")
    public ResponseEntity<String> publishIpCountMessageForGet(@RequestParam("uniqueIp") String uniqueIp){
        kafkaProducer.sendUniqueIpMessageFromGet(uniqueIp);
        return ResponseEntity.ok("Message sent to youtube count kafka topic");
    }

    @GetMapping("/getDistinctCount")
    public ResponseEntity<String> getDistinctViewCount(){
        return ResponseEntity.ok(String.valueOf(hyperLogLogService.getApproximateCount(Util.REDIS_KEY)));
    }

    @GetMapping("/getTotalDistinctViewInTimeRange")
    public ResponseEntity<String> getTotalViewsInTimeRange(@RequestParam("startTime")Long startTime, @RequestParam("endTime") Long endTime){
        return ResponseEntity.ok(String.valueOf(hyperLogLogService.getApproximateCountInTimeRange(startTime, endTime)));
    }
}