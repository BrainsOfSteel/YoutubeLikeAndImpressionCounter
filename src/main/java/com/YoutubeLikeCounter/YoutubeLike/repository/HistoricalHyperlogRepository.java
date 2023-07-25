package com.YoutubeLikeCounter.YoutubeLike.repository;

import com.YoutubeLikeCounter.YoutubeLike.model.HistoricalHyperloglog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoricalHyperlogRepository extends JpaRepository<HistoricalHyperloglog, Long> {

    @Query(value = "select id, redis_key, value, minutes_elapsed from historical_hyperloglog where value is null order by id limit 1" , nativeQuery = true)
    HistoricalHyperloglog findOldestNonPopulatedEntry();

    HistoricalHyperloglog findByRedisKey(String key);

    List<HistoricalHyperloglog> findByMinutesElapsedBetween(long startTime, long endTime);
}
