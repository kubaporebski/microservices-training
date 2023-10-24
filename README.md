# Microservices Training Course
## A bunch of information about this project

This is a main folder that contains all the required resources that are prepared according to exercise requirements.
There could be some minor errors and differences to the exercises' requirements, but hey, I'm still learning ;-)
And I try my best.

# Prerequisites
As services are written in Java 17, you need to set up properly your JAVA_HOME to appropriate version of JDK.

# Structure
This folder contains sub-folders:

* **resource_service**: The service for handling binary data (mp3 files).
* **song_service**: The service for handling mp3 files metadata (song title, artist, etc.).
* **eureka_service**: The service acting as a "Service Registry".
* **scripts**: MySQL files for database handling (database schema creation, etc.).

Also, there are a few files here:

* **rebuild_all.sh**: For running maven builders of services mentioned above.
* **rebuild_and_run.sh**: For re-creating/rebuilding everything. This file runs `docker-compose down`, builds all using `rebuild_all.sh` script, and then runs `docker-compose up`.

And the docker related files:
* docker-compose.yaml
* .env

# How to
To start (run everything) in the easiest way possible, just run `rebuild_and_run.sh`. It will use maven to rebuild all projects,
  and then, docker-compose to start up the environment.

# Services
### Base services
Song service is at http://localhost:33312.
Resource service is at http://localhost:12333.

### Eureka server
Eureka is exposed at http://localhost:8761/

# Links
[Learn Digital Platform Course](https://learn.epam.com/study/path?rootId=515800)

## Author
Jakub Porebski (jakub_porebski@epam.com)

