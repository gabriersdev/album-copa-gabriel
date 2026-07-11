# Diretrizes de Codificação Java

Este documento fornece diretrizes de codificação focadas em **Clean Code** e escalabilidade, específicas para o desenvolvimento do projeto Álbum de Figurinhas em Java.

## Nomenclatura e Padrões

- **Idioma**: Utilize inglês (US) para nomes de classes, métodos, variáveis e pacotes (ex: `AlbumService`, `registerSticker`, `missingStickers`).
- **Classes**: Use **PascalCase** e nomes que sejam substantivos (ex: `FileHandler`, `TradeManager`, `Album`).
- **Métodos e Variáveis**: Use **camelCase**. Métodos devem possuir nomes baseados em verbos de ação (ex: `calculateMissing`, `loadAlbumData`).
- **Constantes**: Use **UPPER_SNAKE_CASE** para campos `static final` (ex: `MAX_TEAMS`, `DEFAULT_FILE_PATH`).

## Princípios de Clean Code

- **Funções Pequenas e Coesas**: Um método deve ter uma única responsabilidade. Se um método exceder 20 linhas de código, extraia partes da lógica para métodos privados bem nomeados.
- **Nomes Significativos**: Evite nomes genéricos ou abreviaturas como `m`, `n`, `data`, `arr`. Prefira nomes descritivos como `stickerStatusMatrix`, `totalTeams`, `stickersPerTeam`.
- **Evite Magic Numbers**: Valores literais que controlam a lógica do domínio devem ser encapsulados em constantes ou `Enums`. Por exemplo:
  - `public static final int STICKER_MISSING = 0;`
  - `public static final int STICKER_OWNED = 1;`
- **Reduza a Complexidade Ciclomática**:
  - Evite múltiplos níveis de aninhamento (ex: `for` dentro de `for` dentro de `if` profundo).
  - Use **Guard Clauses** (retorno antecipado) para tratar condições de erro ou limites no início do método, evitando blocos `else` longos.
- **Comentários Estratégicos**: O código deve ser prioritariamente autoexplicativo. Use JavaDoc e comentários inline apenas para justificar decisões de design complexas, explicar regras de negócio peculiares, ou documentar APIs públicas de classes `Service`.

## Tratamento de Erros e Validação

- **Fail-Fast**: Ao receber parâmetros de seleção e jogador, valide se os índices estão dentro dos limites da matriz imediatamente. Lance uma exceção semântica (ex: `InvalidStickerIndexException`) em caso de erro, garantindo que o programa não sofra com `ArrayIndexOutOfBoundsException` não tratadas.
- **Segurança de Recursos**: Para operações de leitura e gravação em `album.txt`, utilize obrigatoriamente o bloco `try-with-resources` para garantir que objetos como `Scanner` ou `BufferedReader` sejam adequadamente fechados.
- **Validação de Formato**: Ao processar a primeira linha do arquivo (valores `M` e `N`), trate possíveis `NumberFormatException` para evitar a quebra se o arquivo estiver corrompido.

## Escalabilidade e Desempenho

- **Operações Bidirecionais Elegantes**: Na lógica "Tinder", a varredura da matriz [M][N] para checar figurinhas de dois álbuns tem tempo de execução $O(M \times N)$. Ao invés de buscar cada figurinha repetidamente, faça uma única passagem simultânea comparando `albumA[i][j]` e `albumB[i][j]`.
- **Coleções do Java**: A matriz `int[][]` atende bem aos requisitos iniciais para os números das figurinhas. No entanto, se o mapeamento de "Nome da Seleção" para "Índice (Linha)" for frequente, implemente um cache com `Map<String, Integer>` ou use um `Enum` interno para acelerar a localização das seleções pelo nome, em vez de iterar sobre o array de seleções em cada busca O(N).

## Boas Práticas Adicionais

- **Imutabilidade**: Se você criar classes puras para representar as respostas (ex: `TradeResult`), declare seus campos como `final` sempre que possível.
- **Programação Orientada a Interfaces**: Dependa de abstrações (ex: `List<String> list = new ArrayList<>()`) nas declarações de variáveis, retornos de método e injeções de construtor.
