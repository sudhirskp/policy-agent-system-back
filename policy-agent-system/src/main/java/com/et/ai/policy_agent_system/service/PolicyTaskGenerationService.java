package com.et.ai.policy_agent_system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PolicyTaskGenerationService {

    static final String API_URL = "https://api.mistral.ai/v1/chat/completions";
    private static final String DEFAULT_API_KEY = "";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    @Autowired
    public PolicyTaskGenerationService(
            RestTemplateBuilder restTemplateBuilder,
            ObjectMapper objectMapper,
            @Value("${spring.ai.mistralai.api-key:${mistral.api.key:" + DEFAULT_API_KEY + "}}") String apiKey) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }



    PolicyTaskGenerationService(RestTemplate restTemplate, ObjectMapper objectMapper, String apiKey) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public String generateTasksFromPolicy(String policyText) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Mistral API key is missing. Set spring.ai.mistralai.api-key or MISTRAL_API_KEY.");
        }

        String prompt = buildPrompt(policyText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "mistral-small-latest");
        body.put("messages", List.of(message));
        body.put("temperature", 0.2);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);

        return extractContent(response.getBody());
    }

    private String extractContent(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "";
        }

        String trimmed = responseBody.trim();
        if (trimmed.startsWith("[")) {
            return trimmed;
        }

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode contentNode = root.path("choices").path(0).path("message").path("content");
            return stripCodeFences(contentNode.asText(""));
        } catch (JsonProcessingException ex) {
            return "";
        }
    }

    private String stripCodeFences(String content) {
        if (content == null) {
            return "";
        }

        String trimmed = content.trim();
        if (!trimmed.startsWith("```")) {
            return trimmed;
        }

        int firstBreak = trimmed.indexOf('\n');
        int lastFence = trimmed.lastIndexOf("```");
        if (firstBreak >= 0 && lastFence > firstBreak) {
            return trimmed.substring(firstBreak + 1, lastFence).trim();
        }

        return trimmed.replace("```", "").trim();
    }

    private String buildPrompt(String policyText) {
        return "Convert the policy text into structured tasks. "
                + "Return JSON array with: taskName, role, deadline, priority. "
                + "Policy text: " + policyText;
    }
}
