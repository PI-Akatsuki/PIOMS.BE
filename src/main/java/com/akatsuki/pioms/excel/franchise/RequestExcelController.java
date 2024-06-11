package com.akatsuki.pioms.excel.franchise;

import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.FranchiseOrderFacade;
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
@Tag(name = "[점주]발주엑셀다운로드 API")
public class RequestExcelController {

    private final FranchiseOrderFacade franchiseOrderFacade;

    public RequestExcelController(FranchiseOrderFacade franchiseOrderFacade) {
        this.franchiseOrderFacade = franchiseOrderFacade;
    }

    @GetMapping("/order-excel")
    public void exceldownload(HttpServletResponse response) throws Exception {
        List<OrderDTO> orderDTOList = franchiseOrderFacade.getOrderListByFranchiseCode();// 최신 데이터 가져오기
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("상품 목록 시트");
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
                "발주코드","발주일","총금액","승인여부","반려사유","가맹코드","교환,반품신청코드"
        };
        row = sheet.createRow(rowNum++);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headers[i]);
        }

        // Body
        for (OrderDTO dto : orderDTOList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getOrderCode());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getOrderDate());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getOrderTotalPrice());
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(String.valueOf(dto.getOrderCondition()));
            cell = row.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getOrderReason());
            cell = row.createCell(5);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getFranchiseCode());
            cell = row.createCell(6);
            cell.setCellStyle(bodyStyle);
            if(dto.getExchange()!=null)
                cell.setCellValue(dto.getExchange().getExchangeCode());
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
