Skill do Projeto - Sistema de Biblioteca (JavaFX + Hibernate + Firebird 2.5)
Objetivo

Desenvolver um sistema de gerenciamento de biblioteca utilizando:

Java 21 (ou versão compatível do projeto)
JavaFX para interface gráfica
Hibernate ORM para persistência
Firebird 2.5 como banco de dados
Arquitetura em camadas
Princípios de orientação a objetos
Código limpo e organizado
Estrutura do Projeto
src/main/java/

controller/
AcervoController.java
LivroController.java
MembroController.java
EmprestimoController.java
ReservaController.java

dao/
AcervoDao.java
MembroDao.java
EmprestimoDao.java
ReservaDao.java

model/
Acervo.java
Livro.java
Periodico.java
Midia.java
Monografia.java

    Membro.java
    MembroComum.java
    MembroEspecial.java

    Emprestimo.java
    Reserva.java

service/
AcervoService.java
MembroService.java
EmprestimoService.java
ReservaService.java

util/
HibernateUtil.java
SessionFactoryUtil.java
DatabaseUtil.java
DateUtil.java

Main.java
Responsabilidade de cada camada
model

Contém apenas entidades do domínio.

Cada classe representa uma tabela do banco ou uma especialização definida no UML.

Exemplo:

Membro
Livro
Emprestimo
Reserva

As entidades devem utilizar anotações do Hibernate (@Entity, @Table, @Id, etc.).

Não colocar código de acesso ao banco nesta camada.

dao

Responsável exclusivamente pela comunicação com o banco de dados.

Todo CRUD deve ficar nesta camada.

Exemplo:

MembroDao

salvar(Membro)
atualizar(Membro)
remover(Membro)
buscarPorId(Integer)
buscarTodos()

Nenhuma regra de negócio deve ser implementada no DAO.

O DAO apenas executa operações de persistência utilizando Hibernate.

service

Responsável pelas regras de negócio.

Toda validação deve acontecer aqui.

Exemplos:

verificar se membro está punido;
verificar limite de empréstimos;
impedir reserva duplicada;
impedir empréstimo de item indisponível;
controlar renovações;
atualizar status do acervo;
calcular penalidades;
verificar validade da reserva.

O Service utiliza um ou mais DAOs.

Controllers nunca devem acessar o banco diretamente.

controller

Responsável pela interação com as telas JavaFX.

Funções:

capturar eventos dos botões;
ler campos da interface;
chamar Services;
atualizar tabelas e componentes visuais;
exibir mensagens ao usuário.

Controllers não devem conter SQL nem lógica complexa.

util

Classes auxiliares compartilhadas.

Exemplos:

HibernateUtil
SessionFactoryUtil
DateUtil
DatabaseUtil

Também pode conter métodos estáticos de conversão ou formatação.

Banco de Dados
MEMBRO

Campos:

id
nomeCompleto
login
senha
isPunido
limiteEmprestimos
tipoMembro

tipoMembro

C = Membro Comum
E = Membro Especial
ACERVO

Campos:

id
titulo
anoPublicacao
valorPenalidade
tempoValidadeReserva
statusEmprestimo
statusReserva
tipoAcervo

tipoAcervo

LIVRO
PERIODICO
MIDIA
MONOGRAFIA
ACERVO_AUTOR

Relacionamento 1 entre Acervo e Autores.

Cada registro representa um único autor de um item.

EMPRESTIMO

Relaciona:

um membro
um item do acervo

Campos:

dataExpiracao
contagemRenovacoes
statusAtivo
RESERVA

Relaciona:

um membro
um item do acervo

Campo:

statusAtivo
Mapeamento Hibernate

Usar:

@Entity
@Table(name = "MEMBRO")

IDs devem utilizar:

@Id
@Column(name = "ID")

Como o Firebird 2.5 não possui BOOLEAN, mapear os campos SMALLINT para boolean usando um conversor (AttributeConverter) ou tratá-los como Short/Integer internamente.

Relacionamentos sugeridos:

Emprestimo -> @ManyToOne Membro
Emprestimo -> @ManyToOne Acervo

Reserva -> @ManyToOne Membro
Reserva -> @ManyToOne Acervo
Herança
Membro

Classe base.

Especializações:

MembroComum
MembroEspecial

A distinção deve ser feita pelo campo tipoMembro.

Acervo

Classe abstrata.

Especializações:

Livro
Periodico
Midia
Monografia

A distinção deve ser feita pelo campo tipoAcervo.

Interface Transacao

Criar uma interface:

public interface Transacao {

    void registrar();

    boolean isAtiva();

}

Implementar em:

Emprestimo
Reserva
Regras de Negócio
Empréstimo

Ao registrar:

verificar se o membro não está punido;
verificar se o limite de empréstimos não foi atingido;
verificar se o item não está emprestado;
marcar statusEmprestimo = true;
criar registro em EMPRESTIMO.
Renovação

Ao renovar:

incrementar contagemRenovacoes;
atualizar dataExpiracao.
Devolução

Ao devolver:

marcar statusAtivo = false;
marcar statusEmprestimo = false no acervo;
liberar o item.
Reserva

Ao registrar:

verificar se o item já não possui reserva ativa;
marcar statusReserva = true;
criar registro em RESERVA.
Cancelamento ou encerramento de reserva
marcar statusAtivo = false;
marcar statusReserva = false no acervo.
Padrão de Fluxo

A comunicação entre as camadas deve seguir obrigatoriamente:

JavaFX
↓

Controller
↓

Service
↓

DAO
↓

Hibernate
↓

Firebird 2.5

Nunca acessar o banco diretamente pelo Controller.

Nunca colocar regras de negócio no DAO.

Convenções
Classes em PascalCase.
Métodos em camelCase.
Um DAO para cada entidade persistente.
Um Service para cada domínio principal.
Utilizar Optional para buscas por ID quando apropriado.
Evitar duplicação de código.
Manter separação clara entre interface, regras de negócio e persistência.