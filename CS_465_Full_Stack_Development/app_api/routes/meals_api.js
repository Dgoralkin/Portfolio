// This file routes the process to the meals_api.js controller in app_api

const express = require("express");
const router = express.Router();

// Define the path for the roots page api
const mealsController = require("../controllers/meals_api");

// Defines the endpoint '/meals' and pass the content to the 'mealsController.allMealsList' function.
router.route("/meals").get(mealsController.allMealsList);

// Defines the endpoint '/meals/:mealName' and pass the content to the 'mealsController.findMeal' function.
router.route("/meals/:mealName").get(mealsController.findMeal);

module.exports = router;