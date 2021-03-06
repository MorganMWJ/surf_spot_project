# Instructions for **Running**

The app was developed using Android Studio without an enmulator by running it directly on my device.    
The app was built to be run on a vertical mobile screen.   

Suggested steps for running:

* Clone or download zip of project from github
* Load the project into Android Studio
* Connect a mobile device with a USB cable (or use an emulator)
* Build and run the app

Note: The current app does not query with the keyword set to 'surf'. 
This is because when querying a location near Fistral beach zero results are returned when using the keyword 'surf'.
If wanting the keyword 'surf' to be included like specified in the brief, uncomment line 266 in MainActivity.kt.

  Change this line `//"keyword" to "surf",` to this `"keyword" to "surf",`.

# Instructions for **Usage**

* Click the key icon in the top right to change API key
* Click the search icon in the top right to show user inputs for searching
* Enter a decimal value in both input fields and click the 'Search' button to get results
* Click the 'Use Current Location' button to fill the inputs with your current location
* Any results returned by the Places API will be displayed below in a recycler view sorted by rating

The map icon in the top right is currently not functional.  
It will later be used to launch the map actvity for displaying locations on a map.
