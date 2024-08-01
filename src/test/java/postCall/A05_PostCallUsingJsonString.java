package postCall;
import java.io.IOException;
import java.util.HashMap;
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
import net.datafaker.Faker;

public class A05_PostCallUsingJsonString {
	APIRequestContext requestContext ;
	APIRequest apiRequest;
	Playwright playwright;
	String emailId;
	
	@BeforeTest
	public void setUp() {
		 playwright = Playwright.create();
		 apiRequest = playwright.request();
		 requestContext = apiRequest.newContext();
	}
	
	@Test
	public void createUserJsonStringTest() throws IOException
	{
		// User data
		/*	{
			"name":"Reka",
			"email":"reka@gmail.com",
			"gender": "female",
			"status": "active"
			}
		*/
		//String json:
		String reJsonBody="{\n" 
				+ "			\"name\":\"Raja\",\r\n"
				+ "			\"email\":\"rekaraPPja@gmail.com\",\r\n"
				+ "			\"gender\": \"female\",\r\n"
				+ "			\"status\": \"active\""
				+ "    }";
		
		System.out.println("*************************** Post call Response ****************************");		
		 // API request
		APIResponse	apiPostResponse=requestContext.post("https://gorest.co.in/public/v2/users", 
								RequestOptions.create()
										.setHeader("Content-Type", "application/json; charset=utf-8")
										.setHeader("Authorization", "Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6")
										.setData(reJsonBody)
										);
		
		// Logging the full response for debugging
	    System.out.println("Response status: " + apiPostResponse.status());
	    System.out.println("Response body: " + apiPostResponse.text());
	    
	    System.out.println(apiPostResponse.text());
	    ObjectMapper objectMapper= new ObjectMapper();
	    JsonNode postJsonResponse = objectMapper.readTree(apiPostResponse.body());
	    String prettyResponseBody=postJsonResponse.toPrettyString();
	    System.out.println(prettyResponseBody);
	    String userId =postJsonResponse.get("id").asText();
	    System.out.println("user id : "+ userId);
	    // Assertions
	    Assert.assertEquals(apiPostResponse.status(), 201);
		Assert.assertEquals(apiPostResponse.statusText(), "Created");
		//Capture id from post json Response"
		
		// Get Call : Fetch the same user by id:
		System.out.println("*************** Get call response **************************");
		APIResponse apiGetResponse=requestContext.get("https://gorest.co.in/public/v2/users/"+ userId,
												RequestOptions.create()
												.setHeader("Content-Type", "application/json; charset=utf-8")
												.setHeader("Authorization", "Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6")
												); 
		
		System.out.println(apiGetResponse.status());
		System.out.println(apiGetResponse.text());
		System.out.println(apiGetResponse.statusText());
		Assert.assertTrue(apiGetResponse.text().contains(userId));
		//Assert.assertTrue(apiGetResponse.text().contains("male"));
		//Assert.assertTrue(apiGetResponse.text().contains(emailId));
	}
	
	@AfterTest
	public void tearDown() {
		playwright.close();		
	}
}
