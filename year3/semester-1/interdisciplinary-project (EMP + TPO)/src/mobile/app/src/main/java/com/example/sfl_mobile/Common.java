package com.example.sfl_mobile;

import DBCore.DBAPI;

public class Common {
    /** The database connectivity api. Logging is disabled due to Read-only file system. */
    public static DBAPI dbapi = new DBAPI(false);
    public static final String connectionStringLogin = "http://164.90.163.179:8080/SFL-API/login";
    public static final String connectionStringJobs = "http://164.90.163.179:8080/SFL-API/jobs";
    public static final String connectionStringEmployees = "http://164.90.163.179:8080/SFL-API/employees";
    public static final String connectionStringStatistics = "http://164.90.163.179:8080/SFL-API/statistics";
    public static final String connectionStringJobsUpdate = "http://164.90.163.179:8080/SFL-API/jobsupdate";

    public static final String debugConnectionStringLogin = "http://10.0.2.2:17076/api/login";
    public static final String debugConnectionStringJobs = "http://10.0.2.2:17076/api/jobs";
    public static final String debugConnectionStringEmployees = "http://10.0.2.2:17076/api/employees";
    public static final String debugConnectionStringStatistics = "http://10.0.2.2:17076/api/statistics";
}
