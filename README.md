# AI Multi-Agent Policy Execution System

Minimal Spring Boot app for policy-based task generation.

## Overview

This project implements an AI-powered multi-agent system that converts enterprise policies into structured tasks and executes them through an autonomous workflow pipeline.
The system simulates execution, detects failures, and applies recovery actions while maintaining a complete audit trail.

## Related repositories

- Frontend: https://github.com/sudhirskp/Policy-Agent-System-frontend


## Demo UI

**Input**

<img src="src/docs/images/input.png" alt="Policy Agent System Input" width="700" />

**Output**

<img src="src/docs/images/output.png" alt="Policy Agent System Output" width="700" />


## Architecture
**This diagram illustrates the multi-agent workflow including parsing, execution, monitoring, and recovery.**

<img src="src/docs/images/AI%20Multi-Agent%20Policy%20Execution%20System%20Architecture.png" alt="AI Multi-Agent Policy Execution System Architecture" width="700" />

For the full architecture writeup, see `ARCHITECTURE.md`.

## Execution Flow

1. `PolicyProcessingController` receives a policy text.
2. `PolicyService` orchestrates the pipeline.
3. `ParserAgent` calls the LLM service and parses tasks.
4. `PlannerAgent` sets default status to PENDING.
5. `ExecutionAgent` simulates execution and marks one task as FAILED.
6. `MonitoringAgent` detects failed tasks.
7. `RecoveryAgent` reassigns failed tasks and logs actions.

## Setup

### Prerequisites

- Java 17+
- Maven (or use the included `mvnw`/`mvnw.cmd`)

### Configuration

Set your Mistral API key using one of the following:

- `spring.ai.mistralai.api-key` in `application.properties`, or
- environment variable `MISTRAL_API_KEY`

Example (PowerShell):

```powershell
$env:MISTRAL_API_KEY = "your-key-here"
```

### Build

```powershell
D:\policy-agent-system\policy-agent-system\mvnw.cmd clean package
```

### Run

```powershell
D:\policy-agent-system\policy-agent-system\mvnw.cmd spring-boot:run
```

### Test

```powershell
D:\policy-agent-system\policy-agent-system\mvnw.cmd test
```


## Components

### Controller

- `src/main/java/com/et/ai/policy_agent_system/controller/PolicyProcessingController.java`
- Endpoint: `POST /api/process-policy`
- Input: `PolicyRequest`
- Output: `PolicyResponse`

### Service

- `src/main/java/com/et/ai/policy_agent_system/service/PolicyService.java`
- Orchestrates agent flow and aggregates logs.

### LLM service

- `src/main/java/com/et/ai/policy_agent_system/service/PolicyTaskGenerationService.java`
- Calls Mistral chat completions endpoint.
- Builds a prompt that asks for a JSON array of tasks.
- Uses a mocked API key in code (replace for real usage).

### Agents

- `ParserAgent`: calls the LLM service and parses JSON into tasks.
- `PlannerAgent`: applies default status `PENDING`.
- `ExecutionAgent`: marks tasks as `COMPLETED` and one as `FAILED`.
- `MonitoringAgent`: finds failed tasks.
- `RecoveryAgent`: reassigns failed tasks and emits logs.

### DTOs

- `PolicyRequest`: `policyText`
- `TaskResponse`: `taskName`, `role`, `deadline`, `priority`, `status`
- `PolicyResponse`: `tasks`, `logs`

## API

### POST /api/process-policy

Request body:

```json
{
  "policyText": "All customer data must be encrypted at rest and rotated quarterly."
}
```

Response body:

```json
{
  "tasks": [
    {
      "taskName": "Encrypt customer data at rest",
      "role": "Security Engineer",
      "deadline": "2024-12-31",
      "priority": "HIGH",
      "status": "REASSIGNED"
    }
  ],
  "logs": [
    "Parsed tasks: 1",
    "Applied default status to tasks.",
    "Simulated task execution.",
    "Detected failed tasks: 1",
    "Task 'Encrypt customer data at rest' failed; reassigned role to UNASSIGNED."
  ]
}
```

## Quick start

```powershell
D:\policy-agent-system\policy-agent-system\mvnw.cmd test
```
