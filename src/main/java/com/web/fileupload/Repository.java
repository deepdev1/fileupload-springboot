package com.web.fileupload;

import com.web.fileupload.model.FileModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Repository extends MongoRepository<FileModel, String> {
}
