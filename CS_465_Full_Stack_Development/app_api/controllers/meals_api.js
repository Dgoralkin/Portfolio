// This file integrates with MongoDB to retrieve data for our application.

// Pull Model details from tripsSchema
const DB_meal = require('../models/mealsSchema');

// ======================================== //
//          *** Methods for GET ***         //
//    Triggered by the meals_api router     //
// ======================================== //

// GET: /meals -> Endpoint lists all meals from DB.meal collection.
const allMealsList = async (req, res) => {
    try {
        // Query the DB with get all
        const query = await DB_meal.find({}).exec();

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

// GET: /meals:mealName -> Endpoint lists a single meal from DB.meal collection.
const findMeal = async (req, res) => {
    try {
        // Query the DB with find one document by code pased through "/meals/:mealName"
        // GET request for: http://localhost:3000/api/meals/SeaFood Special
        const query = await DB_meal.find({'meal' : req.params.mealName}).exec();

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

// Execute allRoomsList endpoints.
module.exports = {
    allMealsList,
    findMeal
};