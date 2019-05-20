var app = new Vue({
  el: "#vueApp",
  data: {
    data: [],
    opponent: " ",
    thisPlayer: " ",
    thisGamePlayerId: null,
    thisPlayerId: null,
    opponentId: null,
    opponentGamePlayerId: null,
    thisPlayers: [],
    allLocations: [],
    thisPlayerSalvoes: {},
    allSalvoPlayerLocations: [],
    allSalvoOpponentLocations: [],
    opponentSalvoes: {},
    placeShips: false,
    dragAirLocations: [],
    dragBatLocations: [],
    dragSubLocations: [],
    dragDesLocations: [],
    dragPatLocations: [],
    firstSalvo: false,
    salvoesToSend: [],
    thisPlayerTurn: null

  },
  beforeCreate() {
    let url = new URLSearchParams(window.location.search);
    let nn = url.get("gp");
    fetch(`../api/game_view/${nn}`)
      .then(response => response.json())
      .then(json => {
        this.data = json;
        if (this.data.status === "Error") {
          alert(this.data.message);
        } else {
          console.log(this.data);
          this.thisPlayer = this.data.thisPlayer.playerEmail;
          this.thisPlayerId = this.data.thisPlayer.playerId;
          this.players = this.data.Info.gamePlayers;
          this.thisGamePlayerId = this.data.thisPlayer.gamePlayerId;


          if (this.data.Ships.length === 0) {
            this.placeShips = true;
          } else {
            this.findOpponent();
            this.displayShips(this.data);
            this.identifySalvoes(this.data);

            for (let j = 0; j < this.data.gameHistory.length; j++) {
              if (this.thisGamePlayerId == Object.keys(this.data.gameHistory[j])) {
                this.displayHis(this.data.gameHistory[j][this.thisGamePlayerId]); // showing hits on my opponent
              } else {
                this.displayHisOpponent(this.data.gameHistory[j][this.opponentGamePlayerId]);
              }
            }

            if (this.thisPlayerSalvoes[this.thisGamePlayerId].length === 0) {
              this.firstSalvo = true;
            }
            if (!this.firstSalvo) {

              let lengthOfSalvoes = this.thisPlayerSalvoes[this.thisGamePlayerId].length
              this.thisPlayerTurn = this.thisPlayerSalvoes[this.thisGamePlayerId][lengthOfSalvoes - 1].turn;
            }


          }
        }
      });
  },

  mounted() {
    this.displayGrid();

  },

  methods: {

    displayHis(opponentHits) { // showing hits on my opponent 
      var table = document.getElementById('opponentHistory')
      for (let i = 0; i < opponentHits.length; i++) {
        var row = document.createElement('tr')
        row.insertCell().innerHTML = opponentHits[i].turnNumber;
        row.insertCell().innerHTML = opponentHits[i].allHitsInTurn;
        table.append(row);
      }

    },

    displayHisOpponent(hitsOnMe) { // showing hits on me 
      var table = document.getElementById('yourHistory')
      for (let i = 0; i < hitsOnMe.length; i++) {
        var row = document.createElement('tr')
        row.insertCell().innerHTML = hitsOnMe[i].turnNumber;
        row.insertCell().innerHTML = hitsOnMe[i].allHitsInTurn;
        table.append(row);

      }
    },

    postSalvo() {
      if (this.salvoesToSend.length != 5) {
        alert("You have to select exactly 5 cells to proceed!");
      } else {
        console.log(this.firstSalvo);
        if (this.firstSalvo) {
          var turn = 1;
        } else {
          var turn = this.thisPlayerTurn + 1;
        }
        let gpid = this.thisGamePlayerId;

        console.log(turn);

        $.post({
            url: `/api/games/players/${gpid}/salvoes`,
            data: JSON.stringify({
              turn: turn,
              location: this.salvoesToSend
            }),

            dataType: "text",
            contentType: "application/json"
          })
          .done(res => {
            console.log(res);
            location.reload();
          })
          .fail(err => console.log(err));
      }
    },

    selectShot() {
      if (event.target.className.length === 0) {
        // checking if inside grid
        alert("You can only select cells inside the grid!");
      } else {
        if (this.salvoesToSend.includes(event.target.className)) {
          // checking if i have to remove it
          this.salvoesToSend = this.salvoesToSend.filter(element => {
            // remove event target
            return element !== event.target.className;
          });
          event.target.style.backgroundColor = "white";
        } else {
          if (this.salvoesToSend.length > 4) {
            // checking if more than 5
            alert("You can not select more than 5 cells!");
          } else if (event.target.style.backgroundColor === "red") {
            alert("You can not select cells you already fired on in previous turns!");
          } else {
            event.target.style.backgroundColor = "yellow";
            this.salvoesToSend.push(event.target.className);
          }
        }
      }
    },

    postShip() {
      if (
        this.dragDesLocations.length === 0 ||
        this.dragPatLocations.length === 0 ||
        this.dragAirLocations.length === 0 ||
        this.dragSubLocations.length === 0 ||
        this.dragBatLocations.length === 0
      ) {
        alert("You have to place all ships in order to start the game");
      } else {
        let type1 = "Patrol Boat";
        let location1 = this.dragPatLocations;

        let type2 = "Aircraft Carrier";
        let location2 = this.dragAirLocations;

        let type3 = "Submarine";
        let location3 = this.dragSubLocations;

        let type4 = "Battleship";
        let location4 = this.dragBatLocations;

        let type5 = "Destroyer";
        let location5 = this.dragDesLocations;

        let gpid = this.thisGamePlayerId;

        $.post({
            url: `/api/games/players/${gpid}/ships`,
            data: JSON.stringify([{
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
              }
            ]),

            dataType: "text",
            contentType: "application/json"
          })
          .done(res => {
            console.log(res);
            location.reload();
          })
          .fail(err => console.log(err));
      }
    },
    findOpponent() {
      for (var i = 0; i < this.players.length; i++) {
        if (this.thisPlayerId !== this.players[i].player.playerId) {
          this.opponent = this.players[i].player.playerEmail;
          this.opponentId = this.players[i].player.playerId;
          this.opponentGamePlayerId = this.players[i].gamePlayerId;
        }
      }
    },

    displayShips(data) {
      for (var i = 0; i < data.Ships.length; i++) {
        this.allLocations.push(data.Ships[i].location);
      }
      this.allLocations = this.allLocations.flat();
      for (let j = 0; j < this.allLocations.length; j++) {
        occupiedCell = document.getElementById(this.allLocations[j]);
        occupiedCell.style.backgroundColor = "blue";
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

    showHits(salvoes) { // showing where i got hit

      let myObj = salvoes[this.opponentGamePlayerId];

      if (myObj != null) {
        for (var i = 0; i < myObj.length; i++) {
          this.allSalvoOpponentLocations.push(myObj[i].Locations);
        }
        this.allSalvoOpponentLocations = this.allSalvoOpponentLocations.flat();
        console.log(this.allSalvoOpponentLocations)
        for (let j = 0; j < this.allSalvoOpponentLocations.length; j++) {
          hitCell = document.getElementById(this.allSalvoOpponentLocations[j]);
          hitCell.style.backgroundColor = "black";
        }
      }

    },

    displayPlayerSalvoes(salvoes) { // showing the salvoes i fired


      let myObj = salvoes[this.data.thisPlayer.gamePlayerId];
      for (var i = 0; i < myObj.length; i++) {
        this.allSalvoPlayerLocations.push(myObj[i].Locations);
      }
      this.allSalvoPlayerLocations = this.allSalvoPlayerLocations.flat();

      for (let j = 0; j < this.allSalvoPlayerLocations.length; j++) {
        var className = this.allSalvoPlayerLocations[j];
        shotCell = document.getElementsByClassName(className)[0];
        shotCell.style.backgroundColor = "red";
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
      var letters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", ""];
      for (var i = 0; i < 121; i++) {
        if (i <= 10) {
          var singleCell = document.createElement("td");
          var singleCellSalvo = document.createElement("td");

          singleCell.innerHTML = numbers[i];
          singleCellSalvo.innerHTML = numbers[i];

          tHead.append(singleCell);
          tHeadSalvo.append(singleCellSalvo);
        } else {
          if (i % 11 === 0) {
            var row = document.createElement("tr");
            var rowSalvo = document.createElement("tr");
            for (let i = 0; i < letters.length; i++) {
              row.insertCell().innerHTML = " ";
              rowSalvo.insertCell().innerHTML = " ";
            }
          }
          tBody.append(row);
          tBodySalvo.append(rowSalvo);
        }
      }
      table.append(tHead, tBody);
      tableSalvo.append(tHeadSalvo, tBodySalvo);

      for (var j = 0; j < 10; j++) {
        table.rows.item(j).childNodes[0].innerHTML = letters[j];
        tableSalvo.rows.item(j).childNodes[0].innerHTML = letters[j];
      }

      for (var j = 0; j <= 9; j++) {
        for (var i = 1; i <= 10; i++) {
          table.rows.item(j).childNodes[i].id =
            table.rows.item(j).childNodes[0].innerHTML + numbers[i];
          tableSalvo.rows.item(j).childNodes[i].className =
            tableSalvo.rows.item(j).childNodes[0].innerHTML + numbers[i];

          //                            table.rows.item(j).childNodes[i].innerHTML = table.rows.item(j).childNodes[i].id;
          //                            tableSalvo.rows.item(j).childNodes[i].innerHTML = tableSalvo.rows.item(j).childNodes[i].className;
        }
      }
    },

    logout() {
      $.post("/api/logout").done(function () {
        location.replace(`http://localhost:8080/web/games.html`);
      });
    },

    drop(ev) {
      if (event.target.id.length === 0) {
        alert("You can not place a ship outside of the grid.");
      } else {
        if (event.target.id === "dragAir") {
          alert("Ships can not overlap.");
        } else if (event.target.id === "dragDes") {
          alert("Ships can not overlap.");
        } else if (event.target.id === "dragSub") {
          alert("Ships can not overlap.");
        } else if (event.target.id === "dragPat") {
          alert("Ships can not overlap.");
        } else if (event.target.id === "dragBat") {
          alert("Ships can not overlap.");
        } else {
          if (event.target.nextElementSibling != null) {
            var next = event.target.nextElementSibling;
            if (next.nextElementSibling != null) {
              var next1 = next.nextElementSibling;
              if (next1.nextElementSibling != null) {
                var next2 = next1.nextElementSibling;
              }
            }
          }

          var data = ev.dataTransfer.getData("text");
          ev.preventDefault();
          document.getElementById(data).style.width = "40px";
          document.getElementById(data).style.color = "red";

          if (document.getElementById(data).id === "dragAir") {
            // length 5

            if (
              next == null ||
              next1 == null ||
              next2 == null ||
              next2.nextElementSibling == null
            ) {
              alert("You can not place a ship outside of the grid.");
            } else {
              event.target.appendChild(document.getElementById(data));
              this.dragAirLocations = [];
              this.dragAirLocations.push(event.target.id); // push 1
              this.dragAirLocations.push(event.target.nextElementSibling.id); // push 2
              this.dragAirLocations.push(next.nextElementSibling.id); // push 3
              this.dragAirLocations.push(next1.nextElementSibling.id); // push 4
              this.dragAirLocations.push(next2.nextElementSibling.id); // push 5
            }
          } else if (document.getElementById(data).id === "dragBat") {
            // length 4
            if (next == null || next1 == null || next2 == null) {
              alert("You can not place a ship outside of the grid.");
            } else {
              event.target.appendChild(document.getElementById(data));
              this.dragBatLocations = [];
              this.dragBatLocations.push(event.target.id); // push 1
              this.dragBatLocations.push(event.target.nextElementSibling.id); // push2
              this.dragBatLocations.push(next.nextElementSibling.id); // push 3
              this.dragBatLocations.push(next1.nextElementSibling.id); // push 4
            }
          } else if (document.getElementById(data).id === "dragSub") {
            // length 3
            if (next == null || next1 == null) {
              alert("You can not place a ship outside of the grid.");
            } else {
              event.target.appendChild(document.getElementById(data));
              this.dragSubLocations = [];
              this.dragSubLocations.push(event.target.id); // push 1
              this.dragSubLocations.push(event.target.nextElementSibling.id); // push 2
              this.dragSubLocations.push(next.nextElementSibling.id); // push 3
            }
          } else if (document.getElementById(data).id === "dragDes") {
            if (next == null || next1 == null) {
              alert("You can not place a ship outside of the grid.");
            } else {
              event.target.appendChild(document.getElementById(data));
              this.dragDesLocations = [];
              this.dragDesLocations.push(event.target.id); // push 1
              this.dragDesLocations.push(event.target.nextElementSibling.id); // push 2
              this.dragDesLocations.push(next.nextElementSibling.id); // push 3
            }
          } else {
            if (next == null) {
              alert("You can not place a ship outside of the grid.");
            } else {
              event.target.appendChild(document.getElementById(data));
              this.dragPatLocations = [];
              this.dragPatLocations.push(event.target.id); // push 1
              this.dragPatLocations.push(event.target.nextElementSibling.id); // push 2
            }
          } // Patrol Boat
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
});