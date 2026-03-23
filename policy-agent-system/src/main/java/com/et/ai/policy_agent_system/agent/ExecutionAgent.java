package com.et.ai.policy_agent_system.agent;

import com.et.ai.policy_agent_system.dto.TaskResponse;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class ExecutionAgent {

    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_FAILED = "FAILED";

    public List<TaskResponse> simulateExecution(List<TaskResponse> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return tasks;
        }

        for (TaskResponse task : tasks) {
            if (task != null) {
                task.setStatus(STATUS_COMPLETED);
            }
        }

        int failedIndex = ThreadLocalRandom.current().nextInt(tasks.size());
        TaskResponse failedTask = tasks.get(failedIndex);
        if (failedTask != null) {
            failedTask.setStatus(STATUS_FAILED);
        }

        return tasks;
    }
}

