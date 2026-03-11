# empreendimentos-sc-api

API REST para cadastro e consulta de empreendimentos em Santa Catarina.

## Objetivo do projeto

Este projeto tem como objetivo disponibilizar uma API REST para gerenciamento de empreendimentos localizados no estado de Santa Catarina, permitindo operações de cadastro, consulta, atualização e controle de status dos registros.

Além disso, o projeto também disponibiliza consulta de municípios previamente carregados em base de dados, servindo como apoio ao cadastro dos empreendimentos.

A proposta é demonstrar uma implementação backend organizada, seguindo boas práticas de desenvolvimento com Java e Spring Boot, incluindo persistência em banco de dados relacional, versionamento de schema com Flyway, documentação e testes automatizados.

---

## Vídeo pitch

Vídeo de apresentação da solução desenvolvida, com demonstração das principais funcionalidades e decisões técnicas adotadas:

- [Assistir no YouTube](COLE_AQUI_O_LINK)

---

## Stack utilizada

- **Java 17**
- **Spring Boot 4.0.3**
- **Spring Web**
- **Spring Data JPA**
- **PostgreSQL 14**
- **Flyway**
- **Lombok**
- **Maven**
- **JUnit**
- **Mockito**

---

## Pré-requisitos

Antes de executar o projeto localmente, é necessário ter instalado:

- **Java 17** ou superior
- **Apache Maven 3.9+**
- **PostgreSQL 14**

Também é necessário criar previamente o banco de dados que será utilizado pela aplicação.

Exemplo:

```sql
CREATE DATABASE empreendimentos_sc;
```

---

## Como rodar localmente

**Clonar o repositório**

```bash
git clone https://github.com/denram/empreendimentos-sc-api.git
cd empreendimentos-sc-api
```

**Configurar as variáveis de ambiente**

Defina as variáveis de ambiente necessárias para conexão com o banco de dados.

Por padrão, a aplicação já está configurada para utilizar a configuração abaixo:
- host: localhost
- porta: 5432
- banco: empreendimentos_sc
- usuário: postgres
- senha: postgres

Se o seu ambiente local estiver com essa mesma configuração, não é necessário alterar nada.
Caso utilize valores diferentes, configure as variáveis de ambiente DB_URL, DB_USERNAME e DB_PASSWORD antes de iniciar a aplicação.

Exemplo no Windows:

```bash
set DB_URL=jdbc:postgresql://localhost:5432/empreendimentos_sc
set DB_USERNAME=postgres
set DB_PASSWORD=postgres
```

Exemplo no Linux/macOS:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/empreendimentos_sc
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

**Executar a aplicação**

Via Maven:

```bash
mvn spring-boot:run
```

Ou gerar o artefato e executar:

```bash
mvn clean package
java -jar target/empreendimentos-sc-api-0.8.0.jar
```

**Validar a inicialização**

Após subir a aplicação, a API estará disponível localmente em:

```
http://localhost:8080
```

---

## Como executar os testes

Para rodar todos os testes automatizados do projeto:

```bash
mvn test
```

---

## Estrutura básica do projeto

A estrutura do projeto segue uma organização simples e adequada ao porte da aplicação, separando responsabilidades por camada.

```
src
├── main
│   ├── java
│   │   └── br/com/denram/empreendimentos_sc_api
│   │       ├── controller
│   │       ├── converter
│   │       ├── domain
│   │       ├── dto
│   │       ├── exception
│   │       ├── handler
│   │       ├── mapper
│   │       ├── repository
│   │       ├── service
│   │       ├── specification
│   │       └── EmpreendimentosScApiApplication.java
│   └── resources
│       ├── db
│       │   └── migration
│       └── application.yml
└── test
    └── java
        └── br/com/denram/empreendimentos_sc_api
            ├── controller
            ├── converter
            ├── domain
            ├── handler
            ├── mapper
            ├── service
            └── specification
```

**Resumo dos principais pacotes**

- controller: expõe os endpoints REST da aplicação
- converter: realiza conversões auxiliares de tipos e parâmetros recebidos pela aplicação
- domain: entidades de negócio e enums do sistema
- dto: define os objetos de entrada e saída da API
- exception: concentra as classes de exceção da aplicação
- handler: realiza o tratamento global das exceções da API
- mapper: realiza conversão entre entidades e DTOs
- repository: acesso aos dados via Spring Data JPA
- service: concentra as regras de negócio e a orquestração entre as camadas
- specification: filtros dinâmicos para consultas
- db/migration: scripts versionados do Flyway

**Classe principal da aplicação**

- EmpreendimentosScApiApplication: ponto de entrada da aplicação. É responsável por inicializar o contexto do Spring Boot, carregar as configurações do projeto e disponibilizar a API para execução.

---

## Funcionalidades principais:
- Cadastro de empreendimentos
- Atualização de empreendimentos
- Exclusão de empreendimentos
- Consulta de empreendimentos com filtros e paginação
- Controle de status ativo/inativo
- Consulta de municípios com filtros e paginação
- Carga inicial de municípios de Santa Catarina via Flyway

