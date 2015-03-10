Free Speech Watch

In this assignment you will build an application that supports a collaborative, location based, poster creation activity in honor of the 50 year anniversary of the Free Speech Movement (FSM) on the UC Berkeley campus. During the FSM, posters were often made using a cut-up (Links to an external site.) and collage technique from the Dadaists (Links to an external site.) consisting of hand drawings and text along with actual cut and pasted images (cut with real scissors and pasted with actual paste) that were then photocopied and distributed.  Even computer scientists of that day found their own rallying call...using IBM punchcards, the de facto programing interface material of the day:

ibm-punchcards.jpg

The Free Speech Watch affords a revisiting of this method using today's digital tools in the participatory spirit of the UC Berkeley FSM.  You can also interpret it as a FSM themed variation on the Exquisite corpse.

 

The purpose of the assignment is to:

deepen your understanding the Toq smart watch interactions across both the watch and phone platform through alerts and touch callbacks
develop a fluency working with drawing and touch event-handling on the Android platform
utilize the Flickr API for storing and retrieving data
understand the usage of the location API
Interaction Flow

Wearing the Toq Watch and carrying their Android phone, a user walks across Sproul Plaza, and their Toq Watch vibrates and presents a notification stating to draw the text corresponding to a random individual personally involved in the 1963 Free Speech Movement (FSM):

fsmrevised.jpg

The user then goes to his/her Toq smartwatch app and clicks on the Card corresponding to the figure in the notification. The user is now presented with this visualization:

FSM-01.jpg

Upon entering this card, the Toq smartwatch notifies the Android phone to launch the Drawing Activity and allow the user to draw on the phone.

The contents of this card  has an image of the individual, his/her name, and a symbol/text to draw in the Android application.

FSM02.jpg

Upon looking at the contents of the card, the user then draws the symbol/text on his/her Android application. This application provides a basic canvas upon which users can draw.  It has a color chooser (at least 4 colors) and the ability to erase.  The user can sketch their drawing freely.  When they are satisfied with the result, they tap a "Submit" button located at the bottom of the drawing canvas (see below).

ScreenShotUSE.png

When a drawing is submitted, it is uploaded to Flickr with the tag "cs160fsm". Following the upload, a request is again made to the Flickr API to fetch another photo, also with the tag "cs160fsm". This new image from another user is then pushed onto the Toq smartwatch as another card in the app. and the user can now see this photo on his/her Toq app:

FSM-03.jpg

As a bonus feature, we (i.e. the teaching staff) will provide a website that will animate through a composite view of the poster consisting of all user submissions. The poster will ultimately consist of contributions from all users as requested six important figures within the free speech movement.FSM Poster .png

 

You will submit five things:

your source code (shared with us via a Git repository - see end of assignment for details)
your executable (.apk) (Make sure your executable has all resources that it needs to run) (shared Git repository)
a 90 sec max narrated video demo of your app (handed in via Hackster.io)
screenshots and images (via Hackster.io)
Short overview text describing your assignment and solution (via Hackster.io)
Design Requirements

Requirements:

Phone Drawing Application (15 points)
Create an application that recognizes and responds to touch events
Using the touch data, draw points on a canvas. Continuous dragging should produce continuous lines.
User must be able to switch between drawing and erasing
User must be able to select the color for drawing (selecting between a few pre-defined colors is sufficient; full color pickers get extra credit)
Send and receive drawn images with the Flickr API
Create a new card for the Toq app. with the newly fetched image from Flickr
Toq App (25 points)
Create a "deck of cards" with notifications for the six possible events as described below
From Android application using Location API (Links to an external site.) trigger watch event when within 50 meters of Latitude: N 37.86965 and Longitude: W -122.25914 .
Randomly select and deliver one of the six notifications with vibration on the watch
Images must be 250x288 pixels to be compatible with Toq.
Allow callback from watch to Android drawing app to signal start of drawing when watch tapped
Allow watch to receive a new card and push it to the Toq smartwatch
Video: Submit a narrated video demo of your application, max of 90 seconds, that shows and describes the features.
Extra credit: Up to 5 extra credit points are available if you design and implement more features, such as:

Additional drawing tools such as shapes or different brushes
User is able to change the brush size for drawing
Use voice audio for drawing prompts
Any additional feature you feel improves the overall usability (make sure you draw our attention to that in the video you submit)
Implementation Notes for Android Drawing Application

