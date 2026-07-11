Trabalho Prático: Álbum de Figurinhas da Copa do Mundo

## 1. Objetivos

Este trabalho prático tem como objetivo consolidar, em um problema concreto e lúdico, os principais conceitos de
programação trabalhados no primeiro período do curso. Ao final do desenvolvimento, a dupla deverá ter exercitado:

* Manipulação de matrizes bidimensionais para representar dados tabulares;

* Leitura de arquivos de texto e interpretação do seu conteúdo para preencher estruturas de dados em memória;

* Modularização do código em métodos com responsabilidades bem definidas;

* Uso de estruturas de repetição e de decisão para percorrer, filtrar e comparar dados;

* Validação de entradas fornecidas pelo usuário e tratamento básico de erros.

## 2. Descrição do Cenário

Durante os períodos que antecedem a Copa do Mundo, é comum que torcedores colecionem álbuns de figurinhas contendo
jogadores das diversas seleções participantes do torneio. À medida que novas figurinhas são adquiridas, algumas posições
do álbum são preenchidas enquanto outras permanecem vazias. Além disso, é bastante comum obter figurinhas repetidas, que
podem posteriormente ser trocadas com outros colecionadores.

Neste trabalho, você deverá desenvolver um sistema em JAVA capaz de simular o gerenciamento de um álbum de figurinhas da
Copa do Mundo, permitindo registrar novas figurinhas, identificar figurinhas faltantes, visualizar figurinhas repetidas
e sugerir possíveis trocas entre colecionadores.

A dupla deverá desenvolver um sistema que simula um álbum de figurinhas de uma Copa do Mundo. Cada álbum é representado
por uma matriz de números inteiros, na qual cada posição (linha, coluna) indica a situação de uma figurinha específica:

* **0**: a figurinha ainda não foi colada no álbum (faltante);

* **1**: o(a) colecionador(a) possui exatamente uma figurinha colada nessa posição (completa, sem repetição);

* **Valor maior que 1**: quantidade total de figurinhas que o(a) colecionador(a) possui para aquela posição; logo, o
  número de unidades repetidas é o valor da célula menos 1.

Cada linha da matriz representa uma seleção participante da Copa do Mundo, e cada coluna representa um jogador daquela seleção, numerado de 1 a N, sendo N a quantidade de jogadores por elenco definida pela dupla (por exemplo, 23 ou 26). No álbum da copa atual, cada seleção possui 20 figurinhas, sendo 18 de jogadores e 2 representando o emblema e a seleção perfilada. A seguir temos uma figura com a representação das seleções no aplicativo Figuritas. Podem utilizar o aplicativo como fonte de inspiração.

## Descrição da Imagem (Página 2)

> A imagem exibe a captura de tela de um aplicativo móvel chamado "Figuritas" (com base no texto de contexto). Trata-se de um gerenciador digital de álbum de figurinhas para a Copa do Mundo de "Eua Méx Can 26".
>
> No topo da tela, há um menu com três abas horizontais: **"Todas"** (que está selecionada), **"Me faltam"** e **"Repetidas"**. Logo abaixo, há uma seção intitulada **"MEX - México"** acompanhada da bandeira do país.
>
> A parte principal mostra uma grade numérica contendo círculos de 1 a 20, representando as figurinhas da seleção
> mexicana:
> 
> * **Figurinhas Faltantes:** Os círculos de número **1, 2, 9, 15, 18, 19 e 20** aparecem com um fundo cinza-claro ou quase branco, indicando que não foram adquiridas. O número 1 especificamente possui um leve sombreado amarelado.
>
> * **Figurinhas Possuídas:** Os círculos de número **3, 4, 5, 6, 8, 10, 11, 12, 13, 14, 16 e 17** possuem um fundo cinza-escuro opaco, indicando que o colecionador já as colou. Os números dentro deles estão riscados horizontalmente.
> 
> * **Figurinhas Repetidas:** O círculo de número **7** destaca-se por possuir um pequeno círculo azul flutuante no canto superior direito com o número **1**, indicando que há uma unidade repetida desta figurinha.

O exemplo da figura mostra a seleção do México, sendo que ainda não foram encontradas as figurinhas 1, 2, 9, 15, 18, 19 e 20 e a figurinha de número 7 possui uma repetição.

## 3. Modelagem da Estrutura de Dados

A matriz principal deve ter dimensões $M \times N$, em que $M$ é o número de seleções e $N$ é o número de jogadores por seleção. Essas dimensões podem ser fixas e devem ser lidas dinamicamente do arquivo de entrada a ser disponibilizado.

O exemplo abaixo é apenas ilustrativo, com 4 seleções e 5 jogadores, e não faz parte do enunciado obrigatório:

| Seleção e Jogador | 1   | 2   | 3   | 4   | 5   |
| ----------------- | --- | --- | --- | --- | --- |
| **Brasil**        | 0   | 1   | 3   | 1   | 0   |
| **Argentina**     | 0   | 1   | 1   | 0   | 2   |
| **França**        | 1   | 1   | 0   | 0   | 1   |
| **Alemanha**      | 0   | 2   | 1   | 1   | 1   |

