package com.et.ai.policy_agent_system.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PolicyTaskGenerationService {

    static final String API_URL = "https://api.example-llm.com/v1/generate";
    private static final String API_KEY = "mock-api-key";

    private final RestTemplate restTemplate;

    public PolicyTaskGenerationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    PolicyTaskGenerationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateTasksFromPolicy(String policyText) {
        String prompt = buildPrompt(policyText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, Object> body = new HashMap<>();
        body.put("prompt", prompt);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);

        return response.getBody();
    }

    private String buildPrompt(String policyText) {
        return "Convert the policy text into structured tasks. "
                + "Return JSON array with: taskName, role, deadline, priority. "
                + "Policy text: " + policyText;
    }
}

