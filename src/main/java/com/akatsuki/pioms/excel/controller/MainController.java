package com.akatsuki.pioms.excel.controller;

import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ComponentScan
public class MainController {

    private final ProductService productService;
    private List<ProductDTO> productList;

    public MainController(ProductService productService) {
        this.productService = productService;
        this.productList = productService.getAllProduct();
    }

    @GetMapping("/excel/go")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/excel/download")
    public void excelDownload(HttpServletResponse response) throws Exception {
//        Workbook wb = new HSSFWorkbook();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("상품 목록 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // table header style
        CellStyle headStyle = wb.createCellStyle();

        // thin border style
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // background color
        headStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
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
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue("번호");
        cell = row.createCell(1);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상품코드");
        cell = row.createCell(2);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상품명");
        cell = row.createCell(3);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상품가격");
        cell = row.createCell(4);
        cell.setCellStyle(headStyle);
        cell.setCellValue("등록일");
        cell = row.createCell(5);
        cell.setCellStyle(headStyle);
        cell.setCellValue("수정일");
        cell = row.createCell(6);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상품 설명");
        cell = row.createCell(7);
        cell.setCellStyle(headStyle);
        cell.setCellValue("색상");
        cell = row.createCell(8);
        cell.setCellStyle(headStyle);
        cell.setCellValue("사이즈");
        cell = row.createCell(9);
        cell.setCellStyle(headStyle);
        cell.setCellValue("성별");
        cell = row.createCell(10);
        cell.setCellStyle(headStyle);
        cell.setCellValue("본사 총 보유량");
        cell = row.createCell(11);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상태");
        cell = row.createCell(12);
        cell.setCellStyle(headStyle);
        cell.setCellValue("노출상태");
        cell = row.createCell(13);
        cell.setCellStyle(headStyle);
        cell.setCellValue("알림기준 수량");
        cell = row.createCell(14);
        cell.setCellStyle(headStyle);
        cell.setCellValue("본사 폐기량");
        cell = row.createCell(15);
        cell.setCellStyle(headStyle);
        cell.setCellValue("본사 보유량");
        cell = row.createCell(16);
        cell.setCellStyle(headStyle);
//        cell.setCellValue("카테고리");
//        cell = row.createCell(17);
//        cell.setCellStyle(headStyle);

        // Body
        for (ProductDTO dto : productList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductCode());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductName());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductPrice());
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductEnrollDate());
            cell = row.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductUpdateDate());
            cell = row.createCell(5);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductContent());
            cell = row.createCell(6);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductColor().toString());
            cell = row.createCell(7);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductSize());
            cell = row.createCell(8);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductGender().toString());
            cell = row.createCell(9);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductTotalCount());
            cell = row.createCell(10);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductStatus().toString());
            cell = row.createCell(11);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.isProductExposureStatus());
            cell = row.createCell(12);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductNoticeCount());
            cell = row.createCell(13);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductDiscount());
            cell = row.createCell(14);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getProductCount());
            cell = row.createCell(15);
            cell.setCellStyle(bodyStyle);
//            cell.setCellValue(dto.getCategoryThird().getCategoryThirdCode());
//            cell = row.createCell(16);
//            cell.setCellStyle(bodyStyle);
        }
        System.out.println("productList = " + productList);
        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=product.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