Tabela 1 - Exemplo ilustrativo com 4 seleções e 5 jogadores por seleção

**Leitura do exemplo:** o Brasil possui a figurinha do jogador 1 (valor 1), não possui a do jogador 2 (valor 0, faltante), possui 3 figurinhas do jogador 3 (1 colada + 2 repetidas), e assim por diante.

**Sugestão de dimensionamento:** inicie seus testes com valores pequenos, tal como 6 seleções com 10 jogadores cada. O teste servirá para validar a lógica e funcionamento do código. Para fins de entrega final do trabalho, o dimensionamento será realizado via leitura de arquivo que conterá todas as seleções da copa, contendo 20 figurinhas cada.

## 4. Arquivo de Entrada

O programa deve ler, na opção "Carregar álbum", um arquivo de texto contendo as informações do álbum, tais como a dimensão e as figurinhas já existentes no álbum. Sugere-se o seguinte formato:

* **1ª linha:** dois números inteiros separados por espaço - a quantidade de seleções ($M$) e a quantidade de jogadores por seleção ($N$);

* **M linhas seguintes:** cada linha contém o nome da seleção considerada no álbum;

* **As demais M linhas seguintes:** cada linha contém $N$ valores inteiros separados por espaço, representando a linha
  correspondente da matriz.

Exemplo de arquivo `"album.txt"` (correspondente à tabela 1 ilustrativa na seção anterior):

```text
4 5
Brasil
Argentina
França
Alemanha
1 0 3 1 0
0 1 1 0 2
1 1 0 0 1
0 2 1 1 1

```

Nota de correção da transcrição: A linha correspondente à França no arquivo de exemplo original continha `"11001"`, mas foi ajustada aqui com espaços para condizer com o padrão de separação e com a tabela representada.

Os nomes das seleções (Brasil, Argentina, França, Alemanha, ...) não precisam constar no arquivo de matriz: podem ser mantidos em um vetor auxiliar de `strings`, na mesma ordem das linhas, definido pela dupla.

## 5. Especificação Funcional (Menu Principal)

O programa deve apresentar um menu principal em modo texto, exibido repetidamente até que o usuário escolha a opção de saída, contendo, no mínimo, as opções a seguir:

1. Carregar álbum a partir de arquivo (fornecerei o método de leitura de arquivos)

2. Registrar nova figurinha

3. Listar figurinhas faltantes

4. Listar figurinhas repetidas

5. Comparar dois álbuns (verificar trocas possíveis)

6. Sair

### 5.1 Carregar Álbum

O método de leitura será disponibilizado junto ao trabalho. Solicitar ao usuário o nome (ou caminho) do arquivo de texto e carregar seus dados para uma matriz em memória, seguindo o formato definido na Seção 4. O caminho do arquivo pode ser predefinido no código. Validar se o arquivo existe e se o conteúdo é compatível com as dimensões informadas na primeira linha, tratando adequadamente possíveis erros de leitura.

### 5.2 Registrar Nova Figurinha

Solicitar ao usuário a seleção e o jogador da figurinha obtida (por exemplo, pelos índices de linha e coluna, ou por nomes, caso a dupla implemente um vetor auxiliar de nomes). Validar e os índices informados são válidos, isto é, se estão dentro dos limites da matriz.

* Se a posição estiver com valor 0, ela passa a valer 1 (nova figurinha colada).

* Se a posição já tiver valor maior ou igual a 1, o valor deve ser incrementado em 1 (nova figurinha repetida).

### 5.3 Listar Figurinhas Faltantes

Percorrer a matriz e exibir todas as posições (seleção e jogador) cujo valor seja igual a 0. Exibir, ao final da listagem, o total de figurinhas faltantes.

Exemplo considerando o álbum da seção 4:

```text
Brasil Jogador 2
Brasil Jogador 5
Argentina Jogador 1
Argentina Jogador 4
França Jogador 3
França Jogador 4
Alemanha Jogador 1
Total de 7 figurinhas faltantes
```

(Nota de ajuste: O texto original continha pequenos erros tipográficos de quebra de linha na listagem da França e Alemanha, corrigidos aqui para melhor compreensão lógica).

### 5.4 Listar Figurinhas Repetidas

Percorrer toda a matriz e exibir todas as posições cujo valor seja maior que 1, indicando a quantidade de repetidas (valor da célula menos 1). Exibir, ao final da listagem, o total de figurinhas repetidas (soma de todas as repetições da matriz).

Exemplo considerando o álbum da seção 4:

```text
Brasil 2 repetições do Jogador 3
Argentina 1 repetição do Jogador 5
Alemanha 1 repetição do Jogador 2
Total de 4 figurinhas repetidas

```

