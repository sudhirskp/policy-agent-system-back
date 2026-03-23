package com.et.ai.policy_agent_system.controller;

import com.et.ai.policy_agent_system.dto.PolicyProcessRequest;
import com.et.ai.policy_agent_system.dto.PolicyProcessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PolicyProcessingController {

    @PostMapping("/process-policy")
    public ResponseEntity<PolicyProcessResponse> processPolicy(@Valid @RequestBody PolicyProcessRequest request) {
        PolicyProcessResponse response = new PolicyProcessResponse("received");
        return ResponseEntity.ok(response);
    }
}

