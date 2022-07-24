package com.kakuab.service.impl;

import com.kakuab.mapper.CustomerMapper;
import com.kakuab.pojo.Customer;
import com.kakuab.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> selectAllCustomer() {
        return customerMapper.selectAllCustomer();
    }

    @Override
    public List<String> selectCustomerNameByName(String name) {
        return customerMapper.selectCustomerNameByName(name);
    }

    @Override
    public Customer selectCustomerByName(String name) {
        return customerMapper.selectCustomerByName(name);
    }
}
