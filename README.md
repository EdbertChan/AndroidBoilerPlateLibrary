AndroidBoilerPlateLibrary
=========================

Boilerplate code to jumpstart common complex features in mobile apps. 

<b> TL;DR I WANT TO USE IT NOW SECTION</b><br>
Sample App (For demo purposes):<br>
https://github.com/EdbertChan/AndroidBoilerPlateLibraryDemoApp

<b>Problem Library Addresses:</b> People don't have time/want to read boring documentation. People code now, ask how it works when they make mistakes, then say the documentation is bad when they can't fix it in 10 minutes.

<b>Libraries required (no gradle provided):</b><br>
me.grantland.widget.autofittextview:<br>
https://github.com/grantland/android-autofittextview<br>

com.readystatesoftware.viewbadger:<br>
https://github.com/jgilfelt/android-viewbadger<br>


<b>Features include:</b>

1) Navigation Drawer<br>
  a) Supports login/logout detection and layout customization <br>
  b) Dynamically add/remove items and associate an action with each<br>
  
2) Pull to Refresh and Sync<br>
  a) Requests Manager to read from source and write to database<br>
  b) Single service multiple adapters<br>
  c) Cursor-backed pull-to-refresh listviews<br>

3) Database Table Manager<br>
  a) Easily add and remove tables<br>
  b) Thread safe<br>
  c) Common table query operations simplified <b>

4) Location Tracking<br>
  a) Improved tracking for both GPS and Network.

5) Google Maps<br>

6) Json parser<br>
  a) Based on Gson, can create Java classes that resemble the classes in JSON responses.<br>
  
7) GET, POST, PATCH, DELETE Socket Operator<br>
  a) Basic HTTP requests with header map support.<br>

<b> TODO:</b><br>
1) Background syncing<br>
  a) Will create a second service for that.<br>

<b>Motivation</b>

Several core features are ubiquitous across apps like Facebook and other social apps. <b> This library is 
geared towards creating a minimally viable app as quickly as possible that is also not a "glass cannon" </b> 

The second motiviation is that the <b>features should take no more than 5 minutes to learn how to use and how to implement</b>. 

If you think there are other core features that should be added, then please note what they are in your pull
request and an example of how an app can implement it (implementation should be braindead and should take no more 
than 5 minutes to learn).

*Seriously, do you know how long it takes for a new person to learn how syncing and services works? It took me a full 4 days straight of tinkering just to understand how authority/accounts work and while its nice to have those options, most people (with a startup idea) don't really care about that. They just want to sync and fetch from the background

LICENCE
The MIT License

Copyright (c) 2010-2015 Edbert Chan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
