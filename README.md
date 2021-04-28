# aequilibrium-transformers-android

## Run project
1. Clone or download this project to your local machine
2. Open project on Android Studio
3. Run project

## Assumptions
- The app will display all transformers in the same order the API returns them
- The battle can't start without at least 1 Autobot and 1 Decepticon have been created
- Both teams will fight starting from the lower ranks to the higher ones. That way more transformers can engage in battle
- To facilitate multiple battles, the app will not delete the losers from the API's list

## Introduction

Scope of the technical assessment
This technical assessment is the first version of Transformers for Aequilibrium. High level functionalities include token retrieval, create/view/edit/delete transformers and simulate a battle between them.


### Technologies


## Development tools
Android Studio
Postman


## Language

Kotlin


## Software

Support from Android 4.4+ (API version 19)


## Hardware

Android phones running Android 4.4+



## Feature Overview

The following features have been introduced in this technical assessment:

o	Token:

 - Get token for other APIs access

o	Transformers:

 - Create transformer

 - View transformer list

 - Pull to refresh on transformers list

 - View transformer

 - Edit transformer by sending a JSON request

 - Delete transformer by its id

 - APIs error handling

 - Connection error handling

 - Simulate a battle between transformers



## Services

Services layers were implemented following MVVM model.

 
In addition, other common Android patterns and frameworks such as Coroutines, Retrofit2, Hilt, LiveData, Android Jetpack’s Navigation component, etc. are used combined with Kotlin to create a Single Activity architecture.




## API

APIs which are used to support the functionalities:

•	allspark:

(GET) https://transformers-api.firebaseapp.com/allspark - Returns a token that should be cached accordingly. For each subsequent requests to any of the '/transformers’ endpoints, the token must be attached to the request’s header. All data saved/retrieved from those endpoints will be unique to the attached token
  
•	Transformers:

(GET) https://transformers-api.firebaseapp.com/transformers - Gets a list of the Transformers created using the POST API

(GET) https://transformers-api.firebaseapp.com/transformers/{transformerId} - Gets a Transformer based on a valid ID.

(POST) https://transformers-api.firebaseapp.com/transformers/ - Creates a Transformer with the provided data in the request body (in JSON). Note that the “overall 
rating” is not returned

(PUT) https://transformers-api.firebaseapp.com/transformers - Updates an existing Transformer with the provided data in the request body, the Transformer ID must 
be valid

(DELETE) https://transformers-api.firebaseapp.com/transformers/{transformerId} - Deletes a Transformer based on the transformer ID passed in




## Unit tests

Unit tests have been added to the following view models

•	Transformers View Model

•	New Transformer View Model

•	Edit Transformer View Model

•	Shared View Model
