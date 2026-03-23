package com.et.ai.policy_agent_system.service;

import com.et.ai.policy_agent_system.agent.ExecutionAgent;
import com.et.ai.policy_agent_system.agent.MonitoringAgent;
import com.et.ai.policy_agent_system.agent.ParserAgent;
import com.et.ai.policy_agent_system.agent.PlannerAgent;
import com.et.ai.policy_agent_system.agent.RecoveryAgent;
import com.et.ai.policy_agent_system.dto.PolicyResponse;
import com.et.ai.policy_agent_system.dto.TaskResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PolicyServiceTest {

    @Test
    void processPolicy_runsFullFlowAndReturnsLogs() {
        List<TaskResponse> tasks = Arrays.asList(
                new TaskResponse("Draft memo", "Analyst", "2025-01-01", "HIGH", null),
                new TaskResponse("Review policy", null, "2025-02-01", "MEDIUM", null)
        );

        ParserAgent parserAgent = new StubParserAgent(tasks);
        PlannerAgent plannerAgent = new PlannerAgent();
        ExecutionAgent executionAgent = new StubExecutionAgent();
        MonitoringAgent monitoringAgent = new MonitoringAgent();
        RecoveryAgent recoveryAgent = new RecoveryAgent();

        PolicyService service = new PolicyService(
                parserAgent,
                plannerAgent,
                executionAgent,
                monitoringAgent,
                recoveryAgent
        );

        PolicyResponse response = service.processPolicy("Sample policy text");

        assertNotNull(response);
        assertEquals(2, response.getTasks().size());
        assertEquals("COMPLETED", response.getTasks().get(0).getStatus());
        assertEquals("REASSIGNED", response.getTasks().get(1).getStatus());
        assertEquals(5, response.getLogs().size());
    }

    private static class StubParserAgent extends ParserAgent {

        private final List<TaskResponse> tasks;

        StubParserAgent(List<TaskResponse> tasks) {
            super(null, new ObjectMapper());
            this.tasks = tasks;
        }

        @Override
        public List<TaskResponse> parseTasksFromPolicy(String policyText) {
            return tasks;
        }
    }

    private static class StubExecutionAgent extends ExecutionAgent {

        @Override
        public List<TaskResponse> simulateExecution(List<TaskResponse> tasks) {
            if (tasks == null || tasks.size() < 2) {
                return tasks;
            }

            tasks.get(0).setStatus("COMPLETED");
            tasks.get(1).setStatus("FAILED");
            return tasks;
        }
    }
}

