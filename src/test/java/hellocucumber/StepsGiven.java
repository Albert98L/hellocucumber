package hellocucumber;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;


public class StepsGiven {

    @Given("gorest.co.in server is working.")
    public void isServerWorking() {
        RestAssured.baseURI = "https://gorest.co.in";
        RequestSpecification request = RestAssured.given();
        Response response = request.get();


        System.out.println(response.getStatusCode());

    }

}
