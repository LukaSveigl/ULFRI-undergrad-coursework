# SFL-Mobile

## Table of contents:
1. [Description](#1-description)
2. [Structure](#2-structure)
3. [Building](#3-building)
4. [Appearance](#4-appearance)


## 1. Description

The module SFL-Mobile contains the implementation of the mobile android 
application for the SFL project, which is intended for warehouse managers,
warehouse agents, delivery and international drivers. It offers an overview
of staff jobs, and in the case of agents and drivers, also scanning parcel
QR codes to complete their jobs.

## 2. Structure

The module structure is as follows:
- Any important source files can be found in the `app/src/main directory`:
    - The `res` directory contains the resource files needed by the module.
    - The `java/com/example/sfl_mobile` directory contains the source files:
        - The `Common.java` file contains all common data used by the module.
        - The `JobsActivity.java` file contains the job overview activity.
        - The `ManagerActivity.java` file contains the warehouse manager's warehouse statistics.
        - The `EmployeesActivity.java` file contains the list of employees working in the manager's warehouse. 
        - The `EmployeeJobsActivity.java` file contains the list of jobs for a particular employee.
        - The `PictureBarcodeActivity.java` file contains the implementation of a barcode scanner.

## 3. Building

The module `SFL-Mobile` should be built into an `.apk` file using Android Studio.
A working apk file is provided in the `app/build/outputs/apk/debug` directory.

## 4. Appearance
[UI screenshots](https://github.com/cugalord/SFL-TPO/tree/main/docs/mobile)
![activity](https://github.com/cugalord/SFL-TPO/blob/main/docs/mobile/SFL_mobile_activity.png)


