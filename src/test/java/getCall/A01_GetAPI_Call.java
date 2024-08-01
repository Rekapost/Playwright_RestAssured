package getCall;

import java.io.IOException;
import java.util.Map;
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

public class A01_GetAPI_Call {
	APIRequestContext requestContext ;
	APIRequest apiRequest;
	Playwright playwright;
	
	@BeforeTest
	public void setUp() {
		 playwright = Playwright.create();
		 apiRequest = playwright.request();
		 requestContext = apiRequest.newContext();
	}

	@Test
	public void getSpecificUserApiTest() throws IOException {
		APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users", 
										RequestOptions.create()
										.setQueryParam("gender", "male")
										.setQueryParam("status", "active"));
		
		int statusCode = apiResponse.status();
		System.out.println("Response Status Code :" + statusCode);
		Assert.assertEquals(statusCode, 200);

		String statusText = apiResponse.statusText();
		System.out.println("Response Status Text :" + statusText);

		System.out.println(apiResponse.text());

		Assert.assertEquals(apiResponse.ok(), true);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
		System.out.println("JSON Pretty Response: " + jsonResponse.toPrettyString());

		Map<String, String> headerMap = apiResponse.headers();
		System.out.println("Header Map :" + headerMap);
		Assert.assertEquals(headerMap.get("content-type"), "application/json; charset=utf-8");

		System.out.println("------------------------------------");
	}

	@Test
	public void getUsersApiTest() throws IOException {
		
		APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");
		int statusCode = apiResponse.status();
		System.out.println("Response Status Code :" + statusCode);
		Assert.assertEquals(statusCode, 200);

		String statusText = apiResponse.statusText();
		System.out.println("Response Status Text :" + statusText);

		System.out.println(apiResponse.text());

		Assert.assertEquals(apiResponse.ok(), true);

		System.out.println(" Api URL : " + apiResponse.url());

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
		System.out.println("JSON Pretty Response: " + jsonResponse.toPrettyString());

		Map<String, String> headerMap = apiResponse.headers();
		System.out.println("Header Map :" + headerMap);
		Assert.assertEquals(headerMap.get("content-type"), "application/json; charset=utf-8");

	}
	
	@AfterTest
	public void tearDown() {
		playwright.close();		
	}
}
