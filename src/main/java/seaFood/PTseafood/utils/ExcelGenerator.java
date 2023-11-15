package seaFood.PTseafood.utils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.OrderDetail;

import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List <Order> orderList;

    public ExcelGenerator(List < Order > orderList) {
        this.orderList = orderList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("All_Order");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Order Code", style);
        createCell(row, 2, "Giá tiền", style);
        createCell(row, 3, "Sản phẩm", style);
        createCell(row, 4, "Email khách hàng", style);
        createCell(row, 5, "Phương thức thanh toán", style);

    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }
    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Order record: orderList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getId(), style);
            createCell(row, columnCount++, record.getCode(), style);
            createCell(row, columnCount++, record.getFinalPrice(), style);
            createCell(row, columnCount++, getOrderDetailsAsString(record.getOrderDetails()), style);
            createCell(row, columnCount++, record.getUser().getEmail(), style);
            createCell(row, columnCount++, record.getPaymentMethod(), style);

        }
    }
    private String getOrderDetailsAsString(List<OrderDetail> orderDetailList) {
        if (orderDetailList == null || orderDetailList.isEmpty()) {
            return ""; // Hoặc một giá trị mặc định khác nếu muốn
        }

        StringBuilder sb = new StringBuilder();
        for (OrderDetail orderDetails : orderDetailList) {
            // Định dạng dữ liệu từ OrderDetails thành một chuỗi và thêm vào StringBuilder
            sb.append(orderDetails.getProductVariantName().toString()); // Hoặc sử dụng các trường cụ thể của OrderDetails để tạo chuỗi
            sb.append(", ");
        }
        // Loại bỏ ký tự ngăn cách cuối cùng nếu có
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