(Nota de ajuste: O texto bruto da linha da Alemanha continha "$-1$" e "Jogador 1", mas cruzando com a tabela real do enunciado, a Alemanha possui valor 2 no Jogador 2, o que gera 1 repetição).

### 5.5 Comparar Dois Álbuns (Verificar Trocas Possíveis)

Permitir carregar um segundo álbum, a partir de outro arquivo no mesmo formato da Seção 4, representando o álbum de outro(a) colecionador(a). Para cada posição em que o primeiro álbum tenha figurinha repetida (valor > 1) e o segundo álbum tenha a mesma figurinha faltante (valor = 0), identificar e listar essa posição como uma possível troca. 

E vice-versa, ou seja, essa opção deve mostrar todas as figurinhas repetidas do 1º álbum que servem para o 2º álbum, e também devem mostrar todas as figurinhas repetidas do 2º álbum que servem para o 1º álbum. Essa é a funcionalidade TINDER das figurinhas. Exibir, ao final, o total de trocas possíveis encontradas.

**Exemplo:**
Suponha os dois álbuns a seguir:

* Matriz do Álbum 1:

```text
1 0 3 1 0
0 1 1 0 2
1 1 0 0 1
0 2 1 1 1

```

* Matriz do Álbum 2:

```text
1 2 0 1 0
0 1 3 0 0
0 0 3 0 5
1 4 1 0 1

```

Possíveis trocas:

* **Álbum 1 para Álbum 2:** Brasil jogador 3; Argentina jogador 5; Alemanha jogador 4.
* **Álbum 2 para Álbum 1:** Brasil jogador 2; França jogador 3; Alemanha jogador 2.

Após a análise, deve solicitar a confirmação de troca. Caso a troca seja aceita, os álbuns ao final devem ficar conforme abaixo:

* Álbum 1 Modificado:

```text
1 1 2 1 0
0 1 1 0 1
1 1 1 0 1
0 1 1 1 1

```

* Álbum 2 Modificado:

```text
1 1 1 1 0
0 1 3 0 0
0 0 2 0 5
1 3 1 1 1

```

## 6. Requisitos Técnicos

* A matriz deve ser implementada como um array bidimensional (`int[][]`).

* Uso de `JAVA.util.Scanner` ou `JAVA.io.BufferedReader` para leitura dos arquivos.

* Código modularizado em métodos (por exemplo: `carregarAlbum`, `registrarFigurinha`, `listarFaltantes`,`listarRepetidas`, `compararAlbuns`).

* Uso de constantes/variáveis para as dimensões da matriz, evitando valores fixos espalhados pelo código.

* Interface por linha de comando (terminal), com menu textual conforme a Seção 5.

## 7. Tratamento de Erros e Validações

* Validar índices de seleção e jogador fora dos limites da matriz, sem permitir que o programa encerre de forma abrupta.

* Validar arquivos inexistentes ou com formato incorreto, exibindo mensagem clara ao usuário.

* Validar que os dois álbuns comparados na Seção 5.5 possuem as mesmas dimensões antes de iniciar a comparação.

* Validar entradas numéricas do usuário (por exemplo, opções de menu inválidas).

## 8. Critérios de Avaliação

| Critério                    | Peso     | Descrição                                                                                     |
|-----------------------------|----------|-----------------------------------------------------------------------------------------------|
| **Registrar figurinha**     | 20%      | Atualização correta da matriz (nova ou repetida), com validação dos índices informados.       |
| **Figurinhas faltantes**    | 20%      | Listagem correta de todas as posições com valor 0 e do total de faltantes.                    |
| **Figurinhas repetidas**    | 20%      | Listagem correta das posições com valor > 1 e do total de repetidas.                          |
| **Comparação entre álbuns** | 20%      | Identificação correta dos possíveis matches entre repetidas de um álbum e faltantes de outro. |
| **Tratamento de erros**     | 10%      | Validação de entradas e tratamento de exceções sem encerramento abrupto do programa.          |
| **Total**                   | **100%** | -                                                                                             |

## 9. Forma de Entrega

* Código-fonte JAVA (`.JAVA`) organizado em projeto.

* Arquivo(s) de exemplo de álbum (`.txt`) utilizados para testar o programa.

* Breve relatório (PDF ou texto) descrevendo as decisões de implementação.

* Identificação completa dos dois integrantes da dupla em todos os arquivos entregues.

## 10. Observações Finais e Extensões Opcionais (Bônus)

As sugestões abaixo não são obrigatórias, mas podem ser usadas como pontuação extra ou para diferenciar duplas com maior
domínio do conteúdo:

* Salvar no arquivo lido as alterações feitas durante a execução (por exemplo, salvar o álbum atualizado ao registrar novas figurinhas ou ao realizar as trocas).

* Gerar estatísticas adicionais, como percentual do álbum completo, seleção com mais figurinhas faltantes, ou jogador mais repetido.

* Implementar uma interface gráfica simples (fora do escopo mínimo do trabalho). A interface pode ser feita com a classe swing em JAVA ou via html que invoca o `back-end` feito em JAVA.