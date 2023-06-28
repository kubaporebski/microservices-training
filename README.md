# Microservices Training Course
## A bunch of information about this project

This is a main folder that contains all the required resources that are prepared according to exercise requirements.
There could be some minor errors and differences to the exercises' requirements, but hey, I'm still learning ;-)
And I try my best.

Services are written in Java 17.

# Structure
This folder contains sub-folders:

* resource_service: The service for handling binary data (mp3 files).
* song_service: The service for handling mp3 files metadata (song title, artist, etc.).
* scripts: MySQL files for database handling (database schema creation, etc.).

Also, there are a few files here:

* rebuild_all.sh: For running maven builders of services mentioned above.
* rebuild_and_run.sh: For re-creating/rebuilding everything. This file runs `docker-compose down`, builds all using `rebuild_all.sh` script, and then runs `docker-compose up`.

And the docker related files:
* docker-compose.yaml
* .env

# How to
To start (run everything) in the easiest way possible, just run `rebuild_and_run.sh`.

# Service discovery
Song service is at http://localhost:33312.
Resource service is at http://localhost:12333.


# Links
[Learn Digital Platform Course](https://learn.epam.com/study/path?rootId=515800)

## Author
Jakub Porebski (jakub_porebski@epam.com)

