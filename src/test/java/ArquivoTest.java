import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ArquivoTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://restapi.wcaquino.me/";
//        RestAssured.basePath = "v2/";
        //        RestAssured.port = "443";
    }
    @Test
    public void deveObrigarEnviarArquivo() {
        given()
            .log().all()
        .when()
            .post("upload")
        .then()
            .log().all()
            .statusCode(404)  // Deveria ser 400
                .body("error", is("Arquivo não enviado"))
            ;
    }
    @Test
    public void deveFazerUploadArquivo() {
        given()
            .log().all()
        .when()
            .multiPart("arquivo", new File("path do arquivo Ex: src/main.resources/nome do arquivo"))
            .post("upload")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("nome do arquivo"))
        ;
    }

    @Test
    public void naoDeveFazerUploadArquivoGrande() {
        given()
            .log().all()
        .when()
            .multiPart("arquivo", new File("path do arquivo Ex: src/main.resources/nome do arquivo"))
            .get("upload")
        .then()
            .log().all()
            .time(lessThan(5000L)) // Define o tempo e não considerado como teste de performance
            .statusCode(200) // 413 Request Entity Too Large
            .body("name", is("nome do arquivo"))
        ;
    }

    @Test
    public void DeveFazerDownloadArquivo() throws IOException {

        byte[] imagem =
                given()
                    .log().all()
                .when()
                    .multiPart("arquivo", new File("path do arquivo Ex: src/main.resources/nome do arquivo"))
                    .get("download")
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract().asByteArray();// Extraindo para um Array

        File imagens = new File("path do arquivo Ex: src/main.resources/nome do arquivo"); //faz o download do arquivo tipo imagem
        OutputStream saidaImagem = new FileOutputStream(imagens);  //Prepara o arquivo do tipo imagem
        saidaImagem.write(imagem); // monta a imagem
        saidaImagem.close();

        System.out.println(imagens.length()); // Usar so para verificar o tamanho da imagem

        Assert.assertThat(imagens.length(), lessThan(100000L));  // assertiva deve ser menor que 100k o tamanho
    }
}
