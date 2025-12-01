// This is the controller module to add navigation functionality for the rooms page and manage the application logic.
// res.render is the Express function for compiling a view template to send as the HTML response that the browser will receive

// Option 1: Read the rooms from the database @travlr.room collection and store data as array.
// Fetch data from the database through an API endpoint.

// Create a variable for our API endpoint
// Use env var to switch between local and deployed on Render modes
const apiHost = process.env.API_HOST || "http://localhost:3000";
const roomsEndpoint = `${apiHost}/api/rooms`;

const options = {
  method: "GET",
  headers: { Accept: "application/json" }
};

/* GET rooms view */
const rooms = async (req, res, next) => {
  console.log("ROOMS CONTROLLER BEGIN");

  try {
    // Fetch results from the database (backend API URL)
    const response = await fetch(roomsEndpoint, options);

    // Throw an error if the response has a bad status (404 or 500)
    if (!response.ok) {
      throw new Error(`API error: ${response.statusText}`);
    }

    // Converts the API response into a JS object/array.
    let allRooms = await response.json();
    // console.log(allRooms);
    let message = null;

    if (!Array.isArray(allRooms)) {
      console.error("API returned unexpected data");
      message = "API lookup error";
      allRooms = [];
    } else if (allRooms.length === 0) {
      message = "No trips were found in our database.";
    }

    // Response 200 OK, Render the "rooms.js" page with data from the DB.meal.
    res.render("rooms", {
      title: "Rooms - Travlr Getaways",
      currentPage: "rooms",
      rooms_data: allRooms,
      message
    });
  } catch (err) {
    console.error(err);
    res.status(500).send(err.message);
  }
};

module.exports = { rooms };


// Option 2: Read the data from the rooms.json file @app_server.data and store data as trips.
/*
const fs = require('fs');
const rooms_data = JSON.parse(fs.readFileSync('./data/rooms.json', 'utf8'));

// GET rooms view
const rooms = (req, res) => {
    res.render('rooms', {title: "Rooms - Travlr Getaways", currentPage: 'rooms', rooms_data});
};

module.exports = {
    rooms
}
*/