// This file defines the Schema for the meals page and the meals collection in the database.

// Require Mongoose
const mongoose = require('mongoose');

// Define the travel collection schema
const mealsSchema = new mongoose.Schema({
    meal: { type: String, required: true, index: true },    // Use meal name as index
    image: { type: String, required: true },
    description: { type: String, required: true }
});

// Compile the Schema
// Structure=ConnectionName.model('nameOfModel', 'schemaToUse', 'collectionName')
const Meal = mongoose.model('Meal', mealsSchema, 'meal');
module.exports = Meal;