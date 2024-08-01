package postCall;

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

import net.datafaker.Faker;
import pojoBody.Pojo;
import pojoBody.PojoLombok;

public class A07_PostCallUsingPojo {
	APIRequestContext requestContext ;
	APIRequest apiRequest;
	Playwright playwright;
	String emailId;
	String name;
	String status;
	String gender;
	
	@BeforeTest
	public void setUp() {
		 playwright = Playwright.create();
		 apiRequest = playwright.request();
		 requestContext = apiRequest.newContext();
	}
	
	@Test
	public void createUserUsingPojoTest() throws IOException
	{
		// User data
		/*	{
			"name":"Reka",
			"email":"reka@gmail.com",
			"gender": "female",
			"status": "active"
			}
		*/
		System.out.println("*************************** Post call Response ****************************");
		
		Faker faker = new Faker();
		emailId=faker.internet().emailAddress();
		name=faker.name().fullName();		
		gender= faker.options().option("male", "female");
		status=faker.options().option("active", "inactive");
		Pojo user =new Pojo("Darshana", emailId, "female", "active");
		// we are supplying details 	
		// API request
		APIResponse	apiPostResponse=requestContext.post("https://gorest.co.in/public/v2/users", 
								RequestOptions.create()
										.setHeader("Content-Type", "application/json; charset=utf-8")
										.setHeader("Authorization", "Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6")
										.setData(user)
										);
		
		// Logging the full response for debugging
	    System.out.println("Response status: " + apiPostResponse.status());
	    System.out.println("Response body: " + apiPostResponse.text());
	    String responseText=apiPostResponse.text();
	    System.out.println(responseText);
	    
	    // Convert response text/json Response to POJO -- Deserialization
	    ObjectMapper objectMapper= new ObjectMapper();
	    Pojo actualUser= objectMapper.readValue(responseText, Pojo.class);
	    System.out.println("***************Actual user from the Response **************");
	    System.out.println(actualUser);
	   
	    // Assertions
	    //supplied user details == response user details
	    Assert.assertEquals(apiPostResponse.status(), 201);
		Assert.assertEquals(apiPostResponse.statusText(), "Created");    
	    Assert.assertEquals(actualUser.getEmail(), user.getEmail());
	    Assert.assertEquals(actualUser.getName(), user.getName());
	    Assert.assertEquals(actualUser.getStatus(), user.getStatus());
	    Assert.assertEquals(actualUser.getGender(), user.getGender());
	    Assert.assertNotNull(actualUser.getId());
	    
//// POJO -> JSON  Serialization
		
	}	
/*		// Get Call : Fetch the same user by id:
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
		Assert.assertTrue(apiGetResponse.text().contains(gender));
		Assert.assertTrue(apiGetResponse.text().contains(emailId));
	
*/	
	@AfterTest
	public void tearDown() {
		playwright.close();		
	}
}
