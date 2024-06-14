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
@Tag(name = "[루트]pdf다운로드 API")
public class RootManualPDFController {

    @GetMapping(value = "/root-pdf")
    public ResponseEntity<Resource> downloadPdf() throws IOException {
        // PDF 생성
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            // 폰트 로드
            InputStream fontStream = getClass().getResourceAsStream("/fonts/malgun.ttf");
            PDType0Font font = PDType0Font.load(document, fontStream);

            // 텍스트 줄 단위로 분리
            String text = "Root 관리자 메뉴얼\n" +
                    "\n" +
                    "Root 관리자는 시스템 전체에 대한 모든 권한을 가지고 있습니다. 시스템의 모든 정보와 데이터를 조회, 수정, 등록, 삭제할 수 있습니다.\n" +
                    "\n" +
                    "◆ 본사정보 관리\n" +
                    "\n" +
                    "  ◇조회: 본사 정보 페이지에서 본사에 대한 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 본사 정보 수정 페이지에서 본사 정보를 변경할 수 있습니다.\n" +
                    "\n" +
                    "◆ 관리자 정보 관리\n" +
                    "\n" +
                    "  ◇등록: 새로운 관리자를 추가할 수 있습니다.\n" +
                    "  ◇조회: 기존 관리자 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 관리자 정보를 수정할 수 있습니다.\n" +
                    "  ◇삭제: 관리자를 삭제할 수 있습니다.\n" +
                    "\n" +
                    "◆가맹점 관리\n" +
                    "\n" +
                    "  ◇등록: 새로운 가맹점을 추가할 수 있습니다.\n" +
                    "  ◇조회: 기존 가맹점 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 가맹점 정보를 수정할 수 있습니다.\n" +
                    "  ◇삭제: 가맹점을 삭제할 수 있습니다.\n" +
                    "\n" +
                    "◆ 점주 관리\n" +
                    "\n" +
                    "  ◇등록: 새로운 점주를 추가할 수 있습니다.\n" +
                    "  ◇조회: 기존 점주 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 점주 정보를 수정할 수 있습니다.\n" +
                    "  ◇삭제: 점주를 삭제할 수 있습니다.\n" +
                    "\n" +
                    "◆ 배송기사 관리\n" +
                    "\n" +
                    "  ◇등록: 새로운 배송기사를 추가할 수 있습니다.\n" +
                    "  ◇조회: 기존 배송기사 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 배송기사 정보를 수정할 수 있습니다.\n" +
                    "  ◇삭제: 배송기사를 삭제할 수 있습니다.\n" +
                    "\n" +
                    "◆ 상품 관리\n" +
                    "\n" +
                    "  ◇등록: 새로운 상품을 추가할 수 있습니다.\n" +
                    "  ◇조회: 기존 상품 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 상품 정보를 수정할 수 있습니다.\n" +
                    "  ◇삭제: 상품을 삭제할 수 있습니다.\n" +
                    "\n" +
                    "◆ 상품카테고리 관리\n" +
                    "\n" +
                    "  ◇등록: 새로운 상품카테고리를 추가할 수 있습니다.\n" +
                    "  ◇조회: 기존 상품카테고리 정보를 조회할 수 있습니다.\n" +
                    "  ◇수정: 상품카테고리 정보를 수정할 수 있습니다.\n" +
                    "  ◇삭제: 상품카테고리를 삭제할 수 있습니다.\n" +
                    "\n" +
                    "◆ 배송정보 + 발주 관리\n" +
                    "\n" +
                    "  ◇명세서 및 송장 발급: 명세서와 송장을 발급할 수 있습니다.\n" +
                    "  ◇조회: 모든 배송 정보와 발주 내역을 조회할 수 있습니다.\n" +
                    "  ◇수정: 배송 정보와 발주 내역을 수정할 수 있습니다.\n" +
                    "\n" +
                    "◆ 반품 및 교환 처리\n" +
                    "\n" +
                    "  ◇조회: 반품 및 교환 내역을 조회할 수 있습니다.\n" +
                    "  ◇수정: 반품 및 교환 내역을 수정할 수 있습니다.\n" +
                    "\n" +
                    "◆ 통계 조회\n" +
                    "\n" +
                    "  ◇조회: 모든 통계 정보를 조회할 수 있습니다.\n" +
                    "\n" +
                    "◆ 이력 조회\n" +
                    "\n" +
                    "  ◇조회: 모든 작업 이력을 조회할 수 있습니다.\n" +
                    "\n" +
                    "◆ 공지사항 관리\n" +
                    "\n" +
                    "  ◇등록: 새로운 공지사항을 작성할 수 있습니다.\n" +
                    "  ◇조회: 기존 공지사항을 조회할 수 있습니다.\n" +
                    "  ◇수정: 공지사항을 수정할 수 있습니다.\n" +
                    "  ◇삭제: 공지사항을 삭제할 수 있습니다.\n" +
                    "\n" +
                    "◆ 문의사항 관리\n" +
                    "\n" +
                    "  ◇조회: 모든 문의사항을 조회할 수 있습니다.\n" +
                    "  ◇수정: 문의사항을 수정할 수 있습니다.";
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rootManual.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
