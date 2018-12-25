# Prova de conceito para recuperar dados no BIGBOOST.
Prova de conceito


## Rodar a aplicação:
### mvn spring-boot:run
ou
### mvn package && java -jar target/proxy_gibboost-0.0.1-SNAPSHOT.jar

Esse roda na porta 8090 e é responsável por buscar as informações das pessoas no BigBoost.

Todas as informações do bigboost é gravada na tabela <b>conteiner</b> com a coluna tipo = 'bigBoost'

### Query: 
select * from conteiner where tipo = 'bigBoost';

### BigBoost:
Exemplo de chamada no bigboost:

curl --location --request GET "https://bigboost.bigdatacorp.com.br/peoplev2?Datasets=basic_data&q=cpf%7B070.680.938-68%7D&&AccessToken=???" --header "Content-Type: application/json"

exemplo de chamada via edsdf_proxy:

curl -d "filtro={\"Datasets\":\"basic_data\", \"q\":\"doc%7B070.680.938-68%7D\"}" -X POST http://localhost:8090/consulta

Exemplo de como deve ser montado o consulta:
Body:
{
  "Datasets": "seus_datasets",
  "q": "name{seu nome},birthdate{10/02/1995},dateformat{dd/MM/yyyy} ",
  "AccessToken": "",
  "Limit"="5"
}

fonte:
https://docs.bigboost.bigdatacorp.com.br/
