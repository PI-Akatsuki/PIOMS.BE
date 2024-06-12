package com.akatsuki.pioms.excel.admin;

import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin/exceldownload")
@Tag(name = "[관리자]점주엑셀다운로드 API")
public class FranchiseExcelController {

    private final FranchiseService franchiseService;

    public FranchiseExcelController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @GetMapping(value = "/franchise-excel")
    public void excelDownload(HttpServletResponse response) throws Exception {
        List<FranchiseDTO> franchiseList = franchiseService.findFranchiseList();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("가맹점 목록 시트");
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
                "가맹코드", "가맹점명", "주소", "전화번호", "등록일", "수정일", "삭제일",
                "사업자등록번호", "배송요일", "점주코드", "점주명", "관리자코드", "관리자명", "배송기사코드", "배송기사명"
        };
        row = sheet.createRow(rowNum++);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headers[i]);
        }
        // Body
        for (FranchiseDTO dto : franchiseList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseCode());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseName());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseAddress());
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseCall());
            cell = row.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseEnrollDate());
            cell = row.createCell(5);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseUpdateDate());
            cell = row.createCell(6);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseDeleteDate());
            cell = row.createCell(7);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseBusinessNum());
            cell = row.createCell(8);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(String.valueOf(dto.getFranchiseDeliveryDate()));

            cell = row.createCell(9);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(String.valueOf(dto.getFranchiseOwner() != null ? dto.getFranchiseOwner().getFranchiseOwnerCode() : ""));

            cell = row.createCell(10);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwner() != null ? dto.getFranchiseOwner().getFranchiseOwnerName() : "");

            cell = row.createCell(11);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminCode());
            cell = row.createCell(12);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminName());
            cell = row.createCell(13);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDeliveryDriver() != null ? String.valueOf(dto.getDeliveryDriver().getDriverCode()) : "");
            cell = row.createCell(14);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDeliveryDriver() != null ? dto.getDeliveryDriver().getDriverName() : "");
        }

        // Column width auto-sizing
        for (int k = 0; k < headers.length; k++) {
            sheet.autoSizeColumn(k);
            sheet.setColumnWidth(k, (sheet.getColumnWidth(k)) + (short) 1024); //너비 더 넓게
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=franchiseList.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
