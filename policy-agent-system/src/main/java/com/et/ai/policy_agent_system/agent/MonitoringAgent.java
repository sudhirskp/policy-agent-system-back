package com.et.ai.policy_agent_system.agent;

import com.et.ai.policy_agent_system.dto.TaskResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MonitoringAgent {

    private static final String STATUS_FAILED = "FAILED";

    public List<TaskResponse> findFailedTasks(List<TaskResponse> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyList();
        }

        List<TaskResponse> failedTasks = new ArrayList<>();
        for (TaskResponse task : tasks) {
            if (task != null && STATUS_FAILED.equalsIgnoreCase(task.getStatus())) {
                failedTasks.add(task);
            }
        }

        return failedTasks;
    }
}

