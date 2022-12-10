# File Upload Microservice - Spring Boot + MongoDB

Microservice for multi-part file upload with MongoDB's GridFS as file store.

## REST API

```markdown
+---------------------+--------------+---------------------+----------------+------------------------------------------------------+
| URI                 | HTTP Method  | Resource Operation  | Response Code  | Description                                          |
+=====================+==============+=====================+================+======================================================+
| /api/upload         | POST         | Create              | 201            | Upload file as multipart content                     |
| /api/download/{id}  | GET          | Fetch               | 200            | Download the file as content disposition attachment  |
| /api/files          | GET          | Fetch               | 200            | Get a list of uploaded files                         |
+---------------------+--------------+---------------------+----------------+------------------------------------------------------+
```

<details>
    <summary>Open doc API definitions (import as postman collection)</summary>

```json
{"openapi":"3.0.1","info":{"title":"OpenAPI definition","version":"v0"},"servers":[{"url":"http://localhost:8080","description":"Generated server url"}],"paths":{"/api/upload":{"post":{"tags":["fileupload-application"],"operationId":"uploadFile","requestBody":{"content":{"multipart/form-data":{"schema":{"required":["file"],"type":"object","properties":{"file":{"type":"string","format":"binary"}}}}}},"responses":{"200":{"description":"OK","content":{"application/json":{"schema":{"type":"object"}}}}}}},"/api/healthcheck":{"get":{"tags":["fileupload-application"],"operationId":"echoHealth","responses":{"200":{"description":"OK","content":{"application/json":{"schema":{"type":"object"}}}}}}},"/api/files":{"get":{"tags":["fileupload-application"],"operationId":"getFiles","responses":{"200":{"description":"OK","content":{"application/json":{"schema":{"type":"object"}}}}}}},"/api/download/{id}":{"get":{"tags":["fileupload-application"],"operationId":"downloadFile","parameters":[{"name":"id","in":"path","required":true,"schema":{"type":"string"}}],"responses":{"200":{"description":"OK","content":{"*/*":{"schema":{"type":"string","format":"binary"}}}}}}}},"components":{}}
```

</details>



## Implementation Details

1. Upload
   
   - `org.springframework.web.multipart.MultipartFile` - file
   - `org.springframework.data.mongodb.gridfs.GridFsTemplate` - MongoDB GridFS operations
     - ```
       public org.bson.types.ObjectId store( java.io.InputStream content,
                                             @Nullable String filename,
                                             @Nullable String contentType,
                                             @Nullable Object metadata )
       ```
       
2. Download
   
   - ```
     public com.mongodb.client.gridfs.model.GridFSFile findOne( org.springframework.data.mongodb.core.query.Query query )
     ``` 
   - Response header: `header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + <filename> + "\"")`
   - Response body: `ByteArrayResource(filecontent)`



- The file storage is MongoDB's [GridFS](https://www.mongodb.com/docs/manual/core/gridfs/)
- GridFS (and the microservice's) file size limit is `15MB`
- The uploaded file metadata is stored in the collection `files`

## MongoDB Installation

<details>
    <summary>docker-compose.yml</summary>

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

`docker-compose up -d`


## References

- [File upload spring boot | Medium](https://medium.com/nerd-for-tech/file-upload-with-springboot-and-mongodb-76a8f5b9f75d)
