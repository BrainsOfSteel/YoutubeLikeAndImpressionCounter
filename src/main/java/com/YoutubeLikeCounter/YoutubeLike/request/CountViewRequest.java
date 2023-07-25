package com.YoutubeLikeCounter.YoutubeLike.request;

public class CountViewRequest {
    private String userIp;

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    @Override
    public String toString() {
        return "CountViewRequest{" +
                "userIp='" + userIp + '\'' +
                '}';
    }
}
