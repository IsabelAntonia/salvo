
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
this.displayGrid();
this.displayShips(data);
                console.log(this.data);



            })
    },

    methods: {

    findOpponent(){

    for (var i = 0; i < this.players.length; i++){
    if (this.thisPlayerId !== this.players[i].player.playerId){
   this.opponent = this.players[i].player.playerEmail; }
    }

 },


        displayGrid() {

            let table = document.getElementById("gridTable");
            let tHead = document.createElement("thead");
            let tBody = document.createElement("tbody");

            let numbers = [" ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
            let letters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", ""]
            for (let i = 0; i < 121; i++) {

                if (i <= 10) {
                    let singleCell = document.createElement('td');
                    singleCell.innerHTML = numbers[i];
                    tHead.append(singleCell);
                } else {

                    if (i % 11 === 0) {

                        var row = document.createElement('tr');
                        for (let i = 0; i < letters.length; i++) {
                            row.insertCell().innerHTML = ' ';
                        }
                    }
                    tBody.append(row);
                }
            }
            table.append(tHead, tBody);

            for (var j = 0; j < 10; j++) {
                table.rows.item(j).childNodes[0].innerHTML = letters[j]
            }

            for (var j = 0; j <= 9; j++) {
                for (var i = 1; i <= 10; i++){
                table.rows.item(j).childNodes[i].id = table.rows.item(j).childNodes[0].innerHTML + numbers[i]
                table.rows.item(j).childNodes[i].innerHTML = table.rows.item(j).childNodes[i].id;
                }

            }

        }



    }
  })
