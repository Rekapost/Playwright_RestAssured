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

public class B01_TokenTest {

	//https://restful-booker.herokuapp.com/apidoc/index.html
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
	public void getTokenTest() throws IOException {
				// User data
				/*	{
					"username":"admin",
					"password":"password123"
					}
				*/
				//String json:
				String requestTokenJsonBody="{\r\n"
						+ "					\"username\":\"admin\",\r\n"
						+ "					\"password\":\"password123\"\r\n"
						+ "					}";

				System.out.println("*************************** Post call Response ****************************");		
				// API request
				APIResponse	apiPostTokenResponse=requestContext.post("https://restful-booker.herokuapp.com/auth", 
										RequestOptions.create()
												.setHeader("Content-Type", "application/json")
												.setData(requestTokenJsonBody)
												);
				
				// Logging the full response for debugging
			    System.out.println("Response status: " + apiPostTokenResponse.status());
			    System.out.println("Response body: " + apiPostTokenResponse.text());
			    
			    // Assertions
			    Assert.assertEquals(apiPostTokenResponse.status(), 200);
				Assert.assertEquals(apiPostTokenResponse.statusText(), "OK");
				
				//Capture TOKEN from post json Response"
			    System.out.println(apiPostTokenResponse.text());
			    ObjectMapper objectMapper= new ObjectMapper();
			    JsonNode postJsonTokenResponse = objectMapper.readTree(apiPostTokenResponse.body());
			    String prettyResponseBody=postJsonTokenResponse.toPrettyString();
			    System.out.println(prettyResponseBody);
			    String token =postJsonTokenResponse.get("token").asText();
			    System.out.println("Token from Response : "+ token);
			    Assert.assertNull(token);			
	}
	
	@AfterTest
	public void tearDown() {
		playwright.close();		
	}
}
