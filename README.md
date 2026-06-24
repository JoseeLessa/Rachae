# CRUD Java + PostgreSQL (Aiven) + Swing

Aplicação Java com interface gráfica (Swing) que faz CRUD completo (Inserir,
Listar, Buscar, Atualizar, Excluir) em uma tabela `plano` de um banco
PostgreSQL hospedado no Aiven, usando **JDBC puro** (driver oficial do
PostgreSQL + `PreparedStatement`).

## Estrutura do projeto

```
crud-aiven/
├── pom.xml                          -> dependências e build (Maven)
├── schema.sql                       -> script para criar a tabela manualmente
└── src/main/java/com/meuapp/
    ├── Main.java                    -> ponto de entrada da aplicação
    ├── db/ConexaoBD.java            -> configuração da conexão com o Aiven
    ├── model/Cliente.java           -> classe que representa um registro
    ├── dao/ClienteDAO.java          -> métodos de CRUD (SQL)
    └── gui/TelaPrincipal.java       -> tela gráfica (Swing)
```

## 1. Pré-requisitos

- **Java JDK 17 ou superior** instalado.
- **Maven** instalado (ou use o Maven embutido da sua IDE — IntelliJ, Eclipse, VS Code).
- Uma instância **PostgreSQL criada no Aiven** (console.aiven.io).

## 2. Pegando os dados de conexão no Aiven

No painel do Aiven, abra o seu serviço PostgreSQL e veja a aba **Overview**.
Lá você encontrará Conection information:

- **Host** (algo como `pg-xxxxxxx-seuprojeto.aivencloud.com`)
- **Port** (porta numérica, ex: `12345`)
- **User** (geralmente `avnadmin`)
- **Password**

Na aba lateral databases escolha, ou crie, uma banco de dados.
- **Database name** (geralmente `defaultdb`)

## 3. Configurando o projeto com os SEUS dados

Abra o arquivo:

```
src/main/java/com/meuapp/db/ConexaoBD.java
```

E altere estas constantes com os dados do passo 2:

```java
private static final String HOST     = "SEU-HOST.aivencloud.com";
private static final String PORT     = "12345";
private static final String DATABASE = "defaultdb";
private static final String USUARIO  = "avnadmin";
private static final String SENHA    = "SUA-SENHA-AQUI";
```

## 4. Compilando e executando

No terminal, dentro da pasta do projeto:

```bash
mvn clean package
```

Isso gera o arquivo `target/crud-aiven-postgres.jar` (já com as dependências
embutidas). Para executar:

```bash
java -jar target/crud-aiven-postgres.jar
```

Ou, direto pela sua IDE (IntelliJ, Eclipse, VS Code com extensão Java):
basta rodar a classe `com.meuapp.Main`.

## 5. O que cada botão da tela faz

- **Inserir**: cria um novo registro com os dados dos campos do formulário.
- **Atualizar**: atualiza o registro selecionado na tabela (clique numa linha
  da tabela para carregar os dados no formulário).
- **Excluir**: remove o registro selecionado (pede confirmação).
- **Limpar**: limpa o formulário e a seleção da tabela.
- **Listar Todos**: recarrega a tabela com todos os registros do banco.
- **Buscar**: filtra os registros pelo nome digitado no campo "Buscar por nome".
