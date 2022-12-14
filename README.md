# Web Calendar Manager

- This repo contains all the necessary components used to build the executable file that runs the local Web Calendar.

The webcal link must be provided by the user.



## How it started

This Web Calendar project was started as an evaluation requirement for a Software Engineering class as a part of the Telecommunications and Computer Engineering course.

Using the Scrum methodology, the repo members aimed to build a compelling and useful way for students to consult their calendars using their institution-provided webcal link in order to manage their daily events.

It also aimed to allow future users to schedule meetings with other users by cross-referencing multiple calendars and analyzing their availability for one-time or regular meetings.



## What it does

As planned, the Web Calendar can take in a given webcal link, which it can internally interpret in order to give the user a clean, easy to use interface to check their daily activities.

Multiple webcal links may be provided sequentially to the server through the labelled link at the bottom of the page.

This, in turn, allows the user to use a checklist on the right side of the page to cross the information of each calendar and view which days are recommended for a common event (henceforth referred as a meeting).

Multiple calendars may be selected to accommodate more members that wish to partake in said meeting.



### Server-side

Using the Java baseline, it builds a local server, opening up an appropriate port, to view the page that will server as the User-Interface for all calendar management.

When a new web calendar insertion occurs, the server parses the information obtained from it into a .json file, both for ease of use in JavaScript, and for a more visually distinguishable way of analyzing the information of a given calendar when debugging.

Upon parsing the info from the webcal link, the server waits for a request from the web page, after which it provides it with the requested information for the display (e.g. the events of a given day upon it being clicked). 



### Web-side

With a combination of HTML, CSS and JavaScript, the Web Calendar starts with a display of a monthly calendar of the current month (based on the local date of your device), which is able to be scrolled from month to month by clicking on the arrows on each side of it.

A hyperlink is displayed below the calendar which allows for the user to input their desired webcal link. This link is then processed on the back-end to give back to the server objects capable of displaying the events of said user. 

Upon a webcal link being inserted, if no errors occur, the name associated with the calendar will be displayed to the right side of the calendar, alongside a combo box. This combo box can be used when more webcal links are inserted in order to display on the calendar, through color-coding, the best dates for a meeting to be set. Green are the recommended days for a meeting, Yellow are the less optimal, but still potentially good days to set a meeting, and Red is not recommended date for a meeting to be set.

The colors for each day are updated live when the user checks more of the calendar combo boxes available (expected behaviour is for more green to be displayed when less calendars are selected, as there are less conflicting events to work with).

On the top-right corner of the calendar a "Find Meeting" button can be used to find the best timeslot for a meeting to be had, given a start-date and an end-date provided by the user to search for said timeslot.



## What is missing

- Planned for the project were functionalities such as being able to insert new events for each user, as well as finding a form of periodic meetings (such as weekly, bi-weekly, etc.). These functionalities, due to various circumstances, weren't able to be implemented in time for release.

- Some features are also only partially complete, such as the FindMeeting feature, which ill-prepared for a potential lack of a timeslot for a meeting, as well as any kind of error server-side. 


