# SFL-APIs

## Table of contents:
- [SFL-APIs](#sfl-apis)
  - [Table of contents:](#table-of-contents)
  - [1. Description](#1-description)
  - [2. Structure](#2-structure)
  - [3. Building](#3-building)


## 1. Description

The module SFL-APIs is a support module containing the implementations of web apis
needed by the SFL-Webapp and SFL-Mobile, and is not considered a proper module by
the developers. It contains the Java servlets that are used to serve requests made
to them by the corresponding modules. 

## 2. Structure

The module structure is as follows:
- The `mobile-api` directory contains the implementation of the API needed by the SFL-Mobile module.
- The `webapp-api` directory contains the implementation of the API needed by the SFL-Webapp module. 

## 3. Building

All of the .java files in each of the submodules need to be compiled, and their respective
.class files must be placed in the WEB-INF/classes subdirectory to be served by the Tomcat 
server. All of the library files must be placed into Tomcat's main lib directory.