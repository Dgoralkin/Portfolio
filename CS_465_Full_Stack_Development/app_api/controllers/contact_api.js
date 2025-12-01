// This file integrates with MongoDB to retrieve data for our application.

// Pull Model details from tripsSchema
const DB_Contact = require('../models/contactSchema');
// Pull Model details from leadsSchema to store the submitted form documents.
const DB_Leads = require('../models/leadsSchema');

// ======================================== //
//          *** Methods for GET ***         //
//    Triggered by the contact_api router   //
// ======================================== //

// GET: /contact -> Endpoint lists all rooms from DB.room collection.
const getContactUsForm = async (req, res) => {
    try {
        // Query the DB with get all
        const query = await DB_Contact.find({}).exec();

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
//          *** Methods for POST ***         //
//    Triggered by the contact_api router   //
// ======================================== //

// POST: @ /api/contact -> Save a new contact form entry
// Read values through x-www-form-urlencoded body → req.body
const submitContactForm = async (req, res) => {
  try {
    // Fetch values from the submitted form.
    const { name, email, subject, message } = req.body;

        // Validate all fields were filled.
        if (!name || !email || !subject || !message) {
            // Return JSON response to the api/contact page if any of the fields is missing.
            return res.status(200).json({ message: `Fields: name, email, subject, or message cannot be empty!` });
        }

    // Add document to the DB.leads collection.
    const newContact = new DB_Leads({
      name,
      email,
      subject,
      message,
      createdAt: new Date()
    });
    
    // Save the lead data to the database.
    await newContact.save();

    console.log("FORM INPUT:", name, email, subject, message);

    // Return JSON response to the api/contact page.
    return res.status(201).json({ message: `Thank you ${name}, your form submitted successfully` });

    // Optional: Redirect to reload the contact page.
    // return res.redirect("/contact");

  } catch (err) {
    console.error("Error saving contact form:", err);
    return res.status(500).json({ message: "Server error", error: err });
  }
};

// Execute allRoomsList endpoints.
module.exports = {
    getContactUsForm,
    submitContactForm
};