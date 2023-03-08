import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UsuarioJsonTest {

    @Test
    public void deveVerificarPrimeiroNivel() {
        given()  //Pre condiçoes
        .when()   //Açao
            .get("http://restapi.wcaquino.me/users/1")
        .then() //Assertivos
            .statusCode(200)  // Valida o status code
            .body("id",is(1))   // Valida se o atributo esta vindo com id = 1
            .body("name",containsString("Silva"))   // Valida se o atributo "name" contains nome Silva
            .body("age", greaterThan(20));   // Valida o atributo "age" eh maior que 18 anos
    }
    @Test
    public void deveVerificarPrimeiroNivelOutraForma() {
        Response response = RestAssured.request(Method.GET,"http://restapi.wcaquino.me/users/1");
        // path
        Assert.assertEquals(new Integer(1),response.path("id"));
        Assert.assertEquals(new Integer(1),response.path("%s","id"));

        // JsonPath
        JsonPath jpath = new JsonPath(response.asString());
        Assert.assertEquals(1, jpath.getInt("id"));

        // from
        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);
    }

    @Test
    public void deveVerificarSegundoNivel() {
        given()  //Pre condiçoes
        .when()   //Açao
            .get("http://restapi.wcaquino.me/users/2")
        .then() //Assertivos
            .statusCode(200)  // Valida o status code
            .body("name",containsString("Joaquina"))   // Valida se o atributo "name" contains nome Silva
            .body("endereco.rua", is("Rua dos bobos"));   // Valida o atributo "rua" dentro de endereço
    }

    @Test
    public void deveVerificarList(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users/3")
        .then()
            .statusCode(200)
            .body("name",containsString("Ana"))
            .body("filhos", hasSize(2))
            .body("filhos[0].name", is("Zezinho"))
            .body("filhos[1].name", is("Luizinho"))
            .body("filhos.name", hasItem("Luizinho"))
            .body("filhos.name", hasItems("Zezinho", "Luizinho"));
    }
    @Test
    public void deveRetornarErrorUsuarioInexistente(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users/4")
        .then()
            .statusCode(404)
                .body("error", is("Usuário inexistente"));
    }

    @Test
    public void deveVerificarListaJson(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users")
        .then()
            .statusCode(200)
            .body("$", hasSize(3))
            .body("name", hasItems("João da Silva", "Maria Joaquina","Ana Júlia"))
            .body("age[1]", is(25))
            .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho"))); // Valida se contains Zezinho no Array list
    }


}
