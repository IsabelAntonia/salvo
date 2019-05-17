        var app = new Vue({
            el: '#vueApp',
            data: {
                data: [],
                opponent: ' ',
                thisPlayer: ' ',
                thisGamePlayerId: null,
                thisPlayerId: 0,
                opponentId: 0,
                thisPlayers: [],
                allLocations: [],
                thisPlayerSalvoes: {},
                allSalvoPlayerLocations: [],
                allSalvoOpponentLocations: [],
                opponentSalvoes: {},
                placeShips : false,
                dragAirLocations: [],
                dragBatLocations: [],
                dragSubLocations: [],
                dragDesLocations: [],
                dragPatLocations: []



            },
            beforeCreate() {
                let url = new URLSearchParams(window.location.search);
                let nn = url.get('gp');
                fetch(`../api/game_view/${nn}`)
                    .then(response => response.json())
                    .then(json => {
                        this.data = json;
                        if (this.data.status === "Error") {

                            alert(this.data.message)

                        } else {

                            console.log(this.data)
                            this.thisPlayer = this.data.thisPlayer.playerEmail;
                            this.thisPlayerId = this.data.thisPlayer.playerId;
                            this.players = this.data.Info.gamePlayers;
                            this.thisGamePlayerId = this.data.thisPlayer.gamePlayerId


                            // evaluating State



                            if (this.data.Ships.length === 0){
                            this.placeShips = true;
                            }

                            else {

                            this.findOpponent();
                            this.displayShips(this.data);
                            this.identifySalvoes(this.data);

                            }


                        }

                    })

            },

            mounted(){
            this.displayGrid();

            },


            methods: {


             postShip() {
                   let type1 = "Patrol Boat"
                   let location1 = ['H2','H3']

                   let type2 = "Aircraft Carrier"
                   let location2 = ['A3','A4','A5']

                   let type3 = "Aircraft Carrier"
                   let location3 = ['B3','B4','B5']

                   let type4 = "Aircraft Carrier"
                   let location4 = ['C3','C4','C5']

                   let type5 = "Aircraft Carrier"
                   let location5 = ['D3','D4','D5']

                   let type6 = "Aircraft Carrier"
                   let location6 = ['E3','E4','E5']


                   let gpid = this.thisGamePlayerId



                                $.post({
                                        url: `/api/games/players/${gpid}/ships`,
                                        data: JSON.stringify([
                                        {
                                            type: type1,
                                            location: location1
                                        },

                                        {
                                            type: type2,
                                            location: location2
                                        },

                                          {
                                            type: type3,
                                            location: location3
                                        },

                                          {
                                             type: type4,
                                             location: location4
                                        },

                                           {
                                            type: type5,
                                            location: location5
                                          },

                                            {
                                            type: type6,
                                            location: location6
                                          }



                                        ]),

                                        dataType: "text",
                                        contentType: "application/json"
                                    })
                                    .done(res => {
                                        console.log(res)
                                        location.reload();
                                    })
                                    .fail(err => console.log(err))
                 },
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
                        } else {
                            this.opponentSalvoes = data.Salvoes[i];
                        }
                    }
                    this.displayPlayerSalvoes(this.thisPlayerSalvoes);
                    this.showHits(this.opponentSalvoes);


                },

                showHits(salvoes) {

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

//                            table.rows.item(j).childNodes[i].innerHTML = table.rows.item(j).childNodes[i].id;
//                            tableSalvo.rows.item(j).childNodes[i].innerHTML = tableSalvo.rows.item(j).childNodes[i].className;
                        }

                    }

                },


                logout() {
                    $.post("/api/logout").done(function () {

                        location.replace(`http://localhost:8080/web/games.html`);

                    })

                },

                   drop(ev) {
                      if (event.target.id.length === 0) {
                        console.log("You can not drop a ship here.");
                      } else {
                        console.log(this.dragAirLocations.includes(event.target.id));
                        console.log(event.target.id);
                        if (event.target.id === "dragAir") {
                          console.log("Ships can not overlap.");
                        } else {
                          ev.preventDefault();
                          var data = ev.dataTransfer.getData("text");
                          document.getElementById(data).style.width = "40px";
                          document.getElementById(data).style.color = "red";
                          event.target.appendChild(document.getElementById(data)); // red one

                          if (document.getElementById(data).id === "dragAir") {
                            this.dragAirLocations = [];
                            this.dragAirLocations.push(event.target.id);
                            this.dragAirLocations.push(event.target.nextElementSibling.id);
                          } else if (document.getElementById(data).id === "dragDes") {
                            this.dragDesLocations = [];
                            this.dragDesLocations.push(event.target.id);
                            this.dragDesLocations.push(event.target.nextElementSibling.id);
                          }

                          console.log(this.dragAirLocations);
                          console.log(this.dragDesLocations);
                        }
                      }
                    },

                    allowDrop(ev) {
                      ev.preventDefault();
                    },

                    drag(ev) {
                      ev.dataTransfer.setData("text", ev.target.id);
                    }


            }
        })