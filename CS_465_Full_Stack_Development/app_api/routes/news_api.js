// This file routes the process to the news_api.js controller in app_api

const express = require("express");
const router = express.Router();

// Define the path for the news page api
const newsController = require("../controllers/news_api");

// Defines the endpoint '/news' and pass the content to the 'newsController.allNewsList' function.
router.route("/news").get(newsController.allNewsList);

// Defines the endpoint '/news/:latest_news' and pass the content to the 'newsController.findLatestNews' function.
router.route("/news/:latest_news").get(newsController.findLatestNews);

// Defines the endpoint '/news/:latest_news' and pass the content to the 'newsController.findLatestNews' function.
router.route("/news/latest_news/:title").get(newsController.findLatestNewsArticle);

// Defines the endpoint '/news/:vacation_tips' and pass the content to the 'newsController.findVacationTipsArticle' function.
router.route("/news/vacation_tips/:title").get(newsController.findVacationTipsArticle);

module.exports = router;