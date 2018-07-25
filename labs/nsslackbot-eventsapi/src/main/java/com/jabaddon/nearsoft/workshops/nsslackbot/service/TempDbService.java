package com.jabaddon.nearsoft.workshops.nsslackbot.service;

import org.springframework.stereotype.Service;

@Service
public class TempDbService {
    private BotInfo botInfo;

    public void saveBotInfo(BotInfo botInfo) {
        this.botInfo = botInfo;
    }

    public BotInfo getBotInfo() {
        return botInfo;
    }
}
