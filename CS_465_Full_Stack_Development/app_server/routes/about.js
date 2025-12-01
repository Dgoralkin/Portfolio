// Routing should map URL requests to controllers

const express = require('express');
const router = express.Router();
// Where the router looks for the URL:
const ctrlMain = require('../controllers/about');

/* Go to travel page @/about */
router.get('/about', ctrlMain.about);

module.exports = router;
