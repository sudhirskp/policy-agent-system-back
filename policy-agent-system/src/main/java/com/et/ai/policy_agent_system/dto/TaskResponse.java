package com.et.ai.policy_agent_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String taskName;
    private String role;
    private String deadline;
    private String priority;
    private String status;
}

