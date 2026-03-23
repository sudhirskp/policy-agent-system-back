package com.et.ai.policy_agent_system.dto;

import jakarta.validation.constraints.NotBlank;

public record PolicyProcessRequest(@NotBlank String policyText) {
}

