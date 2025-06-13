# CS 340 - Austin Animal Center Dashboard

### Author: Daniel Gorelkin  
**Email**: Daniel.Gorelkin@SNHU.edu  
**GitHub**: [Dgoralkin](https://github.com/Dgoralkin)

---

## 📌 About the Project

The **Austin Animal Center Dashboard** is a full-stack interactive application that enables users to seamlessly connect to a MongoDB database (`AAC`) and perform CRUD (Create, Read, Update, Delete) operations on the `animals` collection. This Python-based project bridges a powerful backend (MongoDB) with a dynamic, browser-based front end using the **Dash** framework. The main user interface from which the data can be queried, rendered, filtered, sorted, and visualized after successful user authentication. The front-end dashboard will enable users to search and filter the various dogs available in the database by the predefined rescue categories, using the specified criteria, and output the filtered results in an interactive and dynamic table. Additionally, the functionality will output a pie chart of all the available dog breeds and display the location of a chosen dog on a geolocation map. The application will also handle errors such as a bad connection to the database, user authentication errors, an empty database, or no search results found.


The dashboard supports:
- User authentication.
- Dynamic filtering of rescue dog data by predefined criteria.
- Display of results in an interactive table.
- Visualizations through pie charts and geolocation mapping.

It is designed to streamline the querying, rendering, filtering, and visualization of animal shelter data with robust error handling for authentication failures, connection issues, and empty or invalid queries.

---

### 💥 Try It Now Live!
🕹 Run a live app demo in a real browser:

<img src="https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExNm50empubHhqN2N6YXdyNzloNm14aWNjcGgxeGxnemljeTRxMjF5YyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/aT3HkQgHLlCrvQWmPj/giphy.gif" alt="Play it Live onrender.com" width="200">
- Click the link to Play it Live on austinanimalcenter.com: <a href="https://austinanimalcenter-sers.onrender.com" target="_blank" rel="noopener noreferrer">Play it Live on austinanimalcenter.com</a>

---

## 🖼️ Application Screenshots

### ✅ Dashboard View Before Login
![Dashboard View Before Login](https://github.com/Dgoralkin/AustinAnimalCenter_RenderDeploy/blob/main/ScreenShots/Dashboard_View_Before_Login.png)

### ✅ Dashboard View After Login
![Dashboard View After Login](https://github.com/Dgoralkin/AustinAnimalCenter_RenderDeploy/blob/main/ScreenShots/Dashboard_View_After_Login.png)

---

## 🛠️ Tools & Technologies

### Backend / Model
- **MongoDB**:
  - NoSQL document-based storage.
  - JSON-like BSON documents fit Python’s `dict` structures perfectly.
  - Supports geolocation fields.
  - Flexible querying and indexing.
- **PyMongo**:
  - Python library to interface with MongoDB.
  - Implements the CRUD operations in the `AnimalShelter` class.

### Frontend / View + Controller
- **Dash (Plotly)**:
  - Web application framework for Python.
  - Built on Flask and React.js.
  - Handles callbacks for dynamic user interactions.
- **Dash Bootstrap Components**:
  - For responsive and styled layouts.
- **Dash Leaflet**:
  - Adds geolocation-based map visualizations.
- **Plotly Express**:
  - Used to generate pie charts of animal breed distributions.
- **Pandas**:
  - Data manipulation and formatting before display.

---

## 🚀 Getting Started

### Prerequisites

Ensure the following tools are installed:
- Python 3.8+
- MongoDB installed and running locally (`localhost:27017`)
- Git (optional, to clone the repo)

### Installation Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Dgoralkin/AustinAnimalCenterDashboard.git
   cd AustinAnimalCenterDashboard
   ```

2. **Set up the MongoDB Database**:
   - Import the dataset using:
     ```bash
     mongoimport --db AAC --collection animals --file aac_shelter_outcomes.csv --type csv --headerline
     ```

3. **Create a MongoDB User**:
   Inside `mongosh`:
   ```javascript
   use AAC
   db.createUser({
     user: "aacuser",
     pwd: "SNHU1234",
     roles: [{ role: "readWrite", db: "AAC" }]
   })
   ```

4. **(Optional) Create Index for Optimization**:
   ```javascript
   db.animals.createIndex(
     { breed: 1, sex_upon_outcome: 1, age_upon_outcome_in_weeks: 1 },
     { name: "breed_sex_age_index" }
   )
   ```

5. **Install Required Python Packages**:
   ```bash
   pip install dash dash-bootstrap-components dash-leaflet pandas plotly pymongo
   ```
   ![Dashboard error Handlers](https://github.com/Dgoralkin/AustinAnimalCenter_RenderDeploy/blob/main/ScreenShots/RequiredLibs.png)

---

## 💡 Usage

1. **Launch the Dashboard**:
   Run the Dash app script:
   ```bash
   python DashForAnimalShelter.py
   ```

2. **Authenticate**:
   Use the following credentials to connect:
   ```
   Username: aacuser
   Password: SNHU1234
   ```

3. **Explore Features**:
   - View and sort animals in a searchable table.
   - Filter by rescue types: Water Rescue, Mountain Rescue, Disaster Rescue.
   - View breed distribution (pie chart).
   - Visualize selected animal’s location on a map.

4. **Optional Customizations**:
   - Uncomment the default credentials and filters in the script for quick testing without login.
   - Adjust filter logic or queries in the `DashForAnimalShelter.py` file.

---

## ✅ Testing & Error Handling

The application gracefully handles:
- Missing credentials (prompts the user).
- Incorrect username/password (auth failure message).
- No records found (displays warning, hides visuals).
- Manual filters that yield no results.

### 🪲 Dashboard Error Handlers
![Dashboard rror Handlers](https://github.com/Dgoralkin/AustinAnimalCenter_RenderDeploy/blob/main/ScreenShots/Error_Handling_Examples.png)
---

## 🛠️ Project Development Notes

This project was developed in stages:
1. Initial database setup using `mongosh` and CSV import.
2. CRUD model class creation (`AnimalShelter.py`).
3. UI construction using Dash and Bootstrap.
4. Callback logic to connect UI with database functions.
5. Graph and map integration using Plotly and Dash Leaflet.
6. Error handling, UX optimization, and final testing.

---

## 📬 Contact

For issues, suggestions, or contributions:

**Daniel Gorelkin**  
📧 Daniel.Gorelkin@SNHU.edu  
🔗 [GitHub Profile](https://github.com/Dgoralkin)

---

## 📚 References

- [PyMongo Documentation](https://pymongo.readthedocs.io/)
- [Dash Documentation](https://dash.plotly.com/)
- [Dash DataTable](https://dash.plotly.com/datatable)
- [Dash Leaflet](https://dash-leaflet-docs.onrender.com/)
- [Plotly Pie Charts](https://plotly.com/python/pie-charts/)
- [MongoDB Manual - Users](https://www.mongodb.com/docs/manual/core/security-users/)
- *Mastering MongoDB 6.x* - Alex Giamas (2022)

---
