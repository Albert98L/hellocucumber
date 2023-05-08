package hellocucumber;


import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class StepsWhen {
    StepsAnd stepsAnd = new StepsAnd();
    String usersPath = "/public-api/users";
    private static final String ACCESS_TOKEN = "47994804d41cf601b5c492687b32362386458f1035ccad3b0b4fac32a5977323";


    @When("we run GET users list request")
    public Response getUserListRequest() {
        return stepsAnd.validAccessToken().get(RestAssured.baseURI + usersPath);
    }

    @When("we run POST new user request")
    public void postNewUser() {
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .body(stepsAnd.followingUser())
                .when()
                .post("/public-api/users/");

        JsonPath body = response.jsonPath();
        List<Integer> ids = Collections.singletonList(body.getInt("data.id"));
        List<Integer> code = Collections.singletonList(body.getInt("code"));

        ApiRequestDataSingleton data = ApiRequestDataSingleton.getInstance(ids, code);
    }

    @When("we run PATCH user {}")
    public void runPatchRequest(String requestType) {
        ApiRequestDataSingleton2 data1 = ApiRequestDataSingleton2.getInstance();

        if (requestType.equals("name")) {
            Map<String, String> body = stepsAnd.updateUserName();
            if (!body.isEmpty()) {
                Response response = given()
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .patch("/public-api/users/" + data1.getIds().get(0));
                StepsThen.h = response.statusCode();
            }
        } else {
            Map<String, String> body = stepsAnd.setInactiveStatus();
            if (!body.isEmpty()) {
                Response response = given()
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .patch("/public-api/users/" + data1.getIds().get(0));
                StepsThen.h = response.statusCode();
            }
        }


    }
}
