# Sudoku Game Java

## Visão geral

Este projeto implementa um jogo de Sudoku em Java com interface gráfica Swing.  
O código está organizado para facilitar manutenção, testes e futuras melhorias.

---

## Estrutura de pacotes

- **`br.com.sudoku.design`**  
  Contém a interface gráfica e componentes Swing.  
  Exemplo: `BoardForm` que monta o tabuleiro, botões e interações visuais.

- **`br.com.sudoku.logic`**  
  Regras e lógica do Sudoku, como geração do tabuleiro, validação e remoção de números.  
  Exemplo: `SudokuGenerator` e `RemoveNumbers`.

- **`br.com.sudoku.model`**  
  Classes que representam o estado do jogo, modelo de dados, encapsulando informações do tabuleiro e estado das células.

- **`br.com.sudoku.util`**  
  Utilitários e helpers para funcionalidades gerais usadas no projeto (ex: classes utilitárias para cópia de matrizes, geração de números aleatórios, etc).

---

## Ferramentas utilizadas

- **JUnit 5**  
  Framework para criação e execução de testes automatizados.  
  Os testes garantem que a lógica da geração e validação do Sudoku funcione corretamente.

- **SonarQube (SonarLint)**  
  Para análise estática de código e identificação de problemas como complexidade, má prática e vulnerabilidades.

- **Java Swing**  
  Para a criação da interface gráfica (GUI).

---

## Como rodar o projeto

1. Clone o repositório:

2. Importe o projeto na sua IDE (Eclipse, IntelliJ, VSCode com Java) usando Maven/Gradle ou configuração manual.

3. Compile e execute a classe principal com interface gráfica, geralmente `BoardForm` ou classe controladora.

4. Para rodar os testes com JUnit, execute:
- Na IDE: clique com o botão direito na pasta `test` ou nas classes de teste → `Run as → JUnit Test`
- Pela linha de comando (se usar Maven):
  ```
  mvn test
  ```

---

## Testes implementados

- Geração do tabuleiro válido (classe `SudokuGeneratorTest`)
- Remoção correta dos números para criação do desafio (classe `RemoveNumbersTest`)
- Validação do preenchimento e verificação de soluções

---

## Melhores práticas adotadas

- Encapsulamento usando campos privados e métodos getter/setter
- Separação de responsabilidades: UI, lógica, modelo e utilitários em pacotes distintos
- Refatoração para reduzir complexidade cognitiva, métodos pequenos e legíveis
- Uso de uma instância única de `Random` para geração aleatória, evitando problemas de semente
- Uso de mensagens ao usuário via `JOptionPane` para feedback sobre o jogo

---

## Futuras melhorias

- Adicionar modos de dificuldade configuráveis
- Implementar salvar/recuperar partidas
- Melhorar UI com temas e animações
- Adicionar dicas e tutoriais dentro do jogo

---

Se precisar de ajuda para rodar, testar ou entender o código, só avisar!  

