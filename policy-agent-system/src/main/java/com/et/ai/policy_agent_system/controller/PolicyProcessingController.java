package com.et.ai.policy_agent_system.controller;

import com.et.ai.policy_agent_system.dto.PolicyRequest;
import com.et.ai.policy_agent_system.dto.PolicyResponse;
import com.et.ai.policy_agent_system.service.PolicyService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PolicyProcessingController {

    private final PolicyService policyService;

    public PolicyProcessingController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("/process-policy")
    public ResponseEntity<PolicyResponse> processPolicy(@Valid @RequestBody PolicyRequest request) {
        try {
            PolicyResponse response = policyService.processPolicy(request.getPolicyText());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            PolicyResponse errorResponse = new PolicyResponse(null, List.of("Failed to process policy."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
