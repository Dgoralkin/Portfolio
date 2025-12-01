// This file integrates with MongoDB to retrieve data for our application.

// Pull Model details from tripsSchema
const DB_Index = require('../models/indexSchema');

// ======================================== //
//          *** Methods for GET ***         //
//    Triggered by the index_api router     //
// ======================================== //

// GET: / -> Endpoint lists all articles from DB.index collection.
const allArticlesList = async (req, res) => {
    try {
        // Query the DB with get all
        const query = await DB_Index.find({}).exec();

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

// Execute allArticlesList endpoints.
module.exports = {
    allArticlesList
};
