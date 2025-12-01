// This is the controller for authenticating and registering users.

const jwt = require("jsonwebtoken");
const User = require("../models/user");     // Methods to set JWT, register and validate user password
const passport = require('passport');       // 

// Register new user controller
const register = async (req, res) => {
    // Validate all user fields exist
    if (!req.body.name || !req.body.email || !req.body.password) {
        // Any field is missing error response
        return res.status(400).json({ message: "All fields required" });
    }

    try {
        // Instanciate new user schema model for db storage
        const user = new User({
            name: req.body.name,        // Fetch username
            email: req.body.email,      // Fetch password
        });

        // Set salt as "91d2a67...71d8d94706" and hash "e562....60d" to user
        user.setPassword(req.body.password);
        // Add user to database -> travlr.users
        await user.save();
        console.log("User: ", req.body.name, " added to database.");

        // Generate unique token for the user.
        const token = user.generateJWT();
        return res.status(200).json({ token });                 // Return JSON WEB Token(JWT)
    } catch (err) {
        return res.status(500).json({ error: err.message });    // Return error details
    }
};

// Login handler controller
const login = async (req, res) => {
    // Check that all fields are in place.
    if (!req.body.email || !req.body.password) {
        return res.status(400).json({ message: "All fields required" });    // Return error
    }

    try {
        // Query the database for the user by email.
        const user = await User.findOne({ email: req.body.email });

        // Check credentials and validate password (returns True if Hash metches)
        if (!user || !user.validPassword(req.body.password)) {
            return res.status(401).json({ message: "Invalid credentials" });    // No Hash metch
        }

        // Generate a JSON Web Token for the user for 1H
        const token = user.generateJWT();
        console.log("Username: ", req.body.email, "authenticated :-)");
        return res.status(200).json({ token });
    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
};

// JWT authentication middleware - sits between the client’s request and the route handler.
// Called by Express automatically before running the route logic where authentication is needed.
const authenticateJWT = (req, res, next) => {
    console.log('In Middleware - authenticateJWT');
    const authHeader = req.headers.authorization;

    if (!authHeader) {
        console.log('Not enough tokens in Auth Header.');
        return res.status(401).json({ message: "Missing Authorization header" });
    }

    // Extract (["Bearer", "eyJhbGciOiJIUzI1NiIs..."]) and store the token.
    const token = authHeader.split(" ")[1];

    // Verify the token is valid and up to date.
    jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
        if (err) {
            return res.status(403).json({ message: "Invalid or expired token" });
        }

        req.user = decoded; // store user info in request
        next();
    });
};

module.exports = { register, login, authenticateJWT };
