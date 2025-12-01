// This file defines the Schema for the about us page and the about collection in the database.

// Require Mongoose
const mongoose = require('mongoose');

// Define the travel collection schema
const aboutSchema = new mongoose.Schema({
    content: {
        paragraph_1: { type: String, required: true },
        paragraph_2: { type: String, required: true }
    },
    community: { type: String, required: true },
    details: { type: String, required: true },
    crews: { type: String, required: true },
    amenities: { type: String, required: true }
});

// Compile the Schema
// Structure=ConnectionName.model('nameOfModel', 'schemaToUse', 'collectionName')
const About = mongoose.model('About', aboutSchema, 'about');
module.exports = About;