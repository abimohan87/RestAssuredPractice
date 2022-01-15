package restAssuredSample;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import org.testng.annotations.Test;

import data.Payload;
import groovy.json.JsonParser;

import static org.hamcrest.Matchers.*;

public class POSTaddLocationTest {

	@Test
	public void addPlaceTest() {
	
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
	//POST API to create/add entry...	
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>POST>>>>>>>>>>>>>>>>>>>>>>>>");
		String res = given()
		.queryParam("key", "qaclick123")
		.headers("Content-Type","application/json")
		.log().uri()
		.body(Payload.addPayload())
		.when()
		.post("maps/api/place/add/json")
		.then()
		.assertThat().statusCode(200)
		.body("status", equalTo("OK"))
		.body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.18 (Ubuntu)")
		.extract()
		.response().asPrettyString();
		
		System.out.println(res);
		
		JsonPath jp = new JsonPath(res);
		String placeId = jp.getString("place_id");
		
		System.out.println(">>>>Place_id created is : "+placeId);
		
		//PUT API to update the data
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>PUT>>>>>>>>>>>>>>>>>>>>>>>>");
		given()
		.queryParam("key", "qaclick123")
		.headers("Content-Type","application/json")
		.body(Payload.updatePayload(placeId))
		.log().uri()
		.when().put("maps/api/place/update/json")
		.then()
		.assertThat().statusCode(200)
		.body("msg", equalTo("Address successfully updated"))
		.log().body();
		
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>GET>>>>>>>>>>>>>>>>>>>>>>>>");
		
		String getResponse=given()
		.queryParam("place_id", placeId)
		.queryParam("key", "qaclick123")
		.headers("Content-Type","application/json")
		.log().uri()
		.when().get("maps/api/place/get/json")
		.then()
		.assertThat().statusCode(200)
		.body("address", equalTo("70 Summer lk, USA__UPDATED1"))
		.log().body().extract().response().asPrettyString();
		
		JsonPath jp1 = new JsonPath(getResponse);
		System.out.println("The updated address is : "+jp1.getString("address"));
	
	}
}
