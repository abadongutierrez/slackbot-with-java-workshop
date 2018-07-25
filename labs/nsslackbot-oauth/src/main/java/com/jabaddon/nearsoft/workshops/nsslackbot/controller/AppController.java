package com.jabaddon.nearsoft.workshops.nsslackbot.controller;

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

        // TODO Read https://api.slack.com/methods/oauth.access and implement that call
        RestTemplate restTemplate = new RestTemplate();

        // TODO Add headers and params
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://slack.com/api/oauth.access", request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getBody());
        // TODO read json to get bot_access_token
        String botAccessToken = "";
        LOGGER.debug("Bot Access Token: {}", botAccessToken);

        return new ModelAndView("index", model);
    }
}
