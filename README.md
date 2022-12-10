# File Upload Microservice - Spring Boot + MongoDB

## APIs

1. [POST] `/api/upload`        : Upload file
2. [GET]  `/api/download/{id}` : Download file of a given id 
3. [GET]  `/api/files`         : Get a list of uploaded files 

Import the postman collection's raw text below:
<details>
    <summary>Open API 3.0 Schema</summary>

```json
{"openapi":"3.0.1","info":{"title":"OpenAPI definition","version":"v0"},"servers":[{"url":"http://localhost:8080","description":"Generated server url"}],"paths":{"/api/upload":{"post":{"tags":["fileupload-application"],"operationId":"uploadFile","requestBody":{"content":{"multipart/form-data":{"schema":{"required":["file"],"type":"object","properties":{"file":{"type":"string","format":"binary"}}}}}},"responses":{"200":{"description":"OK","content":{"application/json":{"schema":{"type":"object"}}}}}}},"/api/healthcheck":{"get":{"tags":["fileupload-application"],"operationId":"echoHealth","responses":{"200":{"description":"OK","content":{"application/json":{"schema":{"type":"object"}}}}}}},"/api/files":{"get":{"tags":["fileupload-application"],"operationId":"getFiles","responses":{"200":{"description":"OK","content":{"application/json":{"schema":{"type":"object"}}}}}}},"/api/download/{id}":{"get":{"tags":["fileupload-application"],"operationId":"downloadFile","parameters":[{"name":"id","in":"path","required":true,"schema":{"type":"string"}}],"responses":{"200":{"description":"OK","content":{"*/*":{"schema":{"type":"string","format":"binary"}}}}}}}},"components":{}}
```

</details>



## Technologies used

```text
Framework    : Spring Boot 2.7.5 + Java 11
Web layer    : Spring MVC
Data layer   : Spring Data Mongo
Build system : Maven
```

- The file storage is MongoDB's [GridFS](https://www.mongodb.com/docs/manual/core/gridfs/)
- GridFS (and the microservice's) file size limit is `15MB`
- The uploaded file metadata is stored in the collection `files`

<details>
    <summary>docker-compose_mongodb.yml</summary>

```yml
version: '3.7'

x-mongo-common: &mongo-common
  logging:
    driver: "json-file"
    options:
      max-size: "10m"
      max-file: "2"
      
services:
  mongo:
    image: mongo:6-focal
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: admin123
    container_name: mongo
    restart: unless-stopped
    ports:
      - 27017:27017
    volumes:
      - mongodb:/data/db
    <<: *mongo-common

volumes:
  mongodb:
```

</details>


## References

- [File upload spring boot | Medium](https://medium.com/nerd-for-tech/file-upload-with-springboot-and-mongodb-76a8f5b9f75d)
