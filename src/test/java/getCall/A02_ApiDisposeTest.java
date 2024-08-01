package getCall;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

public class A02_ApiDisposeTest {
	APIRequestContext requestContext ;
	APIRequest apiRequest;
	Playwright playwright;
	APIResponse apiResponse1;
	APIResponse apiResponse2;
	
	@BeforeTest
	public void setUp() {
		 playwright = Playwright.create();
		 apiRequest = playwright.request();
		 requestContext = apiRequest.newContext();
	}

	@Test
	public void disposeResponseTest() {
// to free up memory space , we can dispose body after each run	
		//REQUEST -1 
		apiResponse1 = requestContext.get("https://gorest.co.in/public/v2/users");
		int statusCode = apiResponse1.status();
		System.out.println("Response Status Code :" + statusCode);
		Assert.assertEquals(statusCode, 200);
		String statusText = apiResponse1.statusText();
		System.out.println("Response Status Text :" + statusText);
		System.out.println(apiResponse1.text());
		Assert.assertEquals(apiResponse1.ok(), true);
		System.out.println(" Api URL : " + apiResponse1.url());
	
		apiResponse1.dispose();  // will dispose only response body, but status code , url and status text remains same 
		
		int statusCode1 = apiResponse1.status();
		System.out.println("Response Status Code after dispose :" + statusCode1);
		Assert.assertEquals(statusCode1, 200);
		String statusText1 = apiResponse1.statusText();
		System.out.println("Response Status Text after dispose:" + statusText1);
		System.out.println("Api URL : " + apiResponse1.url());
		
		try {
			String responseBody1=apiResponse1.text();
			System.out.println("API Response after dispose : " +responseBody1);
			//FAILED: api.tests.ApiDisposeTest.disposeResponseTest
			//com.microsoft.playwright.PlaywrightException: Response has been disposed
		}
		catch(Exception e) {
			System.out.println(" --------Exception for test case 1-----------------");
			System.out.println("Api response body is disposed");
			
		}	
	}
	
	@Test
	public void requestContextDisposeTest() {
		//REQUEST -2  Request context dispose
		apiResponse2 = requestContext.get("https://gorest.co.in/public/v2/users/7247579");
		System.out.println("*********************************************** ");
		System.out.println("Get response body for 2nd request");
		
		System.out.println("Status code2 : "+apiResponse2.status());
		System.out.println("Response Body2 : "+apiResponse2.text());
		
		System.out.println(" --------Request context dispose -----------------");
		requestContext.dispose();
		System.out.println(" After Request Context Dispose ");
		
		int statusCode2 = apiResponse2.status();
		System.out.println("Response Status Code 2 after dispose :" + statusCode2);
		Assert.assertEquals(statusCode2, 200);
		String statusText2 = apiResponse2.statusText();
		System.out.println("Response Status Text 2 after dispose:" + statusText2);
		System.out.println("Api URL 2: " + apiResponse2.url());
		
		//System.out.println("Response Body 1 After Request Context Dispose: "+apiResponse1.text());
		System.out.println("Response Body 2 After Request Context Dispose : "+apiResponse2.text());
		System.out.println("Api response body is disposed Exception");
	}
		
	@AfterTest
	public void tearDown() {
		playwright.close();		
	}
}
