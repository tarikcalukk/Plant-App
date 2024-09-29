The application is written through the Kotlin programming language in Android Studio.
The "Plant App" application allows users to manage information about various plants, including their medical, culinary, and botanical characteristics. Users can add new plants, browse existing ones, filter them based on different criteria, and add plant images using the device's camera.

Features:

Users can input plant details such as the name, family, medical warnings, culinary uses, medicinal benefits, climate types, soil types, and flavor profiles through a dedicated data entry form. The application also enables users to capture images of plants using the device's camera and store these images in the database. Additionally, the app validates the entered data to ensure correctness, checking aspects like text length, name format, duplicate entries in culinary uses, and selected options for medicinal benefits, climate types, soil types, and flavor profiles. 
The main screen displays a list of plants using a RecyclerView, organized into various categories such as Medical, Culinary, and Botanical. Users can filter plants by category, perform quick searches based on flower color and name queries, and reset all active filters to return to the initial list of plants.
The application utilizes the Room Persistence Library for local storage of plant data, including images (Bitmaps) associated with each plant. It manages database operations such as inserting, updating, retrieving, and deleting plant records. It also facilitates adding plant images and correcting data that hasn't been verified online.
App handles communication with an external API (presumably the Trefle API) to correct plant data and retrieve plants based on specific criteria, enhancing the app's data accuracy.
