# SFL-Core

## Table of contents:
1. [Description](#1-description)
2. [Structure](#2-structure)
3. [Building](#3-building)
4. [Extending](#4-extending)


## 1. Description

The module SFL-Core contains the implementation of the lowest level
core functionality of the SFL project. It contains classes that are 
used to fetch data from configs, logging, utilities, connecting to 
the database and a public facing API that contains the methods used 
in other sections of the project.

## 2. Structure

The module structure is as follows:
- The `ConfigLoader` directory contains the implementation of fetching data 
  and storing it into a DBData object:
  - The `ConfigLoader.java` file contains the implementation of the 
    config loader.
  - The `DBData.java` file contains the POD (plain-old-data) implementation
    of the database url parameters, which are filled by the ConfigLoader.
- The `DBCore` directory contains the implementation of data connectivity and
  the public facing API:
  - The `DBCore.java` file contains the backend implementation of a database
    connection manager.
  - The `DBAPI.java` file contains the public facing API of a database 
    manipulation tool.
- The `Utils` directory contains the implementation of the utilities and
  logging package, which can be used in multiple parts of the project:
  - The `Logger.java` file contains the implementation of the Logger.
  - The `Utils.java` file contains the utilities used by other parts of 
    the project.
- The `Data` directory contains the POD implementations of database tables, 
  or constructs that are returned by the prepared or callable statements
- The `config.xml` file contains the database url parameters, such as ip, 
  port and schema name.
- The `system.log` file contains the log of messages from the SFL-Core module.

## 3. Building

The module SFL-Core should be pre-built into a .jar file, to ensure 
better portability. The .jar is created using the IntelliJ artifact creator,
and can be included into other projects either via GUI or manually via commands
`javac -cp .;core.jar <.java-files>` and run with 
`java -cp .;core.jar main.java`.

## 4. Extending

To extend the functionality of this module modify only the DBAPI.java or 
Utils.java files. Modify the DBAPI.java file by adding new methods under 
the `// PREPARED STATEMENTS` or `// CALLABLE STATEMENTs` comment and 
recompile the jar. Of course you also need to add the statements to either
`precompileStatements()` or `precompileCallables()` methods.
