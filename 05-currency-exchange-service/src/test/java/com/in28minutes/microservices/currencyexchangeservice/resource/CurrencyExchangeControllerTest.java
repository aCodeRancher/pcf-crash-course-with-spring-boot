package com.in28minutes.microservices.currencyexchangeservice.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CurrencyExchangeController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ComponentScan("com.in28minutes.microservices.currencyexchangeservice.util.environment")
//@SpringBootTest using it as a SpringBootTest is fine, but it will load the unncessary components
class CurrencyExchangeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void retrieveExchangeValue() throws Exception{
       String expected = "{\"id\":10001,\"from\":\"USD\",\"to\":\"INR\",\"conversionMultiple\":65.00,\"exchangeEnvironmentInfo\":\"UNKNOWN : UNKNOWN\"}";
       MvcResult result = mockMvc.perform(get("/currency-exchange/from/USD/to/INR"))
                .andExpect(status().is2xxSuccessful()).andReturn();
       String output = result.getResponse().getContentAsString();
       assertTrue(output.equals(expected));
    }
}