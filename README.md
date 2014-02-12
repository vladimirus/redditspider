redditspider
===

The spider for the front page of the Internet

===

### To build:

Prerequisites:

    * Java 7 or newer
    * Maven 3.0.4 or newer
    * MongoDb 2.4 or newer
    * Tomcat 7 or newer

Install:

    mvn clean install
    cp target/redditspider-*.war into Tomcat's webapps directory
    start your tomcat

### To run the tests:

    mvn test

<!--
Resources for Newcomers
---
  - [The Wiki](https://...)
  -->
