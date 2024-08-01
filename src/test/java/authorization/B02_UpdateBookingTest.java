package authorization;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class B02_UpdateBookingTest {
	APIRequestContext requestContext;
	APIRequest apiRequest;
	Playwright playwright;
	private static String TOKEN;
	
	@BeforeTest
	public void setUp() throws IOException {
		playwright = Playwright.create();
		apiRequest = playwright.request();
		requestContext = apiRequest.newContext();
		
		// PreCondition: Get the Token:		
		String requestTokenJsonBody="{\r\n"
				+ "					\"username\":\"admin\",\r\n"
				+ "					\"password\":\"password123\"\r\n"
				+ "					}";

		System.out.println("*************************** Getting Token ****************************");		
		// API request
		APIResponse	apiPostTokenResponse=requestContext.post("https://restful-booker.herokuapp.com/auth", 
								RequestOptions.create()
										.setHeader("Content-Type", "application/json")
										.setData(requestTokenJsonBody)
										);
		ObjectMapper objectMapper= new ObjectMapper();
	    JsonNode postJsonTokenResponse = objectMapper.readTree(apiPostTokenResponse.body());
	    TOKEN =postJsonTokenResponse.get("token").asText();
	    System.out.println("Token from Response : "+ TOKEN);
	}

	@Test(priority =1)
	public void updateBookingTest() {

		// User data
		  /*
			 {
			    "firstname": "Sally",
			    "lastname": "Brown",
			    "totalprice": 111,
			    "depositpaid": true,
			    "bookingdates": {
			        "checkin": "2013-02-23",
			        "checkout": "2014-10-23"
			    },
			    "additionalneeds": "Breakfast"
			}
		 */
		
		// String json:
		String bookingJson = "{\r\n"
				+ "			    \"firstname\": \"hariReka\",\r\n"
				+ "			    \"lastname\": \"dars\",\r\n"
				+ "			    \"totalprice\": 856,\r\n"
				+ "			    \"depositpaid\": true,\r\n"
				+ "			    \"bookingdates\": {\r\n"
				+ "			        \"checkin\": \"2024-04-23\",\r\n"
				+ "			        \"checkout\": \"2025-11-23\"\r\n"
				+ "			    },\r\n"
				+ "			    \"additionalneeds\": \"Lunch\"\r\n"
				+ "			}";
		System.out.println("*************************** Put call Response ****************************");	
		// API request
		APIResponse apiPutResponse = requestContext.put("https://restful-booker.herokuapp.com/booking/1",
				RequestOptions.create()
							.setHeader("Content-Type", "application/json")
						    .setHeader("Cookie", "token="+ TOKEN)
						    .setData(bookingJson));

		// Logging the full response for debugging
		System.out.println("Post Response status: " + apiPutResponse.status());
		System.out.println("Post Response body: " + apiPutResponse.text());
		Assert.assertEquals(apiPutResponse.status(), 200);
		Assert.assertEquals(apiPutResponse.statusText(), "OK");
		String responseText = apiPutResponse.text();
		System.out.println(responseText);
	}
	
	@Test(priority =2)
	public void deleteBookingTest() {
		System.out.println("*************************** Delete call Response ****************************");	
		APIResponse apiDeleteResponse = requestContext.delete("https://restful-booker.herokuapp.com/booking/1",
				RequestOptions.create()
							.setHeader("Content-Type", "application/json")
						    .setHeader("Cookie", "token="+ TOKEN));
		// Logging the full response for debugging
				System.out.println("Delete Response status: " + apiDeleteResponse.status());
				System.out.println("Delete Response body: " + apiDeleteResponse.text());
				Assert.assertEquals(apiDeleteResponse.status(), 201);
				Assert.assertEquals(apiDeleteResponse.statusText(), "Created");
				String responseText = apiDeleteResponse.text();
				System.out.println(responseText);
				Assert.assertTrue(apiDeleteResponse.text().contains("Created"));
	}

	@AfterTest
	public void tearDown() {
		playwright.close();
	}
}
