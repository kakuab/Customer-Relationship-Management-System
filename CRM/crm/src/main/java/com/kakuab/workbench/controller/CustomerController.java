package com.kakuab.workbench.controller;

import com.kakuab.pojo.Customer;
import com.kakuab.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/workbench/customer/selectAllCustomer.do")
    public String selectAllCustomer(HttpServletRequest request){
        List<Customer> customerList = customerService.selectAllCustomer();
        request.setAttribute("customerList",customerList);
        return "workbench/customer/index";
    }


}
