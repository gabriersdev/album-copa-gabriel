# Projeto: Álbum de Figurinhas da Copa do Mundo - Versão Gabriel e Jorge

Este projeto é um sistema de gerenciamento para um álbum de figurinhas da Copa do Mundo, desenvolvido em Java como parte
de um trabalho prático acadêmico.

## Dependências

O projeto utiliza álguns scripts que são acionados via comandos NPM para automatizar a execução de tarefas.
Para utilizá-los é necessário que tenha o NodeJS instalado no ambiente.  
[Clique aqui e faça o donwload do NodeJS](https://nodejs.org/en/download).  
[Consulte os comandos NPM clicando aqui](package.json).

## 1. Descrição

O sistema simula o gerenciamento de um álbum de figurinhas, permitindo ao usuário realizar operações como carregar um
álbum, registrar novas figurinhas, listar as que faltam e as repetidas, e comparar dois álbuns para encontrar
oportunidades de troca.

A lógica principal é implementada na classe `Main.java` e a estrutura de dados principal é uma matriz
bidimensional que representa as seleções e as figurinhas.

## 2. Funcionalidades

O programa oferece um menu de texto com as seguintes opções:

1. Carregar álbum: Carrega os dados de um álbum a partir de um arquivo de texto (`album.txt`).
2. Registrar nova figurinha: Adiciona uma nova figurinha ao álbum, atualizando seu status para colada ou repetida.
3. Listar figurinhas faltantes: Exibe todas as figurinhas que o colecionador ainda não possui.
4. Listar figurinhas repetidas: Mostra todas as figurinhas que o colecionador possui em duplicidade.
5. Comparar dois álbuns: Identifica e sugere trocas possíveis entre o álbum do usuário e o de um segundo colecionador.
6. Sair: Encerra o programa.

## 3. Arquitetura

O programa se organiza da seguinte forma:

- Diretório `assets`: arquivos de exemplo para teste e arquivos base.
- Diretório `scripts`: scripts JS para automatizar tarefas do projeto.
- Diretório `docs`: arquivos de documentação do projeto.
- Diretório `src`:
    - `src/Album.java`: Lógica específica para o tratamento dos álbuns.
    - `src/controller.Controller.java`: Lógica principal do projeto.
    - `src/Main`: Ponto de partida da execução do projeto.
    - `src/SoccerMain`: Lógica específica para as seleções.
    - `src/utils.Utils`: Métodos utilitários para o funcionamento do projeto.

## 4. Como Executar

1. Compilar o código:
   ```bash
   javac Main.java
   ```
2. Executar o programa:
   ```bash
   java Main
   ```
3. Siga as instruções apresentadas no menu para interagir com o sistema. Certifique-se de que um arquivo `album.txt` com
   o formato correto esteja disponível no diretório.

## 5. Formato do Arquivo de Entrada (`album.txt`)

O arquivo de entrada deve seguir o seguinte formato:

* 1ª linha: Dois números inteiros separados por espaço: a quantidade de seleções (`M`) e a quantidade de figurinhas por
  seleção (`N`).
* M linhas seguintes: O nome de cada seleção.
* M linhas finais: Cada linha contém `N` valores inteiros separados por espaço, representando o estado de cada
  figurinha (0 para faltante, 1 para colada, >1 para repetida).

## Execução do teste

- Passo 1: Primeiro é preciso compilar os scripts `.java`, para isso execute o comando:
  ```bash
  javac src/Main.java src/Testing.java
  ```

- Passo 2: Depois, execute o arquivo compilado da class Testing:
  ```bash
  java -cp src Testing
  ```

> [!IMPORTANT]
> Para cada alteração em um desses arquivos, é necessário executar o primeiro comando (passo 1) para que os arquivo
> sejam novamente compilados.

> [!NOTE]
> Também é possível executar o teste através do comando NPM `npm run test`.
