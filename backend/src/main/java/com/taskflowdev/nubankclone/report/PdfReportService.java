package com.taskflowdev.nubankclone.report;

import com.taskflowdev.nubankclone.statement.StatementEntry;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfReportService {
    public byte[] createStatementReport(List<StatementEntry> entries) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.newLineAtOffset(50, 730);
                contentStream.showText("Extrato Nubank Clone");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                int offsetY = 700;
                for (StatementEntry entry : entries) {
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText(entry.toString());
                    offsetY -= 20;
                    if (offsetY < 80) {
                        break;
                    }
                }
                contentStream.endText();
            }
            document.save(output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to build PDF report", e);
        }
    }
}
