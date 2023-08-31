# This Project Demostrate a DDD application with Event Sourcing

The implemented app consist of multiple microservices working together to archive a common goal

## App Description

### Article Service

This service offers fucntions for a verify user to create and publish an article.

### User Profile Service

This service offers fucntions for user profile creation. which can then be used to create and publish an article

### Comment Service

This service fucntions for a verify user to leave a comment under a published article.

### Article Management Service

This service is responsible for coordinathing communications between all the services.

Offers Sagas for coordinating user profile creation as well as article creation and comment creation.

### Starting application with Docker

Make sure you have both maven and docker installed, then run the following commands

```bash
    mvn clean package
    docker compose up --build -d
```
