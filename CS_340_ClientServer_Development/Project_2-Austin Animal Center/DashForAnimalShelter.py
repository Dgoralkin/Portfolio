# Set up the Jupyter version of Dash
# Configure the necessary Python module import libraries

from dash import Dash, dash_table, html, dcc
from dash.dependencies import Input, Output, State
import urllib.parse
from pymongo.errors import OperationFailure
import dash_leaflet as dl

# Configure the plotting routines
import pandas as pd
import dash_bootstrap_components as dbc
import base64
import plotly.express as px

# Local application import CRUD Python module
from AnimalShelter import AnimalShelter

###########################
# Data Manipulation / Model
###########################

###########################
# ReadWrite predefined Username credentials
###########################
# username = "aacuser"
# password = "SNHU1234"
# host = "localhost"
# port = 27017
# database = "AAC"
# collection = "animals"

#########################
# Dashboard Layout / View
#########################
app = Dash(external_stylesheets=[dbc.themes.BOOTSTRAP])

# Format and Add in Grazioso Salvare’s logo
image_filename = 'Grazioso Salvare Logo.png'
encoded_image = base64.b64encode(open(image_filename, 'rb').read())

# Define the layout of the dashboard.
app.layout = dbc.Container([
    # Highlight the selected cell.
    dbc.Label(),

    # left-aligning the text
    html.Div([dash_table.DataTable(style_cell={'textAlign': 'left'})]),

    # Header div
    html.Div(id='hidden-div', style={'display': 'none'}),

    # Wrap logo + title together in a div with a target URL that opens in a new tab.
    html.Div([
        html.A(
            href='https://www.snhu.edu/',
            target='_blank',
            children=html.Img(
                src='data:image/png;base64,{}'.format(encoded_image.decode()),
                style={
                    'height': '80px',
                    'margin-right': '20px'
                }
            )
        ),
        html.H1(
            'SNHU CS-340 AnimalShelter Dashboard - By Daniel Gorelkin',
            style={
                'color': 'blue',
                'fontSize': 24,
                'margin': 0,
                'fontWeight': 'bold',
                'textDecoration': 'underline',
            }
        )
    ], style={
        'display': 'flex',
        'alignItems': 'center',
        'justifyContent': 'center',
        'marginBottom': '5px'
    }),

    html.Hr(),

    # Define the DB connection div form.
    html.Div([
        html.H2(
            'Connect to MongoDB, AAC Database:',
            style={'color': 'green', 'fontSize': 16, 'fontWeight': 'bold', 'textDecoration': 'underline'}
        ),
        html.H3(
            'Enter username and password to connect to MongoDB.',
            style={'color': 'black', 'fontSize': 12, 'fontWeight': 'italic'}
        )
    ]),

    # Set up an Input field for the username div.
    dcc.Input(
        id="input_user".format("text"),
        type="text",
        placeholder="Enter {}".format("Username"),
        # TODO: uncomment -> value="aacuser" to turn user authentication off.
        # value="aacuser"
    ),

    # Set up an Input field for the password while masking the user input on the screen.
    dcc.Input(
        id="input_passwd".format("password"),
        type="password",
        placeholder="Enter {}".format("password"),
        # TODO: uncomment -> value="SNHU1234" to turn user authentication off.
        # value="SNHU1234"),
    ),

    # Create a button labeled 'Submit' to run the @callback and authenticate the user.
    html.Button('Submit', id='submit-val', n_clicks=0),

    # Response status div to display the status of the connection attempt.
    html.Div(id='status-message', style={'color': 'red', 'marginBottom': '20px'}),

    # Define the Checklist select div
    html.Div([
        html.Label("✔️ Select Rescue Type(s) to filter results:"),
        dcc.Checklist(
            id='rescue-options',
            options=[
                {'label': 'Water Rescue', 'value': 'Water Rescue'},
                {'label': 'Mountain or Wilderness Rescue', 'value': 'Mountain Rescue'},
                {'label': 'Disaster or Individual Tracking', 'value': 'Disaster Rescue'},
            ],
            # TODO: Choose optional default values to display in the table.
            # value=['Water Rescue', 'Mountain Rescue', 'Disaster Rescue'],
            inputStyle={"margin-right": "5px"},
            style={'margin': '10px'},
            labelStyle={'display': 'inline-block', 'margin-right': '10px'}
        )
    ]),

    # Define table response div
    dash_table.DataTable(
        # Define a table's body for each document field.
        id='datatable-id',
        style_table={'overflowX': 'auto'},
        style_cell={
            'textAlign': 'left',
            'padding': '5px',
            'fontSize': '13px',
        },
        style_data={
            'backgroundColor': 'rgb(50, 50, 50)',
            'color': 'white'
        },
        style_header={
            'backgroundColor': 'black',
            'color': 'white',
            'fontWeight': 'bold',
            'fontSize': '15px',
        },
        columns=[
            {"name": "rec_num", "id": "rec_num", "deletable": False, "selectable": True},
            {"name": "age_upon_outcome", "id": "age_upon_outcome", "deletable": False, "selectable": True},
            {"name": "animal_id", "id": "animal_id", "deletable": False, "selectable": True},
            {"name": "animal_type", "id": "animal_type", "deletable": False, "selectable": True},
            {"name": "breed", "id": "breed", "deletable": False, "selectable": True},
            {"name": "color", "id": "color", "deletable": False, "selectable": True},
            {"name": "date_of_birth", "id": "date_of_birth", "deletable": False, "selectable": True},
            {"name": "datetime", "id": "datetime", "deletable": False, "selectable": True},
            {"name": "monthyear", "id": "monthyear", "deletable": False, "selectable": True},
            {"name": "name", "id": "name", "deletable": False, "selectable": True},
            {"name": "outcome_subtype", "id": "outcome_subtype", "deletable": False, "selectable": True},
            {"name": "outcome_type", "id": "outcome_type", "deletable": False, "selectable": True},
            {"name": "sex_upon_outcome", "id": "sex_upon_outcome", "deletable": False, "selectable": True},
            {"name": "age_upon_outcome_in_weeks", "id": "age_upon_outcome_in_weeks",
             "deletable": False, "selectable": True},
            {"name": "location_lat", "id": "location_lat", "deletable": False, "selectable": True},
            {"name": "location_long", "id": "location_long", "deletable": False, "selectable": True},
        ],
        data=[],  # Display empty table if Exception raised or results is None
        editable=False,
        filter_action="native",
        sort_action="native",
        sort_mode="multi",
        column_selectable=False,
        row_selectable="single",
        row_deletable=False,
        selected_columns=[],
        selected_rows=[0],
        page_action="native",
        page_current=0,
        page_size=10
    ),

    # Status filter div to display the search and filter result for the Dash DataTable filter box.
    html.Div(id='DataTable-filter-message', style={'color': 'red', 'marginBottom': '20px'}),

    html.Hr(),

    # Define the div for the map and graph to display geolocation and pie chart side-by-side.
    html.Div(className='row',
             style={'display': 'flex',
                    'width': '100%'},
             children=[
                 # Map div
                 html.Div(
                     id='graph-id',
                     className='col s12 m6',

                 ),
                 # Pie chart div
                 html.Div(
                     id='map-id',
                     className='col s12 m6',
                 )
             ]
             ),
], style={
    'maxHeight': '100vh',
    'overflowY': 'auto',
    'padding': '5px'
})


