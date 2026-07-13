const noTradeMessageComponent = {
  props: {
    message: {
      type: String,
      required: true
    }
  },
  template: `
    <div class="text-base text-white text-balance">
      <p class="">
          <span class="text-danger d-inline-flex align-items-center justify-content-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-lg" viewBox="0 0 16 16">
              <path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8z"/>
            </svg>
          </span>&nbsp;
        <span class="font-inter lh-base text-danger-emphasis">{{ message }}</span>
      </p>
    </div>
  `
};

const gsapCardComponent = {
  props: {
    card: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      isTrading: false
    };
  },
  methods: {
    doTrade(from, to) {
      this.isTrading = true;
      this.$emit('trade', {
        stickerNumber: parseInt(this.card.number),
        team: this.card.team,
        fromAlbum: from,
        toAlbum: to
      });
      setTimeout(() => { this.isTrading = false; }, 2000);
    }
  },
  template: `
    <li>
      <div class="card-container" :class="card.gradClass">
        <div class="card-inner-border"></div>
        <div class="card-header text-gradient">
          <span class="font-inter">Jogador #{{card.number}}</span>
        </div>
        <div class="card-center-number text-gradient">{{card.number}}</div>
        <div class="card-footer text-gradient">
          <p class="d-block font-inter m-0 p-0">
            <span :class="(card.team.toLowerCase() === 'argentina' ? 'text-decoration-line-through ' : '')">
              {{card.team}}
            </span>
          </p>
        </div>
      </div>
      
      <div class="mt-3 d-flex flex-column gap-3">
        <!-- Se ambos os álbuns possuírem a mesma figurinha -->
        <no-trade-message v-if="card.album1Has && card.album2Has" message="Os dois álbuns possuem esta carta. Não é possível trocá-la."></no-trade-message>
        
        <!-- Se nenhum dos álbums possuírem a figurinha -->
        <no-trade-message v-else-if="!card.album1Has && !card.album2Has" message="Nenhum dos álbuns possuem esta carta. Não é possível trocá-la."></no-trade-message>
        
        <!-- Oferece troca, já que 1 álbum tem a figurinha e o outro não e possui duplicata -->
        <div
          v-else-if="(card.album1IsDuplicate && !card.album2Has) || (card.album2IsDuplicate && !card.album1Has)"
          style="max-width: 300px" class="mx-auto"
        >
          <div class="d-flex flex-column gap-1">
            <button
              type="button"
              class="btn btn-primary text-base font-inter"
              @click="doTrade(card.album1IsDuplicate ? 'Álbum #1' : 'Álbum #2', card.album1IsDuplicate ? 'Álbum #2' : 'Álbum #1')"
              :disabled="isTrading"
            >
              <span v-if="!isTrading" class="d-block">
                Transferir do {{ card.album1IsDuplicate ? 'Álbum #1' : 'Álbum #2' }}
              </span>
              <div v-else class="d-flex align-items-center justify-content-center gap-2">
                <div class="spinner-border text-dark spinner-border-sm" style="border-width: 3px" role="status">
                  <span class="visually-hidden">Trocando...</span>
                </div>
                <span class="d-block">Trocando...</span>
              </div>
            </button>
            <span class="text-small d-inline-block text-balance font-inter">
              O {{ card.album1IsDuplicate ? 'Álbum #1' : 'Álbum #2' }} possui mais de uma desta carta, portanto é possível transferir.
            </span>
          </div>
        </div>
        
        <!-- Nao possui repetida para trocar -->
        <no-trade-message v-else message="Um dos álbuns possui esta carta, mas não tem repetidas para trocar."></no-trade-message>
      </div>
    </li>
  `
};

const albumComparisonComponent = {
  props: ['album'],
  template: `
    <div class="mb-4">
      <p class="m-0 p-0">
        <strong class="d-block">{{ album.name }}:</strong>
        <span>Faltam {{ album.missing }} figurinhas ({{ album.progress }}% concluído)</span>
      </p>
      <details class="text-small mt-1">
        <summary class="text-primary">Detalhes</summary>
        <div class="pt-1">
          <div>
            <ul>
              <li v-for="team in album.teams" class="mb-1">
                <span>Seleção {{ team.name }} - {{ team.progress }}% completa ({{ team.missing }} figurinhas faltantes).</span>
              </li>
            </ul>
          </div>
        </div>
      </details>
    </div>
  `
};

const commonStickersComponent = {
  props: ['commonStickers', 'totalStickers'],
  computed: {
    stickersByTeam() {
      const grouped = {};
      for (const sticker of this.commonStickers) {
        if (!grouped[sticker.team]) {
          grouped[sticker.team] = [];
        }
        grouped[sticker.team].push(sticker);
      }
      return grouped;
    },
    totalStickersSanitization() {
      return this.totalStickers?.toString().replace(/[\s\[\]]/g, '');
    }
  },
  template: `
        <div class="mb-4">
            <p class="m-0 p-0">
                <strong>Figurinhas em comum:</strong> <span>{{ commonStickers.length }} de {{ totalStickersSanitization }}</span>
            </p>
            <details class="text-small mt-1">
                <summary class="text-primary">Detalhes</summary>
                <div class="pt-1">
                    <div v-for="(stickers, team) in stickersByTeam" :key="team" class="mb-2">
                        <strong>Seleção do(a) {{ team }}</strong>
                        <ul>
                            <li v-for="sticker in stickers" :key="sticker.number" class="mb-1">
                                <span>Jogador #{{ sticker.number }}.</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </details>
        </div>
    `
};

const possibleTradesComponent = {
  props: ['trades'],
  template: `
        <div class="mb-0">
            <p class="m-0 p-0">
                <strong>Possíveis trocas:</strong> <span>{{ trades.length }}</span>
            </p>
            <details class="text-small mt-1">
                <summary class="text-primary">Detalhes</summary>
                <div class="pt-1">
                    <div>
                        <ul>
                            <li v-for="trade in trades" class="mb-1">
                                <span>Jogador #{{ trade.number }} - Seleção {{ trade.team }} do {{ trade.from }} para o {{ trade.to }}.&nbsp;</span>
                                <button class="fw-normal text-small border-0 p-0 m-0 bg-transparent text-primary"
                                        @click="requestTrade(trade)"
                                        :disabled="trade.requested"
                                        :class="{ 'cursor-not-allowed opacity-50': trade.requested }">
                                    <span v-if="!trade.requested" class="text-decoration-underline">Trocar</span>
                                    <span v-else>Troca solicitada. Aguarde...</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                </div>
            </details>
        </div>
    `,
  methods: {
    requestTrade(trade) {
      trade.requested = true;
      this.$emit('trade', {
        stickerNumber: trade.number,
        team: trade.team,
        fromAlbum: trade.from,
        toAlbum: trade.to
      });
    }
  }
};
