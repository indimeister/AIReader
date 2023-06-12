package com.indimeister.ai.reader.controller;

import com.indimeister.ai.reader.domain.entity.Document;
import com.indimeister.ai.reader.service.DocumentReaderService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class DocumentReaderController {
    private final DocumentReaderService service;

    @Autowired
    public DocumentReaderController(DocumentReaderService service) {
      this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAll() {
      return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> get(@PathVariable("id") Long id) {
      return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Document> save() {
      return ResponseEntity.ok()
          .body(service.save(new Document("src/main/resources/TestDocument.pdf"))
          );
    }

}
