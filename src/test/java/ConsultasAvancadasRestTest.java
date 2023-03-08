import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ConsultasAvancadasRestTest {

    @Test
    public void deveFazerVerificacao(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(3))
                .body("age.findAll{it <= 25}.size()",is(2))  // Varrendo idade ate 25 anos
                .body("age.findAll{it <= 25 && it > 20}.size()",is(1))   // Varrendo idade de 20 ate 25 anos

                .body("findAll{it.age <= 25 && it.age > 20}.name",hasItem("Maria Joaquina"))
                .body("findAll{it.age <= 25}[0].name",is("Maria Joaquina"))
                .body("findAll{it.age <= 25}[-1].name",is("Ana Júlia"))
                .body("find{it.age <= 25}.name",is("Maria Joaquina"))
                .body("findAll{it.name.contains('n')}.name",hasItems("Maria Joaquina","Ana Júlia"))
                .body("findAll{it.name.length() > 10}.name",hasItems("João da Silva", "Maria Joaquina"))
                .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}",hasItems("MARIA JOAQUINA"))
                .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()",allOf(arrayContaining("MARIA JOAQUINA"), (arrayWithSize(1))))
                .body("age.collect{it * 2}", hasItems(60, 50, 40)) // Valida a idade multiplicado por 2
                .body("id.max()", is(3))  // Valida id maximo
                .body("salary.min()", is(1234.5678f))    // Valida salary min
                .body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))    // Valida soma dos salarios que são diferente de null com margem de erro
                .body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)));   // Valida soma dos salarios maior que 3k e menor que 5k
    }

    @Test
    public void deveUnirJsonPathComJava() {
        ArrayList<String> names =
        given()
        .when()
            .get("http://restapi.wcaquino.me/users")
        .then()
            .statusCode(200)
            .extract().path("name.findAll{it.startsWith('Maria')}");

        Assert.assertEquals(1,names.size());
        Assert.assertTrue(names.get(0).equalsIgnoreCase("mArIa JoAqUIna"));
        Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
    }


}
