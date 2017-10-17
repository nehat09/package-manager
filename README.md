package-manager is an indexer for packages, which keeps track of package dependencies. *Packages* are executables or libraries that can be installed in a system, often via a package manager such as apt, RPM, or Homebrew. Many packages use libraries that are also made available as packages themselves, so usually a package will require you to install its dependencies before you can install it on your system.

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
