package com.spectrum.gateway.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectrum.gateway.bean.Customer;
import com.spectrum.gateway.bean.CustomerRepository;
import com.spectrum.gateway.bean.MonthlyRewards;
import com.spectrum.gateway.bean.Transaction;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RewardsProcessor {
	
	public static final Logger logger = LoggerFactory.getLogger(RewardsProcessor.class);

    public static CustomerRepository querySingleCustomer(String customerID, CustomerRepository repository) {
        Customer customer = querySingleCustomer(customerID);
        if(customer != null) {
            repository.save(customer);
        }
        return repository;
    }

    /**
     *
     * @param customerID
     * @return
     */
    public static Customer querySingleCustomer(String customerID) {
        List<Transaction> tranlist = generateTestTransactions();
        Map<String, Long> monthlyMap = calculateMonthlyRewards(tranlist);
        Customer customer = constructSingleCustomerRewards(monthlyMap, customerID);
        return customer;
    }

    /**
     *
     * @param monthlyMap
     * @param customerID
     * @return
     */
    public static Customer constructSingleCustomerRewards(Map<String, Long> monthlyMap, String customerID) {
        Map<String, Customer> rprMap = createCustomerRewardsMap(monthlyMap);
        return rprMap.get(customerID);
    }

    /**
     * Util to generate test data set for query
     * @return
     */
    public static List<Transaction> generateTestTransactions() {
        ObjectMapper mapper = new ObjectMapper();
        List<Transaction> tranlist = new ArrayList<>();
        try{
            File transFile = new File(RewardsProcessor.class.getClassLoader().getResource("transactions.json").getFile());
//            List<LinkedHashMap> tranFieldsList = mapper.readValue(transFile, List.class);
            List<Map> tranFieldsList = mapper.readValue(transFile, List.class);
            for(Map tranFields: tranFieldsList) {
                Transaction tran = new Transaction();
                tran.setCustomerID(tranFields.get("customerID").toString());
                tran.setTranID(tranFields.get("tranID").toString());
                tran.setTranDate(tranFields.get("tranDate").toString());
                tran.setTranAmount(new BigDecimal(tranFields.get("tranAmount").toString()));
                tranlist.add(tran);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return tranlist;
    }

    /**
     * @param tranAmount
     * @return
     */
    public static long calculateSingleTransaction(BigDecimal tranAmount) {
        /*
         * A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.
         * A customer receives 2 points for every dollar spent over $100 in each transaction,
         * plus 1 point for every dollar spent over $50 in each transaction
         * (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).
         * Given a record of every transaction during a three month period, calculate the reward points earned for each customer per month and total.
         * Make up a data set to best demonstrate your solution
         * Check solution into GitHub
         *
         */
        long tranPoints = 0;
        long tranAmountLong = tranAmount.longValue();
        if (tranAmountLong > 100) {
            // $120 purchase = 2x$20 + 1x$50 = 90 points
            tranPoints = 2 * (tranAmountLong - 100) + 50;
        } else if (tranAmountLong > 50) {
            tranPoints = tranAmountLong - 50;
        }
        return tranPoints;
    }

    /**
     * @param tranlist
     * @return
     */
    public static Map<String, Long> calculateMonthlyRewards(List<Transaction> tranlist) {
        //key: 8e249dcb-b75a-4e42-af04-0615a29c52aa+"|"+"2020-04" value: points per month
        Map<String, Long> monthlyMap = new HashMap<>();
        for (Transaction tran : tranlist) {
            String tranKey = tran.getCustomerID() + "|" + tran.getTranDate().substring(0, 7);
            if (monthlyMap.containsKey(tranKey)) {
                long lastPoints = monthlyMap.get(tranKey);
                monthlyMap.put(tranKey, lastPoints + calculateSingleTransaction(tran.getTranAmount()));
            } else {
                monthlyMap.put(tranKey, calculateSingleTransaction(tran.getTranAmount()));
            }
        }
        return monthlyMap;
    }

    /**
     *
     * @param monthlyMap
     * @return
     */
    public static Map<String, Customer> createCustomerRewardsMap(Map<String, Long> monthlyMap) {
        Map<String, Customer> rprMap = new HashMap<>();
        monthlyMap.forEach(
                (key, value) -> {
                    String customerID = key.substring(0, key.indexOf("|"));
                    String rewardsMonth = key.substring(key.indexOf("|") + 1);
                    MonthlyRewards summary = new MonthlyRewards();
                    summary.setRewardsMonth(rewardsMonth);
                    summary.setMonthlyPoints(value);
                    if (rprMap.containsKey(customerID)) {
                        Customer rpr = rprMap.get(customerID);
                        // add the monthly points to total points
                        long lastPoints = rpr.getTotalPoints();
                        rpr.setTotalPoints(lastPoints + value);
                        rpr.getSummarylist().add(summary);
                    } else {
                        // initialize a new Customer with the same customerID
                        Customer rpr = new Customer();
                        rpr.setCustomerID(customerID);
                        rpr.setTotalPoints(value);
                        List<MonthlyRewards> summarylist = new ArrayList<MonthlyRewards>();
                        summarylist.add(summary);
                        rpr.setSummarylist(summarylist);
                        // use Customer customerID as key
                        rprMap.put(customerID, rpr);
                    }
                }
        );
        return rprMap;
    }


}
