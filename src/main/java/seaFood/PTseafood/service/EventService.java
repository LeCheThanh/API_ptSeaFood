package seaFood.PTseafood.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.dto.CreateEventRequest;
import seaFood.PTseafood.entity.Event;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.repository.IEventRepository;
import seaFood.PTseafood.repository.IProductVariantRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    IProductVariantRepository productVariantRepository;
    @Autowired
    private IEventRepository eventRepository;
//    public Double getDiscountedPrice(ProductVariant productVariant) {
//        LocalDateTime now = LocalDateTime.now();
//        List<Event> activeEvents = eventRepository.findByProductVariantAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(productVariant, now, now);
//
//        if (!activeEvents.isEmpty()) {
//            // Giả sử chỉ lấy sự kiện đầu tiên
//            Event event = activeEvents.get(0);
//            return productVariant.getPrice() * (1 - event.getDiscountRate());
//        }
//
//        return productVariant.getPrice();
//    }
    @Transactional
    public Event createEvent(CreateEventRequest eventRequest) {
        Event event = new Event();
        // Set details for the new event
        event.setDiscountRate(eventRequest.getDiscountRate());
        event.setStartTime(eventRequest.getStartTime());
        event.setEndTime(eventRequest.getEndTime());
        event.setName(eventRequest.getNameEvent());
        // Save the event first to generate an event ID
        Event savedEvent = eventRepository.save(event);

        List<Long> variantIds = eventRequest.getProductVariantIds();
        List<ProductVariant> variants = productVariantRepository.findByIdIn(variantIds);
        List<ProductVariant> eligibleVariants = new ArrayList<>();

        for (ProductVariant variant : variants) {
            // Check if the variant is not already part of another event
            if (variant.getEvent() == null) {
                variant.setEvent(savedEvent);
                eligibleVariants.add(variant);
            } else {
                // Log or handle the case where the variant is part of another event
                System.out.println("Variant " + variant.getId() + " is already part of another event.");
                throw new RuntimeException("Biến thể: " + variant.getName() +"/"+ variant.getProduct().getName() + " đã tồn tại trong một event khác!.");
            }
        }

        // Save the updated product variants if they are eligible
        productVariantRepository.saveAll(eligibleVariants);

        // Only link the eligible variants to the event
        savedEvent.setProductVariants(eligibleVariants);
        return eventRepository.save(savedEvent);
    }
    @Transactional
    public Event updateEvent(Long eventId, CreateEventRequest updateRequest) {
        // Retrieve the existing event
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện với id: " + eventId));

        // Update the fields of the event
        existingEvent.setDiscountRate(updateRequest.getDiscountRate());
        existingEvent.setStartTime(updateRequest.getStartTime());
        existingEvent.setEndTime(updateRequest.getEndTime());
        existingEvent.setName(updateRequest.getNameEvent());

        // Update the product variants if provided
        if (updateRequest.getProductVariantIds() != null && !updateRequest.getProductVariantIds().isEmpty()) {
            List<ProductVariant> updatedVariants = productVariantRepository.findByIdIn(updateRequest.getProductVariantIds());
            for (ProductVariant variant : updatedVariants) {
                if (variant.getEvent() == null || variant.getEvent().getId().equals(eventId)) {
                    variant.setEvent(existingEvent);
                } else {
                    throw new RuntimeException("Biến thể: " + variant.getName() +"/"+ variant.getProduct().getName() + " đã tồn tại trong một event khác!.");
                }
            }
            existingEvent.setProductVariants(updatedVariants);
            productVariantRepository.saveAll(updatedVariants); // Save the updated variants
        }

        // Save and return the updated event
        return eventRepository.save(existingEvent);
    }



    public List<Event> getActiveEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findActiveEvents(now);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public double calculateDiscountedPrice(Event event, double originalPrice) {
        if (event != null && event.getDiscountRate() != null && event.getDiscountRate() > 0) {
            // Tính giá đã giảm
            double discountAmount = originalPrice * event.getDiscountRate() / 100;
            return originalPrice - discountAmount;
        }
        // Nếu không có thông tin giảm giá hợp lệ, trả về giá gốc
        return originalPrice;
    }
    public Optional<Event> getById(Long id){
        return eventRepository.findById(id);
    }
}
