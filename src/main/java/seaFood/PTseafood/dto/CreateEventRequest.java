package seaFood.PTseafood.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateEventRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double discountRate;
    private List<Long> productVariantIds;
    private String nameEvent;
}
