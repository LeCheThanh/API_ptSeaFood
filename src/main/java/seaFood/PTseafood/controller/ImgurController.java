package seaFood.PTseafood.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seaFood.PTseafood.service.ImgurUploadService;
import seaFood.PTseafood.service.UploadService;
@CrossOrigin
@RestController
@RequestMapping("/api/imgur")
public class ImgurController {
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ImgurUploadService imgurUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        JSONObject responseJson = new JSONObject();
        try {

            byte[] imageData = image.getBytes();
                // Kiểm tra kích thước
                if (image.getSize() > 512000) {
                    responseJson.put("message", "Tệp quá lớn. Chúng tôi chỉ hỗ trợ tệp tin có kích thước tối đa là 512KB.");
                    return ResponseEntity.badRequest().body(responseJson.toString());
                }
                // Kiểm tra định dạng tệp tin
                String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
                String fileExtension = uploadService.getFileExtension(originalFilename);
                if (!isSupportedImageFormat(fileExtension)) {
                    responseJson.put("message", "Chúng tôi chỉ hỗ trợ tệp tin có định dạng jpg, jpeg, png, webp");
                    return ResponseEntity.badRequest().body(responseJson.toString());
                }
                String imageUrl = imgurUploadService.uploadImage(imageData);
                if (imageUrl == null) {
                   return ResponseEntity.badRequest().body("Upload thất bại");
                }
            return ResponseEntity.ok(imageUrl);

        }catch(Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload thất bại, lỗi server: " + e.getMessage());
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