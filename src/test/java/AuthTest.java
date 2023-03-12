import io.restassured.path.xml.XmlPath;
import jdk.nashorn.internal.scripts.JD;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;
import static org.hamcrest.Matchers.is;

public class AuthTest {

    @BeforeClass
    public static void setup() {
//        baseURI = "http://restapi.wcaquino.me/";
//        basePath = "api/";
//        port = 443;
    }

    @Test
    public void deveAcessarSWAPI() {
        given()
            .log().all()
        .when()
            .get("https://swapi.dev/api/people/1")// site apenas para teste
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("Luke Skywalker"))
        ;
    }

    @Test
    public void naoDeveAcessarSemSenha() {
        given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/basicauth")// site apenas para teste
        .then()
            .log().all()
            .statusCode(401) // Não autorizado
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica() {
        given()
            .log().all()
        .when()
            .get("http://admin:senha@restapi.wcaquino.me/basicauth")// site apenas para teste
        .then()
            .log().all()
            .statusCode(200) // 401 Não autorizado
            .body("status", is("logado")) // 401 Não autorizado
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica2() {
        given()
            .log().all()
                .auth().basic("admin", "senha")
        .when()
            .get("http://restapi.wcaquino.me/basicauth")// site apenas para teste
        .then()
            .log().all()
            .statusCode(200) // 401 Não autorizado
            .body("status", is("logado")) // 401 Não autorizado
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasicaChallange() { // ou chamado de primitivo
        given()
            .log().all()
            .auth().preemptive().basic("admin", "senha")
        .when()
            .get("http://restapi.wcaquino.me/basicauth2")// site apenas para teste
        .then()
            .log().all()
            .statusCode(200) // 401 Não autorizado
            .body("status", is("logado")) // 401 Não autorizado
        ;
    }

    @Test
    public void deveFazerAutenticacaoComTokenJWT() {
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "seu email");
        login.put("senha", "sua senha");

        //Enviando requisição para logar na aplicação de pegando o token
        String token = given()
            .log().all()
            .body(login)
            .contentType(JSON)
        .when()
            .post("http://barrigarest.wcaquino.me/signin")// site apenas para teste
        .then()
            .log().all()
            .statusCode(200) // 401 Não autorizado
            .body("status", is("logado")) // 401 Não autorizado
            .extract().path("token");

        //Pegando requisições logado com o token
        given()
            .log().all()
            .body(login)
            .header("Authorization", "JWT " + token)
        .when()
            .get("http://barrigarest.wcaquino.me/contas")// site apenas para teste
        .then()
            .log().all()
            .statusCode(200)
        ;
    }

    @Test
    public void deveAcessarAplicacaoWeb() {
        String cookie = given()
            .log().all()
            .formParam("attributo email", "seu email")
            .formParam("attributo senha ", "sua senha")
            .contentType(URLENC.withCharset("UTF-8")) // content type url encoded
        .when()
            .post("http://seubarriga.wcaquino.me/logar") // site apenas para teste
        .then()
            .log().all()
            .statusCode(200)
                .extract().header("set-cookie")
        ;

        cookie = cookie.split("=")[1].split(";")[0] ;//metodo split() da Sering que faz a divisão de acordo com o caracter enviado

      String body = given()
            .log().all()
           .cookie("connect.sid", cookie)
        .when()
            .get("http://seubarriga.wcaquino.me/contas") // site apenas para teste
        .then()
            .log().all()
            .statusCode(200)
            .body("html.body.table.tbody.tr[0].td[0]", is("Conta de teste"))
            .extract().body().asString()   // metodo asString() faz com extraia o corpo body para string
       ;

        XmlPath xmlPath = new  XmlPath(XmlPath.CompatibilityMode.HTML, body);
        System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
    }
}
