# Web Calendar Manager

- This repo contains all the necessary components used to build the executable file that runs the local Web Calendar.
- For ease of use, an executable file is already provided.

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



