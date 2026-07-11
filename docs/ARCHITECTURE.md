# Arquitetura do Projeto: Álbum de Figurinhas

Este documento descreve a arquitetura do projeto Álbum de Figurinhas da Copa do Mundo, orientando o desenvolvimento para garantir escalabilidade, manutenibilidade e a correta aplicação das regras de negócio.

## Visão Geral

O sistema é uma aplicação de console em Java que gerencia um álbum de figurinhas. A lógica central lida com o estado das figurinhas (faltantes, coladas, repetidas) por meio de uma matriz bidimensional e permite operações como registrar novas figurinhas, listar faltantes/repetidas e comparar álbuns para trocas (Funcionalidade "Tinder").

## Estrutura de Diretórios

Para garantir o princípio de Single Responsibility Principle (SRP) e facilitar a escalabilidade, a estrutura do projeto deve ser organizada em pacotes lógicos:

- `src/` (ou `src/project/`)
  - `Main.java`: Ponto de entrada da aplicação, contendo o loop principal e o menu interativo.
  - `model/`: Classes de domínio (ex: `Album`, `Sticker`, `Team`). Representam a estrutura de dados e regras puras.
  - `service/`: Classes de lógica de negócio (ex: `AlbumService`, `TradeService`). Orquestram as ações como comparar álbuns e registrar figurinhas.
  - `repository/` ou `io/`: Classes para leitura e escrita de dados, isolando o acesso ao arquivo `album.txt` (ex: `AlbumRepository`, `FileHandler`).
  - `exception/`: Classes de exceções customizadas para tratamento de erros específicos do domínio.
  - `utils/`: Utilitários gerais (validações, formatação).

## Fluxo de Dados

1. **Carga de Dados**: A aplicação inicia solicitando a leitura do estado inicial do arquivo `album.txt` através da camada `repository/io`. Os dados populam a entidade na camada `model` (ex: a matriz do álbum).
2. **Interação do Usuário**: O menu de texto em `Main` captura as ações do usuário.
3. **Processamento Lógico**: As chamadas do menu são direcionadas aos `services`. Por exemplo, a operação de "Registrar nova figurinha" aciona `AlbumService.registerSticker(...)`, aplicando a regra de negócio (se faltante vira 1, se repetida incrementa).
4. **Comparação de Álbuns (Tinder)**: A funcionalidade de troca utiliza o `TradeService`, que recebe dois objetos `Album`, percorre a matriz verificando onde o Álbum 1 possui figurinha repetida e o Álbum 2 possui figurinha faltante, e vice-versa. Em seguida, os objetos são atualizados em memória.

## Princípios Arquiteturais

- **Separação de Preocupações (SoC)**: Manter a lógica de UI (System.out.println / Scanner) estritamente separada da lógica de negócio. Apenas a camada `Main` ou de visualização deve printar dados.
- **Isolamento de Entrada/Saída**: Somente a camada `repository/io` deve ter conhecimento das especificidades e do formato de `album.txt`.
- **Tratamento Centralizado de Exceções**: A aplicação não deve encerrar abruptamente por erros de input. Exceções devem ser capturadas nos controllers/menus e uma mensagem amigável deve ser exibida ao usuário, solicitando um novo fluxo.
