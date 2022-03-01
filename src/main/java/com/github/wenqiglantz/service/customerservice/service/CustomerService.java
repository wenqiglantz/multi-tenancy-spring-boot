package com.github.wenqiglantz.service.customerservice.service;

import com.github.wenqiglantz.service.customerservice.data.CustomerVO;

import java.util.List;

public interface CustomerService {

    CustomerVO saveCustomer(CustomerVO customerVO) throws Exception;

    List<CustomerVO> getCustomers();

    CustomerVO getCustomer(String customerId);

    void updateCustomer(String customerId, CustomerVO customerVO) throws Exception;

    void deleteCustomer(String customerId) throws Exception;
}
