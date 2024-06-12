package com.akatsuki.pioms.excel.driver;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
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
import java.io.InputStream;

@RestController
@RequestMapping("driver/pdfdownload")
@Tag(name = "[driver]배송기사메뉴얼 다운로드 API")
public class DriverManualPDFController {

    @GetMapping(value = "/driver-pdf")
    public ResponseEntity<Resource> downloadPdf() throws IOException {
        // PDF 생성
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            // 폰트 로드
            InputStream fontStream = getClass().getResourceAsStream("/fonts/malgun.ttf");
            PDType0Font font = PDType0Font.load(document, fontStream);

            // 텍스트 줄 단위로 분리
            String text = "배송기사 메뉴얼\n" +
                    "\n" +
                    "배송기사는 배송 관련 정보와 상태를 관리합니다.\n" +
                    "\n" +
                    "◆ 배송상태 변경\n" +
                    "\n" +
                    "  ◇수정: 배송 상태를 변경할 수 있습니다.\n" +
                    "\n" +
                    "◆ 자신의 배송 목록 조회\n" +
                    "\n" +
                    "  ◇조회: 자신의 배송 목록을 조회할 수 있습니다.\n" +
                    "\n" +
                    "◆ 공지사항 조회\n" +
                    "\n" +
                    "  ◇조회: 공지사항을 조회할 수 있습니다.";
            String[] lines = text.split("\n");

            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            // 한글 폰트 설정
            contentStream.setFont(font, 12);
            contentStream.setLeading(14.5f); // 줄 간격 설정
            float margin = 50;
            float yPosition = 700; // 시작 위치 설정
            contentStream.newLineAtOffset(margin, yPosition);

            // 페이지별로 텍스트 추가
            for (String line : lines) {
                if (yPosition < margin) { // 페이지 하단에 도달하면 새 페이지 생성
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.setLeading(14.5f);
                    yPosition = 700;
                    contentStream.newLineAtOffset(margin, yPosition);
                }
                contentStream.showText(line);
                contentStream.newLine();
                yPosition -= 14.5f; // 줄 간격만큼 y 위치를 줄임
            }

            contentStream.endText();
            contentStream.close();

            document.save(out);
        }

        // PDF 파일을 ByteArrayResource로 변환
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        // ResponseEntity 설정
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=driverManual.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
