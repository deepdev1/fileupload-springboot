package com.web.fileupload;

import com.web.fileupload.model.FilePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class FileuploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileuploadApplication.class, args);
	}

	@Autowired
	Service service;

	@Autowired
	ApplicationContext context;

	@GetMapping(value = "/healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> echoHealth() {
		Map<String, String> map = new HashMap<>();
		map.put("Timespamp", new Date().toString());
		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/files", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFiles() {
		return service.getFiles();
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		return service.uploadFile(file);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String id) throws IOException {
		FilePayload filePayload = service.downloadFile(id);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(filePayload.getContentType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePayload.getFileName() + "\"")
				.body(new ByteArrayResource(filePayload.getFile()));
	}

}
