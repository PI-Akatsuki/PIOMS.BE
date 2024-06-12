package com.akatsuki.pioms.excel.admin;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.dto.DeliveryDriverDTO;
import com.akatsuki.pioms.driver.service.DeliveryDriverService;
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
@Tag(name = "[관리자]배송기사엑셀다운로드 API")
public class DriverExcelController {

    private final DeliveryDriverService deliveryDriverService;

    public DriverExcelController(DeliveryDriverService deliveryDriverService) {
        this.deliveryDriverService = deliveryDriverService;
    }

    @GetMapping(value = "driver-excel")
    public void excelDownload(HttpServletResponse response) throws Exception {
        List<DeliveryDriverDTO> driverList = deliveryDriverService.findDriverList();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("배송기사 목록 시트");
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
                "기사코드","이름","ID","PWD","휴대전화","등록일",
                "수정일","삭제일","역할","로그인실패횟수","활성상태"
        };
        row = sheet.createRow(rowNum++);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headers[i]);
        }
        // Body
        for (DeliveryDriverDTO dto : driverList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverCode());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverName());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverId());
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverPwd());
            cell = row.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverPhone());
            cell = row.createCell(5);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverEnrollDate());
            cell = row.createCell(6);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverUpdateDate());
            cell = row.createCell(7);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverDeleteDate());
            cell = row.createCell(8);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverRole());
            cell = row.createCell(9);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getDriverPwdCheckCount());
            cell = row.createCell(10);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.isDriverStatus());
        }

        // Column width auto-sizing
        for(int k = 0 ; k < headers.length ; k++){
            sheet.autoSizeColumn(k);
            sheet.setColumnWidth(k, (sheet.getColumnWidth(k))+(short)1024); //너비 더 넓게
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=driverList.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
