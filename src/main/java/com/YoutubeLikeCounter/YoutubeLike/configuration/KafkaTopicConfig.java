package com.YoutubeLikeCounter.YoutubeLike.configuration;

import com.YoutubeLikeCounter.YoutubeLike.AppConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic javaguidesTopic(){
        return TopicBuilder.name("javaguides")
                .build();
    }

    @Bean
    public NewTopic youtubeTopicGuides(){
        return TopicBuilder.name(AppConstants.YOUTUBE_TOPIC_NAME)
                .build();
    }

    @Bean
    public NewTopic youtubeCountTopicGuides(){
        return TopicBuilder.name(AppConstants.YOUTUBE_COUNT_TOPIC)
                .build();
    }
}