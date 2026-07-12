const API_BASE = 'http://localhost:4321/api';

const app = Vue.createApp({
  data() {
    return {
      selections: [],
      currentSelection: '',
      searchQuery: '',
      cards: [],
      album1: { name: 'Álbum #1', missing: 0, progress: 0, teams: [] },
      album2: { name: 'Álbum #2', missing: 0, progress: 0, teams: [] },
      commonStickers: [],
      possibleTrades: []
    };
  },
  mounted() {
    this.fetchInitialData();
  },
  methods: {
    async fetchInitialData() {
      try {
        const response = await fetch(`${API_BASE}/initial-data`);
        const data = await response.json();
        this.selections = data.selections.sort();
        this.currentSelection = data.currentSelection;
        this.cards = this.mapCards(data.stickers);
        this.reinitGsap();
      } catch (err) {
        console.error("Erro ao carregar dados iniciais:", err);
      }
    },
    async fetchSelection(selectionName) {
      if(this.currentSelection === selectionName) return;
      this.currentSelection = selectionName;
      try {
        const response = await fetch(`${API_BASE}/selection/${selectionName}`);
        const stickers = await response.json();
        this.cards = this.mapCards(stickers);
        this.reinitGsap();
        this.closeDropdown();
      } catch (err) {
        console.error("Erro ao carregar seleção:", err);
      }
    },
    closeDropdown() {
      const dropdownEl = document.querySelector('.dropdown-toggle');
      if (dropdownEl) {
        const dropdown = bootstrap.Dropdown.getInstance(dropdownEl);
        if (dropdown) dropdown.hide();
      }
    },
    searchSelection() {
      const query = this.searchQuery.trim();
      if (!query) {
        Swal.fire('Atenção!', 'Digite o nome de uma seleção para pesquisar.', 'warning');
        return;
      }
      
      const found = this.selections.find(s => s.toLowerCase() === query.toLowerCase());
      if (found) {
        this.fetchSelection(found);
        this.searchQuery = '';
      } else {
        Swal.fire('Erro!', 'Seleção não encontrada.', 'error');
      }
    },
    async fetchComparison() {
      try {
        const response = await fetch(`${API_BASE}/comparison`);
        const data = await response.json();
        this.album1 = data.album1;
        this.album2 = data.album2;
        this.commonStickers = data.commonStickers;
        this.possibleTrades = data.possibleTrades;
        console.log("Informações da comparação de álbuns:", data);
      } catch (err) {
        console.error("Erro ao carregar comparação:", err);
      }
    },
    async tradeSticker(tradeReq) {
      try {
        const response = await fetch(`${API_BASE}/trade`, {
          method: 'POST',
          body: JSON.stringify(tradeReq)
        });
        const result = await response.json();
        
        if (result.success) {
          Swal.fire('Sucesso!', result.message, 'success');
          // Reload everything
          this.fetchSelection(this.currentSelection);
          this.fetchComparison();
        } else {
          Swal.fire('Erro!', result.message, 'error');
        }
      } catch (err) {
        console.error("Erro ao realizar troca:", err);
        Swal.fire('Erro!', 'Ocorreu um erro inesperado.', 'error');
      }
    },
    mapCards(stickers) {
      return stickers.map((s, i) => {
        return {
          ...s,
          gradClass: `grad-${(i % 10) + 1}`
        };
      });
    },
    reinitGsap() {
      // Re-initialize GSAP after cards are rendered
      this.$nextTick(() => {
        initGsap();
      });
    }
  }
});

// Definição do Componente Vue "no-trade-message"
app.component('no-trade-message', {
  props: {
    message: {
      type: String,
      required: true
    }
  },
  template: '#no-trade-message-template'
});

// Definição do Componente Vue "gsap-card"
app.component('gsap-card', {
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
      // The parent will re-fetch data on success which will re-render,
      // but in case of error we can stop the spinner.
      setTimeout(() => { this.isTrading = false; }, 2000);
    }
  },
  template: '#gsap-card-template'
});

app.component('album-comparison-component', albumComparisonComponent);
app.component('common-stickers-component', commonStickersComponent);
app.component('possible-trades-component', possibleTradesComponent);

// Montar a aplicação no DOM
const vm = app.mount('#app');

