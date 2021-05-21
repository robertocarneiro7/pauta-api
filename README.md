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
11. **Springdoc-Openapi-Ui**
    - Dependência utilizada na API para gerar a documentação(Swagger) da API

## Dicas para rodar a aplicação

Para utilizar a aplicação é necessário ter instalado na máquina o Java 11.

Para simplificar o uso, recomendo a utilização do Intellij.

## Observações Importantes
1. Swagger está rodando no seguinte caminho:
    - http://localhost:8080/swagger-ui/index.html
2. Os logs estão sendo gerados na pasta "logs", que está na raiz do projeto(Essa pasta está no .gitignore)
3. A collection do postman, com os recursos da API, está no seguinte path:
    - ***src/main/resources/postman/PautaAPI.postman_collection.json***