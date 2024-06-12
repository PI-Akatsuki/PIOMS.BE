package com.akatsuki.pioms.excel.franchise;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("franchise/exceldownload")
@Tag(name = "[점주]반품엑셀다운로드 API")
public class ExchangeExcelController {

    private final ExchangeService exchangeService;

    public ExchangeExcelController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    // 점주의 교환,반품 신청 내역
    @GetMapping("/exchange-excel")
    public void excelDownload(HttpServletResponse response) throws Exception {
        List<ExchangeDTO> exchangeDTOList = exchangeService.getFrOwnerExchanges();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("교환반품신청내역 목록 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // table header style
        CellStyle headStyle = wb.createCellStyle();

        // thin border style
        headStyle.setBorderTop(BorderStyle.THICK);
        headStyle.setBorderBottom(BorderStyle.THICK);
        headStyle.setBorderLeft(BorderStyle.THICK);
        headStyle.setBorderRight(BorderStyle.THICK);

        // background color
        headStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // align-center
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        // data border style
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);

        // Header
        String[] headers = {
                "교환반품신청코드","신청일","처리여부","가맹코드"
        };

        row = sheet.createRow(rowNum++);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headers[i]);
        }

        // Body
        for (ExchangeDTO dto : exchangeDTOList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getExchangeCode());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getExchangeDate());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(String.valueOf(dto.getExchangeStatus()));
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchise().getFranchiseCode());

        }

        // Column width auto-sizing
        for(int k = 0 ; k < headers.length ; k++){
            sheet.autoSizeColumn(k);
            sheet.setColumnWidth(k, (sheet.getColumnWidth(k))+(short)1024); //너비 더 넓게
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=FrOrderList.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
