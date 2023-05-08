package hellocucumber;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;


public class StepsThen {
    StepsWhen stepsWhen = new StepsWhen();
    public static int h;
    private static final String ACCESS_TOKEN = "47994804d41cf601b5c492687b32362386458f1035ccad3b0b4fac32a5977323";
    @Then("assert that response status code is 200 for Get user request")
    public void responseStatusCodeAssertion() {
        int h = stepsWhen.getUserListRequest().statusCode();
        Assert.assertEquals(h, 200);
    }

    @Then("assert that the Location header contains the URL pointing to the newly created resource")
    public void assertionNewUserCreated() throws Exception {
    }

    @Then("assert that response status code is 200 for PATCH user request")
    public void patchResponseStatusCodeAssertion() {
        System.out.println(h);
        Assert.assertEquals(h, 200);
    }

    @Then("assert that user status has updated to \"Inactive\"")
    public void statusUpdatedAssertion() {
        ApiRequestDataSingleton2 data1 = ApiRequestDataSingleton2.getInstance();
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .when()
                .get("/public-api/users/" + data1.getIds().get(0));
        response.print();


    }


}

