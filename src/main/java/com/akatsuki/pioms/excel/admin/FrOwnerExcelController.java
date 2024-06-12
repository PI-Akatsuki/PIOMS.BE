package com.akatsuki.pioms.excel.admin;

import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.service.FranchiseOwnerService;
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
@Tag(name = "[관리자]가맹점엑셀다운로드 API")
public class FrOwnerExcelController {

    private final FranchiseOwnerService franchiseOwnerService;

    public FrOwnerExcelController(FranchiseOwnerService franchiseOwnerService) {
        this.franchiseOwnerService = franchiseOwnerService;
    }

    @GetMapping(value = "frowner-excel")
    public void excelDownload(HttpServletResponse response) throws Exception {
        List<FranchiseOwnerDTO> franchiseOwnerDTOList = franchiseOwnerService.findAllFranchiseOwners();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("관리자 목록 시트");
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
                "점주코드", "이름", "ID", "PWD", "이메일", "휴대전화", "등록일", "수정일", "삭제일",
                "역할", "로그인실패횟수", "활성상태", "담당 관리자"
        };
        row = sheet.createRow(rowNum++);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headers[i]);
        }
        // Body
        for (FranchiseOwnerDTO dto : franchiseOwnerDTOList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerCode());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerName());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerId());
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerPwd());
            cell = row.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerEmail());
            cell = row.createCell(5);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerPhone());
            cell = row.createCell(6);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerEnrollDate());
            cell = row.createCell(7);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerUpdateDate());
            cell = row.createCell(8);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseOwnerDeleteDate());
            cell = row.createCell(9);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseRole());
            cell = row.createCell(10);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getOwnerPwdCheckCount());
            cell = row.createCell(11);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseStatus());
            cell = row.createCell(12);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminName());
        }

        // Column width auto-sizing
        for (int k = 0; k < headers.length; k++) {
            sheet.autoSizeColumn(k);
            sheet.setColumnWidth(k, (sheet.getColumnWidth(k)) + (short) 1024); //너비 더 넓게
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=frOwnersList.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
