// This file routes the process to the rooms_api.js controller in app_api

const express = require("express");
const router = express.Router();

// Define the path for the rooms page api
const roomsController = require("../controllers/rooms_api");

// Defines the endpoint '/rooms' and pass the content to the 'roomsController.allRoomsList' function.
router.route("/rooms").get(roomsController.allRoomsList);

// Defines the endpoint '/rooms/:roomName' and pass the content to the 'roomsController.findRoom' function.
router.route("/rooms/:roomName").get(roomsController.findRoom);

module.exports = router;