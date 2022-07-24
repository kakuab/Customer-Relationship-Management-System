package com.kakuab.service;

import com.kakuab.pojo.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> selectAllCustomer();

    List<String> selectCustomerNameByName(String name);

    Customer selectCustomerByName(String name);
}
