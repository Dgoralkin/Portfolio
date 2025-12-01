// This file defines the Schema for the rooms page and the room collection in the database.

// Require Mongoose
const mongoose = require('mongoose');

// Define the room collection schema
const roomSchema = new mongoose.Schema({
    name: { type: String, required: true, index: true },    // Use room name code as index
    image: { type: String, required: true },
    description: { type: String, required: true },
    rate: { type: String, required: true }
});

// Compile the Schema
// Structure=ConnectionName.model('nameOfModel', 'schemaToUse', 'collectionName')
const Room = mongoose.model('Room', roomSchema, 'room');
module.exports = Room;