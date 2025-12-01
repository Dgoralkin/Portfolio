// Routing should map URL requests to controllers

const express = require('express');
const router = express.Router();
// Where the router looks for the URLs:
const controller = require('../controllers/contact');

/* GET contact page. */
router.get('/contact', controller.getContactUs);

module.exports = router;
