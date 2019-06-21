var vm = new Vue({
  el: "#app",

  data: {
    games: [],
    leaderBoardData: null,
    loginPos: true,
    linkedGames: [],
    notLinkedGames: [],
    reenterPos: false,
    linkedGamesArray: [],
    joinableGames: [],
    fullGames: [],
    openGames: [],
    overGames: [],
    myRow: null,
    noOpGames: []
  },

  beforeCreate() {

    setInterval(() => {
      fetch(`../api/games`)
        .then(response => response.json())
        .then(response => {
          data = response;
          this.games = data.games;
          this.makeLinks();
          if (data.currentUser !== null) {
            this.loginPos = false;
          } else {
            this.loginPos = true;
          }
        });
    }, 5000);

    fetch("../api/games")
      .then(response => response.json())
      .then(response => {
        data = response;
        this.games = data.games;
        this.makeLinks();

        if (data.currentUser !== null) {
          this.loginPos = false;
        } else {
          this.loginPos = true;
        }
      });
  },

  created() {
    fetch("../api/leaderboard")
      .then(response => response.json())
      .then(response => {

        this.leaderBoardData = response;

        this.buildTable(this.leaderBoardData);
        if (this.loginPos == false) {
          this.thisPlayerTable();
        }


      });
  },

  methods: {
    thisPlayerTable() {
      document.getElementById("myScoreTable").append(this.myRow);
    },

    buildTable(leaderBoardData) {
      console.log(leaderBoardData);

      // check for thisPlayer and create personal board
      for (var i = 0; i < leaderBoardData.length; i++) {

        if (this.loginPos == false) { // only show personal score if logged in
          if (leaderBoardData[i].email === data.currentUser.playerEmail) {
            this.myRow = document.createElement("tr");
            this.myRow.insertCell().innerHTML = leaderBoardData[i].email;
            this.myRow.insertCell().innerHTML = this.calculateTotalScore(
              leaderBoardData[i].allScores.flat()
            );
            this.myRow.insertCell().innerHTML = this.calculateWon(
              leaderBoardData[i].allScores.flat()
            );
            this.myRow.insertCell().innerHTML = this.calculateLost(
              leaderBoardData[i].allScores.flat()
            );
            this.myRow.insertCell().innerHTML = this.calculateTied(
              leaderBoardData[i].allScores.flat()
            );
          }
        }

      }

      //build 25 score table

      var sortedBoard = [];
      for (var h = 0; h < leaderBoardData.length; h++) {

        sortedBoard[h] = leaderBoardData[h]

        sortedBoard[h].totalScore = this.calculateTotalScore(
          leaderBoardData[h].allScores.flat()
        );
      }

      console.log(sortedBoard)
      sortedBoard = leaderBoardData.sort(
        (first, second) => second.totalScore - first.totalScore
      );
      var lengthOfArray;
      if (sortedBoard.length < 26) {
        lengthOfArray = sortedBoard.length;
      } else {
        lengthOfArray = 26;
      }


      for (var i = 0; i < lengthOfArray; i++) {
        var row = document.createElement("tr");
        row.insertCell().innerHTML = sortedBoard[i].email;
        row.insertCell().innerHTML = this.calculateTotalScore(
          sortedBoard[i].allScores.flat()
        );
        row.insertCell().innerHTML = this.calculateWon(
          sortedBoard[i].allScores.flat()
        );
        row.insertCell().innerHTML = this.calculateLost(
          sortedBoard[i].allScores.flat()
        );
        row.insertCell().innerHTML = this.calculateTied(
          sortedBoard[i].allScores.flat()
        );
        document.getElementById("table").append(row);
      }
    },

    calculateTotalScore(arrayScores) {
      var sum;
      if (arrayScores.length == 0) {
        sum = 0;

      } else {
        sum = arrayScores.reduce((acc, val) => {
          return acc + val;
        });
      }

      return sum;
    },

    calculateWon(arrayScores) {
      var won = arrayScores.filter(element => {
        return element === 1 || element === 1.0;
      });

      var wonLength = won.length;

      return wonLength;
    },

    calculateLost(arrayScores) {
      var lost = arrayScores.filter(element => {
        return element === 0;
      });

      var lostLength = lost.length;

      return lostLength;
    },

    calculateTied(arrayScores) {
      var tied = arrayScores.filter(element => {
        return element === 0.5;
      });

      var tiedLength = tied.length;

      return tiedLength;
    },

    login() {
      let email = document.getElementById("email").value;
      let pw = document.getElementById("password").value;

      $.post("/login", {
          email: email,
          password: pw
        })
        .done(function () {
          location.reload();
        })
        .fail(function () {
          alert("Your email or your password was not correct.");
        });
    },

    logout() {
      $.post("/api/logout").done(function () {
        location.reload();
      });
    },

    signUp() {
      let email = document.getElementById("email").value;
      let pw = document.getElementById("password").value;

      $.post("/api/players", {
          email: email,
          password: pw
        })
        .done(function () {
          fetch("/login", {
              credentials: "include",
              headers: {
                Accept: "application/json",
                "Content-Type": "application/x-www-form-urlencoded"
              },
              method: "POST",
              body: "email=" + email + "&password=" + pw
            })
            .then(function (res) {
              location.reload();
            })
            .catch(function (res) {
              console.log(res);
            });
        })
        .fail(function () {
          console.log("uups");
        });
    },

    makeLinks() {

      this.fullGames = [];
      this.linkedGames = [];
      this.fullGames = [];
      this.noOpGames = [];
      this.openGames = [];
      this.overGames = [];
      this.joinableGames = [];
      this.notLinkedGames = [];
      if (data.currentUser !== null) {


        for (var i = 0; i < data.games.length; i++) {
          // three games

          for (var j = 0; j < data.games[i].gamePlayers.length; j++) {


            if (
              data.games[i].gamePlayers[j].player.playerId ===
              data.currentUser.playerId
            ) {
              // if one of the gamePlayers is you

              this.reenterPos = true; // i can reenter the game = linkedGames

              if (!this.linkedGames.includes(data.games[i])) {
                this.linkedGames.push(data.games[i]);
              }
            }
          }
        }

        for (var h = 0; h < this.linkedGames.length; h++) {
          if (this.linkedGames[h].gameOver === true) { // game is over 
            if (!this.overGames.includes(this.linkedGames[h])) {
              this.overGames.push(this.linkedGames[h]);
            }
          } else if (this.linkedGames[h].gamePlayers.length === 1) { // i am waiting for an opponent 
            if (!this.noOpGames.includes(this.linkedGames[h])) {
              this.noOpGames.push(this.linkedGames[h]);
            }
          } else {
            if (!this.openGames.includes(this.linkedGames[h])) {
              this.openGames.push(this.linkedGames[h]); // game is going on 
            }
          }
        }

        for (var u = 0; u < data.games.length; u++) {
          if (!this.linkedGames.includes(data.games[u])) {
            this.notLinkedGames.push(data.games[u]);
          }
        }

        for (var i = 0; i < this.notLinkedGames.length; i++) {
          // checking if there is only one player

          for (var j = 0; j < this.notLinkedGames[i].gamePlayers.length; j++) {
            if (this.notLinkedGames[i].gamePlayers.length === 1) {

              if (!this.joinableGames.includes(this.notLinkedGames[i])) {
                // checking if the game is not already in the list
                this.joinableGames.push(this.notLinkedGames[i]);
              }
            } else {
              if (!this.fullGames.includes(this.notLinkedGames[i])) {
                this.fullGames.push(this.notLinkedGames[i]);
              }
            }
          }
        }
      } else {
        for (var l = 0; l < data.games.length; l++) {
          if (data.games[l].gamePlayers.length === 1) {
            if (!this.joinableGames.includes(data.games[l])) {
              this.joinableGames.push(data.games[l])
            }
          } else {
            if (!this.fullGames.includes(data.games[l])) {
              this.fullGames.push(data.games[l]);
            }
          }
        }
      }
    },

    insertPathVariable() {
      var linkedGamesCollection = document.getElementsByClassName("linked");

      this.linkedGamesArray = Array.from(linkedGamesCollection);

      var gameId = event.target.id;
      var playerId = data.currentUser.playerId;

      fetch("/api/getGamePlayerID", {
          credentials: "include",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/x-www-form-urlencoded"
          },
          method: "POST",
          body: "gameId=" + gameId + "&playerId=" + playerId
        })
        .then(response => response.json())
        .then(response => {
          var pathVar;

          pathVar = response;

          location.replace(`http://localhost:8080/web/game.html?gp=` + pathVar);
        });
    },

    createGame() {
      $.post("/api/games")
        .done(res => {
          console.log(res.gpid);
          var pathVar = res.gpid;
          location.replace(`http://localhost:8080/web/game.html?gp=` + pathVar);
        })

        .fail(res => {
          alert(res.responseJSON.message);
          console.log(res);
        });
    },
    joinGame() {
      var gameId = event.target.id;
      console.log(gameId);

      $.post(`/api/game/${gameId}/players`)
        .done(res => {
          console.log(res.gpid);
          var pathVar = res.gpid;
          location.replace(`http://localhost:8080/web/game.html?gp=` + pathVar);
        })
        .fail(res => {
          alert(res.responseJSON.message);
          console.log(res);
        });
    }
  }
});