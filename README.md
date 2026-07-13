# Projeto: Álbum de Figurinhas da Copa do Mundo - Versão Gabriel e Jorge

Este projeto é um sistema de gerenciamento para um álbum de figurinhas da Copa do Mundo, desenvolvido em Java com uma interface moderna na Web ("Tinder de Álbum da Copa"). O projeto engloba um backend construído em Java e um frontend que consome esta API utilizando HTML, CSS, e Vue.js.

## Dependências

O projeto utiliza alguns scripts que são acionados via comandos NPM para automatizar a execução de tarefas, assim como gerenciar as requisições HTTP da interface local.
Para utilizá-los é necessário que tenha o NodeJS instalado no ambiente.  
[Clique aqui e faça o donwload do NodeJS](https://nodejs.org/en/download).  
[Consulte os comandos NPM clicando aqui](package.json).

## 1. Descrição

O sistema simula o gerenciamento de um álbum de figurinhas, permitindo ao usuário realizar operações como carregar um álbum, registrar novas figurinhas, listar as que faltam e as repetidas, e comparar dois álbuns para encontrar oportunidades de troca (match).

O projeto conta com dois modos de operação:
1. **Modo Web (Tinder da Copa):** Uma API Java que serve os dados para uma aplicação Vue.js interativa.
2. **Modo Terminal:** Uma aplicação interativa por linha de comando (`Main.java`).

## 2. Funcionalidades e Regras de Negócio

- **Carregamento de Álbum:** O sistema lê dados de um arquivo de texto (`album.txt`) para inicializar os álbuns.
- **Visualização e Pesquisa (Web):** Navegação fluida nas figurinhas por meio de um carrossel implementado com animações avançadas. É possível pesquisar rapidamente por qualquer seleção.
- **Status das Figurinhas:** Uma figurinha pode estar **Faltante (0)**, **Colada (1)**, ou **Repetida (>1)**.
- **Comparação e Trocas (Match):** 
  - O sistema identifica figurinhas repetidas no Álbum #1 que estão faltando no Álbum #2, e vice-versa.
  - A troca ("transferência") é possível e garantida apenas se o doador possui duplicatas. Ao efetuar a troca, o estoque é atualizado em tempo real e a interface reagrupa as estatísticas instantaneamente.
  
## 3. Arquitetura

O projeto organiza o backend (Java) e o frontend (Web) da seguinte forma:

- Diretório `ui/`: Contém a aplicação Frontend construída com HTML, Bootstrap (para base), CSS puro customizado, e Vue.js para reatividade.
- Diretório `assets/`: arquivos de exemplo para teste e arquivos base.
- Diretório `scripts/`: scripts JS para automatizar tarefas do projeto.
- Diretório `docs/`: arquivos de documentação do projeto.
- Diretório `src/` (Backend):
    - `src/Album.java`: Lógica específica para o tratamento dos álbuns.
    - `src/controller/Controller.java`: Lógica de regras de negócio.
    - `src/Main.java`: Ponto de partida da execução do modo Terminal.
    - `src/ApiMain.java`: Servidor HTTP que expõe endpoints (API REST) para o frontend Vue.js.
    - `src/SoccerMain.java`: Lógica específica para as seleções.
    - `src/utils/Utils.java`: Métodos utilitários auxiliares.

## 4. Como Executar

O projeto possui comandos NPM já configurados (`package.json`) que facilitam o processo.

### Opção A: Executar via Interface Web (Recomendado)

Esta abordagem subirá o servidor Java e a interface de usuário simultaneamente:

1. Abra um terminal na raiz do projeto e inicie a API:
   ```bash
   npm run start:api
   ```
2. Em um segundo terminal, inicie o servidor da interface Web:
   ```bash
   npm run start:ui
   ```
3. Acesse a aplicação no seu navegador: `http://localhost:4322/`

### Opção B: Executar via Terminal Clássico

Se preferir usar o menu de texto:
1. Compile os arquivos Java:
   ```bash
   npm run compile:java
   ```
2. Execute o programa:
   ```bash
   java -cp src Main
   ```

*Siga as instruções apresentadas no menu para interagir com o sistema. Certifique-se de que o arquivo `album.txt` com o formato correto esteja disponível no diretório raiz.*

## 5. Formato do Arquivo de Entrada (`album.txt`)

O arquivo de entrada base do banco de dados deve seguir o seguinte formato:

* **1ª linha:** Dois números inteiros separados por espaço: a quantidade de seleções (`M`) e a quantidade de figurinhas por seleção (`N`).
* **M linhas seguintes:** O nome de cada seleção.
* **M linhas finais:** Cada linha contém `N` valores inteiros separados por espaço, representando o estado de cada figurinha (0 para faltante, 1 para colada, >1 para repetida).

## Execução dos Testes Automatizados

Caso queira debugar ou testar a lógica sem executar a aplicação:

- Execute o teste rapidamente usando o script NPM já preparado:
  ```bash
  npm run test
  ```

> [!NOTE]
> Este comando compilará as classes e executará a classe `Testing.java` automaticamente.
