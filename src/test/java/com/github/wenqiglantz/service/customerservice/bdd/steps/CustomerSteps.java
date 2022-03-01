package com.github.wenqiglantz.service.customerservice.bdd.steps;

import com.github.wenqiglantz.service.customerservice.bdd.CucumberBootstrap;
import com.github.wenqiglantz.service.customerservice.config.multitenancy.TenantContext;
import com.github.wenqiglantz.service.customerservice.data.CustomerVO;
import com.github.wenqiglantz.service.customerservice.persistence.entity.Customer;
import com.github.wenqiglantz.service.customerservice.persistence.repository.CustomerRepository;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Slf4j
public class CustomerSteps extends CucumberBootstrap {

    @Autowired
    private CustomerRepository customerRepository;

    //this method executes after every scenario
    @After
    public void cleanUp() {
        log.info(">>> cleaning up after scenario!");
        TenantContext.setTenantId("1");
        customerRepository.deleteAll();
        TenantContext.setTenantId("2");
        customerRepository.deleteAll();
    }

    //this method executes after every step
    @AfterStep
    public void afterStep() {
        log.info(">>> AfterStep!");
        //placeholder for after step logic
    }

    //this method executes before every scenario
    @Before
    public void before() {
        log.info(">>> Before scenario!");
        //placeholder for before scenario logic
    }

    //this method executes before every step
    @BeforeStep
    public void beforeStep() {
        log.info(">>> BeforeStep!");
        //placeholder for before step logic
    }

    @Given("^two customer records are persisted in db$")
    public void two_customer_records_persisted() {
        CustomerVO customer1 = CustomerVO.builder()
                .firstName("first1")
                .lastName("last1")
                .address("address1")
                .build();
        ResponseEntity<CustomerVO> response = testRestTemplate.postForEntity(
                "/customers", new HttpEntity<>(customer1, getHeadersTenant("1")),
                CustomerVO.class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.CREATED)));

        CustomerVO customer2 = CustomerVO.builder()
                .firstName("first2")
                .lastName("last2")
                .address("address2")
                .build();
        ResponseEntity<CustomerVO> response2 = testRestTemplate.postForEntity(
                "/customers", new HttpEntity<>(customer2, getHeadersTenant("2")),
                CustomerVO.class);
        assertThat(response2.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
    }

    @When("^the user retrieves all records by passing tenantId (.+)$")
    public void retrieve_record_tenantId_1(String tenantId) {
        TenantContext.setTenantId(tenantId);
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList.size(), is(equalTo(1)));
        assertThat(customerList.get(0).getFirstName(), is(equalTo("first" + tenantId)));
    }

    @Then("^Only 1 record for tenantId (.+) is returned successfully$")
    public void tenantId_1_record_returned(String tenantId) {
        TenantContext.setTenantId(tenantId);
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList.size(), is(equalTo(1)));
        assertThat(customerList.get(0).getFirstName(), is(equalTo("first" + tenantId)));
    }
}
