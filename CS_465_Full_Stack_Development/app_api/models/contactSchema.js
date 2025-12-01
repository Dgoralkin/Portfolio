// This file defines the Schema for the contact us page and the contact collection in the database.

// Require Mongoose
const mongoose = require('mongoose');

// Define the travel collection schema
const contactSchema = new mongoose.Schema({
    Company_name: { type: String, required: true },
    Address: { type: String, required: true, index: true },
    Telephone: { type: String, required: true },
    Email: { type: String, required: true },
});

// Compile the Schema
// Structure=ConnectionName.model('nameOfModel', 'schemaToUse', 'collectionName')
const Contact = mongoose.model('Contact', contactSchema, 'contact');
module.exports = Contact;