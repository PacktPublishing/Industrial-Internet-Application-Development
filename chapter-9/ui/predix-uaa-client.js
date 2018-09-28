const UAA_ID = 'e0fd8047-0a1d-4076-9143-fc4cbf60cd79';
const VIEWS_ID = 'b3e07566-273f-4d1e-85ce-a5d4b2632a27';
const TIMESERIES_ID = '6f6882ff-3a3b-45b2-b20b-b537e652471d';
const CLIENT_ID = 'client';
const CLIENT_SECRET = '';
let ACCESS_TOKEN;

const data = {
  client_id: CLIENT_ID,
  client_secret: CLIENT_SECRET,
  grant_type: 'client_credentials',
  response_type: 'token'
};
const body = Object.entries(data).map(pair => pair.map(encodeURIComponent).join('=')).join('&');

const addCard = (card) => {
  const el = document.createElement(card.slug);
  el.setAttribute('id', 'card-' + card.id);
  el.setAttribute('title', card.title);
  el.setAttribute('context', '{{context}}');
  if (card.attributes && typeof card.attributes === 'object') {
    for (const attr in card.attributes) {
      if (typeof card.attributes[attr] !== 'object') {
        el.setAttribute(attr, card.attributes[attr]);
      }
      else {
        el.setAttribute(attr, JSON.stringify(card.attributes[attr]));
      }
    }
  }
  Polymer.dom(document.body).appendChild(el);
};

$(document).ready(() => {
  fetch(`https://${UAA_ID}.predix-uaa.run.aws-usw02-pr.ice.predix.io/oauth/token`, {
    method: 'POST',
    body: body,
    headers: new Headers({
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json'
    })
  }).then((r) => r.json()).then((context) => {
    ACCESS_TOKEN = context.access_token;

    const $http = angular.element('body').injector().get('$http');
    $http.defaults.headers.common['Predix-Zone-Id'] = VIEWS_ID;
    $http.defaults.headers.common['Authorization'] = `Bearer ${ACCESS_TOKEN}`;

    const deck = 'deck-1';

    $http.get('https://predix-views.run.aws-usw02-pr.ice.predix.io/api/decks/' + deck + '?filter[include][cards]&filter[order]=cards').success((data, status, headers, config) => {
      data.cards.forEach(addCard);
    }).error((data, status, headers, config) => {
      console.error('Failed to load cards for deck ' + deck);
    });
  });
});
