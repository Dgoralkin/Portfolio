// This file defines the Schema for the forms on the contact us page and the leads collection in the database.

// Require Mongoose
const mongoose = require('mongoose');

// Define the leads collection schema
const leadsSchema = new mongoose.Schema({
    name: { type: String, required: true},
    email: { type: String, required: true},
    subject: { type: String, required: true},
    message: { type: String, required: true},
    createdAt: { type: Date, default: Date.now }
});

// Compile the Schema
// Structure=ConnectionName.model('nameOfModel', 'schemaToUse', 'collectionName')
const Leads = mongoose.model('Leads', leadsSchema);
module.exports = Leads;