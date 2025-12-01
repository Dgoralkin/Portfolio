// This file routes the process to the travel_api.js controller in app_api
// This enables the backend CRUD functionality

const express = require("express");
const router = express.Router();
const jwt = require('jsonwebtoken');                        // Enable JSON Web Tokens

// Define the path for the travel page api
const tripsController = require("../controllers/travel_api");

// Get the path to activate authentication
const authController = require("../controllers/authentication");
const { authenticateJWT } = require("../controllers/authentication");

// Define the end point for registering and login users in.
router.route("/register").post(authController.register);
router.route("/login").post(authController.login);

// Defines the endpoint `/travel` and pass the content to the 'tripsController.tripsList' function.
router.route("/travel")
.get(tripsController.allTripsList)                          // GET  -> fetch all trips from DB.travel collection.
// Express calls authenticateJWT before running the route logic.
.post(authenticateJWT, tripsController.tripsAddTrip);       // POST -> save an article to DB.travel collection.

// Defines the endpoint `/travel/:tripCode` and pass the content to the 'tripsController.findTrip' function.
router.route("/travel/:tripCode")
.get(tripsController.findTrip)                              // GET  -> fetch one specific trip from DB.travel collection.
// Express calls authenticateJWT before running the route logic.
.put(authenticateJWT, tripsController.tripsUpdateTrip);     // PUT  -> find and update item from DB.travel collection.

module.exports = router;