package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadService {
    @Value("${image-storage}")
    private String imageStorage;
    public String uploadImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try {
                // Lưu trữ tệp tin ảnh trên server
                int lastDotIndex = file.getOriginalFilename().indexOf(".");
                String fileName = file.getOriginalFilename().substring(0,lastDotIndex);
                String fileExtension = getFileExtension(file.getOriginalFilename());
                String uniqueFileName = generateUniqueFileName(fileName);
                String uploadDir = imageStorage; // Thay đổi thành đường dẫn thư mục lưu trữ thực tế trên server

                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs(); // Tạo thư mục lưu trữ nếu không tồn tại
                }
                File serverFile = new File(uploadPath.getAbsolutePath() + File.separator + uniqueFileName + "." + fileExtension);
                file.transferTo(serverFile);
                return serverFile.getName();
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi khi lưu trữ tệp tin
            }
        }
        return  null;
    }
    public String getFileExtension(String filename) {
        return StringUtils.getFilenameExtension(filename);
    }
    private String generateUniqueFileName(String filename) {
        return filename+"_"+ UUID.randomUUID().toString();
    }
}

