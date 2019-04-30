

$(function() {


  // display text in the output area
  function showOutput(data) {

console.log(data);

  data.forEach(element => {
  var listItem = document.createElement('li');

  listItem.innerHTML =  element.GameId + ' ' + element.creationDate;

   $("#putHere").append(listItem);


  })

  }


  function loadData() {

    $.get("/api/games")
    .done(function(data) {
//      showOutput(JSON.stringify(data, null, 2));
      showOutput(data);
    })
    .fail(function( jqXHR, textStatus ) {
      showOutput( "Failed: " + textStatus );
    });
  }


  loadData();
});


