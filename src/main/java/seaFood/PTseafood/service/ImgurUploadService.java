package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.*;
import seaFood.PTseafood.dto.ImgurUploadResponse;

@Service
public class ImgurUploadService {
//    @Value("${imgur.clientId}")
//    private String clientId;

//    public String uploadImage(MultipartFile imageFile) {
//        String uploadUrl = "https://api.imgur.com/3/image";
//
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        headers.set("Authorization", "Client-ID " + clientId);
//
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("image", new ByteArrayResource(imageFile.getBytes()));
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<ImgurUploadResponse> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, ImgurUploadResponse.class);
//
//        if (response != null && response.getBody() != null) {
//            ImgurUploadResponse uploadResponse = response.getBody();
//            return uploadResponse.getData().getLink();
//        }
//
//        return null;
//    }
    @Value("${imgur.client-id}")
    private String imgurClientId;

    private static final String IMGUR_API_URL = "https://api.imgur.com/3/image";
//    private static final String CLIENT_ID = "your-client-id";

    @Autowired
    private RestTemplate restTemplate;


    public String uploadImage(byte[] imageData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Client-ID " + imgurClientId);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageData);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ImgurUploadResponse> responseEntity = restTemplate.exchange(
                IMGUR_API_URL,
                HttpMethod.POST,
                requestEntity,
                ImgurUploadResponse.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            ImgurUploadResponse response = responseEntity.getBody();
            if (response != null && response.isSuccess()) {
                return response.getData().getLink();
            }
        }

        return null;
    }
}

