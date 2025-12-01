// This file integrates with MongoDB to retrieve data for our application.

// Pull Model details from tripsSchema
const DB_News = require('../models/newsSchema');

// ======================================== //
//          *** Methods for GET ***         //
//    Triggered by the news_api router      //
// ======================================== //

// GET: /news -> Endpoint lists all news from DB.news collection.
const allNewsList = async (req, res) => {
    try {
        // Query the DB with get all
        const query = await DB_News.find({}).exec();

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

// GET: /news:latest_news -> Endpoint lists all latest_news from DB.news collection.
const findLatestNews = async (req, res) => {
    try {
        // Query the DB with find all document by LatestNews pased through "/news/:latest_news"
        // GET request for: http://localhost:3000/api/news/latest_news
        const query = await DB_News.find({}, { latest_news: 1, _id: 0 }).exec();

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

// GET: /news/latest_news/:title -> Endpoint lists selected latest_news article from DB.news collection.
const findLatestNewsArticle = async (req, res) => {
    try {
        const { title } = req.params;

        // Find one doc where latest_news has the requested title
        // http://localhost:3000/api/news/latest_news/2023 Best Beaches Contest Winners
        // db.news.find({ "latest_news.title": "2023 Best Beaches Contest Winners"},{ "latest_news.$": 1, _id: 0 })
        const result = await DB_News.findOne(
            { "latest_news.title": title },
            { "latest_news.$": 1, _id: 0 } // only return the matching array element
            ).lean();

        if (!result || !result.latest_news || result.latest_news.length === 0) {
            return res.status(404).json({ message: "Article not found" });
        }

        // Send back the one matching news item
        res.json(result.latest_news[0]);
    } catch (err) {
        console.error("Error retrieving article:", err);
        res.status(500).json({ message: "Server error" });
    }
};

// GET: /news/vacation_tips/:title -> Endpoint lists selected vacation_tips article from DB.news collection.
const findVacationTipsArticle = async (req, res) => {
    try {
        const { title } = req.params;

        // Find one doc where vacation_tips has the requested title
        // http://localhost:3000/api/news/vacation_tips/First Aid
        // db.news.find({ "vacation_tips.title": "First Aid"},{ "vacation_tips.$": 1, _id: 0 })
        const result = await DB_News.findOne(
            { "vacation_tips.title": title },
            { "vacation_tips.$": 1, _id: 0 } // only return the matching array element
            ).lean();

        if (!result || !result.vacation_tips || result.vacation_tips.length === 0) {
            return res.status(404).json({ message: "Article not found" });
        }

        // Send back the one matching news item
        res.json(result.vacation_tips[0]);
    } catch (err) {
        console.error("Error retrieving article:", err);
        res.status(500).json({ message: "Server error" });
    }
};

// Execute allNewsList endpoints.
module.exports = {
    allNewsList,
    findLatestNews,
    findLatestNewsArticle,
    findVacationTipsArticle
};