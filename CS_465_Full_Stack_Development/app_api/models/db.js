// This file use used to establish the connection to the MongoDB database through Mongoose
// And require it in app.js

const mongoose = require('mongoose');

// Define the localhost (for a local DB)
const localhost = '127.0.0.1';

// Connect to the Atlas DB by default via env vars.
// Secondary connection option is via the local host if cannot connect to Atlas remotely.
const dbURI = process.env.ATLAS_DB_HOST || `mongodb://${localhost}/travlr`;

// Async function to connect with try/catch
async function connectDB() {
  try {
    // Connect to the database
    await mongoose.connect(dbURI);

    // Log if Atlas or local connected
    if (process.env.ATLAS_DB_HOST) {
      // strip credentials before logging
      const safeURI = dbURI.replace(/\/\/.*@/, '//***:***@');
      console.log(`Mongoose connected to AtlasDB @ ${safeURI}`);
    } else {
      console.log(`Mongoose connected to Local DB @ ${dbURI}`);
    }
  } catch (err) {
    console.error('Mongoose connection error:', err);
  }
}

// Call the connection function
connectDB();

// Registers a listener (callback function) that runs whenever the event occurs.
// Display db connection status via events
mongoose.connection.on('error', err => {
  console.log('Mongoose connection error:', err);
});

mongoose.connection.on('disconnected', () => {
  console.log('Mongoose disconnected');
}); 

// Close connection gracefully when app is terminated
process.on('SIGINT', async () => {
  await mongoose.connection.close();
  console.log('Mongoose disconnected on app termination');
  process.exit(0);
});

// Outout the connection state:
console.log('Connection state:', mongoose.connection.readyState);

// Expose the database schema from the models dir
// Import Mongoose schema from the travlr dir
require('./tripsSchema');
require('./indexSchema');
require('./roomsSchema');
require('./mealsSchema');
require('./aboutSchema');
require('./contactSchema');
module.exports = mongoose;