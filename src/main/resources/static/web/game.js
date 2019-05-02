
    var app = new Vue({
    el: '#vueApp',
    data: {
        data: [],
        opponent: ' ',
        thisPlayer: ' ',
        thisPlayerId: 0,
        thisPlayers: []
    },
    beforeCreate() {
        let url = new URLSearchParams(window.location.search);
        let nn = url.get('gp');
        fetch(`../api/game_view/${nn}`)
            .then(response => response.json())
            .then(json => {
                this.data = json;
                this.thisPlayer = this.data.thisPlayer.email;
                this.thisPlayerId = this.data.thisPlayer.id;
                this.players = this.data.Info.gamePlayers;
this.findOpponent();
                console.log(this.data);



            })
    },

    methods: {

    findOpponent(){

    for (var i = 0; i < this.players.length; i++){
    if (this.thisPlayerId !== this.players[i].player.playerId){
   this.opponent = this.players[i].player.playerEmail;


    }
    }


    }

    }
  })