You should first start on the Android drawing application.  Then add the Watch events, followed by the POST/GET interaction, and finally location.  For drawing, you will first need to get the user's input. You will need an OnTouchListener over the paint window to catch touch events.

There are a few ways to paint in Android. One way is to paint directly on a View object. If performance is an issue, i.e. for real-time graphics as the case here, its preferred to paint onto a Canvas object which gives the programmer more control and is usually faster. To do this, define a custom class which extends View and override its onDraw method. onDraw is where you do your drawing. onDraw will automatically be called with a Canvas object argument, so you dont need to extract the canvas from the View. In order to draw on the canvas, you will need to create one or more paint objects with the desired colors. (see the Canvas and Drawing developer guide).

To put this custom View object in the hierarchy, create an instance of it in the main onCreate() method as you would for other Views, and then make it a child of a View or Layout widget that you have included in your XML layout file using View.addView. The new view will have "fill_parent" attributes by default, and so will fill available space which is probably what you want.

Because painting in Android (like most other GUI toolkits) is "smart", the display doesn't automatically change when you paint on the canvas. Instead you have to tell android that the image really changed by calling View.invalidate().

This is by no means a brilliant design. You are free to design your own app provided that it has at least the functionality as enumerated above.

Screencast Video

What your screencast should contain:
Narrated walkthrough of the interface from the user's perspective
No implementation details
In the end, point out any extra credit points
Be CONCISE. Your video shouldn't be longer than 90 seconds for the basic functionality. You may use additional time to point out possible extra credit features.
Be prepared to do multiple takes; writing a script down first is helpful.
Only record the parts specified
Record the watch using a friend's phone or other video capture device (laptop camera)
If you implemented the location feature to trigger events include it in your video by visiting Sproul Plaza with your watch and phone.
There are different free software packages available to create narrated screencasts. Here are some options:

Windows: CamStudio
OS X: Quicktime X has screen recording.
Linux: http://www.linuxhaxor.net/?p=815
screencast-o-matic.com (Links to an external site.)
Please crop your video to only show the emulator - we don't need to see your entire desktop.

Grading criteria

Up to 40 points will be given if your application compiles, runs, contains the functionality as detailed in the instructions. We will grade the usability of your application. This means you may lose points for design problems, even if your application formally fulfills the requirements. Examples of problems that will cost points are: color choices that make the interface hard to read; inappropriate sizing and positioning, overly complicated and onerous sequences of steps required to complete tasks, lack of feedback, etc.

Up to 5 extra points will be given if you implement additions that make the application more usable or more useful (see extra credit).

Handing In Assignment 

The video, screenshots, and description will be handed in using Hackster.io.  The code will be handed using bitbucket.  Using Hackster is familiar but the use of Bitbucket may not be.  Below are instructions to help you set and submit your code using this system.

Make an account on bitbucket.org

Make sure you have git, setup instructions (Links to an external site.)

Make a repo, push code onto it

If you already have a git repo

cd /path/to/my/repo
git remote add origin git@bitbucket.org:andrewfang/test.git
git push -u origin --all # pushes up the repo and its refs for the first time

If not...

mkdir /path/to/your/project
cd /path/to/your/project
git init
git remote add origin git@bitbucket.org:andrewfang/test.git
echo "Andrew Fang" >> readme.txt # Please put your name in a readme
git add readme.txt
git commit -m 'Initial commit'
git push -u origin master

Share it with the staff account (username: cs160)

Invite users to this repo (Send invitation)



-> cs160 -> add -> share



 

Common data for use in this assignment

List of people to use in FSM notifications. You should create cards for the following people and corresponding actions.  You will need these images for use within the watch notifications.  The images below should be correctly formatted (250px x 288px) for the Toq watch screen at API. You can also download them as a single .zip at the bottom of the assignment. We will be using a Flickr API to store and retrieve images and will be using the tag "cs160fsm".

Name	Image	Draw Request
Mario Savio	 mario_savio_toq.png	Express your own view of free speech in an image
Jack Weinberg	jack_weinberg_toq.png	Draw Text: FSM
Joan Baez	joan_baez_toq.png	Draw Image of: A Megaphone
Jackie Goldberg	 jackie_goldberg_toq.png	Draw Text: SLATE
Michael Rossmann	michael_rossman_toq.png	Draw Text: Free Speech
Art Goldberg	 art_goldberg_toq.png	Draw Text: Now
