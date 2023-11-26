package seaFood.PTseafood.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgurUploadResponse {

    private boolean success;
    private ImgurUploadData data;

    // Getters and setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ImgurUploadData getData() {
        return data;
    }

    public void setData(ImgurUploadData data) {
        this.data = data;
    }
}