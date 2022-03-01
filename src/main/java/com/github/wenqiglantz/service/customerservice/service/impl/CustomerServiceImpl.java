package com.github.wenqiglantz.service.customerservice.service.impl;

import com.github.wenqiglantz.service.customerservice.data.CustomerVO;
import com.github.wenqiglantz.service.customerservice.data.exception.NotFoundException;
import com.github.wenqiglantz.service.customerservice.persistence.entity.Customer;
import com.github.wenqiglantz.service.customerservice.persistence.repository.CustomerRepository;
import com.github.wenqiglantz.service.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerVO saveCustomer(CustomerVO customerVO) throws Exception {
        customerVO.setCustomerId(Strings.isBlank(customerVO.getCustomerId()) ? UUID.randomUUID().toString() :
                customerVO.getCustomerId());
        Customer customer = Customer.builder()
                .customerId(customerVO.getCustomerId())
                .firstName(customerVO.getFirstName())
                .lastName(customerVO.getLastName())
                .address(customerVO.getAddress())
                .build();
        customerRepository.save(customer);
        return customerVO;
    }

    @Override
    public CustomerVO getCustomer(String customerId) {
        Customer customer =
                customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));

        CustomerVO customerVO = CustomerVO.builder()
                .customerId(customerId)
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
        return customerVO;
    }

    @Override
    public List<CustomerVO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerVO> customerVOS = customers.stream()
                .map(customer -> CustomerVO.builder()
                        .customerId(customer.getCustomerId())
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .build())
                .collect(toList());

        return customerVOS;
    }

    @Override
    public void updateCustomer(String customerId, CustomerVO customerVO) throws Exception {
        Customer customer =
                customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));
        customer.setFirstName(customerVO.getFirstName());
        customer.setLastName(customerVO.getLastName());
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(String customerId) throws Exception {
        Customer customer =
                customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));
        customerRepository.delete(customer);
    }
}
