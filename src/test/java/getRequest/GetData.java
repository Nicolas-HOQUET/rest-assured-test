package getRequest;

import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class GetData {
	
	String api="http://samples.openweathermap.org/data/2.5/weather?q=London,"
			+ "uk&appid=b6907d289e10d714a6e88b30761fae22 ";

	@Test
	public void testResponseCode() {
		int code = get(api).getStatusCode();
		System.out.println("Code: "+code);
		Assert.assertEquals(code,  200);
	}
	
	@Test
	public void testbody() {
		
		Long time=get(api).getTime();
		System.out.println("Response Time: "+time);
	}
}
