# Documentação de Integração com o Backend

Este documento descreve os dados, funções e informações que o frontend espera do backend para a página de troca de figurinhas e suas interações. O frontend atualmente utiliza dados estáticos (mock data), e os endpoints a seguir devem substituí-los.

---

## 1. Endpoints de Dados

### 1.1. Dados Gerais da Aplicação

#### **Endpoint:** `GET /api/initial-data`
**Descrição:** Retorna os dados iniciais necessários para renderizar a página, incluindo as seleções disponíveis e os dados da primeira seleção a ser exibida.

**Resposta Esperada (JSON):**
```json
{
  "selections": [
    "Brasil",
    "Argentina",
    "França",
    "Alemanha"
  ],
  "currentSelection": "Brasil",
  "stickers": [
    {
      "number": "1",
      "team": "Brasil",
      "album1Has": true,
      "album1IsDuplicate": false,
      "album2Has": true,
      "album2IsDuplicate": true
    },
    {
      "number": "2",
      "team": "Brasil",
      "album1Has": true,
      "album1IsDuplicate": true,
      "album2Has": false,
      "album2IsDuplicate": false
    },
    {
      "number": "3",
      "team": "Brasil",
      "album1Has": false,
      "album1IsDuplicate": false,
      "album2Has": false,
      "album2IsDuplicate": false
    }
  ]
}
```

---

### 1.2. Dados de uma Seleção Específica

#### **Endpoint:** `GET /api/selection/{selectionName}`
**Descrição:** Retorna a lista de figurinhas para uma seleção específica, informando o status de cada figurinha nos dois álbuns.

**Parâmetros de URL:**
- `selectionName` (string): O nome da seleção (ex: "Argentina").

**Resposta Esperada (JSON):**
```json
[
  {
    "number": "1",
    "team": "Argentina",
    "album1Has": true,
    "album1IsDuplicate": false,
    "album2Has": false,
    "album2IsDuplicate": false
  }
]
```

---

### 1.3. Dados de Comparação dos Álbuns (Para o Modal)

#### **Endpoint:** `GET /api/comparison`
**Descrição:** Retorna um objeto completo com a comparação entre os dois álbuns, incluindo estatísticas, figurinhas em comum e trocas possíveis.

**Resposta Esperada (JSON):**
```json
{
  "album1": {
    "name": "Álbum #1",
    "missing": 45,
    "progress": 50,
    "teams": [
      { "name": "Brasil", "progress": 85, "missing": 3 },
      { "name": "Argentina", "progress": 90, "missing": 1 }
    ]
  },
  "album2": {
    "name": "Álbum #2",
    "missing": 12,
    "progress": 80,
    "teams": [
      { "name": "Brasil", "progress": 70, "missing": 6 },
      { "name": "Argentina", "progress": 95, "missing": 0 }
    ]
  },
  "commonStickers": [
    { "number": 1, "team": "Brasil" },
    { "number": 10, "team": "Argentina" }
  ],
  "possibleTrades": [
    { "number": 11, "team": "Brasil", "from": "Álbum #1", "to": "Álbum #2" },
    { "number": 5, "team": "Argentina", "from": "Álbum #2", "to": "Álbum #1" }
  ]
}
```

---

## 2. Endpoints de Ação

### 2.1. Realizar uma Troca de Figurinha

#### **Endpoint:** `POST /api/trade`
**Descrição:** Executa a troca de uma figurinha entre dois álbuns. O backend deve validar se a troca é possível (ex: se o álbum de origem possui a figurinha como duplicata).

**Corpo da Requisição (JSON):**
```json
{
  "stickerNumber": 11,
  "team": "Brasil",
  "fromAlbum": "Álbum #1",
  "toAlbum": "Álbum #2"
}
```

**Resposta Esperada (JSON):**

* **Sucesso:**
```json
{
  "success": true,
  "message": "Troca realizada com sucesso!"
}
```

* **Erro:**
```json
{
  "success": false,
  "message": "Erro: A troca não pôde ser realizada. O álbum de origem não possui esta figurinha como duplicata."
}
```
