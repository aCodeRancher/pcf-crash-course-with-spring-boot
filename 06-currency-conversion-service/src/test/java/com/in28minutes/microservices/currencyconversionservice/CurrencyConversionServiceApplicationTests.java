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

	//Before running this integration test , run eureka naming server, spring cloud config server, currency exchange service and
	//currency conversion service.
    //When cloud config server is up, the conversion profit percent = 60 by default
    //conversionMultiple = (100-60)/100 = 0.4
    //convertedValue = 100*65*0.4=2600
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
      assertTrue(responseBean.getTotalCalculatedAmount().intValue() == 2600 );
      assertTrue(responseBean.getId() == 10001);
   }

    //Before running this test, don't run the cloud config server. Just run
    //Eureka, exchange service and conversion service.
    //When cloud config server is not up, the conversion profit percent = 5 by default
    //conversionMultiple = (100-5)/100 = 0.95
    //convertedValue = 100*65*0.95=6175
    @Test
    public void contextLoads_withoutConfigServer() throws RestClientException {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<CurrencyConversionBean> response = testRestTemplate.exchange(
                "http://localhost:8100/currency-converter/from/{from}/to/{to}/quantity/{quantity}",
                HttpMethod.GET, null, CurrencyConversionBean.class, "USD", "INR", "100");
        CurrencyConversionBean responseBean = response.getBody();
        assertTrue(responseBean.getQuantity().equals(BigDecimal.valueOf(100)));
        assertTrue(responseBean.getFrom().equals("USD"));
        assertTrue(responseBean.getTo().equals("INR"));
        assertTrue(responseBean.getConversionMultiple().intValue() == 65);
        assertTrue(responseBean.getTotalCalculatedAmount().intValue() == 6175 );
        assertTrue(responseBean.getId() == 10001);
    }

   /* @Test
    public void contextLoads_usingServiceRegistry() throws RestClientException {
	    //This is just my route. Other people will have different routes.
	    String route = "currency-conversion-service-hma-101.cfapps.io";
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<CurrencyConversionBean> response = testRestTemplate.exchange(
                "http://"+route+"/currency-converter/from/{from}/to/{to}/quantity/{quantity}",
                HttpMethod.GET, null, CurrencyConversionBean.class, "USD", "INR", "100");
        CurrencyConversionBean responseBean = response.getBody();
        assertTrue(responseBean.getQuantity().equals(BigDecimal.valueOf(100)));
        assertTrue(responseBean.getFrom().equals("USD"));
        assertTrue(responseBean.getTo().equals("INR"));
        assertTrue(responseBean.getConversionMultiple().intValue() == 65);
        assertTrue(responseBean.getTotalCalculatedAmount().intValue() == 6500 );
        assertTrue(responseBean.getId() == 10001);
    }*/
}
