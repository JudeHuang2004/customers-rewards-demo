package com.spectrum.gateway.bean;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Monthly reward points per customer - part of response
 */
@Entity
public class MonthlyRewards {

    @Id
    @ApiModelProperty
    private String rewardsMonth;

    @ApiModelProperty
    private long monthlyPoints;

    public String getRewardsMonth() {
        return rewardsMonth;
    }

    public void setRewardsMonth(String rewardsMonth) {
        this.rewardsMonth = rewardsMonth;
    }

    public long getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(long monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }
}
