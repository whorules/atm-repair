## Description
Spring REST project where you can upload file with ATM repairs data
## Installation
* Set up JDK 8 or higher
* Create database "atm_repair" in PostgreSQL and set up login and password in src -> main -> resources -> "application.properties" file
* Project uses lombok. You have to enable annotation processing: Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> click "Enable annotation processing"
* Start application: Maven -> Plugins -> spring-boot -> spring-boot:run
* Find "index.html" file in src -> main -> resources -> view directory and open it in browser