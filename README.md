# fakeDataGeneration
Using Slick to connect to hosted database and insert dummy data into some tables

Primary use case is for multiple tables (`ActivityLogs` and `Proposals`) on Postgres to be populated with generated data. Can be further enchanced with a generic implementation of populating any table given it's schema (or properly represented case class)

This can be done to provision data as part of the staging process for testing and/or debugging any database connections for a given application

This code was used when initially setting up the plumbing for an [ETL project](https://github.com/tanjinP/zero2etl)
