# **Calculadora Financeira HP-12C em Java**

Este projeto é uma calculadora financeira de desktop, desenvolvida em Java com o framework Swing, que emula as funcionalidades essenciais da clássica calculadora HP-12C. Ela é projetada para resolver problemas de matemática financeira (TVM \- Time Value of Money) e realizar operações aritméticas básicas.

A interface gráfica é simples e intuitiva, apresentando um visor principal e um painel dedicado que exibe em tempo real os valores armazenados nos cinco registros financeiros fundamentais:

* **VP (Valor Presente)** 1

* **VF (Valor Futuro)** 2

* **PMT (Pagamento ou Parcela)** 3

* **i (Taxa de Juros)** 4

* **n (Número de Períodos)** 5

## **🚀 Funcionalidades**

* **Cálculos Financeiros:** Calcule qualquer uma das cinco variáveis financeiras (VP, VF, PMT, i, n) a partir das outras.  
* **Operações Aritméticas:** Suporte para adição (+), subtração (-), multiplicação (×) e divisão (÷).  
* **Interface Interativa:** Um visor principal para entrada e resultados, e um painel de informações que atualiza os valores dos registros financeiros instantaneamente6.

* **Controles Intuitivos:** Botões para limpar todos os dados (C) 7, inverter o sinal de um número (

  CHS) 8e confirmar entradas (

  ENTER)9.

* **Validação e Feedback:** O sistema valida as entradas e exibe mensagens de erro claras em caso de operações inválidas, como divisão por zero 10ou parâmetros insuficientes para um cálculo financeiro111111111111111111.

## **🛠️ Detalhes Técnicos do Código**

A aplicação é construída como uma classe única,

CalculadoraHP, que herda de JFrame12.

* **Estrutura da Interface:** A interface é organizada usando BorderLayout e GridLayout13131313. O visor fica na parte superior (

  NORTH), o painel de botões no centro (CENTER), e o painel com os registros financeiros na parte inferior (SOUTH)14.

* **Gerenciamento de Estado:**  
  * Um  
    HashMap\<String, Double\> chamado registrosFinanceiros armazena os valores de VP, VF, PMT, i e n15.

  * Variáveis como  
    ultimaOperacao e aguardandoNovoValor controlam o fluxo de cálculos aritméticos16.

* **Lógica de Cálculo Financeiro:**  
  * O método  
    processarFuncaoFinanceira(String funcao) é o ponto central que decide entre armazenar um novo valor em um registro ou calcular o valor daquele registro com base nos dados já inseridos17.

  * **Cálculo de Taxa (i):** Implementado através da fórmula de juros compostos i \= ((VF / VP)^(1 / n)) \- 1\. Esta fórmula pressupõe que não há pagamentos periódicos (PMT)18.

  * **Cálculo de Períodos (n):** Realizado com a fórmula logarítmica n \= ln(VF / VP) / ln(1 \+ i)19191919.

  * **Cálculos de VP, VF e PMT:** Utilizam as fórmulas padrão de valor do dinheiro no tempo, com tratamentos específicos para casos onde a taxa de juros é zero202020202020202020.

## **⚙️ Como Executar o Projeto**

### **Pré-requisitos**

* Java Development Kit (JDK) 8 ou superior instalado e configurado no sistema.

### **Compilando e Executando via Terminal**

1. Salve o código com o nome CalculadoraHP.java dentro da estrutura de pastas javaricci/com/br/.  
2. Navegue até o diretório raiz do seu projeto (a pasta que contém a pasta javaricci).  
3. Compile o código:  
   Bash  
   javac javaricci/com/br/CalculadoraHP.java

4. Execute a aplicação:  
   Bash  
   java javaricci.com.br.CalculadoraHP

A janela da calculadora será exibida21.

## **📖 Como Usar a Calculadora**

O fluxo de operação da calculadora foi projetado para ser similar ao de uma HP-12C.

### **Convenção de Sinais**

A calculadora segue a convenção de fluxo de caixa:

* **Entradas de dinheiro (investimentos, aportes)** devem ser inseridas como números **negativos**. Use o botão CHS para trocar o sinal.  
* **Saídas de dinheiro (resgates, benefícios)** são consideradas números **positivos**.

Por padrão, os cálculos de

VP, VF e PMT retornam um valor com sinal invertido, seguindo a lógica de que para um investimento (saída de caixa) haverá um resgate (entrada de caixa)222222222222222222.

### **Passos para um Cálculo Financeiro**

1. **Limpar Memória:** Pressione C para zerar todos os registros e começar um novo cálculo23.

2. **Inserir Valor:** Digite o número no teclado numérico.  
3. **Armazenar no Registro:** Pressione o botão do registro correspondente (VP, VF, PMT, i, ou n). O valor será armazenado e exibido no painel inferior24242424.

4. **Repetir:** Repita os passos 2 e 3 para todas as variáveis conhecidas.  
5. **Calcular Resultado:** Pressione o botão da variável que você deseja descobrir. O resultado aparecerá no visor principal25.

