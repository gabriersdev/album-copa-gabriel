
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
              <li v-for="team in album.teams">
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
                            <li v-for="sticker in commonStickers">
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
                            <li v-for="trade in trades">
                                <span>Jogador #{{ trade.number }} - Seleção {{ trade.team }} do Album {{ trade.from }} para o Album {{ trade.to }}.&nbsp;</span>
<!--                                TODO: adicionar feedback ao usuário: se trade.requested, impossivel clicar no botão (usar cursor not-allowed e opacity alem de disabled) -->
                                <button class="fw-normal text-small border-0 p-0 m-0 bg-transparent text-primary" @click="requestTrade(trade)">
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
        }
    }
};
