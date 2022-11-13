package com.web.fileupload;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.web.fileupload.model.FileModel;
import com.web.fileupload.model.FilePayload;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private Repository repository;


    public ResponseEntity<?> getFiles() {
        try {
            return ResponseEntity.ok(repository.findAll());
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            return ResponseEntity.internalServerError().body(e.getCause());
        }
    }

    public ResponseEntity<?> uploadFile(MultipartFile file) throws IOException {
        FileModel fileModel = new FileModel(file.getName(), file.getContentType(), null);

        BasicDBObject metadata = new BasicDBObject();
        metadata.put("fileName", file.getOriginalFilename());
        metadata.put("contentType", file.getContentType());
        metadata.put("size", file.getSize());

        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metadata);
        if(!id.toString().isBlank()) {
            fileModel.setGridFsId(id.toString());
            repository.save(fileModel);
            return ResponseEntity.ok(fileModel);
        }
        return ResponseEntity.internalServerError().body(fileModel);
    }

    public FilePayload downloadFile(String gridFsId) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(gridFsId)));
        FilePayload filePayload = new FilePayload();
        if(gridFSFile != null && gridFSFile.getMetadata() != null) {
            filePayload.setFileName(gridFSFile.getMetadata().get("fileName").toString());
            filePayload.setContentType(gridFSFile.getMetadata().get("contentType").toString());
            filePayload.setSize(Long.getLong(gridFSFile.getMetadata().get("size").toString()));
            filePayload.setFile(gridFsOperations.getResource(gridFSFile).getInputStream().readAllBytes());
        }
        return filePayload;
    }


}
