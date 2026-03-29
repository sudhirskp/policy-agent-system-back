package com.et.ai.policy_agent_system;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PolicyAgentSystemApplication {

	public static void main(String[] args) {
        System.setProperty("https.protocols", "TLSv1.2");
		SpringApplication.run(PolicyAgentSystemApplication.class, args);
	}

}
