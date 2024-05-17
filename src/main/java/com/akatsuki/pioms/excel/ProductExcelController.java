package com.akatsuki.pioms.excel;

import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ComponentScan
public class ProductExcelController {

    private final ProductService productService;
    private List<ProductDTO> productList;

    public ProductExcelController(ProductService productService) {
        this.productService = productService;
        this.productList = productService.getAllProduct();
    }

    @GetMapping("/excel/go")
    public String home() {
        return "home";
    }

    @GetMapping(value = "/product-excel")
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
                "상품코드", "상품명", "상품가격", "등록일", "수정일", "상품 설명",
                "색상", "사이즈", "성별", "본사 총 보유량", "상태", "노출상태",
                "알림기준 수량", "본사 폐기량", "본사 보유량", "카테고리"
        };
        row = sheet.createRow(rowNum++);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headers[i]);
        }


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
            cell.setCellValue(dto.getCategoryThird());
        }

        // Column width auto-sizing
        for(int k = 0 ; k < headers.length ; k++){
            sheet.autoSizeColumn(k);
            sheet.setColumnWidth(k, (sheet.getColumnWidth(k))+(short)1024); //너비 더 넓게
//            row.setHeight((short)512);
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
