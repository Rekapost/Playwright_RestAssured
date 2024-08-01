package deleteCall;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import net.datafaker.Faker;
import pojoBody.PojoLombok;

public class A10_DeleteUser_Resource {
	APIRequestContext requestContext;
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
	public void deleteUser() throws IOException {
		// User data
		/*
		 * { "name":"Reka", "email":"reka@gmail.com", "gender": "female", "status":
		 * "active" }
		 */
		System.out.println("*************************** Post call Response ****************************");

		Faker faker = new Faker();
		name = faker.name().fullName();
		emailId = faker.internet().emailAddress();
		gender = faker.options().option("male", "female");
		status = faker.options().option("active", "inactive");

		/*
		 * //Create PojoLombok object : PojoLombok pojo =new PojoLombok();
		 * pojo.setEmail(emailId); pojo.setGender(gender); pojo.setName(name);
		 * pojo.setStatus(status);
		 */
		// Create PojoLombok object : using Builder pattern :
		PojoLombok pojoLo = PojoLombok.builder().name(name).email(emailId).gender(gender).status(status).build();

		// API request
		System.out.println("1. ********************Create User - POST CALL Response ****************************");
		APIResponse apiPostResponse = requestContext.post("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setHeader("Content-Type", "application/json; charset=utf-8")
						.setHeader("Authorization",
								"Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6")
						.setData(pojoLo));

		// Logging the full response for debugging
		System.out.println("Post Response status: " + apiPostResponse.status());
		System.out.println("Post Response body: " + apiPostResponse.text());
		Assert.assertEquals(apiPostResponse.status(), 201);
		String responseText = apiPostResponse.text();

		// Convert response text/json Response to POJO -- Deserialization
		ObjectMapper objectMapper = new ObjectMapper();
		PojoLombok actualUser = objectMapper.readValue(responseText, PojoLombok.class);
		System.out.println("***************Actual user from the Response **************");
		System.out.println(actualUser);

		// Assertions
		// supplied user details == response user details
		Assert.assertEquals(actualUser.getEmail(), pojoLo.getEmail());
		Assert.assertEquals(actualUser.getName(), pojoLo.getName());
		Assert.assertEquals(actualUser.getStatus(), pojoLo.getStatus());
		Assert.assertEquals(actualUser.getGender(), pojoLo.getGender());
		Assert.assertNotNull(actualUser.getId());

		// Convert response text/json Response to POJO -- Deserialization
		System.out.println("Actual user from the Response");
		PojoLombok postActualUser = objectMapper.readValue(responseText, PojoLombok.class);
		System.out.println(postActualUser);

		// Fetching user id from Actual user Response Object
		String postUserId = postActualUser.getId();
		System.out.println(" new user id captured from post response is : " + postUserId);

		// API request
		System.out.println("2. ********************Delete User - DELETE CALL Response ****************************");
		APIResponse apiDeleteResponse = requestContext.delete("https://gorest.co.in/public/v2/users/" + postUserId,
				RequestOptions.create().setHeader("Content-Type", "application/json; charset=utf-8")
						.setHeader("Authorization",
								"Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6"));
				
		// Logging the full response for debugging
		System.out.println("Delete Response status: " + apiDeleteResponse.status());
		System.out.println("Delete Response body: " + apiDeleteResponse.text());
		System.out.println("Delete Response status text: " + apiDeleteResponse.statusText());
		Assert.assertEquals(apiDeleteResponse.status(), 204);
		String deleteResponseText = apiDeleteResponse.text();
		System.out.println("Delete Response Text : " + deleteResponseText);

		System.out.println("3. ********************Get User - GET CALL Response ****************************");
		APIResponse apiGetResponse = requestContext.get("https://gorest.co.in/public/v2/users/" + postUserId,
				RequestOptions.create().setHeader("Content-Type", "application/json; charset=utf-8").setHeader(
						"Authorization", "Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6"));

		System.out.println("Get response status :" + apiGetResponse.status());
		System.out.println("Get Response body: " + apiGetResponse.text());
		System.out.println("Get response statusText :" + apiGetResponse.statusText());
		Assert.assertEquals(apiGetResponse.status(), 404);
	}

	@AfterTest
	public void tearDown() {
		requestContext.dispose();
		playwright.close();
	}
}
