package com.spectrum.gateway.controllers;

import com.spectrum.gateway.bean.Customer;
import com.spectrum.gateway.bean.CustomerRepository;
import com.spectrum.gateway.bean.exception.CustomerNotFoundException;
import com.spectrum.gateway.processor.RewardsProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/customers")
@Api(value = "customers", tags = "Customers Rewards API")
public class CustomersRewardsController {

    @Autowired
    private CustomerRepository repository;

    @RequestMapping(value = "/{customerID}/rewardpoints", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Display reward points for expected customer per month and total", response = Customer.class)
    public Customer queryCustomerRewardPoints(@PathVariable String customerID) {
        // @RequestParam : TODO out-of-scope, if needed, can support more specific query like ?totalPoints>100
        RewardsProcessor.querySingleCustomer(customerID, repository);
        return repository.findById(customerID)
                .orElseThrow(() -> new CustomerNotFoundException(customerID));
    }

}