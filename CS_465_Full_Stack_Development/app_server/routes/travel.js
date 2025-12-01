// Routing should map URL requests to controllers

const express = require('express');
const router = express.Router();
// Where the router looks for the URL:
const ctrlMain = require('../controllers/travel');

/* Go to travel page @/travel */
router.get('/travel', ctrlMain.travel);

module.exports = router;
