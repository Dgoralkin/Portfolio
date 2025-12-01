// This file integrates with MongoDB to retrieve data for our application.

// Pull Model details from tripsSchema
const DB_Travel = require('../models/tripsSchema');

// ======================================== //
//          *** Methods for GET ***         //
//    Triggered by the travel_api router    //
// ======================================== //

// GET: /trip -> Endpoint lists all trips from DB.trips collection.
const allTripsList = async (req, res) => {
    try {
        // Query the DB with get all
        const query = await DB_Travel.find({}).exec();

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

// GET: /trip:tripCode -> Endpoint lists a single trip from DB.trips collection.
const findTrip = async (req, res) => {
    try {
        // Query the DB with find one document by code pased through "/travel/:tripCode"
        // GET request for: http://localhost:3000/api/travel/DAWR210315
        const query = await DB_Travel.findOne({'code' : req.params.tripCode}).exec();

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

// ======================================== //
//          *** Methods for POST ***        //
//    Triggered by the travel_api router    //
// ======================================== //
// POST: / -> Adds a trip to DB.travel collection.
const tripsAddTrip = async (req, res) => {

    // console.log("In tripsAddTrip -> req.body: ", req.body);

    try {
        // Fetch the form data from the passed object and store it in the schema format
    const newTrip = new DB_Travel({
        code: req.body.code,
        name: req.body.name,
        length: req.body.length,
        start: req.body.start,
        resort: req.body.resort,
        perPerson: req.body.perPerson,
        image: req.body.image,
        description: req.body.description,
    });

    // Add the form to the db
    const q = await newTrip.save();
    // console.log(q);
    return res.status(201).json(q);

    } catch {
        console.error("Error adding trips:");
        return res.status(400).json({ message: "Error adding trips to db."});
    }
};

// ======================================== //
//          *** Methods for PUT ***         //
//    Triggered by the travel_api router    //
// ======================================== //
// PUT: /trips/:tripCode - Find a Trip from the db and updates its fields
const tripsUpdateTrip = async (req, res) => {

    // console.log("In tripsUpdateTrip -> req.params: ", req.params);
    // console.log("In tripsUpdateTrip -> req.body: ", req.body);

    try {
        // Method updates the "req.params.tripCode" item from the DB.travel collection with req.body parameters.
        const updateTrip = await DB_Travel.findOneAndUpdate(
            {'code' : req.params.tripCode}, 
            {code: req.body.code,
            name: req.body.name,
            length: req.body.length,
            start: req.body.start,
            resort: req.body.resort,
            perPerson: req.body.perPerson,
            image: req.body.image,
            description: req.body.description
            }
        ).exec();

        // console.log(updateTrip);
        return res.status(201).json(updateTrip);
    } catch (err){
        console.error("Error updating trip:");
        return res.status(400).json({ message: "Error updating trip in db.", err});
    }
};

// Execute tripsList endpoints.
module.exports = {
    allTripsList,       // GET method
    findTrip,           // GET method
    tripsAddTrip,       // POST method
    tripsUpdateTrip     // PUT method
};