package seaFood.PTseafood.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seaFood.PTseafood.service.UploadService;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        JSONObject responseJson = new JSONObject();
        try {
//            System.out.println("SADASODASJDSAJDOAS     "+file.getSize());
            // Kiểm tra xem tệp tin có tồn tại và không rỗng không
            if (file == null || file.isEmpty()) {
                return ResponseEntity.ok().body("Vui lòng chọn một tệp tin ảnh.");
            }
            // Kiểm tra kích thước
            if (file.getSize() > 512000) {
                responseJson.put("message", "Tệp quá lớn. Chúng tôi chỉ hỗ trợ tệp tin có kích thước tối đa là 512KB.");
                return ResponseEntity.badRequest().body(responseJson.toString());
            }
            // Kiểm tra định dạng tệp tin
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = uploadService.getFileExtension(originalFilename);
            if (!isSupportedImageFormat(fileExtension)) {
                responseJson.put("message","Chúng tôi chỉ hỗ trợ tệp tin có định dạng jpg, jpeg, png, webp");
                return ResponseEntity.badRequest().body(responseJson.toString());
            }
            // Xử lý nếu đúng định dạng
            String name = uploadService.uploadImage(file);
            responseJson.put("path",name);
            return ResponseEntity.ok().body(responseJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
            responseJson.put("message", "Lỗi xử lý JSON: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseJson.toString());
        }
    }

    private boolean isSupportedImageFormat(String fileExtension) {
        // Danh sách các định dạng hình ảnh được hỗ trợ
        String[] supportedFormats = {"jpg", "jpeg", "png", "webp"};
        for (String format : supportedFormats) {
            if (format.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }
        return false;
    }
}
