    Vue.component("place-ships", {
  template: `<div>
      <section>

          <ul>
              <li>Place the ships on the grid below you.</li>
              <li>To do that, click on one ship and drag it to a position on the grid of your choice.</li>
              <li>Notice, that you can not:
                  <ol>
                      <li>Place ships outside of the grid.</li>
                      <li>Place ships diagonally.</li>
                      <li>Place ships so that they overlap.</li>
                  </ol>
              </li>
              <li>If a position is illegal, you will not be able to place the ship there.</li>
              <li>If you are not happy with a position of a ship, you can change it.</li>
              <li>After you've placed all ships successfully on the grid, click on the Done button down below.</li>
              <li>Notice that after you clicked Done, you will not be able to change the position of your ships anymore.</li>
          </ul>

      </section>

<section class="forms">

    <div class="bigShips">

            <ul>
                <li class="form">
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                </li>
                <li>Name: Aircraft Carrier</li>
                <li>Length: 5</li>
            </ul>



            <ul>
                <li class="form">
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                </li>
                <li>Name: Battleship</li>
                <li>Length: 4</li>
            </ul>
    </div>


    <div class="smallShips">
            <ul>
                <li class="form">
                    <div></div>
                    <div></div>
                    <div></div>
                </li>
                <li>Name: Submarine</li>
                <li>Length: 3</li>
            </ul>


            <ul>
                <li class="form">
                    <div></div>
                    <div></div>
                    <div></div>
                </li>
                <li>Name: Destroyer</li>
                <li>Length: 3</li>
            </ul>

            <ul>
                <li class="form">
                    <div></div>
                    <div></div>
                </li>
                <li>Name: Patrol Boat</li>
                <li>Length: 2</li>
            </ul>

    </div>

</section>


            </div>`
})