#############################################
# Interaction Between Components / Controller
# Define the callback to read input and update the output-blocks.
#############################################
@app.callback(
    [Output('status-message', 'children'),   # Output prompt for the status-message.
     Output('datatable-id', 'data')],               # Update the table with the results.
    [Input('submit-val', 'n_clicks'),               # Input button to trigger the submit-val.
     Input('rescue-options', 'value')],             # Input checkbox for the filter-options.
    [State('input_user', 'value'),                  # Input prompt for the username (static)
     State('input_passwd', 'value')]                # Input prompt for the password (static)
)
# Define App's logic.
def update_table(connect, selected_rescues, username, password):
    # Authenticate user input and attempt to connect to MongoDB.
    if connect > 0:
        # Check for empty username and password fields and force login
        if not username or not password:
            return "❌ Username or Password cannot be empty.", None

        # Store successfully read credentials.
        username = urllib.parse.quote_plus(username)
        password = urllib.parse.quote_plus(password)

        try:
            # Attempt DB connection callback.
            db = AnimalShelter(username, password)

            # TODO: DB Index recommendation:
            #  db.animals.createIndex(
            #  { breed: 1, sex_upon_outcome: 1, age_upon_outcome_in_weeks:1 },
            #  { name: "breed_sex_age" })

            # Define search queries Dict with a key:value pairs parameters:
            # Define rescue queries
            queries = {
                # Query to find Water Rescue animals.
                'Water Rescue': {
                    'breed': {'$in': ['Labrador Retriever Mix', 'Chesapeake Bay Retriever', 'Newfoundland']},
                    'sex_upon_outcome': 'Intact Female',
                    'age_upon_outcome_in_weeks': {"$gte": 26, "$lte": 156}
                },
                # Query to find Mountain or Wilderness Rescue animals.
                'Mountain Rescue': {
                    'breed': {'$in': ['German Shepherd', 'Alaskan Malamute', 'Old English Sheepdog',
                                      'Siberian Husky', 'Rottweiler']},
                    'sex_upon_outcome': 'Intact Male',
                    'age_upon_outcome_in_weeks': {"$gte": 26, "$lte": 156}
                },
                # Query to find Disaster or Individual Tracking Rescue animals.
                'Disaster Rescue': {
                    'breed': {'$in': ['Doberman Pinscher', 'German Shepherd', 'Golden Retriever',
                                      'Bloodhound', 'Rottweiler']},
                    'sex_upon_outcome': 'Intact Male',
                    'age_upon_outcome_in_weeks': {"$gte": 20, "$lte": 300}
                }
            }

            # Construct selected queries
            # Default case when no checkbox is selected.
            if not selected_rescues:
                final_query = {}
            # Case when checkbox(s) is selected.
            else:
                # Merge all selected queries from the queries Dict by the selected_rescues checkbox request.
                selected_queries = [queries[key] for key in selected_rescues if key in queries]
                # Select the first query if only one checkbox is selected.
                if len(selected_queries) == 1:
                    final_query = selected_queries[0]
                # Combine multiple selected queries using Mongo's $or operator
                else:
                    final_query = {'$or': selected_queries}

            # Run the query and return results
            df = pd.DataFrame.from_records(db.read(final_query))
            if not df.empty:
                # Drop the '_id' column to prevent an invalid object type of 'ObjectID'
                if '_id' in df.columns:
                    df.drop(columns=['_id'], inplace=True)

                # Results found, return success message and data.
                return "✅ Data loaded successfully.", df.to_dict('records')
            else:
                # No results found, return an error message and empty List.
                return "⚠️ Can't find any records that corresponds to the query", []

        # Exception handler to return invalid DB connectivity prompt and empty List.
        except OperationFailure as oe:
            return (f"❌ {oe.details.get('errmsg', str(oe))}\n\tUsername or Password is incorrect!",
                    None)

        # General exception handler
        except Exception as e:
            return f"❌ Unexpected error: {str(e)}", None

    # Request user login with a username and password prompt and return an empty List.
    return "📢 Enter Username and Password to login!", None


