package getCall;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;

public class A03_ApiResponseHeadersTest {
	APIRequestContext requestContext ;
	APIRequest apiRequest;
	Playwright playwright;
	APIResponse apiResponse;
	
	@BeforeTest
	public void setUp() {
		 playwright = Playwright.create();
		 apiRequest = playwright.request();
		 requestContext = apiRequest.newContext();
	}
	
	@Test
	public void getHeadersTest()
	{
		apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");
		
		int statusCode = apiResponse.status();
		System.out.println("Response Status Code :" + statusCode);
		Assert.assertEquals(statusCode, 200);

		//Method 1: Using Map   with Map , Assertion is easy 
		Map<String, String> headersMap = apiResponse.headers();
		//System.out.println("Header Map :" + headersMap);
		headersMap.forEach((k,v)-> System.out.println(k + ":"+ v));
		System.out.println("Total response headers :"+ headersMap.size());
		Assert.assertEquals(headersMap.get("content-type"), "application/json; charset=utf-8");

		//Method 2: Using List
		System.out.println("================================");
		List <HttpHeader> headersArrayList=apiResponse.headersArray();
		for(HttpHeader e : headersArrayList) {
			System.out.println(e.name + ":" + e.value);
		}
	}
	
	@AfterTest
	public void tearDown() {
		playwright.close();		
	}
}
