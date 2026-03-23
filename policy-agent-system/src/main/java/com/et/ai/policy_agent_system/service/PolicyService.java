package com.et.ai.policy_agent_system.service;

import com.et.ai.policy_agent_system.agent.ExecutionAgent;
import com.et.ai.policy_agent_system.agent.MonitoringAgent;
import com.et.ai.policy_agent_system.agent.ParserAgent;
import com.et.ai.policy_agent_system.agent.PlannerAgent;
import com.et.ai.policy_agent_system.agent.RecoveryAgent;
import com.et.ai.policy_agent_system.dto.PolicyResponse;
import com.et.ai.policy_agent_system.dto.TaskResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PolicyService {

    private final ParserAgent parserAgent;
    private final PlannerAgent plannerAgent;
    private final ExecutionAgent executionAgent;
    private final MonitoringAgent monitoringAgent;
    private final RecoveryAgent recoveryAgent;

    public PolicyService(
            ParserAgent parserAgent,
            PlannerAgent plannerAgent,
            ExecutionAgent executionAgent,
            MonitoringAgent monitoringAgent,
            RecoveryAgent recoveryAgent) {
        this.parserAgent = parserAgent;
        this.plannerAgent = plannerAgent;
        this.executionAgent = executionAgent;
        this.monitoringAgent = monitoringAgent;
        this.recoveryAgent = recoveryAgent;
    }

    public PolicyResponse processPolicy(String policyText) {
        List<String> logs = new ArrayList<>();

        List<TaskResponse> tasks = parserAgent.parseTasksFromPolicy(policyText);
        logs.add("Parsed tasks: " + safeSize(tasks));

        tasks = plannerAgent.applyDefaultStatus(tasks);
        logs.add("Applied default status to tasks.");

        tasks = executionAgent.simulateExecution(tasks);
        logs.add("Simulated task execution.");

        List<TaskResponse> failedTasks = monitoringAgent.findFailedTasks(tasks);
        logs.add("Detected failed tasks: " + safeSize(failedTasks));

        PolicyResponse recoveryResult = recoveryAgent.recoverFailedTasks(tasks);
        List<String> recoveryLogs = recoveryResult.getLogs();
        if (recoveryLogs != null && !recoveryLogs.isEmpty()) {
            logs.addAll(recoveryLogs);
        }

        return new PolicyResponse(recoveryResult.getTasks(), logs);
    }

    private int safeSize(List<TaskResponse> tasks) {
        return tasks == null ? 0 : tasks.size();
    }
}
