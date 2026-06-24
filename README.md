# CRUD Java + PostgreSQL (Aiven) + Swing

Aplicação Java com interface gráfica (Swing) que faz CRUD completo (Inserir,
Listar, Buscar, Atualizar, Excluir) em uma tabela `clientes` de um banco
PostgreSQL hospedado no Aiven, usando **JDBC puro** (driver oficial do
PostgreSQL + `PreparedStatement`), que é a abordagem mais usada no mercado
para esse tipo de aplicação desktop simples.

> Sobre JPA: o pedido mencionou JPA/Hibernate como alternativa. Para uma
> aplicação Swing simples como esta, JDBC puro é mais direto e mais fácil de
> ajustar. Se no futuro você quiser migrar para JPA/Hibernate (útil em
> projetos maiores, com várias entidades e relacionamentos), me avise que eu
> adapto o projeto.

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
Lá você encontra:

- **Host** (algo como `pg-xxxxxxx-seuprojeto.aivencloud.com`)
- **Port** (porta numérica, ex: `12345`)
- **User** (geralmente `avnadmin`)
- **Password**
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

⚠️ **Boa prática**: não deixe usuário/senha "hardcoded" direto no código em
projetos reais/produção. Para esse exemplo didático está direto no código
para facilitar, mas o ideal é usar variáveis de ambiente (`System.getenv(...)`)
ou um arquivo `.properties` fora do controle de versão (git).

## 4. Ajustando a tabela/colunas para os SEUS dados

O exemplo usa uma tabela `clientes` com as colunas `id`, `nome`, `email`,
`telefone`. Se a sua tabela tiver nome ou colunas diferentes, ajuste:

1. **`model/Cliente.java`** — atributos, getters/setters e construtores.
2. **`dao/ClienteDAO.java`** — constante `TABELA` e os comandos SQL
   (`INSERT`, `SELECT`, `UPDATE`, `DELETE`).
3. **`gui/TelaPrincipal.java`** — rótulos dos campos do formulário e colunas
   da tabela exibida (`new Object[]{"ID", "Nome", "Email", "Telefone"}`).
4. **`schema.sql`** — se quiser criar a tabela manualmente em vez de deixar
   o programa criar sozinho.

Todos esses pontos estão marcados com comentários `ALTERE AQUI` no código.

## 5. Compilando e executando

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

## 6. Sobre SSL (segurança da conexão)

O Aiven exige conexão criptografada. A configuração padrão usa
`sslmode=require`, que funciona na maioria dos casos sem precisar baixar
certificado. Se quiser validação completa do certificado do servidor
(mais seguro, recomendado em produção):

1. No painel do Aiven, baixe o certificado CA (`ca.pem`).
2. Em `ConexaoBD.java`, troque `SSL_MODE` para `"verify-full"`.
3. Adicione `&sslrootcert=/caminho/para/ca.pem` na construção da `URL`.

## 7. O que cada botão da tela faz

- **Inserir**: cria um novo registro com os dados dos campos do formulário.
- **Atualizar**: atualiza o registro selecionado na tabela (clique numa linha
  da tabela para carregar os dados no formulário).
- **Excluir**: remove o registro selecionado (pede confirmação).
- **Limpar**: limpa o formulário e a seleção da tabela.
- **Listar Todos**: recarrega a tabela com todos os registros do banco.
- **Buscar**: filtra os registros pelo nome digitado no campo "Buscar por nome".

## 8. Próximos passos sugeridos (opcional)

- Adicionar validação de e-mail/telefone no formulário.
- Adicionar paginação na listagem, caso a tabela cresça muito.
- Migrar para JPA/Hibernate se o projeto crescer e ganhar mais entidades.
- Externalizar usuário/senha para variáveis de ambiente ou arquivo `.env`.
