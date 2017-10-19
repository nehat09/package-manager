**package-manager** is an indexer for packages, which keeps track of package dependencies. *Packages* are executables or libraries that can be installed in a system, often via a package manager such as apt, RPM, or Homebrew. Many packages use libraries that are also made available as packages themselves, so usually a package will require you to install its dependencies before you can install it on your system.

The package-manager is a TCP server application which starts on port 8080.

Messages from clients follow this pattern:

```
<command>|<package>|<dependencies>\n
```

Where:
* `<command>` is mandatory, and is either `INDEX`, `REMOVE`, or `QUERY`
* `<package>` is mandatory, the name of the package referred to by the command, e.g. `mysql`, `openssl`, `pkg-config`, `postgresql`, etc.
* `<dependencies>` is optional, and if present it will be a comma-delimited list of packages that need to be present before `<package>` is installed. e.g. `cmake,sphinx-doc,xz`
* The message always ends with the character `\n`

Here are some sample messages:
```
INDEX|cloog|gmp,isl,pkg-config\n
INDEX|ceylon|\n
REMOVE|cloog|\n
QUERY|cloog|\n
```

The following are response codes sent by the package-manager server:
* OK - Sent if package was successfully indexed or removed(if package not present for removal, return OK). For query, if the package was present in index.
* FAIL - Sent if package was not successfully indexed or removed. For query, if package is not present in index.
* ERROR - If the command input was invalid, that is, does not follow the above pattern.

HOW TO RUN ?

IMPORTANT: You will need Java 8 Runtime and Maven installed on your system, with *java* and *mvn* on your system path.

1. Download the project to a directory, for example /package-manager.
2. Run following commands:
```
	>> cd package-manager
	>> mvn package
```
3. A JAR will be generated at /target. Run the jar as:
```
	>> java -jar target/<package-manager>.jar 
```
4. The application is running on port 8080 for accepting messages.

*How to run using the Dockerfile?*

At the same level as the Dockerfile, run the following:
```
	>> docker build -t package-manager .
	>> docker run -p <your-port>:8080 package-manager
```

**Design**

*PMApplicationServer class* - is the entry point of application. Hence, a Singleton instance. This starts the ServerSocket and listens for incoming connections.

*PMServerThread class* - is created as a new thread for every new connection.

*PMMessageHandler class* - handles the incoming messages. This is essentially the protocol of communication.

*Indexer interface* - is an interface for providing the indexing functions. i.e Index, Remove, Query.

*PackageManager class* - is the implementation of the Indexer. It has a ConcurrentHashMap of <package, Package (Object)>. The Java ConcurrentHashMap supports thread-safe highly concurrent updation and retrieval.

*Package class* - stores the state of each package, that is, name of package, list of dependencies and list of dependent packages (list of strings). 


 
