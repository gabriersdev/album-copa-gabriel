# Fluxograma (Esquema) de funcionamento do projeto de Álbum de Figurinhas

```mermaid
graph TD
    subgraph "Fluxo do Sistema de Álbum de Figurinhas"
        A[Início] --> B{Menu Principal};
        B --> C[1. Carregar Álbum];
        B --> D[2. Registrar Figurinha];
        B --> E[3. Listar Faltantes];
        B --> F[4. Listar Repetidas];
        B --> G[5. Comparar Álbuns para Troca];
        B --> H[6. Sair];

        C --> C1{Solicitar nome do arquivo};
        C1 --> C2{Arquivo existe e formato é válido?};
        C2 -- Sim --> C3[Carregar dados na matriz];
        C3 --> B;
        C2 -- Não --> C4[Exibir erro];
        C4 --> B;

        D --> D1{Solicitar seleção e jogador};
        D1 --> D2{Índices válidos?};
        D2 -- Sim --> D3{Figurinha já existe (valor >= 1)?};
        D3 -- Sim --> D4[Incrementar valor da célula];
        D3 -- Não --> D5[Definir valor da célula como 1];
        D4 & D5 --> B;
        D2 -- Não --> D6[Exibir erro];
        D6 --> B;

        E --> E1[Percorrer matriz em busca de células com valor 0];
        E1 --> E2[Exibir lista de faltantes e total];
        E2 --> B;

        F --> F1[Percorrer matriz em busca de células com valor > 1];
        F1 --> F2[Exibir lista de repetidas e total];
        F2 --> B;

        G --> G1[Carregar Álbum 2];
        G1 --> G2{Dimensões dos dois álbuns são iguais?};
        G2 -- Sim --> G3[Identificar e listar trocas possíveis (bidirecional)];
        G3 --> G4{Confirmar troca?};
        G4 -- Sim --> G5[Atualizar matrizes de ambos os álbuns];
        G5 --> B;
        G4 -- Não --> B;
        G2 -- Não --> G6[Exibir erro];
        G6 --> B;

        H --> I[Fim];
    end
```
