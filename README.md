Application Management System
===================
v1.0.0-Beta

Management system for applications.

## Environment
* Maven project
* JDK 8

## Quick start
If you are using Maven, you can run the application using mvn spring-boot:run.
Or you can build the WAR file with mvn clean package and run the WAR on Apache Tomcat server.


### Configuration
* Database configuration in file src\main\resources\application.yml

### Todo list:
* Converter for org.joda.time.LocalDateTime (pattern="dd.MM.yyyy HH:mm:ss:SSS") -
 http://www.thymeleaf.org/doc/thymeleafspring.html#model-attributes
* Error pages
* Ajax for actions
* Favicon
* i18N
* Validation
* Member filter name value after submit form
* Exception handling and presentation on views
* Notifications
* Encoding problem (polish -> UTF-8)
* 
### Licencing
Apache License 2.0
