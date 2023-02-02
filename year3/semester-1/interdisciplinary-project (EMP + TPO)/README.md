# SFL-TPO

A simple implementation of an information system for a parcel company.

## Table of contents
- [1. Description](1.Description)
- [2. Consortium](2.Consortium)
- [3. Project structure](3.Project_structure)
- [4. Building and running](4.Building_and_running)
- [5. Demonstration](5.Demonstration)

## 1. Description

The information system consists of 3 user facing modules, connected by the core, which connects them to the database. The 3 modules are the native application, a web application, and an android application.

## 2. Consortium

- Nejc Vrčon Zupan
- Luka Šveigl
- Gašper Lavrih
- Bernard Kuchler

## 3. Project structure

This project is structured in the following way:
- The `docs` directory contains the documentation of this project.
- The `src` directory contains the source code of the project.
  - The `core` directory contains the implementation of the core functionality of this project, such as database connectivity, logging, utilities.
  - The `database` directory contains the implementation of the database.
  - The `native` directory contains the implementation of the native application, intended for employees in the company.
  - The `webapp` directory contains the implementation of the web application, intended for users of the company.
  - The `mobile` directory contains the implementation of the android application, intended for delivery drivers in the company.

## 4. Building and running

The directions for how to build and run these modules are located in the README.md in each subdirectory.

## 5. Demonstration