AndroidBoilerPlateLibrary
=========================

Boilerplate code to jumpstart apps. 

Idea: Several core features are ubiquitous across apps like Facebook and several other social apps. This library is 
geared towards creating a minimally viable app as quickly as possible that is also not a "glass cannon" (EG: not 
so unstable that it will break often but will last long enough to see if it will be a viable product in the long run or
if the app is small enough it doesn't need extensive work). 

The second idea is that the feature should take no more than 5 minutes to learn how to use and how to implement. 
Learn by example because people don't read (including me :3).

If you think there are other core features that should be added, then please note what they are in your pull
request and an example of how an app can implement it (implementation should be braindead and should take no more 
than 5 minutes to learn).

Features include:

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
  

Sample App (For demo purposes):<br>

Libraries required (no gradle provided):<br>
me.grantland.widget.autofittextview:<br>
https://github.com/grantland/android-autofittextview<br>

com.readystatesoftware.viewbadger:<br>
https://github.com/jgilfelt/android-viewbadger<br>
