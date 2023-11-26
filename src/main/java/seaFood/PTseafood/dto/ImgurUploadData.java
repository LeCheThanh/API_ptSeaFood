package seaFood.PTseafood.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgurUploadData {

    private String link;

    // Getters and setters

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}