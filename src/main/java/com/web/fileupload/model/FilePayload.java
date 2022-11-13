package com.web.fileupload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@NoArgsConstructor @AllArgsConstructor
public class FilePayload {
    String fileName;
    String contentType;
    Long size;
    byte[] file;
}
