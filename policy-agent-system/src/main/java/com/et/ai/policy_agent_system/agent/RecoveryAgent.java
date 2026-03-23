package com.et.ai.policy_agent_system.agent;

import com.et.ai.policy_agent_system.dto.PolicyResponse;
import com.et.ai.policy_agent_system.dto.TaskResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RecoveryAgent {

    private static final String STATUS_FAILED = "FAILED";
    private static final String STATUS_REASSIGNED = "REASSIGNED";
    private static final String DEFAULT_ROLE = "UNASSIGNED";

    public PolicyResponse recoverFailedTasks(List<TaskResponse> tasks) {
        List<String> logs = new ArrayList<>();
        if (tasks == null || tasks.isEmpty()) {
            return new PolicyResponse(tasks, logs);
        }

        for (TaskResponse task : tasks) {
            if (task == null || !STATUS_FAILED.equalsIgnoreCase(task.getStatus())) {
                continue;
            }

            task.setStatus(STATUS_REASSIGNED);

            if (task.getRole() == null || task.getRole().isBlank()) {
                String newRole = DEFAULT_ROLE;
                task.setRole(newRole);
                logs.add(buildRoleLog(task, newRole));
            } else {
                String newDeadline = extendDeadline(task.getDeadline());
                task.setDeadline(newDeadline);
                logs.add(buildDeadlineLog(task, newDeadline));
            }
        }

        return new PolicyResponse(tasks, logs);
    }

    private String extendDeadline(String deadline) {
        if (deadline == null || deadline.isBlank()) {
            return "TBD (extended)";
        }

        return deadline + " (extended)";
    }

    private String buildRoleLog(TaskResponse task, String newRole) {
        String taskName = task.getTaskName() == null ? "(unknown)" : task.getTaskName();
        return "Task '" + taskName + "' failed; reassigned role to " + newRole + ".";
    }

    private String buildDeadlineLog(TaskResponse task, String newDeadline) {
        String taskName = task.getTaskName() == null ? "(unknown)" : task.getTaskName();
        return "Task '" + taskName + "' failed; deadline extended to " + newDeadline + ".";
    }
}

