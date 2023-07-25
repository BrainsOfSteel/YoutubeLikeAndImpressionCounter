package com.YoutubeLikeCounter.YoutubeLike.repository;

import com.YoutubeLikeCounter.YoutubeLike.model.YoutubeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface YoutubeLikeRepository extends JpaRepository<YoutubeLike, Long> {
    @Query(value = "select id, video_name, count from youtube_like where video_name =?1 FOR UPDATE" , nativeQuery = true)
    YoutubeLike getVideoName(String name);

}
