# 💳 Meu Banco Digital – Sistema de Gerenciamento Bancário

> Sistema de gerenciamento de clientes, contas, cartões e seguros para uma instituição financeira, com validações, regras de negócio e integração com APIs.

---

## 🧠 Contexto

Este projeto é parte da solução oferecida por uma startup de tecnologia para uma instituição financeira, com foco na criação de um sistema web de cadastro e gerenciamento de clientes, contas, cartões e seguros.  

A aplicação permite operações bancárias como:

- Cadastro de clientes
- Abertura de contas (Corrente/Poupança)
- Emissão de cartões (Crédito/Débito)
- Realização de transações (PIX, transferências)
- Gerenciamento de seguros de cartão
- Validações rigorosas e aplicação de regras de negócio

---

## ⚙️ Tecnologias Utilizadas

- **Java 17** – Linguagem principal
- **Spring Boot** – Framework backend
- **Maven** – Gerenciador de dependências e build
- **APIs** – Integração com APIs internas e externas para dados e validações
- **MySQL** - Integração com Banco de Dados
- **Swagger** - Springdoc
- **Railway** - Deploy em Nuvem

---

## Diagrama de Classes
 ``` mermaid
 classDiagram
    class Cartao {
        <<abstract>>
        +Long idCartao
        +String numCartao
        +boolean status
        +TipoCartao tipoCartao
        +int senha
        +double fatura
    }

    class CartaoCredito {
        +double taxa
        +LocalDate dataVencimento
        +LocalDate dataCompra
        +double pagamento
        +double saldoCredito
        +double saldoMes
        +String diaVencimento
        +double limiteCredito
    }

    class CartaoDebito {
        +double taxa
        +double limiteDiario
        +double totalPgtoHoje
    }

    class Cliente {
        +Long idCliente
        +String nome
        +String cpf
        +LocalDate dataNascimento
        +String statusCpf
        +Categoria categoria
    }

    class Endereco {
        +String logradouro
        +Integer numero
        +String complemento
        +String cep
        +String bairro
        +String localidade
        +String uf
    }

    class Conta {
        <<abstract>>
        +Long idConta
        +int agencia
        +String numConta
        +double saldo
        +TipoConta tipoConta
    }

    class ContaCorrente {
        +double taxaManutencao
    }

    class ContaPoupanca {
        +double taxaRendimento
    }

    %% Relações de herança
    Cartao <|-- CartaoCredito
    Cartao <|-- CartaoDebito
    Conta <|-- ContaCorrente
    Conta <|-- ContaPoupanca

    %% Relações de composição/associação
    Cliente "1" o-- "*" Conta : contas
    Conta "*" o-- "*" Cartao : cartoes
    Cartao "*" --> "1" Conta : conta
    Conta "*" --> "1" Cliente : cliente
    Cliente "*" --> "1" Endereco : endereco
    CartaoDebito "*" --> "1" Conta : conta

 
 ```

## 📜 Regras de Negócio

### 🧾 Cadastro de Clientes
- CPF único com validação via API (formato `xxxxxxxxxxx`)
- Nome: Apenas letras e espaços (2–100 caracteres)
- Data de nascimento: formato `DD/MM/AAAA`, apenas maiores de 18 anos
- Endereço completo com validação de CEP utilizando API viacep (`xxxxxxxx`)
- Categorias: **Comum**, **Super**, **Premium**

### 🏦 Contas Bancárias

**Tipos de conta:**
- Corrente: cobra taxa de manutenção mensal
- Poupança: rendimento mensal com base em juro composto

| Tipo de Cliente | Taxa Corrente (R$) | Rendimento Poupança (%)  |
|-----------------|--------------------|--------------------------|
| Comum           | 12,00              | 0,5% ao ano              |
| Super           | 8,00               | 0,7% ao ano              |
| Premium         | Isenta             | 0,9% ao ano              |

### 💸 Operações Bancárias

- Consultar saldo
- Efetuar PIX
- Aplicar Rendimento conta Poupança
- Aplicar Taxa em conta corrente
- Transferências entre contas
- Depósitos

### 💳 Cartões (Crédito/Débito)

**Cartão de Crédito:**
- Limites: Comum (R$1.000), Super (R$5.000), Premium (R$10.000)
- Bloqueio de pagamentos ao atingir limite
- Operações: alterar senha, ativar/desativar, efetuar pagamento de fatura

**Cartão de Débito:**
- Limite diário ajustável
- Bloqueia pagamentos após atingir o limite
- Operações: ativar/desativar, trocar senha, consultar saldo

---

## 📋 Funcionalidades

### 📌 Menu Principal
1. Cadastro de Cliente
2. Abertura de Conta
3. Operações Conta Corrente/Poupança
4. Cartões de Crédito/Débito
5. Listar Clientes
0. Sair

### 👤 Cadastro de Cliente
- Validações: campos vazios, CPF, data de nascimento, CEP
- Verificação de duplicidade

### 🏦 Abertura de Conta
- Verifica cadastro do cliente
- Seleção entre conta corrente e/ou poupança

### 🔁 Operações Conta Corrente/Poupança
1. Exibir contas
2. PIX
3. Transferência entre contas
4. Exibir saldo
5. Efetuar depósito

### 💳 Cartões
- Consulta de cartões existentes
- Emissão e gestão de cartão de crédito/débito
- Operações:
  - Alterar senha/limite
  - Ativar/desativar


### 📋 Listagem de Clientes
- Exibe todos os clientes cadastrados
- Edição e exclusão de dados

---

## 🚧 Status do Projeto

> 🛠️ Em Desenvolvimento  
- Taxa de 5% sobre gastos mensais caso exceda 80% do limite


---

## 🚀 Como Executar

### 1. Clone o repositório:

```bash
git clone https://github.com/Mikallina/MeuBancoDigitalCompleto
