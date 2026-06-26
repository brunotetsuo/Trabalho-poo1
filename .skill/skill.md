# Skill do Projeto - Sistema de Gerenciamento de Biblioteca

## Objetivo

Desenvolver um Sistema de Gerenciamento de Biblioteca utilizando:

* Java
* JavaFX
* Hibernate ORM
* Firebird 2.5
* Arquitetura em camadas
* Programação Orientada a Objetos
* Boas práticas de desenvolvimento

O projeto deve seguir rigorosamente o diagrama UML fornecido, o banco de dados criado e os requisitos funcionais descritos neste documento.

---

# Arquitetura do Projeto

O projeto deve possuir a seguinte estrutura:

```text
src/
 ├── controller/
 │      AcervoController.java
 │      MembroController.java
 │      EmprestimoController.java
 │      ReservaController.java
 │
 ├── dao/
 │      AcervoDao.java
 │      MembroDao.java
 │      EmprestimoDao.java
 │      ReservaDao.java
 │
 ├── model/
 │      Acervo.java
 │      Livro.java
 │      Periodico.java
 │      Midia.java
 │      Monografia.java
 │
 │      Membro.java
 │      MembroComum.java
 │      MembroEspecial.java
 │
 │      Emprestimo.java
 │      Reserva.java
 │
 │      Transacao.java
 │
 ├── service/
 │      AcervoService.java
 │      MembroService.java
 │      EmprestimoService.java
 │      ReservaService.java
 │      PenalidadeService.java
 │
 ├── util/
 │      HibernateUtil.java
 │      SessionFactoryUtil.java
 │      DatabaseUtil.java
 │      DateUtil.java
 │
 └── Main.java
```

---

# Responsabilidade das Camadas

## Model

Representa as entidades do domínio.

As classes devem conter apenas atributos, relacionamentos e comportamentos próprios da entidade.

Devem utilizar anotações do Hibernate (`@Entity`, `@Table`, `@Id`, etc.).

Não devem executar SQL diretamente.

---

## DAO (Data Access Object)

Cada entidade persistida deve possuir um DAO correspondente.

Exemplos:

* Membro → MembroDao
* Acervo → AcervoDao
* Emprestimo → EmprestimoDao
* Reserva → ReservaDao

Responsabilidades:

* salvar
* atualizar
* remover
* buscarPorId
* buscarTodos
* consultas específicas

Nenhuma regra de negócio deve ser implementada nesta camada.

Toda comunicação com o Firebird deve ocorrer exclusivamente pelos DAOs utilizando Hibernate.

---

## Service

Toda regra de negócio deve ficar nesta camada.

Os Services podem utilizar um ou mais DAOs para executar operações.

Exemplos:

* validar empréstimo;
* validar reserva;
* controlar devoluções;
* aplicar penalidades;
* controlar renovações;
* limpar reservas expiradas;
* verificar disponibilidade de itens.

Controllers nunca devem acessar DAOs diretamente.

---

## Controller

Responsável pela interação com a interface JavaFX.

Funções:

* receber eventos dos componentes gráficos;
* validar entradas simples do usuário;
* chamar Services;
* atualizar tabelas e telas;
* apresentar mensagens de erro ou sucesso.

Não deve conter SQL nem regras complexas de negócio.

---

## Util

Classes auxiliares compartilhadas pelo projeto.

Exemplos:

* HibernateUtil
* SessionFactoryUtil
* DatabaseUtil
* DateUtil

---

# Modelo de Dados

## MEMBRO

Campos:

* id
* nomeCompleto
* login
* senha
* isPunido
* limiteEmprestimos
* tipoMembro

`tipoMembro`:

* C = Membro Comum
* E = Membro Especial

---

## ACERVO

Campos:

* id
* titulo
* anoPublicacao
* valorPenalidade
* tempoValidadeReserva
* statusEmprestimo
* statusReserva
* tipoAcervo

`tipoAcervo`:

* LIVRO
* PERIODICO
* MIDIA
* MONOGRAFIA

---

## ACERVO_AUTOR

Relacionamento 1:N entre Acervo e Autores.

Cada registro representa um autor associado a um item do acervo.

---

## EMPRESTIMO

Relaciona:

* um membro
* um item do acervo

Campos adicionais:

* dataExpiracao
* contagemRenovacoes
* statusAtivo

---

## RESERVA

Relaciona:

* um membro
* um item do acervo

Campo adicional:

* statusAtivo

---

# Herança

## Membro

Classe abstrata.

Especializações:

* MembroComum
* MembroEspecial

A distinção ocorre pelo campo `tipoMembro`.

---

## Acervo

Classe abstrata.

Especializações:

* Livro
* Periodico
* Midia
* Monografia

A distinção ocorre pelo campo `tipoAcervo`.

---

