package com.jabaddon.nearsoft.workshops.nsslackbot.controller;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabaddon.nearsoft.workshops.nsslackbot.service.BotInfo;
import com.jabaddon.nearsoft.workshops.nsslackbot.service.TempDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
public class SlackEventController {
    @Autowired
    private TempDbService tempDbService;

    @PostMapping(value = "/action-endpoint", produces = MediaType.TEXT_PLAIN_VALUE)
    public String slack(@RequestBody String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);

        JsonNode challengeNode = root.path("challenge");
        JsonNode typeNode = root.path("type");
        if (challengeNode != null && typeNode != null && "url_verification".equals(typeNode.asText())) {
            return challengeNode.asText();
        }

        JsonNode eventNode = root.get("event");
        if (eventNode != null) {
            // TODO get BotInfo from tempDbService
            // TODO Create a SlackWebApiClient (look at https://github.com/allbegray/slack-api#slack-client-factory)
            // TODO When message is from a non bot user post a message (using SlackWebApiClient) in the same channel saying "Hi, I'm a bot using Events API."
            BotInfo botInfo = null;
        }

        return "Ok";
    }

    // You can use this method to get the botUserId from the botId in the message
    private String getBotUserId(String botAccessToken, String messageBotId) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("token", botAccessToken);
        map.add("bot", messageBotId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://slack.com/api/bots.info", request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getBody());
        return jsonResponse.path("bot").path("user_id").asText();
    }
}
