package com.akatsuki.pioms.excel.admin;

import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.admin.service.AdminInfoService;
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
@Tag(name = "[관리자]관리자엑셀다운로드 API")
public class AdminExcelController {

    private final AdminInfoService adminInfoService;

    public AdminExcelController(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    @GetMapping(value = "/admin-excel")
    public void excelDownload(HttpServletResponse response) throws Exception{
        List<AdminDTO> adminDTOList = adminInfoService.findAdminList(); // 최신 데이터 가져오기
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
                "관리자코드","이름","ID","PWD","등록일","수정일","삭제일","이메일",
                "휴대전화","발급코드","역할","활성상태","로그인실패횟수"
        };
        row = sheet.createRow(rowNum++);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headers[i]);
        }
        // Body
        for (AdminDTO dto : adminDTOList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminCode());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminName());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminId());
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminPwd());
            cell = row.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getEnrollDate());
            cell = row.createCell(5);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getUpdateDate());
            cell = row.createCell(6);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDeleteDate());
            cell = row.createCell(7);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminEmail());
            cell = row.createCell(8);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminPhone());
            cell = row.createCell(9);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAccessNumber());
            cell = row.createCell(10);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getAdminRole());
            cell = row.createCell(11);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.isAdminStatus());
            cell = row.createCell(12);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getPwdCheckCount());
        }

        // Column width auto-sizing
        for(int k = 0 ; k < headers.length ; k++){
            sheet.autoSizeColumn(k);
            sheet.setColumnWidth(k, (sheet.getColumnWidth(k))+(short)1024); //너비 더 넓게
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=adminList.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
