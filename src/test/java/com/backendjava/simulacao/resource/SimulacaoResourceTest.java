package com.backendjava.simulacao.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SimulacaoResourceTest {

    @Test
    void deveCriarSimulacaoComStatus201() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 3
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("valorTotalFinal", equalTo(1045.68f))
                .body("valorTotalJuros", equalTo(45.68f))
                .body("memoriaCalculo", hasSize(3));
    }

    @Test
    void deveValidarPayloadComStatus400() {
        String payload = """
                {
                  "valorInicial": 0,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 0
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400);
    }

    @Test
    void deveRetornar404QuandoIdNaoExiste() {
        given()
                .when()
                .get("/simulacoes/99999")
                .then()
                .statusCode(404);
    }

    @Test
    void deveBuscarSimulacaoCriada() {
        String payload = """
                {
                  "valorInicial": 500.00,
                  "taxaJurosMensal": 2.0,
                  "prazoMeses": 2
                }
                """;

        Number idNumber = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        long id = idNumber.longValue();

        given()
                .when()
                .get("/simulacoes/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo((int) id))
                .body("memoriaCalculo", hasSize(2));
    }
}
