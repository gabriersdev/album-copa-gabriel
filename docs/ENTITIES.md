# Entidades do Sistema

Com base na documentação do projeto (Arquitetura, Regras de Negócio e README), as seguintes entidades e componentes
podem ser criados e desenvolvidos para estruturar o sistema de forma escalável e seguir o Princípio da Responsabilidade
Única (SRP):

## 1. Entidades de Domínio (Model)

Representam a estrutura de dados principal e as regras puras de negócio.

* **`Album`**: Entidade principal que representa o álbum de um colecionador. Deve conter as dimensões (M seleções e N
  figurinhas por seleção), os nomes das seleções e a matriz bidimensional indicando a quantidade de cada figurinha.
* **`Sticker` (Figurinha)**: Representa os dados de uma figurinha isolada. Pode encapsular regras como verificar se
  está "faltante", "colada" ou "repetida".
* **`Team` (Seleção)**: Representa uma equipe no álbum. Agrupa o nome da seleção e pode encapsular a lista de figurinhas
  pertencentes a ela.

## 2. Lógica de Negócio (Service)

Classes responsáveis por orquestrar as operações e aplicar as regras de negócio de fato.

* **`AlbumService`**: Gerencia operações relacionadas ao álbum, como por exemplo: registrar uma nova figurinha
  incrementando seu valor correspondente e extrair a lista de figurinhas faltantes e repetidas.
* **`TradeService`**: Gerencia a lógica de comparação para trocas (Funcionalidade "Tinder"). Avalia dois álbuns
  verificando de forma bidirecional se as repetidas de um preenchem as faltantes do outro e aplica a troca.

## 3. Persistência de Dados (Repository / IO)

Isolam a entrada/saída (I/O) das outras regras.

* **`AlbumRepository`** (ou **`FileHandler`**): Fica responsável por ler o arquivo base de dados (ex: `album.txt`),
  interpretar os parâmetros de entrada, nomes e a matriz, e instanciar os objetos de domínio (como `Album`). Também fará
  a persistência após eventuais alterações.

## 4. Visualização e Orquestração (View / controller.Controller)

Classes que cuidam das interações com o usuário, delegando a lógica para as camadas apropriadas.

* **`Main`** (ou **`AlbumCopaMain`**): Ponto de entrada responsável pelo loop de execução, apresentando o menu de
  opções.
* **`controller.Controller`**: Recebe os inputs do `Main`, valida as requisições iniciais e invoca os métodos
  correspondentes nas classes de `Service`.

## 5. Tratamento de Exceções (Exception)

* **Exceções Específicas do Domínio**: Criação de classes de exceção personalizadas para tratar regras violadas de
  maneira limpa (ex: `InvalidStickerIndexException` caso o jogador solicitado não exista, ou
  `InvalidAlbumDimensionException` para comparação de álbuns incompatíveis).