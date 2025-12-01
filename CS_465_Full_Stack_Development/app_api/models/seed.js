// Run the seed command manually: from main dir to seed the database -> 
// $ node .\app_api\models\seed

const fs = require('fs');

// Import the DB connection
const Mongoose = require('./db');

// Import the Schemas
const Index = require('./indexSchema');
const Trip = require('./tripsSchema');
const Room = require('./roomsSchema');
const Meal = require('./mealsSchema');
const News = require('./newsSchema');
const About = require('./aboutSchema');
const Contact = require('./contactSchema');
// Read seed data drom the json files
var items = JSON.parse(fs.readFileSync('./data/index.json', 'utf8'));
var trips = JSON.parse(fs.readFileSync('./data/trips.json', 'utf8'));
var rooms = JSON.parse(fs.readFileSync('./data/rooms.json', 'utf8'));
var meals = JSON.parse(fs.readFileSync('./data/meals.json', 'utf8'));
var news = JSON.parse(fs.readFileSync('./data/news.json', 'utf8'));
var about_us = JSON.parse(fs.readFileSync('./data/about.json', 'utf8'));
var contacts = JSON.parse(fs.readFileSync('./data/contact.json', 'utf8'));


// Populate the travel collection if empty
var seedIndex = async () => {
  try {
    // Count docs in the travel collection. Seed if empty, skip othrwise.
    const count = await Index.estimatedDocumentCount();
    if (count === 0) {
      console.log('Index collection is empty, seeding data...');
      await Index.insertMany(items);
      console.log('Seeding complete!');
    } else {
      console.log(`Index collection already has ${count} documents. Skipping seeding.`);
    }
  } catch (err) {
    console.error('Error during seeding:', err);
  }
};


// Populate the travel collection if empty
var seedTravel = async () => {
  try {
    // Count docs in the travel collection. Seed if empty, skip othrwise.
    const count = await Trip.estimatedDocumentCount();
    if (count === 0) {
      console.log('Travel collection is empty, seeding data...');
      await Trip.insertMany(trips);
      console.log('Seeding complete!');
    } else {
      console.log(`Travel collection already has ${count} documents. Skipping seeding.`);
    }
  } catch (err) {
    console.error('Error during seeding:', err);
  }
};


// Populate the room collection if empty
var seedRoom = async () => {
  try {
    // Count docs in the travel collection. Seed if empty, skip othrwise.
    const count = await Room.estimatedDocumentCount();
    if (count === 0) {
      console.log('Room collection is empty, seeding data...');
      await Room.insertMany(rooms);
      console.log('Seeding complete!');
    } else {
      console.log(`Room collection already has ${count} documents. Skipping seeding.`);
    }
  } catch (err) {
    console.error('Error during seeding:', err);
  }
};


// Populate the meal collection if empty
var seedMeal = async () => {
  try {
    // Count docs in the travel collection. Seed if empty, skip othrwise.
    const count = await Meal.estimatedDocumentCount();
    if (count === 0) {
      console.log('Meal collection is empty, seeding data...');
      await Meal.insertMany(meals);
      console.log('Seeding complete!');
    } else {
      console.log(`Meal collection already has ${count} documents. Skipping seeding.`);
    }
  } catch (err) {
    console.error('Error during seeding:', err);
  }
};


// Populate the news collection if empty
var seedNews = async () => {
  try {
    // Count docs in the travel collection. Seed if empty, skip othrwise.
    const count = await News.estimatedDocumentCount();
    if (count === 0) {
      console.log('News collection is empty, seeding data...');
      await News.insertMany(news);
      console.log('Seeding complete!');
    } else {
      console.log(`News collection already has ${count} documents. Skipping seeding.`);
    }
  } catch (err) {
    console.error('Error during seeding:', err);
  }
};


// Populate the contact collection if empty
var seedContacts = async () => {
  try {
    // Count docs in the travel collection. Seed if empty, skip othrwise.
    const count = await Contact.estimatedDocumentCount();
    if (count === 0) {
      console.log('Contact collection is empty, seeding data...');
      await Contact.insertMany(contacts);
      console.log('Seeding complete!');
    } else {
      console.log(`Contact collection already has ${count} documents. Skipping seeding.`);
    }
  } catch (err) {
    console.error('Error during seeding:', err);
  }
};


// Populate the about collection if empty
var seedabout = async () => {
  try {
    // Count docs in the travel collection. Seed if empty, skip othrwise.
    const count = await About.estimatedDocumentCount();
    if (count === 0) {
      console.log('About collection is empty, seeding data...');
      await About.insertMany(about_us);
      console.log('Seeding complete!');
    } else {
      console.log(`About collection already has ${count} documents. Skipping seeding.`);
    }
  } catch (err) {
    console.error('Error during seeding:', err);
  }
};


// Close connection after all collections are seeded.
const seedAll = async () => {
  await seedIndex();
  await seedTravel();
  await seedRoom();
  await seedMeal();
  await seedNews();
  await seedabout();
  await seedContacts();
};


// Close connection after all collections are seeded.
seedAll().then(async () => {
    await Mongoose.connection.close();
    process.exit(0);
});