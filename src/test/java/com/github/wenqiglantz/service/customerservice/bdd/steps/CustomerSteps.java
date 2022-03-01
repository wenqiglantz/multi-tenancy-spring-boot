package com.github.wenqiglantz.service.customerservice.bdd.steps;

import com.github.wenqiglantz.service.customerservice.bdd.CucumberBootstrap;
import com.github.wenqiglantz.service.customerservice.config.multitenancy.TenantContext;
import com.github.wenqiglantz.service.customerservice.data.CustomerVO;
import com.github.wenqiglantz.service.customerservice.persistence.entity.Customer;
import com.github.wenqiglantz.service.customerservice.persistence.repository.CustomerRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class CustomerSteps extends CucumberBootstrap {

    @Autowired
    private CustomerRepository customerRepository;

    //this method executes after every scenario
    @After
    public void cleanUp() {
        log.info(">>> cleaning up after scenario!");
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
        TenantContext.setTenantId("1");
        Customer customer1 = Customer.builder()
                .firstName("first1")
                .lastName("last1")
                .build();
        customerRepository.save(customer1);
        TenantContext.setTenantId("2");
        Customer customer2 = Customer.builder()
                .firstName("first2")
                .lastName("last2")
                .build();
        customerRepository.save(customer2);
    }

    @When("^the user retrieves all records by passing tenantId (.+)$")
    public void retrieve_record_tenantId_1(String tenantId) {
        TenantContext.setTenantId(tenantId);
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList.size(), is(equalTo(1)));
        assertThat(customerList.get(0).getFirstName(), is(equalTo("first" + tenantId)));
        TenantContext.clear();
    }

    @Then("^Only 1 record for tenantId (.+) is returned successfully$")
    public void tenantId_1_record_returned(String tenantId) {
        TenantContext.setTenantId(tenantId);
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList.size(), is(equalTo(1)));
        assertThat(customerList.get(0).getFirstName(), is(equalTo("first" + tenantId)));
        TenantContext.clear();
    }

    @Given("^the collection of customers:$")
    public void collection_of_customers(DataTable dataTable) {
        dataTable.asList(CustomerVO.class).forEach(customerInfo -> {
            saveCustomer((CustomerVO) customerInfo);
        });
    }

    @When("^customerId (.+) is passed in to retrieve the customer details$")
    public void get_customer_details_by_id(String customerId) {
        ResponseEntity<CustomerVO> response = testRestTemplate.getForEntity(
                "/customers/" + customerId, CustomerVO.class, customerId);
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getCustomerId(), is(equalTo(customerId)));
    }

    @Then("^The customer detail is retrieved$")
    public void customer_detail_retrieved(DataTable dataTable) {
        dataTable.asList(CustomerVO.class).forEach(customerInfo -> {
            Optional<Customer> customerOptional =
                    customerRepository.findByCustomerId(((CustomerVO) customerInfo).getCustomerId());
            if (customerOptional.isPresent()) {
                assertThat(customerOptional.get().getFirstName(),
                        is(equalTo(((CustomerVO) customerInfo).getFirstName())));
                assertThat(customerOptional.get().getLastName(),
                        is(equalTo(((CustomerVO) customerInfo).getLastName())));
            }
        });
    }

    private void saveCustomer(CustomerVO customerInfo) {
        customerRepository.save(Customer.builder()
                .customerId(customerInfo.getCustomerId())
                .firstName(customerInfo.getFirstName())
                .lastName(customerInfo.getLastName())
                .build());
    }
}
