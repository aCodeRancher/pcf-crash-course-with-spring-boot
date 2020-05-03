package com.in28minutes.microservices.currencyconversionservice;

import com.in28minutes.microservices.currencyconversionservice.resource.CurrencyConversionBean;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CurrencyConversionServiceApplicationTests {

	//Before running this integration test , run the Eureka naming service, currency exchange service and
	//currency conversion service.
	@Test
	public void contextLoads() throws RestClientException {
      TestRestTemplate testRestTemplate = new TestRestTemplate();
      ResponseEntity<CurrencyConversionBean> response = testRestTemplate.exchange(
      		"http://localhost:8100/currency-converter/from/{from}/to/{to}/quantity/{quantity}",
			  HttpMethod.GET, null, CurrencyConversionBean.class, "USD", "INR", "100");
      CurrencyConversionBean responseBean = response.getBody();
      assertTrue(responseBean.getQuantity().equals(BigDecimal.valueOf(100)));
      assertTrue(responseBean.getFrom().equals("USD"));
      assertTrue(responseBean.getTo().equals("INR"));
      assertTrue(responseBean.getConversionMultiple().intValue() == 65);
      assertTrue(responseBean.getTotalCalculatedAmount().intValue() == 6500 );
      assertTrue(responseBean.getId() == 10001);
   }
}
