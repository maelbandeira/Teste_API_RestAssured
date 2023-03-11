import io.restassured.matcher.RestAssuredMatchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import static io.restassured.RestAssured.*;

public class SchemaTest {

    @BeforeClass
    public static void setup() {
        baseURI = "http://restapi.wcaquino.me/";
        basePath = "v2/";
        port = 443;
    }

    @Test
    public void deveValildarSchemaXML() {

        // Obs: primeiro tera que converter o xml para um arquivo do tipo "xsd"

        given()
            .log().all()
        .when()
            .get("usersXML")
        .then()
            .log().all()
            .statusCode(200)
            .body(RestAssuredMatchers.matchesXsdInClasspath("nome do arquivo em xsd"))
        ;
    }

    @Test (expected = SAXParseException.class) // esperado uma exceção do tipo SAXParseException
    public void naoDeveValildarSchemaXMLInvalido() {
        given()
            .log().all()
        .when()
            .get("invalidUsersXML")
        .then()
            .log().all()
            .statusCode(200)
            .body(RestAssuredMatchers.matchesXsdInClasspath("nome do arquivo em xsd"))
        ;
    }
}
