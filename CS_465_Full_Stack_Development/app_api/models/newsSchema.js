// This file defines the Schema for the news page and the news collection in the database.

// Require Mongoose
const mongoose = require('mongoose');

// Define the News schema
const newsSchema = new mongoose.Schema({
  latest_news: [
    {
      title: { type: String, required: true },
      link: { type: String, required: true }
    }
  ],
  vacation_tips: [
    {
      title: { type: String, required: true },
      link: { type: String, required: true }
    }
  ],
  News_main: {
    image: { type: String, required: true },
    link: { type: String, required: true },
    title: { type: String, required: true },
    date: { type: String, required: true },
    author: { type: String, required: true },
    content: [{ type: String, required: true }] // array of paragraphs
  }
});

// Compile the schema into a model
// 'News' = model name in Node.js
// 'news' = MongoDB collection name
const News = mongoose.model('News', newsSchema, 'news');

module.exports = News;
