package com.in28minutes.microservices.currencyconversionservice.resource;

import com.in28minutes.microservices.currencyconversionservice.util.environment.InstanceInformationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Run this unit test without the currency exchange service and without the cloud config server.
@WebMvcTest(CurrencyConversionController.class)
@AutoConfigureMockMvc
@ComponentScan("com.in28minutes.microservices.currencyconversionservice.util.environment")
class CurrencyConversionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CurrencyExchangeServiceProxy proxy;

    @MockBean
    InstanceInformationService informationService;

    @Test
    public void convertCurrency() throws Exception{

      CurrencyConversionBean ccbean = new CurrencyConversionBean();
      ccbean.setId(10005L);
      ccbean.setFrom("USD");
      ccbean.setTo("INR");
      ccbean.setConversionMultiple(BigDecimal.valueOf(65L));
      ccbean.setExchangeEnvironmentInfo("N/A");
      ccbean.setTotalCalculatedAmount(BigDecimal.valueOf(6500L));
      String conversionEnvInfo = "N/A";
      ccbean.setQuantity(BigDecimal.valueOf(100L));
      when(proxy.retrieveExchangeValue("USD", "INR")).thenReturn(ccbean);
      when(informationService.retrieveInstanceInfo()).thenReturn(conversionEnvInfo);
      MvcResult result = mockMvc.perform(get("/currency-converter/from/USD/to/INR/quantity/100"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
      String expected =
              "{\"id\":10005,\"from\":\"USD\",\"to\":\"INR\",\"conversionMultiple\":65,\"quantity\":100," +
                      "\"totalCalculatedAmount\":6175.00,\"exchangeEnvironmentInfo\":\"N/A\"," +
                      "\"conversionEnvironmentInfo\":\"N/A\"}";

      String output = result.getResponse().getContentAsString();
      assertTrue(output.equals(expected));
      verify(proxy,times(1)).retrieveExchangeValue(anyString(),anyString());
      verify(informationService,times(1)).retrieveInstanceInfo();
    }
}