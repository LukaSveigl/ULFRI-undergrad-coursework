# SFL-Database

## Table of contents:
1. [Description](#1-description)
2. [Schema](#2-Schema)
3. [Procedures](#3-Procedures)


## 1. Description

The module SFL-Database contains the whole database structure. The database is implemented in MySQL 8 and hosted at [Digital Ocean](https://www.digitalocean.com/). 

## 2. Schema

The Database consists of 14 tables:
- `job`
- `job_packet`
- `job_status`
- `job_type`
- `parcel`
- `parcel_status`
- `staff`
- `staff_role`
- `branch`
- `branch_type`
- `customer`
- `city`
- `country`
- `street`

![image](https://github.com/cugalord/SFL-TPO/blob/main/docs/database/SFL_er_model.png)

## 3. Procedures
Procedures are located in `procedures.sql` and are almost exclusively used when other modules are accessing the database. Because of the route algorithm the majority of the logic is baked in the [core](https://github.com/cugalord/SFL-TPO/tree/main/src/core) module.
