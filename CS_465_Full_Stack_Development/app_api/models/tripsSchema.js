// This file defines the Schema for the trips page and the travel collection in the database.

// Require Mongoose
const mongoose = require('mongoose');

// Define the travel collection schema
const tripSchema = new mongoose.Schema({
    code: { type: String, required: true, index: true },    // Use trip code as index
    name: { type: String, required: true, index: true },    // Use trip name as index
    length: { type: String, required: true },
    start: { type: Date, required: true },
    resort: { type: String, required: true },
    perPerson: { type: String, required: true },
    image: { type: String, required: true },
    description: { type: String, required: true }
});

// Compile the Schema
// Structure=ConnectionName.model('nameOfModel', 'schemaToUse', 'collectionName')
const Trip = mongoose.model('Travel', tripSchema, 'travel');
module.exports = Trip;