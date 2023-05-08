package hellocucumber;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.sql.*;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class StepsAnd {

    private static final String ACCESS_TOKEN = "47994804d41cf601b5c492687b32362386458f1035ccad3b0b4fac32a5977323";
    private static final String DB_URL = "jdbc:mysql://localhost:3006/gorestdb1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "albert122298++";
    public RequestSpecification request;

    public Map<String, Object> DATA = new HashMap<>();

    @And("we enter valid access token for Authorization")
    public RequestSpecification validAccessToken() {
        return request = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json");

    }

    @And("assert that there are 10 users data in response")
    public void assertion10Users() {
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/public-api/users");
        JsonPath body = response.jsonPath();
        List<Integer> ids = body.getList("data.id");
        System.out.println(" Number of users " + ids.size());

    }

    @And("we store all Users data in MySql database")
    public void storeUsersDataInDatabase() {
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .when()
                .get("/public-api/users");

        response.statusCode();

        JsonPath body = response.jsonPath();
        List<Integer> ids = body.getList("data.id");
        List<String> username = body.getList("data.name");
        List<String> email = body.getList("data.email");
        List<String> gender = body.getList("data.gender");
        List<String> status = body.getList("data.status");

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            try (Statement ignored = connection.createStatement()) {
                String insertQuery = "INSERT INTO user (UserID, name, email, gender, status) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                for (int i = 0; i < ids.size(); i++) {
                    preparedStatement.setInt(1, ids.get(i));
                    preparedStatement.setString(2, username.get(i));
                    preparedStatement.setString(3, email.get(i));
                    preparedStatement.setString(4, gender.get(i));
                    preparedStatement.setString(5, status.get(i));
                    preparedStatement.executeUpdate();
                }
                System.out.println("User data inserted into MySQL table.");
            }
        } catch (Exception e) {
            System.out.println("Error inserting user data into MySQL table: " + e.getMessage());
        }
    }

    @And("assert that 'id, name, email, gender, status, created_at, updated_at' data is shown for all existing users")
    public void assertDataForAllExistingUsers() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        while (resultSet.next()) {
            String id = resultSet.getString("data.id");
            String name = resultSet.getString("data.name");
            String email = resultSet.getString("data.email");
            String gender = resultSet.getString("data.gender");
            String status = resultSet.getString("data.status");
            String created_at = resultSet.getString("created_at");
            String updated_at = resultSet.getString("updated_at");
            assertThat(id, notNullValue());
            assertThat(name, notNullValue());
            assertThat(email, notNullValue());
            assertThat(gender, notNullValue());
            assertThat(status, notNullValue());
            assertThat(created_at, notNullValue());
            assertThat(updated_at, notNullValue());
        }
    }

    @And("we remove all stored data from database")
    public void deleteAllUsers() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM `gorestdb1`.`user` WHERE (`UserID` >= 0)");
        connection.close();
    }

    @And("we provide following user details: Name - \"Poxos Poxosyan\", Mail - \"poxos@example.com\", Gender - \"Male\", Status - \"Active\"")
    public Map<String, String> followingUser() {
        Map<String, String> user = new HashMap<>();
        user.put("name", "Poxos Poxosyan");
        user.put("email", "poxoos@example.com");
        user.put("gender", "Male");
        user.put("status", "Active");
        return user;
    }


    @And("assert that response status code is 201 for Post user request")
    public void checkPostResponseStatusCode() {
        ApiRequestDataSingleton data = ApiRequestDataSingleton.getInstance();
        System.out.println(data.getCode().get(0));
    }

    @And("we run GET new created user request")
    public void weRunGETNewCreatedUserRequest() {
        ApiRequestDataSingleton data = ApiRequestDataSingleton.getInstance();
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .when()
                .get("/public-api/users/" + data.getIds().get(0));

        System.out.println(response.getStatusCode());
    }

    @And("assert that response status code is 200 for Get new user request")
    public void statusCode200Assertion() {
        ApiRequestDataSingleton data = ApiRequestDataSingleton.getInstance();
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .when()
                .get("/public-api/users/" + data.getIds().get(0));

        System.out.println(response.getStatusCode());
        ;

    }

    @And("assert that created user has details: Name - \"Poxos Poxosyan\", Mail - \"poxos@example.com\", Gender - \"Male\", Status - \"Active\"")
    public void userDataAssertion() {
        ApiRequestDataSingleton data = ApiRequestDataSingleton.getInstance();
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .when()
                .get("/public-api/users/" + data.getIds().get(0));
        response.print();

    }

    @And("we remove new created user")
    public List<Integer> removeNewUsers() {
        ApiRequestDataSingleton data = ApiRequestDataSingleton.getInstance();
        ApiRequestDataSingleton2 data1 = ApiRequestDataSingleton2.getInstance();
        List<Integer> userIds = new ArrayList<>();

        if (data1 != null && data1.getIds() != null) {
            userIds.addAll(data1.getIds());
        }
        if (data != null && data.getIds() != null) {
            userIds.addAll(data.getIds());
        }

        List<Integer> deleteCodes = new ArrayList<>();
        for (Integer userId : userIds) {
            Response response = given()
                    .header("Authorization", "Bearer " + ACCESS_TOKEN)
                    .header("Content-Type", "application/json")
                    .when()
                    .delete("/public-api/users/" + userId);

            JsonPath body = response.jsonPath();
            deleteCodes.add(body.getInt("code"));
        }

        DATA.put("deleteCode", deleteCodes);
        return deleteCodes;
    }

    @And("assert that response status code is 204 for delete user request")
    public void userDeletedAssertion() {
        System.out.println(DATA.get("deleteCode"));
    }

    @And("we assert that user is no longer exist")
    public void userDeletedAssertion404() {
        System.out.println(removeNewUsers().get(0));

    }

    @And("test data ")
    public Map<String, String> testData() {
        Map<String, String> test = new HashMap<>();
        test.put("name", "Test");
        test.put("email", "test@exampl.com");
        test.put("gender", "Male");
        test.put("status", "Active");

        return test;
    }

    @And("user with name \"Test\" is created in users list")
    public void createTestUser() {
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .body(testData())
                .when()
                .post("/public-api/users/");

        response.print();
        JsonPath body = response.jsonPath();
        List<Integer> ids = Collections.singletonList(body.getInt("data.id"));
        ApiRequestDataSingleton2 data1 = ApiRequestDataSingleton2.getInstance(ids);

    }

    @And("we enter \"NewTest\" name for PATCH user request")
    public Map<String, String> updateUserName() {
        Map<String, String> newTest = new HashMap<>();
        newTest.put("name", "NewTest");

        return newTest;
    }

    @And("assert that user name has updated to \"NewTest\"")
    public void userNameUpdatedAssertion() {
        ApiRequestDataSingleton2 data1 = ApiRequestDataSingleton2.getInstance();
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .when()
                .get("/public-api/users/" + data1.getIds().get(0));
        response.print();

    }

    @And("we run GET new test created user request")
    public void weRunGETNewTestUserRequest() {
        ApiRequestDataSingleton2 data1 = ApiRequestDataSingleton2.getInstance();
        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .when()
                .get("/public-api/users/" + data1.getIds().get(0));

        System.out.print(response.getStatusCode());

    }

    @And("we enter user's Inactive status for PATCH user request")
    public Map<String, String> setInactiveStatus() {
        Map<String, String> body = new HashMap<>();
        body.put("status", "inactive");

        return body;
    }

}

