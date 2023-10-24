# Microservices Training Course
## A bunch of information about this project

This is main folder that contains all the required resources that are prepared according to exercise requirements.
There could be some minor errors and differences to the exercises' requirements, but hey, I'm still learning ;-)
And I try my best.

# Prerequisites
As services are written in Java 17, you need to set up properly your JAVA_HOME to appropriate version of JDK.

# Structure
This folder contains sub-folders:

* **jpcommons**: Module containing commonly used java code (utility library).
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
First run `mvn -f jpcommons/ clean install` to install `jpcommons` module, which is required by the song and resource service. 
Then you can rebuild modules in any way you wish (mvn recommended).

However, to start (run everything) in the easiest way possible, just run `rebuild_and_run.sh`. 
This script is using maven to rebuild all projects, and then, docker-compose to build and start up the environment and all the services.

# Services
### Base services
Song service is at http://localhost:33312.
Resource service is at http://localhost:12333.

### Eureka server
Eureka is exposed at http://localhost:8761/

### OpenFeign
I'm using OpenFeign library for a discovery of microservices. The particular usage is in Resource service, where there is a need to connect to Song service. 
After adding binary data of a new MP3 file, Song service should be called to insert metadata of the newly added file. 
So, in Resource service, there is simple interface annotated with `@FeignClient`, and there is also a field of that type interface, which is lazily autowired during construction of the service.

I'm just calling a metod on that object, and, behind the scenes following is happening:
1. First, Feign library is calling Eureka Server, for getting the URL of Song Service,
2. And then, next call to SongService with POST data.

See it for yourself in `resource_service/src/main/java/jporebski/microservices/MainService.java` file.

# Links
[Learn Digital Platform Course](https://learn.epam.com/study/path?rootId=515800)

## Author
Jakub Porebski (jakub_porebski@epam.com)

