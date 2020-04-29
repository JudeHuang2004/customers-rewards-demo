package com.spectrum.gateway.bean;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

/**
 * Customer reward points response pojo - response
 */
@Entity
public class Customer {

    @Id
    @ApiModelProperty
    private String customerID;

    @ApiModelProperty
    private long totalPoints;

    @OneToMany(targetEntity=MonthlyRewards.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @ApiModelProperty
    private List<MonthlyRewards> summarylist;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<MonthlyRewards> getSummarylist() {
        return summarylist;
    }

    public void setSummarylist(List<MonthlyRewards> summarylist) {
        this.summarylist = summarylist;
    }
}
