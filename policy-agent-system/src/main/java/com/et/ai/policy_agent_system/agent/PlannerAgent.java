package com.et.ai.policy_agent_system.agent;

import com.et.ai.policy_agent_system.dto.TaskResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PlannerAgent {

    private static final String DEFAULT_STATUS = "PENDING";

    public List<TaskResponse> applyDefaultStatus(List<TaskResponse> tasks) {
        if (tasks == null) {
            return null;
        }

        for (TaskResponse task : tasks) {
            if (task != null && (task.getStatus() == null || task.getStatus().isBlank())) {
                task.setStatus(DEFAULT_STATUS);
            }
        }

        return tasks;
    }
}

