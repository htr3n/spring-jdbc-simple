[![Build Status](https://img.shields.io/travis/htr3n/spring-jdbc-simple/master.svg?style=flat-square)](https://travis-ci.org/htr3n/spring-jdbc-simple)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

# Spring Boot + Test-Driven CRUD JDBC 

This simple development project is a showcase contains a domain model `Customer` along with a `CustomerDao` class that accesses the database via JDBC and maps each row to an instance of `Customer`. 

The main goal is to illustrate how pure Spring JDBC (with `JdbcTemplate` and Spring Boot auto-configuration) works with minimal setting and dependencies.

Most of JDBC functionality are in `CustomerDao` CRUD methods. A test case `CustomerDaoTest` shows the usage of these CRUD methods.

## Quick Start

Simple execute `mvn clean test` at the command line or with your favorite IDE. 

