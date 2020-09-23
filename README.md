# STAMP Based Investigation Tool (SBIT)

SBIT is an Investigation Tool based on the System-Theoretic Accident Model and Processes (STAMP) model. It was developed as part of the  project.

## Installation

Please follow the instructions in the [installation guide](https://drive.google.com/uc?id=1HpE-Z2-qpJ3Zahr0V95vqKhahv4TiVbn&export=download)


## Development Environment Setup

The following software needs to be installed on the system for development:

- JDK 8
- NodeJS v6 or later (can be installed using apt, in which case you need to install npm as well).
- Maven 3.x
- Apache Tomcat 8.x (or any other application server)

To start developing, first go to `app/root/src/main/webapp` and run `npm install`. This will download the necessary Node dependencies
(they are used by the UI written in ReactJS). You can check that everything is working by running `npm test`.
There are more commands for the UI, you can find them in `app/root/src/main/webapp/package.json`.

## Implementation notes
SBIT was implemented as an extension of an exiting software [Reporting Tool](https://github.com/kbss-cvut/reporting-tool). 
SBIT introduces the following new features:
- import of BPMN - supported formats is Bizagi and Adonis  
- report occurrences according STAMP principles using imported BPMN model
- extends statistical module to allow STAMP based statistics
  
## Developing the Application

To run the application locally, start JS compile watcher by running `npm start` from `app/root/src/main/webapp`. The watcher will
recompile JS whenever a change is made to the UI code.

Another possibility is to tell the watcher to put the compiled bundle directly into `target/reporting-tool-$version$/js`, where it
is automatically picked up by Tomcat.

Running the application is simple, just build it with maven and deploy the artifact into you application server.

The "dev" maven profile is intended for development use, it contains non-minified version of the UI. The "production" profile contains
minified and uglyfied version of the UI and is more suitable for deployments where performance matters.

## Storage Setup

The application uses a RDF4J (formerly known as Sesame) server as its database. The storage requires a RDF4J server deployed 
in some Java web application server. Two URLs to that for two repositories are set up in `config.properties` in the application's resource. 
The relevant properties are called _repositoryUrl_ where the opreational data is stored and _eventTypesRepository_ where the 
imported BPMN scema is stored. Path to the repository has to have the following 
pattern: `http://domain:port/rdf4j-server/repositories/repository-id`, where _repository-id_ is id of the repository.
Using a native/in-memory repository is of course also possible.

Other than the repository url, the config file also specifies URL of the Liferay portal (if present), which is used for user authentication.
If the portal is not accessible, the application will use its own use management.
OntoDriver is also specified here. Don't change it unless necessary. The last property - _eventTypeRepository_ - specifies URL of the 
repository which is queried for supported event types. For development purposes, it is suggested to leave this property value as it is.

## Live Version

A demo version of the SBIT tool is publicly available at - [SBIT](https://www.inbas.cz/inbas-reporting-tool-cast-fd).
This version allows users to freely explore the possibilities of the application and it contains example data.

It is possible to either register as a new unique user, or use the existing example user with username *guest* and password _guestpass_