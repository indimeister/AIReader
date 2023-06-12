package com.indimeister.ai.reader.service;

import com.indimeister.ai.reader.domain.entity.Document;
import com.indimeister.ai.reader.exception.AiReaderException;
import com.indimeister.ai.reader.repository.DocumentRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DocumentReaderService {

  private static final Logger logger = LoggerFactory.getLogger(DocumentReaderService.class);
  @Value("${openai.api.key}")
  private String OPENAI_API_KEY;
  private final DocumentRepository repository;
  private OpenAiService openAiService;


  @Autowired
  public DocumentReaderService(DocumentRepository repository) {
    this.repository = repository;
  }

  public List<Document> findAll() {
    return repository.findAll();
  }

  public Document findById(Long id) {
    Optional<Document> docOpt = repository.findById(id);
    if (docOpt.isEmpty()) {
      throw new IllegalArgumentException("Document not found for id: " + id);
    }
    return docOpt.get();
  }

  public Document save(Document doc) {
    doc.setResumeFile(getResumeDocument(doc));
    return repository.save(doc);
  }

  public List<String> getResumeDocument(Document doc) {
    String contentDoc = readDocumentPDF(doc.getPathFile());

    openAiService = new OpenAiService(OPENAI_API_KEY, Duration.ofSeconds(60));
    CompletionRequest request = CompletionRequest.builder()
        .model("text-davinci-003")
        .prompt(contentDoc)
        .maxTokens(100)
        .echo(true)
        .build();

    try {

      List<CompletionChoice> choices = openAiService.createCompletion(request).getChoices();
      choices.forEach(choice -> logger.info(choice.getText()));

      return choices.stream()
          .map(CompletionChoice::getText)
          .collect(Collectors.toList());

    } catch(Exception exception) {
      throw new AiReaderException(exception.getMessage());
    }

  }

  public String readDocumentPDF(String document) {
    String text = null;
    try (PdfReader reader = new PdfReader(document);
        PdfDocument pdfDocument = new PdfDocument(reader)) {

      int numPages = pdfDocument.getNumberOfPages();

      for (int i = 1; i <= numPages; i++) {
        PdfPage page = pdfDocument.getPage(i);
        text = PdfTextExtractor.getTextFromPage(page);
        logger.info("Texto da página {}: {}", i, text);
        logger.info("---------------------------------");
      }
    } catch (IOException e) {
      logger.error("Error reading PDF document: {}", e.getMessage(), e);
    }

    return text;
  }
}
