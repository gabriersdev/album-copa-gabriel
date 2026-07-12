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
    props: ['commonStickers'],
    template: `
        <div class="mb-4">
            <p class="m-0 p-0">
                <strong>Figurinhas em comum:</strong> <span>{{ commonStickers.length }}</span>
            </p>
            <details class="text-small mt-1">
                <summary class="text-primary">Detalhes</summary>
                <div class="pt-1">
                    <div>
                        <ul>
                            <li v-for="sticker in commonStickers" class="mb-1">
                                <span>Jogador #{{ sticker.number }} - Seleção {{ sticker.team }}.</span>
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
                                <span>Jogador #{{ trade.number }} - Seleção {{ trade.team }} do Album {{ trade.from }} para o Album {{ trade.to }}.&nbsp;</span>
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
