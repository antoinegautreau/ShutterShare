# ShutterShare
ShutterShare is a mobile app built in Android Studio using Kotlin. The purpose is to be able to take a photo during an event and share it with people where you can only view it once the event is over.
The use of it is that you can add/create events(via event code) and then you can contribute to that event with your photos. 

## Getting Started

### Installing
#### Setting Up Own Your Own
* Before running the app make sure that the app is connected to Firebase. Once you have opened the ShutterShare project in Android Studio, click on the 'Tools' menu in the top bar and then click on 'Firebase'.
* For this app you will need to set up the Firebase 'Authentication', 'Cloud Storage', and 'Realtime Database'.
* The option you should choose for each of these respectively is 'Authenticate using a custom authentication system', 'Get started with Cloud Storage', and 'Get started with Realtime Database'.
* For each of these click on 'Connect to Firebase' (step 1) and add the SDKs to your project (step 2).
* During the last step, you may be redirected to a Firebase webpage. At some point during the process you may need to create a Firebase account if you don't already have one.
* Once 'Connected' to the ShutterShare Project in Firebase, you will need to go to https://console.firebase.google.com/ and select the ShutterShare project. In here you will need to add 'Authentication', 'Storage', and 'Realtime Database'. These can be found in the 'Build' drop-down under 'Product categories'. Click on the button that says 'Get Started' for each of these and follow the steps. For cases where you are given the choice between a Test environment and/or Production environment, the Test environment is sufficient to run the app. For 'Authentication' select the 'Email/Password' sign-in provider and enable the first option ('Email/Password'). You can leave 'Email link (passwordless sign-in') disabled.
* Now you should be good to run the app in Android Studio!
* If this doesn't work, try going into the 'Project Setting' (click the gear next to 'Project Overview' and then click 'Project Settings'), scroll down and download the 'google-services.json'. Once downloaded, drag this file onto the top-level 'app' directory of the ShutterShare project in Android studio and drop it. The SDK dependencies should now be properly set up if they weren't already before. Now you can run the app.

#### Using A "google-services.json" File
* If you already have a google-services.json file for the ShutterShare app, then simply drag this file onto the 'app' top-level directory of the ShutterShare project inside of Android Studio.
* Now you should be good to run the app in Android Studio!

### Executing the program
* When the app starts you will be presented with a login screen. Make sure to create an account first by clicking on the 'Not Registered Yet? Sign Up!' at the bottom of the page. This will bring you to a account registration page. Make an account and then login using the credentials you just created the account with.

## Authors

Antoine Gautreau
@agautre2UNB3035

Thomas Gautreau
@tgautreaUNB3035

Harrison Hargrove
@hharg20
