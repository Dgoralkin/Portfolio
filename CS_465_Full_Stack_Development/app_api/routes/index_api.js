// This file routes the process to the index_api.js controller in app_api

const express = require("express");
const router = express.Router();

// Define the path for the travel page api
const indexController = require("../controllers/index_api");

// Defines the endpoint `/` (homepage) and pass the content to the 'indexController.indexList' function.
router.route("/")
.get(indexController.allArticlesList)   // GET method routes lists all articles from DB.index collection

// Defines the endpoint `/travel/:tripCode` and pass the content to the 'tripsController.findTrip' function.
// router.route("/travel/:tripCode").get(indexController.findTrip);

module.exports = router;