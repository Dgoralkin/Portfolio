// This file defines the Schema for the trips page and the travel collection in the database.

// Require Mongoose
const mongoose = require('mongoose');

// Define the index collection schema
const indexSchema = new mongoose.Schema({
    heading: { type: String, required: true, index: true },    // Use article name code as index
    heading_text: {type: String, required: true },
    Latest_Blog: {type: String, required: true },
    // arrays of objects for Blog_Entries
    Blog_Entries: [{ title: String, link: String, date: String, excerpt: String }],
    Testimonials_Heading: { type: String, required: true, index: true },
    // arrays of objects for Testimonials
    Testimonials: [{ text: String, author: String, link: String }]
});

// Compile the Schema
// Structure=ConnectionName.model('nameOfModel', 'schemaToUse', 'collectionName')
const Index = mongoose.model('Index', indexSchema, 'index');
module.exports = Index;