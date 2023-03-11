import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;
import static org.hamcrest.Matchers.*;

public class EnviarDadosViaQueryTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://restapi.wcaquino.me/";
        RestAssured.basePath = "v2/";
//        RestAssured.port = "443";
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
            .contentType(containsString("utf-8"));
    }
    @Test
    public void deveEnviarValorViaQueryViaHeader(){
        given()
            .log().all()
        .when()
            .accept(HTML)
            .get("users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(HTML)
        ;
    }
}
