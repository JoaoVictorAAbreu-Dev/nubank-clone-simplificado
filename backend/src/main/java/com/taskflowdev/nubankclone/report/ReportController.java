package com.taskflowdev.nubankclone.report;

import com.taskflowdev.nubankclone.statement.StatementRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final StatementRepository statementRepository;
    private final PdfReportService pdfReportService;

    public ReportController(StatementRepository statementRepository, PdfReportService pdfReportService) {
        this.statementRepository = statementRepository;
        this.pdfReportService = pdfReportService;
    }

    @GetMapping(value = "/statement.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> statement(Authentication authentication) {
        byte[] pdf = pdfReportService.createStatementReport(statementRepository.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statement.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
