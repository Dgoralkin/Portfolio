const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const passport = require('passport');

// Wire in our authentication module 
require('./app_api/config/passport');

// Require MongoDB from the app_api/models folder
require('./app_api/models/db');

// Pull in the contents of our .env file to bring the variables defined in the file into our memory space
require('dotenv').config();

// Setup routes for page navigation
const indexRouter = require('./app_server/routes/index');         // Update the path for the homepage
const travelRouter = require('./app_server/routes/travel');       // Update the path for the travel page
const roomRouter = require('./app_server/routes/rooms');           // Update the path for the room page
const mealsRouter = require('./app_server/routes/meals');         // Update the path for the meals page
const newsRouter = require('./app_server/routes/news');           // Update the path for the news page
const aboutRouter = require('./app_server/routes/about');         // Update the path for the about page
const contactRouter  = require('./app_server/routes/contact');    // Update the path for the contact us page

// Setup rest api routes for page navigation
const indexApiRouter = require('./app_api/routes/index_api');     // Path to the index api
const travelApiRouter = require('./app_api/routes/travel_api');   // Path to the travel api
const roomsApiRouter = require('./app_api/routes/rooms_api');     // Path to the rooms api
const mealsApiRouter = require('./app_api/routes/meals_api');     // Path to the meals api
const newsApiRouter = require('./app_api/routes/news_api');       // Path to the news api
const contactUsApiRouter = require('./app_api/routes/contact_api');       // Path to the news api

// Enable handlebars to render in multipal pages
const handelbars = require('hbs');

// Enable helper for handelbars
handelbars.registerHelper('eq', function(a, b) {
  return a === b;
});

const app = express();

// view engine setup
app.set('views', path.join(__dirname, 'app_server', 'views'));    // Update the path for the new app_server dir

// register the call to enable partials handlebars:
handelbars.registerPartials(path.join(__dirname, 'app_server', 'views', 'partials'));
app.set('view engine', 'hbs');

app.use(logger('dev'));
app.use(express.json());
// Parse application/x-www-form-urlencoded
app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
// Add an initializer for our passport module
app.use(passport.initialize());

// ENABLE CORS (Cross Origin Resource Sharing) for resource sharing to hook Angular SPA
app.use((req, res, next) => {
  const allowedOrigin = 'http://localhost:4200';
  res.header('Access-Control-Allow-Origin', allowedOrigin);
  res.header(
    'Access-Control-Allow-Headers',
    'Origin, X-Requested-With, Content-Type, Accept, Authorization'
  );
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.header('Access-Control-Allow-Credentials', 'true'); // Allow credentials if needed

  if (req.method === 'OPTIONS') {
    console.log('Preflight request received for:', req.originalUrl);
    return res.sendStatus(204);
  }

  next();
});

// Catch unauthorized error and create 401
app.use((err, req, res, next) => {
  if (err.name === 'UnauthorizedError') {
    res.status(401).json({"message": err.name + ": " + err.message});
  }
});


// Activate the homepage and all other pages
app.use('/', indexRouter);                  // Go to homepage (index).
app.use('/', travelRouter);                 // Go to the travel page.
app.use('/', roomRouter);                   // Go to the room page.
app.use('/', mealsRouter);                  // Go to the room page.
app.use('/', newsRouter);                   // Go to the news page.
app.use('/', aboutRouter);                  // Go to the about page.
app.use('/', contactRouter);                // Go to all contact us pages.

// Wire-up api routes to controllers
app.use('/api', indexApiRouter);            // Trigger the api for the index homepage from app_api/routes/index_api
app.use('/api', travelApiRouter);           // Trigger the api for the travel page from app_api/routes/travel_api
app.use('/api', roomsApiRouter);            // Trigger the api for the rooms page from app_api/routes/rooms_api
app.use('/api', mealsApiRouter);            // Trigger the api for the meals page from app_api/routes/meals_api
app.use('/api', newsApiRouter);             // Trigger the api for the news page from app_api/routes/news_api
app.use('/api', contactUsApiRouter);        // Trigger the api for the contact us page from app_api/routes/news_api

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
