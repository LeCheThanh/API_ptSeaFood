package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seaFood.PTseafood.service.ExcelService;
import seaFood.PTseafood.service.OrderService;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/excel")
public class   ExcelController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private OrderService orderService;

    @CrossOrigin(exposedHeaders = "Content-Disposition")
    @GetMapping("/alls")
    public ResponseEntity<String> exportIntoExcelFile(HttpServletResponse response) throws IOException {
        try{
            excelService.excelAllOrder(response);
            return ResponseEntity.ok("Export thành công!");
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body("Không thể xuất file excel"+e.getMessage());
        }
    }
}
