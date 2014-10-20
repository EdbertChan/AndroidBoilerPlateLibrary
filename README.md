AndroidBoilerPlateLibrary
=========================

Boilerplate code to jumpstart apps. 

<b> TL;DR I WANT TO USE IT NOW SECTION</b><br>
Sample App (For demo purposes):<br>
https://github.com/EdbertChan/AndroidBoilerPlateLibraryDemoApp

Philosophy:<b>People don't have time/want to read boring documentation. Code now, ask how it works when you make mistakes. </b>

<b>Libraries required (no gradle provided):</b><br>
me.grantland.widget.autofittextview:<br>
https://github.com/grantland/android-autofittextview<br>

com.readystatesoftware.viewbadger:<br>
https://github.com/jgilfelt/android-viewbadger<br>


<b>Features include:</b>

1) Dynamic Navigation Bar <br>
  a) Supports login/logout detection and layout customization <br>
  b) Dynamically add/remove items <br>
  
2) Pull to Refresh and Sync<br>
  a) Requests Manager to read from source and write to database<br>
  b) Single service multiple adapters<br>
  c) Cursor-backed listviews<br>

3) Database Table Manager<br>
  a) Easily add and remove tables<br>
  b) Thread safe<br>

4) Location Tracking<br>
  a) Improved tracking for both GPS and Networkv

5) Google Maps<br>

6) Json parser<br>
  a) Based on Gson, can create Java classes that resemble the classes in JSON responses.<br>

<b> TODO:</b>
7) Background syncing
  a) Will create a second service for that.

<b>Motivation</b>

Several core features are ubiquitous across apps like Facebook and several other social apps. <b> This library is 
geared towards creating a minimally viable app as quickly as possible that is also not a "glass cannon" </b> 

The second motiviation is that the <b>features should take no more than 5 minutes to learn how to use and how to implement</b>. 

If you think there are other core features that should be added, then please note what they are in your pull
request and an example of how an app can implement it (implementation should be braindead and should take no more 
than 5 minutes to learn).

*Seriously, do you know how long it takes for a new person to learn how syncing and services works? It took me a full 4 days straight of tinkering just to understand how authority/accounts work and while its nice to have those options, most people (with a startup idea) don't really care about that. They just want to sync and fetch from the background
