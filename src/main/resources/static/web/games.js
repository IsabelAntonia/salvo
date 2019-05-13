var vm = new Vue ({

el: '#app',

data: {

games: [],
leaderBoardData: null,
loginPos: true,
linkedGames: [],
notLinkedGames: [],
reenterPos: false



},

beforeCreate(){

fetch('../api/games')
.then(response => response.json())
.then(response => {

data = response;
console.log(response)

this.games = data.games;

this.makeLinks();





fetch('../api/leaderboard')
.then(response => response.json())
.then(response => {

this.leaderBoardData = response;


this.buildTable(this.leaderBoardData);


if (data.currentUser !== null){

this.loginPos = false;
}

else {

this.loginPos = true;

}




})

});

},

mounted(){

//this.insertPathVariable();

},

methods: {
buildTable(leaderBoardData){


for (var i = 0; i < leaderBoardData.length; i++){

if (leaderBoardData[i].totalScore.length !== 0){

var row = document.createElement('tr');
row.insertCell().innerHTML = leaderBoardData[i].email;
row.insertCell().innerHTML = this.calculateTotalScore(leaderBoardData[i].totalScore.flat());
row.insertCell().innerHTML = this.calculateWon(leaderBoardData[i].totalScore.flat());
row.insertCell().innerHTML = this.calculateLost(leaderBoardData[i].totalScore.flat());
row.insertCell().innerHTML = this.calculateTied(leaderBoardData[i].totalScore.flat());
document.getElementById('table').append(row);
}

}


},

calculateTotalScore(arrayScores){



var sum = arrayScores.reduce((acc, val) => {

return acc + val;

})

return sum;

},

calculateWon(arrayScores){


var won = arrayScores.filter(element => {

return (element === 1 || element === 1.0)

})

var wonLength = won.length;

return wonLength;

},

calculateLost(arrayScores){


var lost = arrayScores.filter(element => {

return (element === 0)

})

var lostLength = lost.length;

return lostLength;
},


calculateTied(arrayScores){


var tied = arrayScores.filter(element => {

return (element === 0.5)

})

var tiedLength = tied.length;

return tiedLength;
},



login() {

    let email = document.getElementById("email").value;
    let pw = document.getElementById("password").value;

     $.post("/login", { email: email, password: pw })
     .done(function() {

location.reload();

     })
     .fail(function(){ console.log('uups')})
},

logout() {
    $.post("/api/logout").done(function(){location.reload();})

},

signUp(){

   let email = document.getElementById("email").value;
    let pw = document.getElementById("password").value;

    $.post("/api/players", { email: email, password: pw })
    .done(function() {

    fetch("/login",
               {
                   credentials: 'include',
                   headers: {
                       'Accept': 'application/json',
                       'Content-Type': 'application/x-www-form-urlencoded'
                   },
                   method: "POST",
                   body: "email=" + email + "&password=" + pw
               })
               .then(function(res){

                   location.reload();

               })
               .catch(function(res){ console.log(res) });
    })
    .fail(function(){ console.log('uups')})
},

makeLinks(){

if (data.currentUser !== null){


for (var i = 0; i < data.games.length; i++){ // three games

for (var j = 0; j < data.games[i].gamePlayers.length; j++){ // two gamePlayers or only one


if (data.games[i].gamePlayers[j].player.playerId === data.currentUser.playerId){

this.reenterPos = true;

if (!this.linkedGames.includes(data.games[i])){

this.linkedGames.push(data.games[i]);}

}


}
}

for (var u = 0; u < data.games.length; u++){

if (!this.linkedGames.includes(data.games[u])){

this.notLinkedGames.push(data.games[u]);

}

}

}

else {

this.notLinkedGames = data.games;

}

},

insertPathVariable(){

// for all list elements with the class linked set attribute
var linkedGamesCollection = document.getElementsByClassName("linked");

console.log(linkedGamesCollection[0])

var linkedGamesArray = Array.from(linkedGamesCollection)

console.log(linkedGamesArray)

var pathVar = 1;

for (var i = 0; i < linkedGamesArray.length; i++){

linkedGamesArray[i].setAttribute("href","game.html?gp="+pathVar)


}

/*
arr[i].setAttribute("href","game.html?gp=1")
arr[i].style.color = "red";*/




}

}


})