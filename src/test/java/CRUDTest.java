import br.com.curso.entidades.Usuario;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;
import static org.hamcrest.Matchers.*;

public class CRUDTest {

    public static RequestSpecification requestSpec;
    public static ResponseSpecification responseSpec;

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = "http://restapi.wcaquino.me/";
//        RestAssured.port = "443";
//        RestAssured.basePath = "";

        RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        requestBuilder.log(LogDetail.ALL);
        requestSpec = requestBuilder.build();

//        ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder();
//        responseBuilder.expectStatusCode(201);
//        responseSpec = responseBuilder.build();

        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
        }

    @Test
    public void deveSalvarUsuario(){
        given()
            .spec(requestSpec)
            .contentType(JSON)
            .body("{\"name\": \"Jose\", \"age\": 50}")
        .when()
            .post("users")
        .then()
            .statusCode(201)   // status created
            .log().all()
            .body("id", is(notNullValue()))   // pra vim não null
            .body("name", is("Jose"))
            .body("age", is(50));
    }

    @Test
    public void deveSalvarUsuarioUsandoMap(){
        Map<String, Object> params = new HashMap<String, Object>(); // Map é apenas interface
        params.put("name", "USuario map");
        params.put("age", 56);

        given()
            .spec(requestSpec)
            .contentType(JSON)
            .body(params)
         .when()
            .post("users")
        .then()
            .statusCode(201)   // status created
            .log().all()
            .body("id", is(notNullValue()))   // deve vim não null
            .body("name", is("USuario map"))
            .body("age", is(56));
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto(){
        Usuario usuario = new Usuario("Usuario via objeto", 37);
        given()
            .spec(requestSpec)
            .contentType(JSON)
            .body(usuario)
        .when()
            .post("users")
        .then()
            .statusCode(201)   // status created
            .log().all()
            .body("id", is(notNullValue()))   // deve vim não null
            .body("name", is(usuario.getName()))
            .body("age", is(37));
    }
    @Test
    public void deveDeserializarObjetoAoSalvarUsuario(){
        Usuario usuario = new Usuario("Usuario deserializado", 37);

        Usuario usuarioInserido =
          given()
            .spec(requestSpec)
            .contentType(JSON)
            .body(usuario)
        .when()
            .post("users")
        .then()
            .log().all()
            .statusCode(201)   // status created
            .extract().body().as(Usuario.class);

        Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(37));
    }
    @Test
    public void deveSalvarUsuarioViaXML(){
        given()
            .log().all()
            .contentType(XML)
            .body("<user><name>Dr Jose</name><age>61</age></user>")
        .when()
            .post("usersXML")
        .then()
            .log().all()
            .statusCode(201)   // status created
            .body("user.@id", is(notNullValue()))   // deve vim não null
            .body("user.name", is("Dr Jose"))
            .body("user.age", is("61"));
    }

    @Test
    public void deveDeserializarXMLAoSalvarUsuario(){
        Usuario usuario = new Usuario("Usuario XML", 43);

        Usuario usuarioInserido =
        given()
            .log().all()
            .contentType(XML)
            .body(usuario)
        .when()
            .post("usersXML")
        .then()
            .log().all()
            .statusCode(201)   // status created
            .extract().body().as(Usuario.class);

        Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertEquals("Usuario XML", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(43));

    }
    @Test
    public void deveSalvarUsuarioViaXMLUsandoObjeto(){
        Usuario usuario = new Usuario("Usuario XML", 43);

        given()
                .log().all()
                .contentType(XML)
                .body(usuario)
                .when()
                .post("usersXML")
                .then()
                .log().all()
                .statusCode(201)   // status created
                .body("user.@id", is(notNullValue()))   // deve vim não null
                .body("user.name", is("Usuario XML"))
                .body("user.age", is("43"));
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){
        given()
            .spec(requestSpec)
            .contentType("application/json")
            .body("{\"age\": \"50\"}")
        .when()
            .post("users")
        .then()
            .statusCode(400)   // status BAD Request
            .log().all()
            .body("id", is(nullValue())) // deve vim null
            .body("error", is("Name é um atributo obrigatório"));
    }
    @Test
    public void deveAlterarUsuario(){
        given()
            .spec(requestSpec)
            .contentType(JSON)
            .body("{\"name\": \"Outro User\", \"age\": 20}")
        .when()
            .put("users/1")
        .then()
            .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Outro User"))
                .body("age", is(20));
    }

    @Test
    public void deveCustomizarURL(){
        given()
            .spec(requestSpec)
            .contentType(JSON)
            .body("{\"name\": \"Outro User\", \"age\": 20}")
        .when()
            .put("{entidade}/{userId}", "users", "1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Outro User"))
            .body("age", is(20));
    }
    @Test
    public void deveCustomizarURLOutraForma(){
        given()
            .spec(requestSpec)
            .contentType(JSON)
            .body("{\"name\": \"Outro User\", \"age\": 20}")
            .pathParams("entidade", "users")
            .pathParams("userId", "1")
        .when()
            .put("{entidade}/{userId}", "users", "1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Outro User"))
            .body("age", is(20));
    }
    @Test
    public void deveDeletarUsuario(){
        given()
            .spec(requestSpec)
        .when()
            .delete("{entidade}/{userId}", "users", "1")
        .then()
            .log().all()
            .statusCode(204);
    }

    @Test
    public void naoDeveDeletarUsuarioInexistente(){
        given()
            .spec(requestSpec)
            .contentType(JSON)
        .when()
            .delete("{entidade}/{userId}", "users", "10000")
        .then()
            .log().all()
            .statusCode(400)
            .body("error", is("Registro inexistente"))
            ;
    }
}