---

## **💡 Exemplos de Uso**

Aqui estão cinco cenários práticos para ilustrar o uso da calculadora.

### **Exemplo 01: Achar Número de Meses (n)**

*Um investidor aplicou R$ 32.114,68 em um LCI, à taxa de juros compostos de 1% a.m., e resgatou no vencimento um valor de R$ 45.948,68. Qual foi o prazo do investimento?*

| Registro | Valor | Ação |
| :---- | :---- | :---- |
| **VP** | 32.114,68 | Investimento (saída de caixa) |
| **i** | 1,00% | Taxa de juros |
| **VF** | 45.948,68 | Resgate (entrada de caixa) |
| **n** | **??** | **A ser calculado** |

**Passos na Calculadora:**

1. Pressione C para limpar a memória.  
2. Digite 32114.68, pressione CHS e depois VP. (Valor Presente negativo)  
3. Digite 1 e pressione i.  
4. Digite 45948.68 e pressione VF. (Valor Futuro positivo)  
5. Pressione n para calcular.

**Resultado no Visor:** 36,00

### **Exemplo 02: Achar Juros ao Mês (i)**

*Um investidor aplicou R$ 32.114,68 em um LCI, pelo prazo de 36 meses, e resgatou no vencimento um valor de R$ 45.948,68. Qual foi a taxa de juros compostos do investimento?*

| Registro | Valor | Ação |
| :---- | :---- | :---- |
| **VP** | 32.114,68 | Investimento (saída de caixa) |
| **n** | 36,00 | Prazo do investimento |
| **VF** | 45.948,68 | Resgate (entrada de caixa) |
| **i** | **??** | **A ser calculada** |

**Passos na Calculadora:**

1. Pressione C para limpar a memória.  
2. Digite 32114.68, pressione CHS e depois VP.  
3. Digite 36 e pressione n.  
4. Digite 45948.68 e pressione VF.  
5. Pressione i.

**Resultado no Visor:** 1,00 (representando 1%)

### **Exemplo 03: Achar Valor Presente (VP)**

*Um investidor resgatou um LCI no valor R$ 45.948,68, aplicado à taxa de 1% a.m., após 36 meses. Qual foi o valor investido?*

| Registro | Valor | Ação |
| :---- | :---- | :---- |
| **i** | 1,00% | Taxa de juros |
| **n** | 36,00 | Prazo do investimento |
| **VF** | 45.948,68 | Resgate (entrada de caixa) |
| **VP** | **??** | **A ser calculado** |

**Passos na Calculadora:**

1. Pressione C para limpar a memória.  
2. Digite 1 e pressione i.  
3. Digite 36 e pressione n.  
4. Digite 45948.68 e pressione VF.  
5. Pressione VP.

**Resultado no Visor:** \-32.114,68 (negativo, pois representa o investimento inicial)

### **Exemplo 04: Achar Valor Futuro (VF)**

*Um investidor aplicou R$ 32.114,68 em um LCI, à taxa de juros compostos de 1% a.m., pelo prazo de 36 meses sem aporte mensal. Qual será o valor de resgate?*

| Registro | Valor | Ação |
| :---- | :---- | :---- |
| **VP** | 32.114,68 | Investimento (saída de caixa) |
| **i** | 1,00% | Taxa de juros |
| **n** | 36,00 | Prazo do investimento |
| **VF** | **??** | **A ser calculado** |

**Passos na Calculadora:**

1. Pressione C para limpar a memória.  
2. Digite 32114.68 e pressione VP. (A convenção de sinal é flexível; se VP for positivo, VF será negativo)  
3. Digite 1 e pressione i.  
4. Digite 36 e pressione n.  
5. Pressione VF.

**Resultado no Visor:** \-45.948,68 (negativo, pois é o oposto do fluxo de caixa do VP)

### **Exemplo 05: Achar Valor Futuro com Aporte Mensal (VF com PMT)**

*Um investidor aplicou R$ 32.114,68 em um LCI, à taxa de juros compostos de 1% a.m., pelo prazo de 36 meses com aporte mensal de R$ 5.000,00. Qual será o valor de resgate?*

| Registro | Valor | Ação |
| :---- | :---- | :---- |
| **VP** | 32.114,68 | Investimento inicial (saída) |
| **PMT** | 5.000,00 | Aporte mensal (saída) |
| **i** | 1,00% | Taxa de juros |
| **n** | 36,00 | Prazo do investimento |
| **VF** | **??** | **A ser calculado** |

**Passos na Calculadora:**

1. Pressione C para limpar a memória.  
2. Digite 32114.68 e pressione VP.  
3. Digite 5000 e pressione PMT.  
4. Digite 1 e pressione i.  
5. Digite 36 e pressione n.  
6. Pressione VF.

**Resultado no Visor:** \-261.333,07 (negativo, representando o valor total acumulado que pode ser resgatado)
