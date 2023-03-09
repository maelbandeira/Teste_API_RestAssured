import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;

public class EnvioDadosQueryTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://restapi.wcaquino.me/";
        RestAssured.basePath = "v2/";
//        RestAssured.port = "443";
    }

    @Test
    public void empldwqdoSDs(){

        given()
        .when()
        .then()
        ;

    }
    @Test
    public void deveEnviarValorViaQuery(){
        given()
            .log().all()
        .when()
            .get("users?format=json")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(JSON);
    }
    @Test
    public void deveEnviarValorViaQueryViaParam(){
        given()
            .log().all()
            .queryParam("format","xml" )
        .when()
            .get("users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(XML)
            .contentType(Matchers.containsString("utf-8"));
    }
}
