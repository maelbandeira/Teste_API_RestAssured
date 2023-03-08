import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.path.xml.element.Node;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;

public class UsuarioXMLTest {

    public static RequestSpecification requestSpec;
    public static ResponseSpecification responseSpec;

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = "http://restapi.wcaquino.me";
//        RestAssured.port = "443";
//        RestAssured.basePath = "";

        RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        requestBuilder.log(LogDetail.ALL);
        requestSpec = requestBuilder.build();

        ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder();
        responseBuilder.expectStatusCode(200);
        responseSpec = responseBuilder.build();

        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }

    @Test
    public void deveTrabalharComXML() {
        given()
        .when()
            .get("/usersXML/3")
        .then()
                .rootPath("user")      // Nó Raiz
                .body("name", is("Ana Julia"))
                .body("@id", is("3")) // obs: para o XML tem que ser String
                .appendRootPath("filhos")    //  Adicionado o  filhos Nó Raiz == user.filhos
                .body("name[0]", is("Zezinho"))
                .body("name[1]", is("Luizinho"))
                .body("name", hasItem("Luizinho"))
                .body("name", hasItems("Luizinho", "Zezinho"));
    }
    @Test
    public void deveFazerPesquisasAvancadasComXML() {
        given()
        .when()
            .get("/usersXML")
        .then()
            .statusCode(200)
            .rootPath("users")
            .body("user.size()", is(3))
            .body("user.findAll{it.age.toInteger() <= 25}.size()",is(2))
            .body("user.@id", hasItems("1","2","3"))
            .body("user.find{it.age == 25}.name", is("Maria Joaquina"))
            .body("user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
            .body("user.salary.find{it != null}", is("1234.5678"))
            .body("user.salary.find{it != null}.toDouble()", is(1234.5678))
            .body("user.age.collect{it.toInteger() * 2}", hasItems(40, 60, 50))
            .body("user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}",is("MARIA JOAQUINA"))
                ;
    }
    @Test
    public void deveFazerPesquisasAvancadasComXMLEJava() {
        String name = given()
                .when()
                .get("/usersXML")
                .then()
                .statusCode(200)
                .extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}")
                ;
        Assert.assertTrue(name.equalsIgnoreCase("mAria JoAqUIna"));
        Assert.assertEquals("Maria Joaquina".toUpperCase(), name.toUpperCase());

    }
    @Test
    public void deveFazerPesquisasAvancadasComXMLEJavaUsandoArrayList() {
//        Object path = given()
        ArrayList<Node> names = given()// OBS>>>> importar a dependecia io.restassured.internal.path.xml.NodeImpl no Maven
                .when()
                .get("/usersXML")
                .then()
                .statusCode(200)
                .extract().path("users.user.name.findAll{it.toString().contains('n')}")
                ;
        Assert.assertEquals(2, names.size());
        Assert.assertEquals("Maria Joaquina".toUpperCase(), names.get(0).toString().toUpperCase());
        Assert.assertTrue("ANA julia".equalsIgnoreCase(names.get(1).toString()));
    }
    @Test
    public void deveFazerPesquisasAvancadasComXPath() {
        given()
                .when()
                .get("/usersXML")
                .then()
                .statusCode(200)
                .body(hasXPath("count(/users/user)", is("3")))
                .body(hasXPath("/users/user[@id = '1']"))
                .body(hasXPath("//user[@id = '1']"))
                .body(hasXPath("//age[text() = '30']"))
                .body(hasXPath("//name[text() = 'Zezinho']/../../name", is("Ana Julia")))
                .body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinh"),containsString("Luizinho"))))
                .body(hasXPath("/users/user/name", is("João da Silva")))
                .body(hasXPath("//name", is("João da Silva")))
                .body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
                .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
                .body(hasXPath("count(/users/user[contains(., 'n')])", is("2")))
                .body(hasXPath("count(/users/user[contains(., 'n')])", is("2")))
                .body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
                .body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
        ;
    }
}
