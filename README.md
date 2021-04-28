# aequilibrium-transformers-android

Introduction

Scope of the technical assessment
This technical assessment is the first version of Transformers for Aequilibrium. High level functionalities include token retrieval, create/view/edit/delete transformers and simulate a battle between them.


Technologies


Development tools
Android Studio
Postman


Language

Kotlin


Software

Support from Android 4.4+ (API version 19)


Hardware

Android phones running Android 4.4+



Feature Overview

The following features have been introduced in this technical assessment:

•	Token

o	Get token for other APIs access

•	Transformers

o	Create transformer

o	View transformer list

o	Pull to refresh on transformers list

o	View transformer

o	Edit transformer by sending a JSON request

o	Delete transformer by its id

o	APIs error handling

o	Connection error handling

o	Simulate a battle between transformers



Services

Services layers were implemented following MVVM model.

 
In addition, other common Android patterns and frameworks such as Coroutines, Retrofit2, Hilt, LiveData, Android Jetpack’s Navigation component, etc. are used combined with Kotlin to create a Single Activity architecture.



API

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




Unit tests

Unit tests have been added to the following view models

•	Transformers View Model

•	New Transformer View Model

•	Edit Transformer View Model

•	Shared View Model
