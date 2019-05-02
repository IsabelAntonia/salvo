var vm = new Vue ({

el: '#app',

data: {

data: null,
gamePlayers: []
},

beforeCreate(){

fetch('../api/games')
.then(response => response.json())
.then(response => {

this.data = response;
response.gamePlayers = this.gamePlayers;
console.log(this.data)

});

}



})