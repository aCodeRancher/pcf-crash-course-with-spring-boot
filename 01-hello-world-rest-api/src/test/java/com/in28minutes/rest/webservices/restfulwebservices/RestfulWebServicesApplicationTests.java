package com.in28minutes.rest.webservices.restfulwebservices;


		import org.junit.jupiter.api.Test;
		import org.springframework.boot.test.context.SpringBootTest;
		import org.springframework.boot.test.web.client.TestRestTemplate;
		import org.springframework.http.ResponseEntity;

		import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class RestfulWebServicesApplicationTests {


	@Test
	public void contextLoads() {
		TestRestTemplate testRestTemplate = new TestRestTemplate();
		ResponseEntity<String> response =
				testRestTemplate.getForEntity("http://hello-world-rest-api-hhma-100.cfapps.io/hello-world/path-variable/good_morning", String.class);
		String expected = "{\"message\":\"Hello World, good_morning\"}";
		assertTrue(response.getBody().contains(expected));
	}

}
