// This is the controller module to add navigation functionality for the travel page and manage the application logic.
// res.render is the Express function for compiling a view template to send as the HTML response that the browser will receive


// Option 1: Read the meals from the database @travlr.meal collection and store data as array.
// Fetch data from the database through an API endpoint.

// Create a variable for our API endpoint
// Use env var to switch between local and deployed on Render modes
const apiHost = process.env.API_HOST || "http://localhost:3000";
const mealsEndpoint = `${apiHost}/api/meals`;

const options = {
  method: "GET",
  headers: { Accept: "application/json" }
};

/* GET meals view */
const meals = async (req, res, next) => {
  console.log("MEALS CONTROLLER BEGIN");

  try {
    // Fetch results from the database (backend API URL)
    const response = await fetch(mealsEndpoint, options);

    // Throw an error if the response has a bad status (404 or 500)
    if (!response.ok) {
      throw new Error(`API error: ${response.statusText}`);
    }

    // Converts the API response into a JS object/array.
    let allMeals = await response.json();
    // console.log(allMeals);
    let message = null;

    if (!Array.isArray(allMeals)) {
      console.error("API returned unexpected data");
      message = "API lookup error";
      allMeals = [];
    } else if (allMeals.length === 0) {
      message = "No trips were found in our database.";
    }

    // Response 200 OK, Render the "meals.js" page with data from the DB.trip.
    res.render("meals", {
      title: "Meals - Travlr Getaways",
      currentPage: "meals",
      meals_data: allMeals,
      message
    });
  } catch (err) {
    console.error(err);
    res.status(500).send(err.message);
  }
};

module.exports = { meals };


// Option 2: Read the meals from the database @travlr.meal collection and store data as trips.
// Fetch data from the database through an API endpoint.
/*
const fs = require('fs');
// Read the meals from the meals.json file @app_server.data and store data as trips.
const meals_data = JSON.parse(fs.readFileSync('./data/meals.json', 'utf8'));

// GET meals view
const meals = (req, res) => {
    res.render('meals', {title: "Meals - Travlr Getaways", currentPage: 'meals', meals_data});
};

module.exports = {
    meals
}
*/