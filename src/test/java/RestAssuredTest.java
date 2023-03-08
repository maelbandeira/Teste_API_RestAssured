import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RestAssuredTest {

    @Test
    public void testRest() {
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue("O status code deveria ser 200", response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }
    @Test
    public void testRestOutraForma() {
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("http://restapi.wcaquino.me/ola").then().statusCode(200);
    }

    @Test
    public void testBoasPraticasRest() {
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("http://restapi.wcaquino.me/ola").then().statusCode(200);

        //Pre condiçoes
        given()
         //Açao
        .when()
            .get("http://restapi.wcaquino.me/ola")
        //Assertivos
        .then()
            .statusCode(200);

    }

    @Test
    public void devoConhecerMatchersHamcrest() {

        assertThat("Maria", Matchers.is("Maria")); // Valida que o nome eh o esperado
        assertThat(128, Matchers.is(128));

        assertThat("Maria", Matchers.isA(String.class)); // Valida o tipo
        assertThat(128, Matchers.isA(Integer.class));
        assertThat(128d, Matchers.isA(Double.class));

        assertThat(128, Matchers.greaterThan(120)); // Valida se o atual eh maior que value

        List<Integer> impares = Arrays.asList(1,3,5,7,9); // collection

        assertThat(impares, Matchers.hasSize(5));       // Valida o tamanho da collction
        assertThat(impares, contains(1,3,5,7,9));      // Valida se tem todos os elementos da lista
        assertThat(impares, hasItem(9));               // Valida se tem apenas 1 elementos da lista
        assertThat(impares, hasItems(1,9));            // Valida se tem os 2 elementos da lista

        assertThat("Maria", not("Joao"));   // Valida que o nome atual nao eh o esperado
        assertThat("Maria", anyOf(is("Joao"),is("Joaquina")));   //  Condiçao "OU" apenas uma verdadeira
        assertThat("Maria", anyOf(startsWith("Ma")));   //  Se o nome começa com
        assertThat("Maria", anyOf(endsWith("ria")));   //  Se o nome começa termina com
        assertThat("Maria", anyOf(containsString("ar")));   //  Contains String

        assertThat("Maria", allOf(startsWith("Ma"), endsWith("ria"), containsString("ar")));   //  Condiçao "E" todas verdadeiras
    }

    @Test
    public void deveValidarBody() {
        //Pre condiçoes
        given()
        //Açao
        .when()
            .get("http://restapi.wcaquino.me/ola")
        //Assertivos
        .then()
            .statusCode(200)  // Valida o status code
            .body(is("Ola Mundo!"))   // Valida o corpo do html se esta vindo essa msg
            .body(containsString("Mundo"))   // Valida o corpo do html contains a palavra
            .body(is(notNullValue()));   // Valida o corpo do html não esta nulo
    }
}
