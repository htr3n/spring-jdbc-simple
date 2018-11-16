[![Build Status](https://img.shields.io/travis/htr3n/spring-jdbc-simple/master.svg?style=flat-square)](https://travis-ci.org/htr3n/spring-jdbc-simple)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

# Spring Boot + Test-Driven CRUD JDBC 

This simple development project is a showcase contains a domain model [`Customer`](https://github.com/htr3n/spring-jdbc-simple/blob/master/src/main/java/io/github/htr3n/springjdbcsimple/data/Customer.java) along with a [`CustomerDao`](https://github.com/htr3n/spring-jdbc-simple/blob/master/src/main/java/io/github/htr3n/springjdbcsimple/data/CustomerDao.java) class that accesses the database via JDBC and maps each row to an instance of `Customer`. 

The main goal is to illustrate how Spring JDBC (with `JdbcTemplate` and Spring Boot auto-configuration) works with minimal setting and dependencies.

Most of JDBC functionality are in `CustomerDao` CRUD methods. A test case [`CustomerDaoTest`](https://github.com/htr3n/spring-jdbc-simple/blob/master/src/test/java/io/github/htr3n/springjdbcsimple/data/CustomerDaoTest.java) shows the usage of these CRUD methods.

## Quick Start

Simple execute `mvn clean test` at the command line or with your favorite IDE. 

