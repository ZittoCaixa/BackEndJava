# BackEndJava - API de Simulacao Financeira

API Quarkus para simular juros compostos, persistir historico e consultar simulacoes por ID.

## Requisitos

- Java 25

## Stack

- Quarkus (REST + Panache)
- H2 Database em arquivo embutido
- Hibernate Validator
- OpenAPI/Swagger
- JaCoCo (minimo 80% de cobertura)

## Endpoints

- `POST /simulacoes`
- `GET /simulacoes/{id}`

### Exemplo de `POST /simulacoes`

```json
{
  "valorInicial": 1000.00,
  "taxaJurosMensal": 1.5,
  "prazoMeses": 12
}
```

## Executar em desenvolvimento

Use preferencialmente o Maven Wrapper do projeto:

```powershell
.\mvnw.cmd quarkus:dev
```

Swagger UI:

- `http://localhost:8080/q/swagger-ui`

## Rodar testes e validar cobertura

```powershell
.\mvnw.cmd clean verify -DskipITs=true
```

Relatorio JaCoCo:

- `target/site/jacoco/index.html`

## Observacoes de projeto

- Calculo com `BigDecimal` para evitar perda de precisao.
- Camadas separadas: `resource -> service -> repository`.
- Erros de validacao retornam `400`, busca inexistente retorna `404`.
- O projeto inclui `mvnw.cmd` para nao depender de instalacao global do Maven.
