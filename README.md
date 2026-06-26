# Sistema de Gerenciamento de Biblioteca

Sistema desktop para gerenciamento de biblioteca acadêmica, desenvolvido com Java, JavaFX, Hibernate ORM e Firebird 2.5.

## Funcionalidades

- **Login e Registro** de membros (comum e especial)
- **Consulta do Acervo** com busca por título e exibição de status (disponível/emprestado/reservado)
- **Empréstimo de Livros** com validação de regras de negócio
- **Devolução de Livros** com atualização automática de status
- **Reserva de Itens** com fila de prioridade
- **Renovação de Empréstimos** com limite de renovações
- **Penalização por Atraso** automática
- **Controle de Limite** por tipo de membro (comum: 1 empréstimo, especial: 3)

## Pré-requisitos

- **Java 21** (JDK)
- **Maven** (ou use o `mvnw` incluso no projeto)
- **Firebird 2.5** instalado e rodando

## Configuração do Banco de Dados

### 1. Instalar o Firebird 2.5

Baixe e instale o Firebird 2.5 em: https://firebirdsql.org/en/firebird-2-5/

### 2. Criar o banco de dados

O arquivo `BIBLIOTECA.FDB` já está na raiz do projeto. Caso precise recriar:

```sql
-- Criar tabelas
CREATE TABLE MEMBRO (
    ID INTEGER NOT NULL,
    NOME_COMPLETO VARCHAR(150) NOT NULL,
    LOGIN VARCHAR(50) NOT NULL,
    SENHA VARCHAR(100) NOT NULL,
    IS_PUNIDO SMALLINT NOT NULL DEFAULT 0,
    LIMITE_EMPRESTIMOS INTEGER NOT NULL DEFAULT 1,
    TIPO_MEMBRO VARCHAR(1) NOT NULL DEFAULT 'C',
    CONSTRAINT PK_MEMBRO PRIMARY KEY (ID)
);

CREATE TABLE ACERVO (
    ID INTEGER NOT NULL,
    TITULO VARCHAR(200) NOT NULL,
    ANO_PUBLICACAO INTEGER,
    VALOR_PENALIDADE DOUBLE PRECISION,
    TEMPO_VALIDADE_RESERVA INTEGER,
    STATUS_EMPRESTIMO SMALLINT NOT NULL DEFAULT 0,
    STATUS_RESERVA SMALLINT NOT NULL DEFAULT 0,
    TIPO_ACERVO VARCHAR(20) NOT NULL,
    CONSTRAINT PK_ACERVO PRIMARY KEY (ID)
);

CREATE TABLE ACERVO_AUTOR (
    ID INTEGER NOT NULL,
    ID_ACERVO INTEGER NOT NULL,
    AUTOR VARCHAR(200) NOT NULL,
    CONSTRAINT PK_ACERVO_AUTOR PRIMARY KEY (ID)
);

CREATE TABLE EMPRESTIMO (
    ID INTEGER NOT NULL,
    ID_MEMBRO INTEGER NOT NULL,
    ID_ACERVO INTEGER NOT NULL,
    DATA_EXPIRACAO TIMESTAMP,
    CONTAGEM_RENOVACOES INTEGER DEFAULT 0,
    STATUS_ATIVO SMALLINT NOT NULL DEFAULT 1,
    CONSTRAINT PK_EMPRESTIMO PRIMARY KEY (ID)
);

CREATE TABLE RESERVA (
    ID INTEGER NOT NULL,
    ID_MEMBRO INTEGER NOT NULL,
    ID_ACERVO INTEGER NOT NULL,
    STATUS_ATIVO SMALLINT NOT NULL DEFAULT 1,
    CONSTRAINT PK_RESERVA PRIMARY KEY (ID)
);
```

### 3. Criar generators e triggers

Execute o script `sql/recreate_triggers.sql`:

