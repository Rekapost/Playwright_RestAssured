package putCall;
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

public class A09_UpdateUserPutCallUsingPojoLombokTest {

	// idempotent
	// entire body is needed whereas in Patch only the field which is need to be
	// changed is needed
	APIRequestContext requestContext;
	APIRequest apiRequest;
	Playwright playwright;
	PojoLombok pojoLo;
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
	public void updateUserUsingPojoLombokTest() throws IOException {
		// User data
		/*
		 * { "name":"Reka", "email":"reka@gmail.com", "gender": "female", "status":
		 * "active" }
		 */
		System.out.println("1. ********************Create User - POST CALL Response ****************************");

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
		pojoLo = PojoLombok.builder().name(name).email(emailId).gender(gender).status(status).build();

		// API request
		APIResponse apiPostResponse = requestContext.post("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setHeader("Content-Type", "application/json; charset=utf-8")
						.setHeader("Authorization",
								"Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6")
						.setData(pojoLo));

		// Logging the full response for debugging
		System.out.println("Post Response status: " + apiPostResponse.status());
		System.out.println("Post Response body: " + apiPostResponse.text());
		Assert.assertEquals(apiPostResponse.status(), 201);
		Assert.assertEquals(apiPostResponse.statusText(), "Created");
		String responseText = apiPostResponse.text();
		System.out.println(responseText);

		// Convert response text/json Response to POJO -- Deserialization
		System.out.println("Actual user from the Response");
		ObjectMapper objectMapper = new ObjectMapper();
		PojoLombok actualUser = objectMapper.readValue(responseText, PojoLombok.class);
		System.out.println(actualUser);

		// Assertions
		// supplied user details == response user details
		Assert.assertEquals(actualUser.getEmail(), pojoLo.getEmail());
		Assert.assertEquals(actualUser.getName(), pojoLo.getName());
		Assert.assertEquals(actualUser.getStatus(), pojoLo.getStatus());
		Assert.assertEquals(actualUser.getGender(), pojoLo.getGender());
		Assert.assertNotNull(actualUser.getId());

		/*
		 * //Capture id from post json Response //Fetching user id from JSON Response
		 * System.out.println(apiPostResponse.text()); JsonNode postJsonResponse =
		 * objectMapper.readTree(apiPostResponse.body()); String
		 * prettyResponseBody=postJsonResponse.toPrettyString();
		 * System.out.println(prettyResponseBody); String userId
		 * =postJsonResponse.get("id").asText(); System.out.println("user id : "+
		 * userId);
		 */

		// Fetching user id from Actual user Response Object
		String postUserId = actualUser.getId();
		System.out.println(" new user id captured from post response is : " + postUserId);

		// Update data , updating status to Stable
		pojoLo.setName(name + "QA");
		
		System.out.println("2. ******************* PUT CALL - Update User ***********************");
		APIResponse apiPutResponse = requestContext.put("https://gorest.co.in/public/v2/users/" + postUserId,
				RequestOptions.create().setHeader("Content-Type", "application/json; charset=utf-8")
						.setHeader("Authorization",
								"Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6")
						.setData(pojoLo));

		// Logging the full response for debugging
		System.out.println("Put Response status: " + apiPutResponse.status());
		System.out.println("Put Response body: " + apiPutResponse.text());
		Assert.assertEquals(apiPutResponse.status(), 200);
		Assert.assertEquals(apiPutResponse.statusText(), "OK");

		String putResponseText = apiPutResponse.text();
		System.out.println(putResponseText);
		PojoLombok actualPutUser = objectMapper.readValue(putResponseText, PojoLombok.class);
		Assert.assertEquals(actualPutUser.getId(), postUserId);
		Assert.assertEquals(actualPutUser.getName(), pojoLo.getName());

		// Fetching user id from Actual user Response Object
		String putUserId = actualPutUser.getId();
		System.out.println(" new user id captured from Put response is : " + putUserId);

		// Get Call : Fetch the new user id from put :
		System.out.println("3. *************** GET CALL response **************************");
		APIResponse apiGetResponse = requestContext.get("https://gorest.co.in/public/v2/users/" + putUserId,
				RequestOptions.create().setHeader("Content-Type", "application/json; charset=utf-8").setHeader(
						"Authorization", "Bearer 86ecc2c3f7f7647f49f07b169414fc61ffdfd0f5385977c798e96de46d1fd5f6"));

		System.out.println("Get response status :"+apiGetResponse.status());
		System.out.println("Get Response body: " + apiGetResponse.text());
		System.out.println("Get response statusText :"+apiGetResponse.statusText());
		Assert.assertTrue(apiGetResponse.text().contains(putUserId));
		
		String getResponseText = apiGetResponse.text();
		System.out.println(getResponseText);
		PojoLombok actualGettUser = objectMapper.readValue(getResponseText, PojoLombok.class);		
		Assert.assertEquals(actualGettUser.getId(), putUserId);
		Assert.assertEquals(actualGettUser.getName(), pojoLo.getName());
		String getUserId = actualGettUser.getId();
		System.out.println(" new user id captured from Get response is : " + getUserId);

	}

	@AfterTest
	public void tearDown() {
		playwright.close();
	}
}
