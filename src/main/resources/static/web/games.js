var vm = new Vue ({

el: '#app',

data: {

data: null
},

beforeCreate(){

fetch('../api/games')
.then(response => response.json())
.then(response => {

this.data = response;
console.log(this.data)

});

}



})