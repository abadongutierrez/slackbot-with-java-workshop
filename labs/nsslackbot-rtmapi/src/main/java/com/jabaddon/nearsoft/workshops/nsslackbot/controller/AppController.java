package com.jabaddon.nearsoft.workshops.nsslackbot.controller;

import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

@Controller
public class AppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/")
    public ModelAndView index(Map<String, Object> model) {
        model.put("title", "Install Bot");
        model.put("client_id", System.getenv("CLIENT_ID"));

        return new ModelAndView("index", model);
    }

    @GetMapping("/redirect")
    public ModelAndView redirect(Map<String, Object> model, @RequestParam String code) throws IOException {
        model.put("title", "Install Bot");
        model.put("client_id", System.getenv("CLIENT_ID"));
        LOGGER.debug("Slack code: {}", code);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", System.getenv("CLIENT_ID"));
        map.add("client_secret", System.getenv("CLIENT_SECRET"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://slack.com/api/oauth.access", request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getBody());

        String botAccessToken = jsonResponse.get("bot").get("bot_access_token").asText();
        String botUserId = jsonResponse.get("bot").get("bot_user_id").asText();
        LOGGER.debug("Bot Access Token: {}", botAccessToken);

        // TODO Create a SlackRealTimeMessagingClient (look at https://github.com/allbegray/slack-api#slack-client-factory)
        // TODO add a Event.MESSAGE listener to your new SlackRealTimeMessagingClient and connect
        // TODO Create a SlackWebApiClient (look at https://github.com/allbegray/slack-api#slack-client-factory)
        // TODO When the bot receives a message from a non bot user post a message (using SlackWebApiClient) in the same channel saying "Hi, I'm a bot using RTM API."

        SlackRealTimeMessagingClient rtmClient = null;
        SlackWebApiClient webApiClient = null;

        return new ModelAndView("index", model);
    }

    // You can use this method to get the botUserId from the botId in the message
    private String getBotUserId(String botAccessToken, String messageBotId) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("botAccessToken", botAccessToken);
        map.add("bot", messageBotId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://slack.com/api/bots.info", request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getBody());
        return jsonResponse.path("bot").path("user_id").asText();
    }
}
