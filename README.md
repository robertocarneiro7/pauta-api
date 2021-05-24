# Pauta API

O **pauta-api** é uma aplicação que foi desenvolvida como um teste para uma vaga de desenvolvedor.

A aplicação tem como principais funcionalidades:
1. Cadastrar Pauta
2. Listar Todas as Pautas
3. Visualizar uma Pauta específica
4. Abrir sessão de votação na Pauta
5. Votação por parte do Associado

As entidades criadas na aplicação, seguindo o formato "noJava => NO_BANCO":
1. Pauta => TB_PAUTA
    - pautaId => PAUTA_ID
    - nome => NOME
    - descricao => DESCRICAO
    - dataAberturaVotacao => DATA_ABERTURA_VOTACAO
    - dataEncerramentoVotacao => DATA_ENCERRAMENTO_VOTACAO 
    - dataCriacao => DATA_CRIACAO
2. Associado => TB_ASSOCIADO
    - associadoId => ASSOCIADO_ID
    - cpf => CPF
    - dataCriacao => DATA_CRIACAO
3. Voto => TB_VOTO
    - votoId => VOTO_ID
    - pauta => PAUTA_ID
    - associado => ASSOCIADO_ID
    - resposta => RESPOSTA
    - dataCriacao => DATA_CRIACAO

## Tecnologias utilizadas
1. Java 11
2. Spring-Boot
3. **Spring-Web**
    - Dependência utilizada na API para trabalhar com REST e com as requisições HTTP
4. **Spring-Data**
    - Dependência utilizada na API para facilitar a comunicação com o banco de dados, na criação das classes Repository e Entity
5. **H2-Database**
    - Dependência utilizada na API para facilitar os testes da aplicação, para não ser necessário a instalação de um banco de dados
6. **Spring-Validation**
    - Dependência utilizada na API para facilitar nas validações dos objetos de request
7. **Spring-Openfeign**
    - Dependência utilizada na API para facilitar na comunicação via HTTP com API externa
8. **Spring-Boot-Starter-Log4j2**:
    - Dependência utilizada na API para gerenciamento de logs da aplicação.
9. **Lombok**:
    - Dependência utilizada na API para geração automática de códigos através de suas anotações, como **@Setter**, **@Getter** e **@RequiredArgsConstructor**.
10. **Mapstruct**:
    - Dependência utilizada na API para facilitar na conversão de Entidade para DTO e de DTO para Entidade
11. **Springfox-Swagger**
    - Dependência utilizada na API para gerar a documentação(Swagger) da API
12. **Git(Github)**
    - Utilizado para gerenciar o versionamento do código
13. **Maven**
    - Utilizado para gerenciar as dependências e o plugin do JMeter
14. **JMeter**
    - Utilizado para executar os testes de performance da aplicação

## Dicas para rodar a aplicação
1. É necessário ter instalado na máquina o Java 11.
2. É necessário ter instalado na máquina o Maven.
3. Para simplificar o uso, recomendo a utilização do Intellij.

## Observações Importantes
1. Swagger está rodando no seguinte caminho:
    - ***http://localhost:8080/swagger-ui/***
2. Os logs estão sendo gerados na pasta ***"logs"***, que está na raiz do projeto(Essa pasta está no .gitignore)
3. A collection do postman, com os recursos da API, está no seguinte caminho:
    - ***${project.basedir}/src/main/resources/postman/PautaAPI.postman_collection.json***
4. Console do banco de dados está rodando no seguinte caminho:
    - ***http://localhost:8080/h2-console***
5. O projeto e o resultado do JMeter, para execução dos testes de performance, estão na seguinte pasta:
    - ***${project.basedir}/src/main/resources/jmeter/***
6. Para cada execução dos testes de performance, é necessários seguir, na ordem, os seguintes passos:
    - Rexecutar a aplicação com a propriedade ***"spring.profiles.active"*** com valor igual a ***"performance-test"***
    - Executar o comando maven ***"mvn verify"***

## Sobre os Testes de Performance
Foi configurado para executar a seguinte quantidade de execuções:
1. **1.000** criações de **Associados**
2. **100** criações de **Pautas**
3. **100** aberturas de votação(1 abertura para cada **Pauta** criada)
4. **100.000** **Votos**(cada **Associado** vota 1 vez por **Pauta**)

Ao executar os testes de performance por completo, foi notado um tempo muito elevado, quando existia a validação com o serviço externo, que valida se o CPF do Associado pode votar(**GET https://user-info.herokuapp.com/users/{cpf}**)

Abaixo o tempo da execução dos testes de performance, para cada caso executado:
1. Tempo total da execução sem validação externa no recurso de votar:
    - 4 minutos e 20 segundos(Foram executados todos os 100.000 votos)
2. Tempo que esperei na execução com validação externa no recurso de votar:
    - 18 minutos e 20 segundos(Parei depois de serem executados pouco mais de 8.000 votos, restavam ainda 92.000 votos)