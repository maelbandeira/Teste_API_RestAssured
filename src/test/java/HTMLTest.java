import io.restassured.RestAssured;
import org.hamcrest.xml.HasXPath;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.HTML;
import static org.hamcrest.Matchers.*;

public class HTMLTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://restapi.wcaquino.me/";
        RestAssured.basePath = "v2/";
    //        RestAssured.port = 443;
    }
    @Test
    public void deveFazerBuscasHTML(){
        given()
            .log().all()
            .accept(HTML)
        .when()
            .get("users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(HTML)
            .body("html.body.div.table.tbody.tr.size()", is(3))  // Verficar quantidades de linha na tabela do html
            .body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
            .appendRootPath("html.body.div.table.tbody")
            .body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"));
    }
    @Test
    public void deveFazerBuscasXpathEmHTML(){
        given()
            .log().all()
            .accept(HTML)
        .when()
            .get("users?format=clean")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(HTML)
            .body(hasXPath("count(//table/tr)", is("4")))
            .body(hasXPath("//table/tr/td[text() = '2']/../td[2]", is("Maria Joaquina")))
                ;
    }

}