# Define the callback to update the map-id div with the selected row.
@app.callback(
    [Output('map-id', 'children'),               # Display updated map div.
     Output('DataTable-filter-message', 'children')],   # Display an internal filter message.
    [Input('datatable-id', 'data'),                     # Full DB data input
     Input('datatable-id', 'derived_virtual_data'),     # Internal table data filter input.
     Input('datatable-id', 'selected_rows')]            # User selected row input.
)
# Define Map's logic.
def update_map(data, viewdata, index):

    # App just started or no DB results case.
    # Check if the table has data from DB.
    if not data or len(data) == 0:
        # print("No data loaded in update_map. App just started case.")
        return None, None

    # User filtered to zero rows case.
    # If DB Data exists and filtered data is empty.
    if viewdata is not None and len(viewdata) == 0:
        return None, html.Div("⚠️ No matching animals from table filter. "
                              "Refine your search and try again.")

    # Default center coordinates (Austin, TX)
    default_center = [30.75, -97.48]

    # Display first raw from the loaded data by default.
    dff = pd.DataFrame.from_dict(viewdata)
    row = index[0]

    # If the index is out of bounds, display the default location.
    if row >= len(dff):
        return [
            dl.Map(
                style={'width': '800px', 'height': '350px'},
                center=default_center, zoom=8.5,
                children=[
                    dl.TileLayer(id="base-layer-id"),
                    dl.Marker(
                        id=f"marker-{row}",
                        position=default_center,
                        children=[
                            dl.Tooltip("No Animal Selected"),
                            dl.Popup([
                                html.H1("Austin Animal Center Location"),
                                html.P("Choose an animal.")
                            ])
                        ])
                ])
        ], None

    # Update the animal name for the tooltip popup if the name is empty.
    animal_name = dff.iloc[index[0], 9]
    animal_name = animal_name if pd.notna(animal_name) and animal_name else "Unnamed Animal"

    # Update the map with the selected row.
    return [
        dl.Map(
            style={'width': '600px', 'height': '450px'},
            center=[30.75, -97.48], zoom=8.5,
            children=[
                dl.TileLayer(id="base-layer-id"),

                # Marker with tool tip and popup
                dl.Marker(
                    id=f"marker-{row}",                                 # Trigger internal refresh for the marker.
                    position=[dff.iloc[row, 13], dff.iloc[row, 14]],    # The grid-coordinates for the map.
                    children=[
                        dl.Tooltip(dff.iloc[row, 4]),                   # Reads and displays animal breed
                        dl.Popup([
                            html.H3("Animal Name:"),
                            html.P(animal_name)                         # Reads and displays animal name
                        ])
                    ])
            ])
    ], None


# Define the callback to update the graph-id div with
# the breeds of animal based on quantity represented in the data table
@app.callback(
    Output('graph-id', "children"),
    [Input('datatable-id', "derived_virtual_data")])
def update_graph(viewdata):
    # Hide the pie chart if no data is loaded.
    if not viewdata:
        # print("No data loaded in update_graph.")
        return None

    # Store results from the loaded data.
    dff = pd.DataFrame.from_dict(viewdata)

    # Update the pie chart with the filtered results.
    fig = px.pie(dff, names='breed', title='Available Animals')

    # Remove trailing results from the pie chart output.
    fig.update_traces(textposition='inside')

    return [
        dcc.Graph(figure=fig)
    ]

# app.run(debug=True)
if __name__ == '__main__':
    app.run(debug=True)
