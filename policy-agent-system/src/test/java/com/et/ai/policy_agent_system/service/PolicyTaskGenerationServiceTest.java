package com.et.ai.policy_agent_system.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.POST;

class PolicyTaskGenerationServiceTest {

    @Test
    void generateTasksFromPolicy_sendsPromptAndReturnsBody() {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        PolicyTaskGenerationService service = new PolicyTaskGenerationService(restTemplate);

        server.expect(requestTo(PolicyTaskGenerationService.API_URL))
                .andExpect(method(POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess("[{\"taskName\":\"A\"}]", MediaType.APPLICATION_JSON));

        String response = service.generateTasksFromPolicy("Sample policy");

        assertNotNull(response);
        server.verify();
    }
}

