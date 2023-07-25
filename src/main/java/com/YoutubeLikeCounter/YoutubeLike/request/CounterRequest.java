package com.YoutubeLikeCounter.YoutubeLike.request;

public class CounterRequest {
    private String videoName;

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public String toString() {
        return "CounterRequest{" +
                "videoName='" + videoName + '\'' +
                '}';
    }
}
