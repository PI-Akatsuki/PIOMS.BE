package com.akatsuki.pioms.excel.admin;

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
@RequestMapping("admin/pdfdownload")
@Tag(name = "[관리자]pdf다운로드 API")
public class AdminManualPDFController {

    @GetMapping(value = "/admin-pdf")
    public ResponseEntity<Resource> downloadPdf() throws IOException {
        // PDF 생성
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            // 폰트 로드
            InputStream fontStream = getClass().getResourceAsStream("/fonts/malgun.ttf");
            PDType0Font font = PDType0Font.load(document, fontStream);

            // 텍스트 줄 단위로 분리
            String text = "본사 관리자 메뉴얼\n" +
                    "\n" +
                    "본사 관리자는 본사에 속한 특정 가맹점들에 대한 관리 권한을 가지고 있습니다.\n" +
                    "\n" +
                    "◆ 본사정보 관리\n" +
                    "\n" +
                    "  ◇조회: 본사 정보 페이지에서 본사에 대한 정보를 조회할 수 있습니다.\n" +
                    "\n" +
                    "◆ 관리자 정보 관리\n" +
                    "\n" +
                    "  ◇조회: 자신의 관리자 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 자신의 관리자 정보를 수정할 수 있습니다.\n" +
                    "\n" +
                    "◆ 가맹점 관리\n" +
                    "\n" +
                    "  ◇조회: 자신이 담당하는 가맹점의 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 자신이 담당하는 가맹점의 정보를 수정할 수 있습니다.\n" +
                    "\n" +
                    "◆ 점주 관리\n" +
                    "\n" +
                    "  ◇조회: 자신이 담당하는 가맹점의 점주 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 자신이 담당하는 가맹점의 점주 정보를 수정할 수 있습니다.\n" +
                    "\n" +
                    "◆ 배송기사 관리\n" +
                    "\n" +
                    "  ◇조회: 배송기사의 이름 정도를 조회할 수 있습니다. 상세 조회는 불가합니다.\n" +
                    "\n" +
                    "◆ 상품 관리\n" +
                    "\n" +
                    "  ◇조회: 상품 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 상품 정보를 수정할 수 있습니다.\n" +
                    "\n" +
                    "◆ 상품카테고리 관리\n" +
                    "\n" +
                    "  ◇조회: 상품카테고리 정보를 조회할 수 있습니다.\n" +
                    "\n" +
                    "◆ 배송정보 + 발주 관리\n" +
                    "\n" +
                    "  ◇명세서 및 송장 발급: 명세서와 송장을 발급할 수 있습니다.\n" +
                    "  ◇조회: 자신이 담당하는 가맹점의 배송 정보와 발주 내역을 조회할 수 있습니다.\n" +
                    "  ◇수정: 자신이 담당하는 가맹점의 배송 정보와 발주 내역을 수정할 수 있습니다.\n" +
                    "\n" +
                    "◆ 반품 및 교환 처리\n" +
                    "\n" +
                    "  ◇조회: 자신이 담당하는 가맹점의 반품 및 교환 내역을 조회할 수 있습니다.\n" +
                    "  ◇수정: 자신이 담당하는 가맹점의 반품 및 교환 내역을 수정할 수 있습니다.\n" +
                    "\n" +
                    "◆ 통계 조회\n" +
                    "\n" +
                    "  ◇조회: 통계 정보를 조회할 수 있습니다.\n" +
                    "\n" +
                    "◆ 공지사항 조회\n" +
                    "\n" +
                    "  ◇조회: 공지사항을 조회할 수 있습니다.\n" +
                    "\n" +
                    "◆ 문의사항 관리\n" +
                    "\n" +
                    "  ◇조회: 자신이 담당하는 가맹점에 대한 문의사항을 조회할 수 있습니다.\n" +
                    "  ◇수정: 자신이 담당하는 가맹점에 대한 문의사항을 수정할 수 있습니다.";
            String[] lines = text.split("\n");

            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            // 한글 폰트 설정
            contentStream.setFont(font, 12);
            contentStream.setLeading(14.5f); // 줄 간격 설정
            float margin = 50;
            float yPosition = 750; // 시작 위치 설정
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
                    yPosition = 750;
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=adminManual.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
