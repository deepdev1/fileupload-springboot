package com.web.fileupload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor @AllArgsConstructor
@Document(collection = "files")
public class FileModel {
    String fileName;
    String contentType;
    String gridFsId;
}
