package com.project.parsing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public ApplicationService(RestTemplate restTemplate, ObjectMapper objectMapper){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    public List<String> fetchDataFromURL(String URL) throws JsonProcessingException {
        List<String> result = new ArrayList<>();
        String jsonResponse = restTemplate.getForObject(URL, String.class);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        result.add(String.valueOf(rootNode.get(0).get("buy").asDouble()));
        result.add(String.valueOf(rootNode.get(1).get("buy").asDouble()));
        return result;
    }
}
