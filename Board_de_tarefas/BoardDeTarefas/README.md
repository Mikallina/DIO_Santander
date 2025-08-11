# Board de Tarefas - Gerenciador de Boards

Este projeto é um gerenciador simples de boards de tarefas, inspirado em metodologias ágeis. Ele permite criar, consultar, mover, bloquear, desbloquear e cancelar cards dentro de colunas de um board.

---

## Tecnologias

- Java 17+
- JDBC (acesso a banco via conexão SQL)
- Lombok (para geração automática de getters/setters)
- SLF4J + Logback (para logging estruturado)
- SQL (banco de dados relacional — configurar conforme seu ambiente)

---

## Funcionalidades

- Criar boards com colunas configuráveis (coluna inicial, pendente, final e cancelamento)
- Criar cards dentro das colunas
- Mover cards entre colunas, incluindo para colunas de cancelamento
- Bloquear e desbloquear cards com motivo registrado
- Visualizar detalhes de boards, colunas e cards
- Excluir boards

---

## Como rodar

1. Configure sua conexão com banco no arquivo `ConnectionConfig.java` (ou similar).
2. Compile o projeto com Maven ou sua IDE.
3. Execute a classe `MainMenu` para iniciar o menu interativo.
4. Navegue pelas opções para criar e gerenciar boards e cards.

---

## Logging

Este projeto utiliza SLF4J com Logback para gerenciamento de logs.  
Os logs são formatados e enviados para o console por padrão.  
Para alterar o comportamento do logger, configure o arquivo `logback.xml` na pasta `src/main/resources`.

---

## Estrutura do Projeto

- **persistence.entity**: Entidades do domínio (`BoardEntity`, `BoardColumnEntity`, `CardEntity`, etc.)
- **services**: Regras de negócio e acesso a banco (`BoardService`, `CardService`, etc.)
- **ui**: Interfaces de usuário via console (`MainMenu`, `BoardMenu`)
- **dto**: Data Transfer Objects para comunicação entre camadas

---

## Melhorias Futuras

- Refatorar código
- Interface gráfica para melhorar a experiência do usuário
- Suporte a múltiplos usuários e permissões
- Persistência em banco de dados via ORM (Hibernate ou JPA)
- API REST para integração com sistemas externos
- Testes unitários e de integração

---

## Contato

Para dúvidas, sugestões ou contribuições, abra uma issue ou envie um pull request.