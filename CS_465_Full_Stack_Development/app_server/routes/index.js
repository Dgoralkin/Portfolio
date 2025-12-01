// Routing should map URL requests to controllers

const express = require('express');
const router = express.Router();
// Where the router looks for the URL:
const ctrlMain = require('../controllers');

/* GET homepage. */
router.get('/', ctrlMain.index);

module.exports = router;
