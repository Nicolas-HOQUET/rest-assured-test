package getRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;


import org.json.*;  

import static org.hamcrest.Matchers.equalTo;


public class graphQlRequest {
	@BeforeClass
	public static void  createRequestSpecification() {
		
		requestSpecification = new RequestSpecBuilder().
				setBaseUri("http://localhost").
				setPort(4000).
				build();
	}
	
	
	String query="{\"query\":\"query GET_PRODUCTS($selectedFinanceFilters: ProductFinanceInput) {\\n  products(\\n    categoryHandle: \\\"car-finder\\\"\\n    productFinanceInput: $selectedFinanceFilters\\n  ) {\\n    name\\n    id\\n    finance(productFinanceInput: $selectedFinanceFilters) {\\n      monthlyPrice\\n    }\\n  }\\n}\",\"variables\":{\"selectedFinanceFilters\":{\"term\":12,\"deposit\":1000,\"financeType\":\"pcp\",\"mileage\":10000}}}";

	String expectedResponse="{\"data\":{\"products\":[{\"name\":\"Landrover\",\"id\":\"1\",\"finance\":{\"monthlyPrice\":100}},{\"name\":\"Jaguar\",\"id\":\"2\",\"finance\":{\"monthlyPrice\":100}},{\"name\":\"Ford\",\"id\":\"3\",\"finance\":{\"monthlyPrice\":100}}]}}\n";
	
	JSONObject json = new JSONObject(expectedResponse);
	
	@Test
	public void TestResponseCode() {
		given()
			.spec(requestSpecification)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(query)
			.post("/graphql")
			.then()
			.statusCode(200).log().all();
	}
	
	@Test
	public void TestResponseFields() {
		given()
			.contentType(ContentType.JSON)
			.body(query)
			.when()
			.post("http://localhost:4000")
			.then()
			.assertThat()
			.body("data.products.name[0]", equalTo("Landrover"))
			.body("data.products.id[0]", equalTo("1"))
			.body("data.products.finance.monthlyPrice[0]", equalTo(100))
			.body("data.products.name[1]", equalTo("Jaguar"))
			.body("data.products.id[1]", equalTo("2"))
			.body("data.products.finance.monthlyPrice[1]", equalTo(100))
			.body("data.products.name[2]", equalTo("Ford"))
			.body("data.products.id[2]", equalTo("3"))
			.body("data.products.finance.monthlyPrice[2]", equalTo(100));
	}
	
	@Test
	public void Get_Products() {
		LogConfig logconfig = new LogConfig().enableLoggingOfRequestAndResponseIfValidationFails().enablePrettyPrinting(true);
		RestAssured.config().logConfig(logconfig);
		
		ExtractableResponse<Response> res = given()
			.spec(requestSpecification)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(query)
			.when()
			.post("http://localhost:4000")
			.then()
			.extract();
		
		String msg1 = "Get_Products before print";
		String msg2 = "Get_Products after print";

		System.out.println(msg1);
		System.out.println(res.asString());
		System.out.println(msg2);
		
		Assert.assertEquals(res.asString().equals(expectedResponse), true);
			
	}
	
	@Test
	public void Get_Finance_Methods() {
		LogConfig logconfig = new LogConfig().enableLoggingOfRequestAndResponseIfValidationFails().enablePrettyPrinting(true);
		RestAssured.config().logConfig(logconfig);
		
		String queryString = "{\"query\":\"query GET_FINANCE_METHODS {\\n  financeMethods {\\n    type\\n    deposits\\n    terms\\n    mileages\\n  }\\n}\",\"variables\":{}}";
		
		String expectedResponse="{\"data\":{\"financeMethods\":[{\"type\":\"pcp\",\"deposits\":[1000,2000,3000,4000,5000],\"terms\":[6,12,18,24],\"mileages\":[10000,20000,30000,40000]},{\"type\":\"hp\",\"deposits\":[1000,2000,3000,4000,5000],\"terms\":[6,12,18,24],\"mileages\":[20000,30000,40000,50000]}]}}\n";
		
		ExtractableResponse<Response> res = given()
			.spec(requestSpecification)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(queryString)
			.when()
			.post("http://localhost:4000")
			.then()
			.extract();
		
		String msg1 = "Get Finance before print";
		String msg2 = "Get Finance after print";
		
		System.out.println(msg1);
		System.out.println(res.asString());
		System.out.println(expectedResponse);
		System.out.println(msg2);
		
		Assert.assertEquals(res.asString().equals(expectedResponse), true);
			
	}
}
