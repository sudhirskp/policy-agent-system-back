package com.et.ai.policy_agent_system.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyResponse {
    private List<TaskResponse> tasks;
    private List<String> logs;
}