---

## Banco de dados e migrations

O versionamento de banco de dados é realizado com Flyway.

As migrations ficam localizadas em:

```
src/main/resources/db/migration
```

Ao iniciar a aplicação, as migrations são executadas automaticamente para criação e atualização da estrutura do banco.

---

## Executando em ambiente de desenvolvimento

Fluxo recomendado para desenvolvimento local:
- Criar o banco PostgreSQL
- Configurar as variáveis de ambiente
- Subir a aplicação
- Validar se as migrations foram aplicadas
- Testar os endpoints via cURL, Postman ou similar.

---

## Uso da API

Após iniciar a aplicação localmente, a API ficará disponível em:

```text
http://localhost:8080
```
Os exemplos abaixo consideram essa URL base.

**Paginação e ordenação**

Os endpoints de listagem suportam paginação e ordenação por query params.

| Parâmetro   | Descrição                              | Exemplo |
| ----------- | -------------------------------------- | ------- |
| `page`      | Número da página, iniciando em `0`     | `0`     |
| `size`      | Quantidade de registros por página     | `10`    |
| `sort`      | Campo utilizado para ordenação         | `nome`  |
| `direction` | Direção da ordenação (`asc` ou `desc`) | `asc`   |

Exemplo:

```http
GET /empreendimentos?page=0&size=10&sort=nome&direction=asc
```

**Endpoints de municípios**

Listar municípios:

```http
GET /municipios
```

Listar municípios filtrando por nome:

```http
GET /municipios/search?nome={nome}
```

**Endpoints de empreendimentos**

Cadastrar empreendimento:

```http
POST /empreendimentos
```

Payload de exemplo:

```json
{
  "nome": "Nome do empreendimento",
  "nomeResponsavel": "Nome do responsável pelo empreendimento",
  "municipioId": 136,
  "segmento": "TECNOLOGIA",
  "email": "contato@email.com",
  "telefone": "(47) 99999-9999",
  "ativo": true
}
```

Atualizar empreendimento:

```http
PUT /empreendimentos/{id}
```

Buscar empreendimento por ID:

```http
GET /empreendimentos/{id}
```

Excluir empreendimento:

```http
DELETE /empreendimentos/{id}
```

Listar empreendimentos:

```http
GET /empreendimentos
```

Listar empreendimentos filtrando por nome do empreendimento:

```http
GET /empreendimentos?nome={nome}
```

Listar empreendimentos filtrando por nome do município.

```http
GET /empreendimentos?municipioNome={nome do município}
```

Listar empreendimentos filtrando por nome do segmento.

```http
GET /empreendimentos?segmento={nome do segmento}
```

**Estrutura do payload de empreendimento**

Campos de entrada:

| Campo             | Tipo      | Descrição                  |
| ----------------- | --------- | -------------------------- |
| `nome`            | `string`  | Nome do empreendimento     |
| `nomeResponsavel` | `string`  | Nome do responsável        |
| `municipioId`     | `long`    | Identificador do município |
| `segmento`        | `string`  | Nome do segmento           |
| `email`           | `string`  | E-mail do empreendimento   |
| `telefone`        | `string`  | Telefone de contato        |
| `ativo`           | `boolean` | Situação do empreendimento |


Campos retornados nas consultas:

Além dos campos de entrada, as consultas retornam também informações complementares como:
- id
- municipioNome
- segmentoIndex
- criadoEm
- atualizadoEm

Observações:
- O campo segmento deve ser enviado pelo nome do enum : TECNOLOGA, COMERCIO, INDUSTRIA, SERVICOS ou AGRONEGOCIO.
- O campo municipioId deve referenciar um município existente.
- Os endpoints de listagem retornam dados paginados.

**Exemplos com cURL**

Listar municípios:

```bash
curl --request GET "http://localhost:8080/municipios?page=0&size=10&sort=nome&direction=asc"
```

Buscar município por nome:

```bash
curl --request GET "http://localhost:8080/municipios/search?nome=Florian&page=0&size=10&sort=nome&direction=asc"
```

Cadastrar empreendimento:

```bash
curl --request POST "http://localhost:8080/empreendimentos" \
  --header "Content-Type: application/json" \
  --data '{
    "nome": "Nome do empreendimento",
    "nomeResponsavel": "Nome do responsável pelo empreendimento",
    "municipioId": 136,
    "segmento": "TECNOLOGIA",
    "email": "contato@email.com",
    "telefone": "(47) 99999-9999",
    "ativo": true
  }'
```

Buscar empreendimento por ID:

```bash
curl --request GET "http://localhost:8080/empreendimentos/10"
```

---

## Autor

Desenvolvido por **Denis Ramos** como parte do processo seletivo para ingresso na trilha **IA para DEVs** do programa **SCTEC**.
