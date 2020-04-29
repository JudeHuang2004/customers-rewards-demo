package com.spectrum.gateway.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomersRewardsControllerTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;
    private MockHttpSession session;

    @Before
    public void setupMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        session = new MockHttpSession();
    }

    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void testQueryCustomerRewardPointsOK() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/customers/8e249dcb-b75a-4e42-af04-0615a29c52aa/rewardpoints")
                .session(session)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPoints").value(226))
                .andExpect(MockMvcResultMatchers.jsonPath("$.summarylist").isArray())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testQueryCustomerRewardPoints404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/customers/1/rewardpoints")
                .session(session)
        )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

}
