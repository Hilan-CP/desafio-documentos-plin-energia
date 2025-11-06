# Desafio Plin Energia
Este projeto é uma API REST para gerenciamento de clientes e documentos, desenvolvida como parte do desafio da Plin Energia. A API permite criar, listar, atualizar e excluir clientes, bem como fazer upload, download e listagem de documentos associados a esses clientes.

## Tecnologias
- Java
- Maven
- Spring Boot
- PostgreSQL
- Selenium (web scrapping)

## Como executar

### Execução Docker
Executa tudo em containers Docker

```
docker-compose up -d
```

### Execução Local
Executa banco de dados e selenium em containers Docker e a aplicação localmente (necessário para executar testes com o Selenium)

```
# sobe o selenium
docker-compose up -d selenium_browser

# sobe o banco de dados
docker-compose up -d plin_database

# sobe a aplicação
./mvnw.cmd spring-boot:run
```

### Testes automáticos
Executa testes unitários e de integração localmente

```
docker-compose up -d selenium_browser
docker-compose up -d plin_database
./mvnw.cmd package
```

Resultado de cobertura de testes pode ser encontrado na pasta do projeto em `/target/jacoco-report/index.html`

### Limitações
Por dificuldades de integração entre a aplicação e o Selenium em containers Docker, a funcionalidade que envolve web scraping só pode ser executada localmente.

Endpoint: `/documents/url`

## Requisições
Coleção do postman disponível para facilitar testes manuais
[Desafio Plin Energia.postman_collection.json](https://github.com/user-attachments/files/23397768/Desafio.Plin.Energia.postman_collection.json)

-----
- Criar cliente

Caminho: /clients

Método: POST

Enviar um request body:
```
{
  "name": "João Santos",
  "email": "joao.santos@email.com"
}
```
-----
- Listar todos os clientes

Caminho: /clients

Método: GET

-----
- Atualizar cliente

Caminho: /clients/{id}

Método: PUT

Enviar um request body:
```
{
  "name": "João Silva Sales",
  "email": "joao.silva@email.com"
}
```
-----
- Apagar um cliente e todos os seus documentos

Caminho: /clients/{id}

Método: DELETE

-----
- Listar cliente junto com seus documentos

Caminho: /clients/{id}/documents

Método: GET

-----
- Criar documento via upload

Caminho: /documents/upload

Método: POST

Enviar um request header:
`X-Client-Id: 1`

Enviar um request body do tipo form-data
`file: [arquivo.pdf]`

-----
- Criar documento a partir de URL (somente execução local)

Caminho: /documents/url

Método: POST

Enviar um request body:
```
{
  "url": "https://www.google.com.br"
}
```
-----
- Baixar um documento

Caminho: /documents/download/{id}

Método: GET

-----
- Listar documentos de um cliente

Caminho: /documents/client/{id}

Método: GET
