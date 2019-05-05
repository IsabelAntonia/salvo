        var app = new Vue({
            el: '#vueApp',
            data: {
                data: [],
                opponent: ' ',
                thisPlayer: ' ',
                thisGamePlayerId: 0,
                thisPlayerId: 0,
                opponentId: 0,
                thisPlayers: [],
                allLocations: [],
                thisPlayerSalvoes: {},
                allSalvoPlayerLocations: [],
                allSalvoOpponentLocations: [],
                opponentSalvoes: {}
            },
            beforeCreate() {
                let url = new URLSearchParams(window.location.search);
                let nn = url.get('gp');
                fetch(`../api/game_view/${nn}`)
                    .then(response => response.json())
                    .then(json => {
                        this.data = json;
                        this.thisPlayer = this.data.thisPlayer.playerEmail;
                        this.thisPlayerId = this.data.thisPlayer.playerId;
                        this.players = this.data.Info.gamePlayers;
                        this.findOpponent();
                        this.displayGrid();
                        this.displayShips(this.data);
                        this.identifySalvoes(this.data)

                        console.log(this.data);
                    })
            },

            methods: {

                findOpponent() {

                    for (var i = 0; i < this.players.length; i++) {
                        if (this.thisPlayerId !== this.players[i].player.playerId) {
                            this.opponent = this.players[i].player.playerEmail;
                            this.opponentId = this.players[i].player.playerId;
                        }
                    }

                },

                displayShips(data) {

                    for (var i = 0; i < data.Ships.length; i++) {
                        this.allLocations.push(data.Ships[i].location);
                    }
                    this.allLocations = this.allLocations.flat();
                    for (let j = 0; j < this.allLocations.length; j++) {

                        occupiedCell = document.getElementById(this.allLocations[j])
                        occupiedCell.style.backgroundColor = 'blue';
                    }
                },

                identifySalvoes(data) {

                    for (var i = 0; i < data.Salvoes.length; i++) {
                        if (data.thisPlayer.gamePlayerId == Object.keys(data.Salvoes[i])) {
                            this.thisPlayerSalvoes = data.Salvoes[i];
                        }

                        else {
                        this.opponentSalvoes = data.Salvoes[i];
                        }
                    }
                    this.displayPlayerSalvoes(this.thisPlayerSalvoes);
                    this.showHits(this.opponentSalvoes);


                },

                showHits(salvoes){

                 let myObj = salvoes[this.opponentId];
                                    for (var i = 0; i < myObj.length; i++) {
                                        this.allSalvoOpponentLocations.push(myObj[i].Locations);
                                    }
                                    this.allSalvoOpponentLocations = this.allSalvoOpponentLocations.flat();

                                    for (let j = 0; j < this.allSalvoOpponentLocations.length; j++) {

                                    hitCell = document.getElementById(this.allSalvoOpponentLocations[j]);
                                    hitCell.style.backgroundColor = 'black';
                                   }



                },

                displayPlayerSalvoes(salvoes) {

                    let myObj = salvoes[this.data.thisPlayer.gamePlayerId];
                    for (var i = 0; i < myObj.length; i++) {
                        this.allSalvoPlayerLocations.push(myObj[i].Locations);
                    }
                    this.allSalvoPlayerLocations = this.allSalvoPlayerLocations.flat();

                    for (let j = 0; j < this.allSalvoPlayerLocations.length; j++) {
                    var className = this.allSalvoPlayerLocations[j];
                    shotCell = document.getElementsByClassName(className)[0];
                    shotCell.style.backgroundColor = 'red';
                   }
                },

                displayGrid() {

                    var table = document.getElementById("table");
                    var tableSalvo = document.getElementById("tableSalvo");

                    var tHead = document.createElement("thead");
                    var tHeadSalvo = document.createElement("thead");

                    var tBody = document.createElement("tbody");
                    var tBodySalvo = document.createElement("tbody");

                    var numbers = [" ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
                    var letters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", ""]
                    for (var i = 0; i < 121; i++) {

                        if (i <= 10) {
                            var singleCell = document.createElement('td');
                            var singleCellSalvo = document.createElement('td');

                            singleCell.innerHTML = numbers[i];
                            singleCellSalvo.innerHTML = numbers[i];

                            tHead.append(singleCell);
                            tHeadSalvo.append(singleCellSalvo);
                        } else {

                            if (i % 11 === 0) {

                                var row = document.createElement('tr');
                                var rowSalvo = document.createElement('tr');
                                for (let i = 0; i < letters.length; i++) {
                                    row.insertCell().innerHTML = ' ';
                                    rowSalvo.insertCell().innerHTML = ' ';
                                }
                            }
                            tBody.append(row);
                            tBodySalvo.append(rowSalvo);
                        }
                    }
                    table.append(tHead, tBody);
                    tableSalvo.append(tHeadSalvo, tBodySalvo);

                    for (var j = 0; j < 10; j++) {

                        table.rows.item(j).childNodes[0].innerHTML = letters[j]
                        tableSalvo.rows.item(j).childNodes[0].innerHTML = letters[j]
                    }

                    for (var j = 0; j <= 9; j++) {
                        for (var i = 1; i <= 10; i++) {


                            table.rows.item(j).childNodes[i].id = table.rows.item(j).childNodes[0].innerHTML + numbers[i]
                            tableSalvo.rows.item(j).childNodes[i].className = tableSalvo.rows.item(j).childNodes[0].innerHTML + numbers[i]

                            table.rows.item(j).childNodes[i].innerHTML = table.rows.item(j).childNodes[i].id;
                            tableSalvo.rows.item(j).childNodes[i].innerHTML = tableSalvo.rows.item(j).childNodes[i].className;
                        }

                    }

                }
            }
        })