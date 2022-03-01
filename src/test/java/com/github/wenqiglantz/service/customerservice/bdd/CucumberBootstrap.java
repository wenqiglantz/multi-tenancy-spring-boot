package com.github.wenqiglantz.service.customerservice.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.github.wenqiglantz.service.customerservice.config.multitenancy.TenantConstants.X_TENANT_ID;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberBootstrap {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    protected MultiValueMap<String, String> getHeadersTenant(String tenantId) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", JSON);
        headers.add("Accept", JSON);
        headers.add(X_TENANT_ID, tenantId);
        return headers;
    }
}
