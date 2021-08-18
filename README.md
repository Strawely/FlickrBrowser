Tech exercise for Android Assignment

For correct work of this app you need *apikey.properties* file in root project directory

Screen Content:

1. Use any approach for screen building (with Jetpack Compose that is)
2. Show 21 photos for “Electrolux” hashtag from Flickr
3. Design of your choice
4. The user should be able to interact with app while it’s fetching the photos
5. Tapping on the image will highlight the cell and the user should be able to save the image

Tech stack:

* MVVM
* Single activity
* Hilt
* Jetpack Compose
* Retrofit
* Coil

Project divided in 4 modules: [:core], [:data], [:app], [:main]

* :core - common interfaces to connect other modules
* :data - encapsulates work with data (now only with network, but in future in will be good to put
  here use-cases for file saving)
* :app - used for holding App class for Hilt
* :main - user interface and file saving logic :(

Screenshots:

###### Main screen

![Main screen](/screenshots/first.png)

###### Highlighted photo

![Highlighted photo](/screenshots/second.png)
