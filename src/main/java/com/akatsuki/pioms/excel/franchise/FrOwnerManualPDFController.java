package com.akatsuki.pioms.excel.franchise;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("franchise/pdfdownload")
public class FrOwnerManualPDFController {

    @GetMapping(value = "/fraowner-pdf")
    public ResponseEntity<Resource> downloadPdf() throws IOException {
        // PDF 생성
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                // 기본 폰트 설정 (예: Times-Roman)
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("FrOwnerManualPDF");
                contentStream.endText();
            }

            document.save(out);
        }

        // PDF 파일을 ByteArrayResource로 변환
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        // ResponseEntity 설정
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=frOwnerManual.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
