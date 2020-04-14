## Description
Spring REST project where you can upload file with ATM repairs data and get some statistics from this file. All business logic realized by **Java**, without using SQL.

File examples are located in test -> java -> resources -> files
## Installation
* Set up JDK 8 or higher
* Create database "atm_repair" in PostgreSQL and set up login and password in src -> main -> resources -> "application.properties" file
* Project uses lombok. You have to install lombok plugin and enable annotation processing: Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> click "Enable annotation processing"
* Start application: Maven -> Plugins -> spring-boot -> spring-boot:run
* To use frontend, find "index.html" file in src -> main -> resources -> view directory and open it in browser