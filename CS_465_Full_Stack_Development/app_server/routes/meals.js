// Routing should map URL requests to controllers

const express = require('express');
const router = express.Router();
// Where the router looks for the URL:
const ctrlMain = require('../controllers/meals');

/* Go to travel page @/meals */
router.get('/meals', ctrlMain.meals);

module.exports = router;