# Interface Transacao

Criar uma interface:

```java
public interface Transacao {

    void registrar();

    boolean isAtiva();

}
```

Ela deve ser implementada por:

* Emprestimo
* Reserva

---

# Mapeamento Hibernate

Utilizar anotações do Hibernate para mapear as entidades.

Os relacionamentos mínimos esperados incluem:

* `Emprestimo` → `@ManyToOne` para `Membro`
* `Emprestimo` → `@ManyToOne` para `Acervo`
* `Reserva` → `@ManyToOne` para `Membro`
* `Reserva` → `@ManyToOne` para `Acervo`

Como o Firebird 2.5 não possui tipo `BOOLEAN`, os campos `SMALLINT` devem ser tratados como `boolean` por meio de conversor (`AttributeConverter`) ou mapeados como `Short`/`Integer` com conversão na aplicação.

---

# Requisitos Funcionais

## 1. Gestão de Empréstimos

O sistema deve permitir registrar o empréstimo de um item do acervo para um membro.

Antes da conclusão, obrigatoriamente deve verificar:

* se o membro não está punido;
* se o membro não atingiu o limite máximo de empréstimos;
* se o item não está emprestado;
* se o item não possui reserva ativa para outro membro.

Após aprovação:

* criar o registro do empréstimo;
* atualizar o status do item para emprestado;
* registrar a transação no histórico.

---

## 2. Processo de Devolução

O sistema deve permitir devolver um item emprestado.

Ao devolver:

* localizar o empréstimo ativo correspondente;
* marcar o empréstimo como inativo;
* atualizar o status do item para disponível;
* permitir futuras reservas ou empréstimos.

O próprio item deve ser capaz de informar se está emprestado.

---

## 3. Reserva de Itens

Caso um item esteja indisponível, o sistema deve permitir que um membro registre uma reserva.

Ao reservar:

* verificar se já existe reserva ativa para o item;
* impedir reservas conflitantes;
* registrar a reserva;
* marcar o item como reservado.

Quando o item retornar, a reserva garante prioridade ao membro que a realizou.

O item deve informar se possui reserva ativa.

---

## 4. Renovação de Empréstimos

O sistema deve permitir renovar um empréstimo ativo.

Durante a renovação:

* atualizar a data de expiração;
* incrementar `contagemRenovacoes`;
* manter o histórico para auditoria.

---

## 5. Administração de Penalidades

Deve existir um processo administrativo capaz de percorrer todos os empréstimos ativos.

Quando houver atraso na devolução:

* identificar o membro responsável;
* aplicar punição (`isPunido = true`);
* considerar o valor de penalidade definido pelo item do acervo.

---

## 6. Limpeza de Reservas

O sistema deve possuir um processo para identificar reservas expiradas.

Quando a validade for excedida:

* remover ou desativar a reserva;
* atualizar o status do item;
* liberar o material para novos interessados.

A validade deve utilizar `tempoValidadeReserva` definido no acervo.

---

# Regras de Negócio

## 1. Limite de Empréstimos

Cada membro possui um limite máximo de empréstimos simultâneos (`limiteEmprestimos`).

O sistema deve impedir novos empréstimos quando esse limite for atingido.

---

## 2. Disponibilidade do Item

Um item somente pode ser emprestado quando:

* não estiver emprestado;
* não possuir reserva ativa para outro membro.

---

## 3. Penalidade por Atraso

Cada item possui um valor de penalidade (`valorPenalidade`) associado ao atraso na devolução.

Esse valor deve ser utilizado pelos processos administrativos de penalização.

---

## 4. Validade das Reservas

Cada item possui um período (`tempoValidadeReserva`) que define a validade de suas reservas.

Reservas vencidas devem ser removidas pelo processo de limpeza.

---

## 5. Validação de Status do Membro

Membros com `isPunido = true` não podem:

* realizar empréstimos;
* renovar empréstimos;
* iniciar novas operações de saída enquanto permanecerem punidos.

---

# Fluxo Obrigatório da Aplicação

Toda operação deve seguir o fluxo abaixo:

```text
Interface JavaFX
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

É proibido que Controllers acessem diretamente o banco de dados ou executem regras de negócio.

---

# Convenções Gerais

* Utilizar nomes de classes em PascalCase.
* Utilizar nomes de métodos e atributos em camelCase.
* Criar um DAO para cada entidade persistida.
* Criar um Service para cada domínio principal.
* Evitar duplicação de código.
* Centralizar regras de negócio na camada Service.
* Manter Controllers leves e focados apenas na interface.
* Utilizar orientação a objetos e aproveitar herança e polimorfismo conforme o UML.
* Implementar tratamento de exceções para operações de persistência e regras de negócio.
* Garantir que o código seja modular, reutilizável e de fácil manutenção.
