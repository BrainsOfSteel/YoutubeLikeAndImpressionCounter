package com.YoutubeLikeCounter.YoutubeLike.service;

import com.YoutubeLikeCounter.YoutubeLike.model.YoutubeLike;
import com.YoutubeLikeCounter.YoutubeLike.repository.YoutubeLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class YoutubeService {

    @Autowired
    private YoutubeLikeRepository youtubeLikeRepository;

    @Transactional
    public YoutubeLike updateLikeCounter(String videoName, Integer value){
        YoutubeLike youtubeLike = youtubeLikeRepository.getVideoName(videoName);
        if(youtubeLike == null){
            youtubeLike = new YoutubeLike();
            youtubeLike.setName(videoName);
            youtubeLike.setCount((long)value);
        }
        else {
            youtubeLike.setCount(value + youtubeLike.getCount());
        }
        return youtubeLikeRepository.save(youtubeLike);
    }
}
