# INBAS Reporting Tool

Safety occurrence reporting tool for the INBAS project.

## Development Environment Setup

The following software needs to be installed on the system for development:

- JDK 8
- NodeJS v4 or later (can be installed using apt, in which case you need to install npm as well). To upgrade from older versions, see e.g. [https://davidwalsh.name/upgrade-nodejs](https://davidwalsh.name/upgrade-nodejs)
- Maven
- Apache Tomcat (or any other application server)

To start developing, first go to `app/root/src/main/webapp` and run `npm install`. This will download the necessary Node dependencies
(they are used by the UI written in ReactJS). You can check that everything is working by running `npm test`.
There are more commands for the UI, you can find them in `app/root/src/main/webapp/package.json`.

## Developing the Application

To run the application locally, start JS compile watcher by running `npm start` from `app/root/src/main/webapp`. The watcher will
recompile JS whenever a change is made to the UI code. Because IDEA and Tomcat sometimes have trouble spotting the updated JS bundle,
it is good to do a refresh (Ctrl + Alt + Y) of the `bundle.js` file. Then update the server resources as usual (by using Ctrl + F10).

Another possibility is to tell the watcher to put the compiled bundle directly into `target/inbas-reporting-tool-$version$/js`, where it
is automatically picked up by Tomcat.

Running the application is simple, just build it with maven and deploy the artifact into you application server.

Use the "dev" Maven profile.

## Storage Setup

The application uses a Sesame native storage as its database. The storage requires a folder somewhere on the file system. Path to that
folder is set up in `config.properties` in the application's resource. The relevant property is called _repositoryUrl_. Path to the
repository has to have the following pattern: `/some/path/repositories/repository-id`, where _repository-id_ is id of the repository.
Using a remote repository is of course also possible.

Other than the repository url, the config file also specifies URL of the Liferay portal (if present), which is used for user authentication.
If the portal is not accessible, the application will use its own use management.
OntoDriver is also specified here. Don't change it unless necessary. The last property - _eventTypeRepository_ - specifies URL of the 
repository which is queried for supported event types. For development purposes, it is suggested to leave this property value as it is.

## Some Notes on Development

- When working on the UI, do not forget to localize it! Message bundle can be found in folder i18n. 
    To see how to use the localized bundle, look into some of the existing components, e.g. `MainView`).
- DTO <-> Entity transformation is done using the _MapStruct_ library. It is executed as a maven plugin during build. See
    `cz.cvut.kbss.inbas.audit.rest.dto.mapper.ReportMapper` for more info.
- Options for the UI - remotely loaded options supersede locally loaded options. So the algorithm works like this:
    - If a local file with options exists (see for example those in `src/main/resources/option`), it is used,
    - If a matching query file exists (in `src/main/resources/query` or corresponding profile directories), it supersedes the local file and is used.
    - See `OptionsServiceImpl`.
- JOPA is set up in `cz.cvut.kbss.inbas.audit.persistence.PersistenceFactory`.
- Write tests!

## Deployment

Building on CI server and deployment is described in detail at [https://www.inbas.cz/group/guest/external-data-sources/-/wiki/Main/Reporting+Tool+Deployment](https://www.inbas.cz/group/guest/external-data-sources/-/wiki/Main/Reporting+Tool+Deployment)
