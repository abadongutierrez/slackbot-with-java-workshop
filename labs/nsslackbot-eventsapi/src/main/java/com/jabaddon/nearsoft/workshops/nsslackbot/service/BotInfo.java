package com.jabaddon.nearsoft.workshops.nsslackbot.service;

public class BotInfo {
    private final String botUserId;
    private final String botAccessToken;

    public BotInfo(String botUserId, String botAccessToken) {
        this.botUserId = botUserId;
        this.botAccessToken = botAccessToken;
    }

    public String getBotUserId() {
        return botUserId;
    }

    public String getBotAccessToken() {
        return botAccessToken;
    }
}
