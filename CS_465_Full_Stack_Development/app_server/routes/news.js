// Routing should map URL requests to controllers

const express = require('express');
const router = express.Router();
// Where the router looks for the URL:
const ctrlMain = require('../controllers/news');

/* Go to travel page @/news */
router.get('/news', ctrlMain.news);

module.exports = router;