```sql
CREATE GENERATOR GEN_MEMBRO_ID;
CREATE GENERATOR GEN_ACERVO_ID;
CREATE GENERATOR GEN_EMPRESTIMO_ID;
CREATE GENERATOR GEN_RESERVA_ID;

-- Triggers para auto-incremento
SET TERM !! ;
CREATE TRIGGER TRG_MEMBRO_ID FOR MEMBRO ACTIVE BEFORE INSERT POSITION 0
AS BEGIN IF (NEW.ID IS NULL) THEN NEW.ID = GEN_ID(GEN_MEMBRO_ID, 1); END !!
SET TERM ; !!

SET TERM !! ;
CREATE TRIGGER TRG_ACERVO_ID FOR ACERVO ACTIVE BEFORE INSERT POSITION 0
AS BEGIN IF (NEW.ID IS NULL) THEN NEW.ID = GEN_ID(GEN_ACERVO_ID, 1); END !!
SET TERM ; !!

SET TERM !! ;
CREATE TRIGGER TRG_EMPRESTIMO_ID FOR EMPRESTIMO ACTIVE BEFORE INSERT POSITION 0
AS BEGIN IF (NEW.ID IS NULL) THEN NEW.ID = GEN_ID(GEN_EMPRESTIMO_ID, 1); END !!
SET TERM ; !!

SET TERM !! ;
CREATE TRIGGER TRG_RESERVA_ID FOR RESERVA ACTIVE BEFORE INSERT POSITION 0
AS BEGIN IF (NEW.ID IS NULL) THEN NEW.ID = GEN_ID(GEN_RESERVA_ID, 1); END !!
SET TERM ; !!
```

### 4. Popular com dados

Execute os scripts na pasta `sql/`:
- `sql/insert_livros.sql` — 25 livros
- `sql/insert_acervos_variados.sql` — 5 acervos variados (periódicos, mídias, monografia)

### 5. Configurar o persistence.xml

O arquivo `src/main/resources/META-INF/persistence.xml` contém a URL de conexão. Ajuste o caminho do banco de dados para o local do `BIBLIOTECA.FDB` na sua máquina:

```xml
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:firebirdsql://localhost:3050/SEU/CAMINHO/BIBLIOTECA.FDB"/>
```

## Como Rodar

```bash
# Compilar
./mvnw compile

# Executar
./mvnw javafx:run
```

Ou pelo IDE (IntelliJ IDEA / Eclipse), execute a classe `com.aula.Main`.

## Arquitetura

O projeto segue arquitetura em camadas com separação de responsabilidades:

```
Interface JavaFX (FXML + CSS)
        │
        ▼
   Controller
        │
        ▼
    Service
        │
        ▼
      DAO
        │
        ▼
    Hibernate
        │
        ▼
  Firebird 2.5
```

**Regra:** Controllers nunca acessam DAOs diretamente. Toda comunicação com o banco ocorre via Services.

## Estrutura do Projeto

```
src/main/java/com/aula/
├── Main.java
├── controller/
│   ├── AcervoController.java
│   ├── DevolucaoController.java
│   ├── EmprestimoController.java
│   ├── LoginController.java
│   ├── MenuController.java
│   ├── RegistroController.java
│   └── ReservasController.java
├── dao/
│   ├── AcervoDao.java
│   ├── EmprestimoDao.java
│   ├── MembroDao.java
│   └── ReservaDao.java
├── model/
│   ├── Acervo.java
│   ├── AcervoAutor.java
│   ├── Emprestimo.java
│   ├── Membro.java
│   ├── Reserva.java
│   └── Transacao.java
├── service/
│   ├── AcervoService.java
│   ├── EmprestimoService.java
│   ├── MembroService.java
│   ├── PenalidadeService.java
│   └── ReservaService.java
└── util/
    ├── BooleanIntegerConverter.java
    ├── JPAUtil.java
    └── Sessao.java

src/main/resources/
├── css/          (estilos de cada tela)
├── fxml/         (interfaces de cada tela)
├── imagens/      (ícones e backgrounds)
└── META-INF/
    └── persistence.xml
```

