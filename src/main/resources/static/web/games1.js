var vm = new Vue ({

el: '#app',

data: {

data: null,
gamePlayers: [],
leaderBoardData: null,

},

beforeCreate(){

fetch('../api/games')
.then(response => response.json())
.then(response => {

this.data = response;
response.gamePlayers = this.gamePlayers;
//console.log(this.data)

fetch('../api/leaderboard')
.then(response => response.json())
.then(response => {

this.leaderBoardData = response;
console.log(this.leaderBoardData);

this.buildTable(this.leaderBoardData);

})

});

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
                let email = document.getElementById("email").value.toLowerCase();
                let password = document.getElementById("password").value;

                $.post("/api/login", {
                        email: email,
                        password: password
                    })
                    .then(response => {
                        // console.log("logged in"),
                        console.log(JSON.stringify(response)),
                            location.reload();
                    })
                    .catch(error => console.error('Error:', error))

}





}



})