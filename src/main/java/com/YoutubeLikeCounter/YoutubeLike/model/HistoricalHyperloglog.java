package com.YoutubeLikeCounter.YoutubeLike.model;

import javax.persistence.*;

@Entity
@Table(name = "historical_hyperloglog")
public class HistoricalHyperloglog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "redis_key")
    private String redisKey;

    @Column(name = "value")
    private byte[] value;

    @Column(name = "minutes_elapsed")
    private long minutesElapsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public long getMinutesElapsed() {
        return minutesElapsed;
    }

    public void setMinutesElapsed(long minutesElapsed) {
        this.minutesElapsed = minutesElapsed;
    }

    @Override
    public String toString() {
        return "HistoricalHyperloglog{" +
                "id=" + id +
                ", key='" + redisKey + '\'' +
                ", value='" + value + '\'' +
                '}';
    }


}
