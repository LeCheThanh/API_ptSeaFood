package seaFood.PTseafood.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.utils.ExcelGenerator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private OrderService orderService;
    //Export ALL_ORDER
    public void excelAllOrder(HttpServletResponse response) throws IOException {

//        response.setContentType("application/octet-stream");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=All_Orders_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Order> listOrder = orderService.getAll();
        ExcelGenerator excelGenerator = new ExcelGenerator(listOrder);
        excelGenerator.generateExcelFile(response);
    }
}
