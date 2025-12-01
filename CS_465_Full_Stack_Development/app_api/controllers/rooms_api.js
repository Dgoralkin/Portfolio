// This file integrates with MongoDB to retrieve data for our application.

// Pull Model details from tripsSchema
const DB_Room = require('../models/roomsSchema');

// ======================================== //
//          *** Methods for GET ***         //
//    Triggered by the rooms_api router     //
// ======================================== //

// GET: /rooms -> Endpoint lists all rooms from DB.room collection.
const allRoomsList = async (req, res) => {
    try {
        // Query the DB with get all
        const query = await DB_Room.find({}).exec();

        // If no results found, still return 200 but with a response message
        if (!query || query.length === 0) {
            return res.status(200).json({ message: "No results found" });
        }
        // Otherwise, return 200 OK and a json result
        return res.status(200).json(query);

    } catch (err) {
        console.error("Error retrieving trips:", err);
        return res.status(404).json({ message: "Server error", error: err });
    }
};

// GET: /rooms:roomName -> Endpoint lists a single room from DB.room collection.
const findRoom = async (req, res) => {
    try {
        // Query the DB with find one document by code pased through "/rooms/:roomName"
        // GET request for: http://localhost:3000/api/rooms/First Class Room
        const query = await DB_Room.find({'name' : req.params.roomName}).exec();

        // If no results found, still return 200 but with a response message
        if (!query || query.length === 0) {
            return res.status(200).json({ message: "No results found" });
        }
        // Otherwise, return 200 OK and a json result
        return res.status(200).json(query);

    } catch (err) {
        console.error("Error retrieving trips:", err);
        return res.status(404).json({ message: "Server error", error: err });
    }
};

// Execute allRoomsList endpoints.
module.exports = {
    allRoomsList,
    findRoom
};