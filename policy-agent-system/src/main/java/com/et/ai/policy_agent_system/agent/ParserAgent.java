package com.et.ai.policy_agent_system.agent;

import com.et.ai.policy_agent_system.dto.TaskResponse;
import com.et.ai.policy_agent_system.service.PolicyTaskGenerationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ParserAgent {

    private final PolicyTaskGenerationService policyTaskGenerationService;
    private final ObjectMapper objectMapper;

    public ParserAgent(PolicyTaskGenerationService policyTaskGenerationService, ObjectMapper objectMapper) {
        this.policyTaskGenerationService = policyTaskGenerationService;
        this.objectMapper = objectMapper;
    }

    public List<TaskResponse> parseTasksFromPolicy(String policyText) {
        String rawResponse = policyTaskGenerationService.generateTasksFromPolicy(policyText);
        if (rawResponse == null || rawResponse.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(rawResponse, new TypeReference<List<TaskResponse>>() {});
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to parse LLM response into tasks", ex);
        }
    }
}

