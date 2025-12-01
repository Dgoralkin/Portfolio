// Routing should map URL requests to controllers

const express = require('express');
const router = express.Router();
// Where the router looks for the URL:
const ctrlMain = require('../controllers/rooms');

/* Go to travel page @/rooms */
router.get('/rooms', ctrlMain.rooms);

module.exports = router;
