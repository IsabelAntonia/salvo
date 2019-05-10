var vm = new Vue ({

el: '#app',

data: {

data: null,
gamePlayers: [],
leaderBoardData: null,
loginPos: true


},

beforeCreate(){

fetch('../api/games')
.then(response => response.json())
.then(response => {

this.data = response;

console.log(this.data)


fetch('../api/leaderboard')
.then(response => response.json())
.then(response => {

this.leaderBoardData = response;


this.buildTable(this.leaderBoardData);

console.log(this.loginPos)
if (this.data.currentUser !== null){

this.loginPos = false;
}

else {

this.loginPos = true;

}

console.log(this.loginPos)


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

    let email = document.getElementById("email").value;
    console.log(email);
    let pw = document.getElementById("password").value;
    console.log(pw);

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

},

logout() {
    $.post("/api/logout").done(function(){location.reload();})

},

signUp(){

   let email = document.getElementById("email").value;
    console.log(email);
    let pw = document.getElementById("password").value;
    console.log(pw);

    fetch("/api/players",
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
        console.log(res)

//            location.reload();




        })
        .catch(function(res){ console.log(res) });


}











}



})