// Expose vm to global so the Info button outside the #app can trigger fetchComparison,
// wait, we can just attach it in HTML or here.
document.querySelector(".info").addEventListener("click", () => {
    vm.fetchComparison();
});


// GSAP Logic separated into function so it can be called dynamically
let scrub, seamlessLoop, trigger;

function initGsap() {
  // Kill old trigger if exists to prevent duplicate pins
  if (trigger) trigger.kill();
  if (scrub) scrub.kill();
  if (seamlessLoop) seamlessLoop.kill();
  
  gsap.registerPlugin(ScrollTrigger);
  gsap.to(".card-container", {opacity: 1, delay: 0.1});
  
  let iteration = 0;
  const spacing = 0.1;
  const snap = gsap.utils.snap(spacing);
  const cardsElements = gsap.utils.toArray('.cards li');
  
  if (cardsElements.length === 0) return;
  
  seamlessLoop = buildSeamlessLoop(cardsElements, spacing);
  scrub = gsap.to(seamlessLoop, {
    totalTime: 0,
    duration: 0.5,
    ease: "power3",
    paused: true
  });
  
  trigger = ScrollTrigger.create({
    start: 0,
    onUpdate(self) {
      if (self.progress === 1 && self.direction > 0 && !self.wrapping) {
        wrapForward(self);
      } else if (self.progress < 1e-5 && self.direction < 0 && !self.wrapping) {
        wrapBackward(self);
      } else {
        scrub.vars.totalTime = snap((iteration + self.progress) * seamlessLoop.duration());
        scrub.invalidate().restart();
        self.wrapping = false;
      }
    },
    end: "+=3000",
    pin: ".gallery"
  });
  
  function wrapForward(trigger) {
    iteration++;
    trigger.wrapping = true;
    trigger.scroll(trigger.start + 1);
  }

  function wrapBackward(trigger) {
    iteration--;
    if (iteration < 0) {
      iteration = 9;
      seamlessLoop.totalTime(seamlessLoop.totalTime() + seamlessLoop.duration() * 10);
      scrub.pause();
    }
    trigger.wrapping = true;
    trigger.scroll(trigger.end - 1);
  }

  window.scrubTo = function(totalTime) {
    let progress = (totalTime - seamlessLoop.duration() * iteration) / seamlessLoop.duration();
    if (progress > 1) wrapForward(trigger);
    else if (progress < 0) wrapBackward(trigger);
    else trigger.scroll(trigger.start + progress * (trigger.end - trigger.start));
  };
  
  // Re-attach listeners to next/prev since the DOM might be the same but we need the new scrub reference
  document.querySelector(".next").onclick = () => window.scrubTo(scrub.vars.totalTime + spacing);
  document.querySelector(".prev").onclick = () => window.scrubTo(scrub.vars.totalTime - spacing);
}

function buildSeamlessLoop(items, spacing) {
  let overlap = Math.ceil(1 / spacing),
    startTime = items.length * spacing + 0.5,
    loopTime = (items.length + overlap) * spacing + 1,
    rawSequence = gsap.timeline({paused: true}),
    seamlessLoop = gsap.timeline({
      paused: true,
      repeat: -1,
      onRepeat() {
        this._time === this._dur && (this._tTime += this._dur - 0.01);
      }
    }),
    l = items.length + overlap * 2,
    time = 0,
    i, index, item;
  
  gsap.set(items, {xPercent: 400, opacity: 0, scale: 0});
  
  for (i = 0; i < l; i++) {
    index = i % items.length;
    item = items[index];
    time = i * spacing;
    rawSequence.fromTo(item, {scale: 0, opacity: 0}, {scale: 1, opacity: 1, zIndex: 100, duration: 0.5, yoyo: true, repeat: 1, ease: "power1.in", immediateRender: false}, time)
      .fromTo(item, {xPercent: 400}, {xPercent: -400, duration: 1, ease: "none", immediateRender: false}, time);
    i <= items.length && seamlessLoop.add("label" + i, time);
  }
  
  rawSequence.time(startTime);
  seamlessLoop.to(rawSequence, {
    time: loopTime,
    duration: loopTime - startTime,
    ease: "none"
  }).fromTo(rawSequence, {time: overlap * spacing + 1}, {
    time: startTime,
    duration: startTime - (overlap * spacing + 1),
    immediateRender: false,
    ease: "none"
  });
  return seamlessLoop;
}
