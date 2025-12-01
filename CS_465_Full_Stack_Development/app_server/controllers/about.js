// This is the controller module to add navigation functionality for the travel page and manage the application logic.
// res.render is the Express function for compiling a view template to send as the HTML response that the browser will receive

const fs = require('fs');
// Read the trips from the trips.json file @app_server.data and store data as trips.
const about_data = JSON.parse(fs.readFileSync('./data/about.json', 'utf8'));

/*GET about view*/
const about = (req, res) => {
    res.render('about', {title: "About - Travlr Getaways", currentPage: 'about', about_us: about_data});
};

module.exports = {
    about
}