## Descrição das Classes

### Model (Entidades)

| Classe | Descrição |
|--------|-----------|
| `Membro` | Usuários do sistema. Pode ser Comum (C) ou Especial (E). Controle de punição e limite de empréstimos. |
| `Acervo` | Itens da biblioteca (livros, periódicos, mídias, monografias). Controla status de empréstimo e reserva. |
| `AcervoAutor` | Relacionamento N:1 entre acervos e autores. |
| `Emprestimo` | Registro de empréstimo entre um membro e um item. Implementa `Transacao`. |
| `Reserva` | Registro de reserva de um item por um membro. Implementa `Transacao`. |
| `Transacao` | Interface com métodos `registrar()` e `isAtiva()`. |

### DAO (Acesso a Dados)

| Classe | Descrição |
|--------|-----------|
| `MembroDao` | CRUD de membros. Busca por login/senha, login, ID. |
| `AcervoDao` | CRUD de acervos. Busca por título, autores. |
| `EmprestimoDao` | CRUD de empréstimos. Busca ativos por membro, contagem de ativos. |
| `ReservaDao` | CRUD de reservas. Busca ativas por membro, ativa por acervo. |

### Service (Regras de Negócio)

| Classe | Descrição |
|--------|-----------|
| `MembroService` | Cadastro com validação de login duplicado. Login e autenticação. |
| `AcervoService` | Consulta, atualização de status de empréstimo/reserva. |
| `EmprestimoService` | Empréstimo com validações (punido, limite, disponibilidade). Devolução. Renovação. |
| `ReservaService` | Criação e cancelamento de reserva. Limpeza de reservas expiradas. |
| `PenalidadeService` | Verificação de atrasos e aplicação de punições. |

### Controller (Interface)

| Classe | Descrição |
|--------|-----------|
| `LoginController` | Tela de login. Autenticação e navegação. |
| `RegistroController` | Cadastro de novos membros. |
| `MenuController` | Menu principal com navegação e logout. |
| `AcervoController` | Listagem, busca e reserva de itens do acervo. |
| `EmprestimoController` | Formulário de empréstimo. Se item emprestado, oferece reserva. |
| `DevolucaoController` | Listagem de empréstimos ativos e devolução. |
| `ReservasController` | Listagem de reservas do membro e cancelamento. |

### Util

| Classe | Descrição |
|--------|-----------|
| `JPAUtil` | Factory do EntityManager (persistence unit `bibliotecaPU`). |
| `Sessao` | Armazena o membro logado (singleton estático). |
| `BooleanIntegerConverter` | Converte boolean ↔ integer para o Firebird (que não tem tipo BOOLEAN). |

## Regras de Negócio

1. **Empréstimo:** Membro punido não pode emprestar. Limite de empréstimos simultâneos (1 para comum, 3 para especial). Máximo 15 dias (comum) ou 30 dias (especial).
2. **Reserva:** Item já reservado não pode ser reservado novamente. Livro emprestado pode ser reservado. Ao emprestar um item reservado, a reserva é removida automaticamente.
3. **Devolução:** Marca empréstimo como inativo e libera o item.
4. **Renovação:** Máximo 3 renovações por empréstimo. Membro punido não pode renovar.
5. **Penalidade:** Empréstimos com data expirada aplicam punição automática (`isPunido = true`).

## Tecnologias

- **Java 21** — linguagem principal
- **JavaFX 21** — interface desktop (FXML + CSS)
- **Hibernate 6.4** — ORM com Jakarta Persistence 3.1
- **Firebird 2.5** — banco de dados relacional
- **Jaybird 4.0** — driver JDBC para Firebird
- **Maven** — gerenciamento de dependências e build
