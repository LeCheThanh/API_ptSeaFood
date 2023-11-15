package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.OrderDetail;
import seaFood.PTseafood.service.ExcelService;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.utils.ExcelGenerator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private OrderService orderService;

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
