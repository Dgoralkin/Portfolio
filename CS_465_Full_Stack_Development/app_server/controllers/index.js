// This is the controller module to add navigation functionality for the index page.
// Controllers should manage the application logic


// Option 1: Read the articles from the database @travlr.index collection and store data as array.
// Fetch data from the database through an API endpoint.


// Create a variable for our API endpoint
// Use env var to switch between local and deployed on Render modes
const apiHost = process.env.API_HOST || "http://localhost:3000";
const indexEndpoint = `${apiHost}/api`;

const options = {
  method: "GET",
  headers: { Accept: "application/json" }
};

/* GET index view */
const index = async (req, res, next) => {
  console.log("INDEX CONTROLLER BEGIN");

  try {
    // Fetch results from the database (backend API URL)
    const response = await fetch(indexEndpoint, options);

    // Throw an error if the response has a bad status (404 or 500)
    if (!response.ok) {
      throw new Error(`API error: ${response.statusText}`);
    }

    // Converts the API response into a JS object/array.
    let articles = await response.json();
    // console.log(articles);
    let message = null;

    if (!Array.isArray(articles)) {
      console.error("API returned unexpected data");
      message = "API lookup error";
      articles = [];
    } else if (articles.length === 0) {
      message = "No trips were found in our database.";
    }

    // Response 200 OK, Render the "index.js" page with data from the DB/trips.
    res.render("index", {
      title: "Travlr Getaways",
      currentPage: "/",
      content: articles[0],
      message
    });
  } catch (err) {
    console.error(err);
    res.status(500).send(err.message);
  }
};

module.exports = { index };



// Option 2: Read the data from the index.json file @app_server.data and store data as trips.
/*
const fs = require('fs');
// Read the trips from the trips.json file @app_server.data and store data as trips.
const index_data = JSON.parse(fs.readFileSync('./data/index.json', 'utf8'));

// GET Homepage view
const index = (req, res) => {
    // render the "index.js" page from the views upon router request
    // res.render is the Express function for compiling a view template to send as 
    // the HTML response that the browser will receive
    res.render('index', {title: "Travlr Getaways", currentPage: '/', content: index_data});
};

module.exports = {
    index
}
*/