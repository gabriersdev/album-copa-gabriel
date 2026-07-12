const app = Vue.createApp({
  data() {
    return {
      // Array de dados iterados pelo Vue para gerar os 10 cards
      cards: [
        {number: '1', gradClass: 'grad-7'},
        {number: '2', gradClass: 'grad-6'},
        {number: '3', gradClass: 'grad-5'},
        {number: '4', gradClass: 'grad-4'},
        {number: '5', gradClass: 'grad-3'},
        {number: '6', gradClass: 'grad-2'},
        {number: '7', gradClass: 'grad-1'},
        {number: '8', gradClass: 'grad-8'},
        {number: '9', gradClass: 'grad-9'},
        {number: '10', gradClass: 'grad-1'},
        {number: '11', gradClass: 'grad-10'},
        {number: '12', gradClass: 'grad-2'},
        {number: '13', gradClass: 'grad-3'},
        {number: '14', gradClass: 'grad-4'},
        {number: '15', gradClass: 'grad-5'},
        {number: '16', gradClass: 'grad-6'},
        {number: '17', gradClass: 'grad-7'},
        {number: '18', gradClass: 'grad-9'},
        {number: '19', gradClass: 'grad-10'},
        {number: '20', gradClass: 'grad-8'},
        {number: '21', gradClass: 'grad-3'},
        {number: '22', gradClass: 'grad-6'},
        {number: '23', gradClass: 'grad-5'},
        {number: '24', gradClass: 'grad-4'},
      ],
      album1: {
        name: 'Álbum #1',
        missing: 45,
        progress: 50,
        teams: [
          {name: 'XYZ', progress: 85, missing: 3},
          {name: 'ABC', progress: 90, missing: 1}
        ]
      },
      album2: {
        name: 'Álbum #2',
        missing: 12,
        progress: 80,
        teams: [
          {name: 'XYZ', progress: 85, missing: 3},
          {name: 'ABC', progress: 90, missing: 1}
        ]
      },
      commonStickers: [
        {number: 1, team: 'ABC'},
        {number: 10, team: 'DEF'},
        {number: 5, team: 'XYZ'}
      ],
      possibleTrades: [
        {number: 11, team: 'ABC', from: 'X', to: 'Y', requested: false},
        {number: 5, team: 'XYZ', from: 'Y', to: 'X', requested: false}
      ]
    };
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
    number: {
      type: String,
      required: true
    },
    gradientClass: {
      type: String,
      required: true
    }
  },
  template: '#gsap-card-template'
});

app.component('album-comparison-component', albumComparisonComponent);
app.component('common-stickers-component', commonStickersComponent);
app.component('possible-trades-component', possibleTradesComponent);

// Montar a aplicação no DOM
app.mount('#app');
app.mount('#modal-app');

gsap.registerPlugin(ScrollTrigger);

gsap.to(".card-container", {opacity: 1, delay: 0.1}) // gentle fade in

let iteration = 0; // gets iterated when we scroll all the way to the end or start and wraps around - allows us to smoothly continue the playhead scrubbing in the correct direction.

const spacing = 0.1,    // spacing of the cards (stagger)
  snap = gsap.utils.snap(spacing), // we'll use this to snap the playhead on the seamlessLoop
  cards = gsap.utils.toArray('.cards li'),
  seamlessLoop = buildSeamlessLoop(cards, spacing),
  scrub = gsap.to(seamlessLoop, { // we reuse this tween to smoothly scrub the playhead on the seamlessLoop
    totalTime: 0,
    duration: 0.5,
    ease: "power3",
    paused: true
  }),
  trigger = ScrollTrigger.create({
    start: 0,
    onUpdate(self) {
      if (self.progress === 1 && self.direction > 0 && !self.wrapping) {
        wrapForward(self);
      } else if (self.progress < 1e-5 && self.direction < 0 && !self.wrapping) {
        wrapBackward(self);
      } else {
        scrub.vars.totalTime = snap((iteration + self.progress) * seamlessLoop.duration());
        scrub.invalidate().restart(); // to improve performance, we just invalidate and restart the same tween. No need for overwrites or creating a new tween on each update.
        self.wrapping = false;
      }
    },
    end: "+=3000",
    pin: ".gallery"
  });

function wrapForward(trigger) { // when the ScrollTrigger reaches the end, loop back to the beginning seamlessly
  iteration++;
  trigger.wrapping = true;
  trigger.scroll(trigger.start + 1);
}

function wrapBackward(trigger) { // when the ScrollTrigger reaches the start again (in reverse), loop back to the end seamlessly
  iteration--;
  if (iteration < 0) { // to keep the playhead from stopping at the beginning, we jump ahead 10 iterations
    iteration = 9;
    seamlessLoop.totalTime(seamlessLoop.totalTime() + seamlessLoop.duration() * 10);
    scrub.pause(); // otherwise it may update the totalTime right before the trigger updates, making the starting value different than what we just set above.
  }
  trigger.wrapping = true;
  trigger.scroll(trigger.end - 1);
}

function scrubTo(totalTime) { // moves the scroll position to the place that corresponds to the totalTime value of the seamlessLoop, and wraps if necessary.
  let progress = (totalTime - seamlessLoop.duration() * iteration) / seamlessLoop.duration();
  
  if (progress > 1) wrapForward(trigger);
  else if (progress < 0) wrapBackward(trigger);
  else trigger.scroll(trigger.start + progress * (trigger.end - trigger.start));
}

document.querySelector(".next").addEventListener("click", () => scrubTo(scrub.vars.totalTime + spacing));
document.querySelector(".prev").addEventListener("click", () => scrubTo(scrub.vars.totalTime - spacing));


function buildSeamlessLoop(items, spacing) {
  let overlap = Math.ceil(1 / spacing), // number of EXTRA animations on either side of the start/end to accommodate the seamless looping
    startTime = items.length * spacing + 0.5, // the time on the rawSequence at which we'll start the seamless loop
    loopTime = (items.length + overlap) * spacing + 1, // the spot at the end where we loop back to the startTime
    rawSequence = gsap.timeline({paused: true}), // this is where all the "real" animations live
    seamlessLoop = gsap.timeline({ // this merely scrubs the playhead of the rawSequence so that it appears to seamlessly loop
      paused: true,
      repeat: -1, // to accommodate infinite scrolling/looping
      onRepeat() { // works around a super rare edge case bug that's fixed GSAP 3.6.1
        this._time === this._dur && (this._tTime += this._dur - 0.01);
      }
    }),
    l = items.length + overlap * 2,
    time = 0,
    i, index, item;
  
  // set initial state of items
  gsap.set(items, {xPercent: 400, opacity: 0, scale: 0});
  
  // now loop through and create all the animations in a staggered fashion. Remember, we must create EXTRA animations at the end to accommodate the seamless looping.
  for (i = 0; i < l; i++) {
    index = i % items.length;
    item = items[index];
    time = i * spacing;
    rawSequence.fromTo(item, {scale: 0, opacity: 0}, {scale: 1, opacity: 1, zIndex: 100, duration: 0.5, yoyo: true, repeat: 1, ease: "power1.in", immediateRender: false}, time)
      .fromTo(item, {xPercent: 400}, {xPercent: -400, duration: 1, ease: "none", immediateRender: false}, time);
    i <= items.length && seamlessLoop.add("label" + i, time); // we don't really need these, but if you wanted to jump to key spots using labels, here ya go.
  }
  
  // here's where we set up the scrubbing of the playhead to make it appear seamless.
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
