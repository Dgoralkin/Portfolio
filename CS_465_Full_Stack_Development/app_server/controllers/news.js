// This is the controller module to add navigation functionality for the travel page and manage the application logic.
// res.render is the Express function for compiling a view template to send as the HTML response that the browser will receive

// Option 1: Read the rooms from the database @travlr.room collection and store data as array.
// Fetch data from the database through an API endpoint.

// Create a variable for our API endpoint
// Use env var to switch between local and deployed on Render modes
const apiHost = process.env.API_HOST || "http://localhost:3000";
const newsEndpoint = `${apiHost}/api/news`;

const options = {
  method: "GET",
  headers: { Accept: "application/json" }
};

/* GET news view */
const news = async (req, res, next) => {
  console.log("NEWS CONTROLLER BEGIN");

  try {
    // Fetch results from the database (backend API URL)
    const response = await fetch(newsEndpoint, options);

    // Throw an error if the response has a bad status (404 or 500)
    if (!response.ok) {
      throw new Error(`API error: ${response.statusText}`);
    }

    // Converts the API response into a JS object/array.
    let allNews = await response.json();
    // console.log(allNews);
    let message = null;

    if (!Array.isArray(allNews)) {
      console.error("API returned unexpected data");
      message = "API lookup error";
      allNews = [];
    } else if (allNews.length === 0) {
      message = "No trips were found in our database.";
    }

    // Response 200 OK, Render the "news.js" page with data from the DB.news.
    res.render("news", {
      title: "News - Travlr Getaways",
      currentPage: "news",
      news_data: allNews[0],
      message
    });
  } catch (err) {
    console.error(err);
    res.status(500).send(err.message);
  }
};

module.exports = { news };


// Option 2: Read the data from the news.json file @app_server.data and store data as trips.
/*

const fs = require('fs');
// Read the trips from the trips.json file @app_server.data and store data as trips.
const news_data = JSON.parse(fs.readFileSync('./data/news.json', 'utf8'));

// GET news view
const news = (req, res) => {
    res.render('news', {title: "News - Travlr Getaways", currentPage: 'news', news_data});
};

module.exports = {
    news
}
*/