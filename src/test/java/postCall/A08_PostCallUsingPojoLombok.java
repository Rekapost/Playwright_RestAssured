package postCall;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import pojoBody.Pojo;
import pojoBody.PojoLombok;

public class A08_PostCallUsingPojoLombok {
	APIRequestContext requestContext ;
	APIRequest apiRequest;
	Playwright playwright;
	String emailId;
	String name;
	String status;
	String gender;
	
// notIdempotent	
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
		name=faker.name().fullName();	
		emailId=faker.internet().emailAddress();
		gender= faker.options().option("male", "female");
		status=faker.options().option("active", "inactive");
		
/*		//Create PojoLombok object :
		PojoLombok pojo =new PojoLombok();
		pojo.setEmail(emailId);
		pojo.setGender(gender);
		pojo.setName(name);
		pojo.setStatus(status);	
*/		
		//Create PojoLombok object : using Builder pattern :
		PojoLombok pojoLo =PojoLombok.builder()
						.name(name)
						.email(emailId)
						.gender(gender)
						.status(status).build();
						
		// API request
		APIResponse	apiPostResponse=requestContext.post("https://gorest.co.in/public/v2/users", 
								RequestOptions.create()
										.setHeader("Content-Type", "application/json; charset=utf-8")
										.setHeader("Authorization", "Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6")
										.setData(pojoLo)
										);
		
		// Logging the full response for debugging
	    System.out.println("Response status: " + apiPostResponse.status());
	    System.out.println("Response body: " + apiPostResponse.text());
	    String responseText=apiPostResponse.text();
	    System.out.println(responseText);
	    
	    // Convert response text/json Response to POJO -- Deserialization
	    ObjectMapper objectMapper= new ObjectMapper();
	    PojoLombok actualUser= objectMapper.readValue(responseText, PojoLombok.class);
	    System.out.println("***************Actual user from the Response **************");
	    System.out.println(actualUser);
	   
	    // Assertions
	    //supplied user details == response user details
	    Assert.assertEquals(apiPostResponse.status(), 201);
		Assert.assertEquals(apiPostResponse.statusText(), "Created");    
	    Assert.assertEquals(actualUser.getEmail(), pojoLo.getEmail());
	    Assert.assertEquals(actualUser.getName(), pojoLo.getName());
	    Assert.assertEquals(actualUser.getStatus(), pojoLo.getStatus());
	    Assert.assertEquals(actualUser.getGender(), pojoLo.getGender());
	    Assert.assertNotNull(actualUser.getId());
	    
	}
	
	@AfterTest
	public void tearDown() {
		playwright.close();		
	}
}
