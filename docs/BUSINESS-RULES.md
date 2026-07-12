# Regras de Negócio

1.  **Estrutura do Álbum**:
    *   O álbum é representado por uma matriz bidimensional de números inteiros (`int[][]`).
    *   As dimensões da matriz são `M` (número de seleções) por `N` (número de figurinhas por seleção).
    *   O valor em cada célula `[linha][coluna]` significa:
        *   `0`: A figurinha é **faltante**.
        *   `1`: A figurinha foi **colada** (o colecionador a possui).
        *   `> 1`: O colecionador possui a figurinha colada e tem `(valor - 1)` unidades **repetidas**.

2.  **Arquivo de Entrada (`album.txt`)**:
    *   O programa deve carregar os dados de um arquivo de texto.
    *   **Formato**:
        1.  A primeira linha contém `M` e `N` (dimensões da matriz), separados por espaço.
        2.  As `M` linhas seguintes contêm os nomes das seleções.
        3.  As `M` linhas finais contêm os dados da matriz, com `N` números inteiros por linha, separados por espaço.

3.  **Registro de Nova Figurinha**:
    *   Ao registrar uma figurinha para uma dada `[seleção][jogador]`:
        *   Se o valor atual for `0`, ele se torna `1`.
        *   Se o valor atual for `>= 1`, ele é incrementado em `1`.

4.  **Comparação para Troca (Funcionalidade "Tinder")**:
    *   Uma troca é possível entre o **Álbum 1** e o **Álbum 2** se:
        *   O Álbum 1 tem uma figurinha repetida (`valor > 1`) que falta no Álbum 2 (`valor == 0`).
        *   **E vice-versa**: O Álbum 2 tem uma figurinha repetida que falta no Álbum 1.
    *   Se a troca for confirmada, as matrizes de ambos os álbuns devem ser atualizadas para refletir a troca.

5.  **Validações e Tratamento de Erros**:
    *   O sistema não deve encerrar abruptamente.
    *   Validar se os índices de seleção/jogador estão dentro dos limites da matriz.
    *   Verificar se os arquivos de entrada existem e se o formato está correto.
    *   Garantir que os dois álbuns a serem comparados tenham as mesmas dimensões.
    *   Não deve ser possível trabalhar com álbuns em que as seleções tenham quantidades diferentes de jogadores, já que, possivelmente, se não se tratar de erro são de copas diferentes.

## Diagrama de Fluxo (Mermaid)

[Este diagrama ilustra o fluxo de interações do usuário com o sistema, desde o menu principal até a execução de cada funcionalidade.](./SCHEMA.md)

